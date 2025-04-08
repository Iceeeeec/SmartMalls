from datetime import datetime

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
    作为健康食谱推荐助手，你专注于根据用户的BMI、过敏源、疾病和体检报告结果，提供个性化的饮食建议。
当用户咨询关于健康饮食、营养搭配或特定健康目标的问题时，你将利用百度Agently技术，结合用户的具体情况，给出专业的解答。
注意：在回答时，要确保信息的科学性和准确性，同时保持沟通的亲和力和易于理解。
回答不能出现差错，并且要表述准确，避免歧义和误导。
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

            finally:
                cursor.close()
                conn.close()
        except Exception as e:
            print(f"获取用户数据失败: {str(e)}")
            raise

    @diet_workflow.chunk()
    def generate_diet_advice(inputs, storage):

        # 从storage获取数据
        result = (
            healthy_diet_agent.input({
                "体重类型": storage.user_weight,
                "疾病": storage.user_disease,
                "过敏源": storage.user_allergen,
                "体检报告结果": storage.user_results,
            })
            .instruct(
                "根据用户的体重类型、疾病、过敏源和体检报告结果，推荐饮食建议"
                "推荐的饮食建议中应举例一些食品以及具体的营养的控制克数(例如: 每日摄入的碳水控制在多少范围)"
                "确保输出格式简单易懂、直观、精炼、不分点，但是字数限制200字左右"
                "若推荐生成不完整或存在问题，请自动补全以确保一致性。"
            )
            .output({"food_advice": ("str",)})
            .start()
        )

        try:
            food_advice = result.get("food_advice", {})
            # 将食品评分存储到storage中
            storage.food_advice = food_advice

            print(food_advice)
        except KeyError as e:
            print(f"数据获取失败，缺少预期字段：{e}")
            storage.food_advice = "暂无建议～"  # 发生错误时设置空字典

    @diet_workflow.chunk()
    def update_user_advice(inputs, storage):
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
                cursor.execute(f"delete from user_advice where user_id = {user_id}")
                # 获取当前时间
                current_time = datetime.now()

                # 将时间格式化为 MySQL DATETIME 格式
                formatted_time = current_time.strftime('%Y-%m-%d %H:%M:%S')

                cursor.execute(f"insert into user_advice (user_id, advice, recommended_time) "
                               f"values ({user_id}, '{storage.food_advice}', '{formatted_time}')")
                conn.commit()
            except Exception as e:
                conn.rollback()  # 添加回滚操作
                print(f"添加失败：{e}")
                raise
            finally:
                cursor.close()
                conn.close()
        except Exception as e:
            print(f"建议失败: {str(e)}")
            raise

    # 连接工作流块
    (
        diet_workflow
        .connect_to("fetch_user_data")
        .connect_to("generate_diet_advice")
        .connect_to("update_user_advice")
    )

    # 可视化工作流
    from mermaid import Mermaid
    Mermaid(diet_workflow.draw())

    return diet_workflow


def get_food_advice(user_id):
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
            result = get_food_advice(user_id)
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