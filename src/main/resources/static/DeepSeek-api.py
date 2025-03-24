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
        .set_settings("model.OAIClient.url", "https://api.deepseek.com")
        .set_settings("model.OAIClient.auth", {"api_key": "sk-19ac3a78cf744d3cab646fdbad5d8794"})
        .set_settings("model.OAIClient.options", {"model": "deepseek-chat"})
    )


def chunk_list(food_list, chunk_size=50):
    """将食品列表分块"""
    for i in range(0, len(food_list), chunk_size):
        yield food_list[i:i + chunk_size]


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

    def chunk_list(food_list, chunk_size=50):
        """将食品列表分块"""
        for i in range(0, len(food_list), chunk_size):
            yield food_list[i:i + chunk_size]

    @diet_workflow.chunk()
    def generate_diet_recommendations(inputs, storage):
        food_score = {}
        all_chunks = list(chunk_list(storage.food_list))  # 将食品列表分块
        batch_size = 5  # 定义批处理大小（比如一次处理5个分块）
        total_batches = len(all_chunks) // batch_size + (1 if len(all_chunks) % batch_size > 0 else 0)

        for batch_idx in range(total_batches):
            batch_chunks = all_chunks[batch_idx * batch_size:(batch_idx + 1) * batch_size]
            batch_data = []

            for chunk in batch_chunks:
                batch_data.append({
                    "体重类型": storage.user_weight,
                    "疾病": storage.user_disease,
                    "过敏源": storage.user_allergen,
                    "体检报告结果": storage.user_results,
                    "食品列表": chunk,
                })

            # 批量发送给模型
            result = (
                healthy_diet_agent.input(batch_data)
                .instruct("根据用户的体重类型、疾病、过敏源和体检报告结果，了解用户营养需求。"
                          "根据现有的食品列表和用户营养需求,为每一个食品打分"
                          "确保输出格式简单易懂、直观（比如 淀粉:1000,苹果:200）"
                          "若推荐生成不完整或存在问题，请自动补全以确保一致性。")
                .output({"food_scores": ("list",)})  # 返回每个批次的评分列表
                .start()
            )

            # 合并每批次的结果
            for single_chunk_result in result.get("food_scores", []):
                food_score.update(single_chunk_result)

        try:
            # 转换为 JSON 格式
            food_score_json = json.dumps(food_score, ensure_ascii=False)
            # 输出 JSON 数据
            print(food_score_json)
        except KeyError as e:
            print(f"数据获取失败，缺少预期字段：{e}")
            storage.food_score = {}  # 发生错误时设置空字典

    # 连接工作流块
    (
        diet_workflow
        .connect_to("fetch_user_data")
        .connect_to("generate_diet_recommendations")
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