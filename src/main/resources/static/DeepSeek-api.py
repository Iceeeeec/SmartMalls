import mysql.connector
import Agently
import nest_asyncio
import sys
import json

# 引入nest_asyncio解决Jupyter环境异步执行的问题
nest_asyncio.apply()

def init_agent_factory():
    # 初始化Agent工厂
    return (
        Agently.AgentFactory()
            .set_settings("current_model", "OAIClient")
            .set_settings("model.OAIClient.url",  "https://api.deepseek.com")
            .set_settings("model.OAIClient.auth", {"api_key": "sk-19ac3a78cf744d3cab646fdbad5d8794"})
            .set_settings("model.OAIClient.options", { "model": "deepseek-chat" })
    )

def create_diet_workflow():
    # 定义健康食谱知识问答角色
    smart_diet_role = """
    分析营养需求: 根据用户输入的体检报告结果、过敏源、疾病和BMI值，分析用户的营养需求。
    了解食品列表: 根据数据库的食品列表，你可以了解现在有哪些食品可供用户推荐。
    食品评分：根据现有的食品列表和营养需求，可以给每个食品打分(打分机制：越推荐，分数越高)
    """
    
    agent_factory = init_agent_factory()
    healthy_diet_agent = agent_factory.create_agent().set_role(smart_diet_role)
    diet_workflow = Agently.Workflow()

    @diet_workflow.chunk()
    def fetch_user_data(inputs, storage):
        try:
            # 添加调试打印，查看inputs内容
            print(f"Received inputs: {inputs}")
            user_id = inputs.get('default', {}).get('user_id')  # 使用get方法安全获取user_id
            storage.user_id = user_id
            if user_id is None:
                raise ValueError("未提供user_id参数")
            
            # 连接数据库
            conn = mysql.connector.connect(
                host="aliyun.ovo.rent",
                user="SmartMalls",
                password="shuwu",
                database="smartmalls"
            )
            
            cursor = conn.cursor()
            
            try:
                # 获取用户体重类型
                cursor.execute(f"SELECT type FROM user_types where id = ("
                            f"select id_uTypes from rela_users_utypes where id_users = {user_id})")
                user_weight = cursor.fetchall()

                # 获取用户过敏源
                cursor.execute(f"SELECT allergen_name FROM allergen where id in ("
                            f"select allergen_id from user_allergen where user_id = {user_id})")
                user_allergen = cursor.fetchall()
                if not user_allergen:
                    user_allergen = [("暂无过敏源",)]

                # 获取用户疾病
                cursor.execute(f"select chronic_disease_name from chronic_disease where id in ("
                            f"select disease_id from user_disease where user_id = {user_id})")
                user_disease = cursor.fetchall()
                if not user_disease:
                    user_disease = [("暂无疾病",)]

                # 获取用户体检报告
                cursor.execute(f"select physical_item_name, status from"
                            f" physical_result pr "
                            f"left JOIN physical_item pi on pr.item_id = pi.id "
                            f"where pr.user_id = {user_id}")
                user_physical = cursor.fetchall()

                # 获取食物列表
                cursor.execute("select food_name from foods")
                food_list = cursor.fetchall()

                # 处理体检报告状态
                status_mapping = {0: "偏低", 1: "正常", 2: "偏高"}
                user_results = []
                if not user_physical:
                    user_results.append("一切指标正常")
                else:
                    for item_name, status in user_physical:
                        description = f"{item_name}{status_mapping.get(status, '未知')}"
                        user_results.append(description)

                # 存储数据供下一个步骤使用
                storage.user_weight = user_weight
                storage.user_allergen = user_allergen
                storage.user_disease = user_disease
                storage.user_results = user_results
                storage.food_list = food_list

            finally:
                cursor.close()
                conn.close()
        except Exception as e:
            print(f"获取用户数据失败: {str(e)}")
            raise

    @diet_workflow.chunk()
    def generate_diet_recommendations(inputs, storage):

        # 从storage获取数据
        result = (
            healthy_diet_agent.input({
                "体重类型": storage.user_weight,
                "疾病": storage.user_disease,
                "过敏源": storage.user_allergen,
                "体检报告结果": storage.user_results,
                "食品列表": storage.food_list,
            })
            .instruct(
                "根据用户的体重类型、疾病、过敏源和体检报告结果，了解用户营养需求。"
                "根据现有的食品列表和用户营养需求,为每一个食品打分"
                "确保输出格式简单易懂、直观（比如 淀粉:1000,苹果:200）"
                "若推荐生成不完整或存在问题，请自动补全以确保一致性。"
            )
            .output({"food_score": ("dict",)})
            .start()
        )

        try:
            food_score = result.get("food_score", {})
            # 将食品评分存储到storage中
            storage.food_score = food_score
            
            print(food_score)
            if isinstance(food_score, dict):
                for food, score in food_score.items():
                    print(f"{food}的评分为：{score}")
        except KeyError as e:
            print(f"数据获取失败，缺少预期字段：{e}")
            storage.food_score = {}  # 发生错误时设置空字典
    
    @diet_workflow.chunk()
    def update_food_recommendations(inputs, storage):
        try:
            user_id = storage.user_id  # 使用get方法安全获取user_id

            if user_id is None:
                raise ValueError("未提供user_id参数")
                
            food_id_score_list = []  # 初始化列表

            # 连接数据库
            conn = mysql.connector.connect(
                host="aliyun.ovo.rent",
                user="SmartMalls",
                password="shuwu",
                database="smartmalls"
            )
            
            cursor = conn.cursor()

            try:
                # 修复 f-string 语法
                cursor.execute(f"delete from user_food where user_id = {user_id}")
                
                # 从foods表中查找food_name字段相同的，获取到id
                for food_name in storage.food_score.keys():
                    cursor.execute(f"select id from foods where food_name = '{food_name}'")
                    food_id_result = cursor.fetchone()  # 使用 fetchone() 获取单个结果
                    if food_id_result:
                        food_id = food_id_result[0]  # 获取 ID
                        # 将 user_id、food_id 和分数添加到列表中
                        food_id_score_list.append((user_id, food_id, storage.food_score[food_name]))

                print(food_id_score_list)
                if food_id_score_list:  # 如果列表不为空才执行插入
                    cursor.executemany(
                        "insert into user_food (user_id, food_id, score) values (%s, %s, %s)", 
                        food_id_score_list
                    )
                    conn.commit()
            except Exception as e:
                conn.rollback()  # 添加回滚操作
                print(f"添加失败：{e}")
                raise
            finally:
                cursor.close()
                conn.close()
        except Exception as e:
            print(f"更新食品推荐失败: {str(e)}")
            raise

    # 连接工作流块
    (
        diet_workflow
        .connect_to("fetch_user_data")
        .connect_to("generate_diet_recommendations")
        .connect_to("update_food_recommendations")
    )

    # 可视化工作流
    from mermaid import Mermaid
    Mermaid(diet_workflow.draw())

    return diet_workflow

def get_food_recommendations(user_id):
    """
    获取食品推荐
    :param user_id: 用户ID
    :return: 返回JSON格式的推荐结果
    """
    try:

        workflow = create_diet_workflow()
        result = workflow.start({'user_id': user_id})
        
        # 将结果转换为JSON格式
        return json.dumps({
            'status': 'success',
            'data': result
        }, ensure_ascii=False)
    except Exception as e:
        return json.dumps({
            'status': 'error',
            'message': str(e)
        }, ensure_ascii=False)

if __name__ == "__main__":
    # 从命令行参数获取用户ID
    if len(sys.argv) > 1:
        try:
            user_id = int(sys.argv[1])
            result = get_food_recommendations(user_id)
            print(result)  # 输出JSON结果
        except ValueError:
            print(json.dumps({
                'status': 'error',
                'message': '用户ID必须是整数'
            }, ensure_ascii=False))
    else:
        print(json.dumps({
            'status': 'error',
            'message': '请提供用户ID参数'
        }, ensure_ascii=False))