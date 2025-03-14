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
    def generate_user_score(inputs, storage):
        # 从storage获取数据
        result = (
            healthy_diet_agent.input({
                "体重类型": storage.user_weight,
                "疾病": storage.user_disease,
                "过敏源": storage.user_allergen,
                "体检报告结果": storage.user_results,
            })
            .instruct(
                "根据用户的体重类型、疾病、过敏源和体检报告结果，给用户健康状态评分。"
                "请只返回一个0到100之间的数字，不要返回其他内容。"
            )
            .start()
        )

        try:
            # 确保返回值是字符串
            if isinstance(result, dict):
                score_str = str(result.get("content", "60"))
            else:
                score_str = str(result or "60")
            # 提取数字
            import re
            numbers = re.findall(r'\d+', score_str)
            user_score = int(numbers[0]) if numbers else 60
            # 确保分数在0-100之间
            user_score = max(0, min(100, user_score))
            
            # 存储最终分数
            storage.final_score = user_score
            return user_score
        except Exception as e:
            print(f"获取分数失败：{e}")
            storage.final_score = 60
            return 60

    # 连接工作流块
    (
        diet_workflow
        .connect_to("fetch_user_data")
        .connect_to("generate_user_score")
    )

    # 可视化工作流
    from mermaid import Mermaid
    Mermaid(diet_workflow.draw())
    return diet_workflow


def get_user_score(user_id):
    """
    获取用户健康评分
    :param user_id: 用户ID
    :return: 返回JSON格式的评分结果
    """
    try:
        workflow = create_diet_workflow()
        workflow.start({'user_id': user_id})
        score = vars(workflow.get_runtime_store()).get('final_score')
        # 确保分数是整数
        try:
            health_score = int(float(str(score))) if score is not None else 60
            health_score = max(0, min(100, health_score))  # 确保分数在0-100之间
        except (ValueError, TypeError):
            health_score = 60

        print(health_score)
        return json.dumps({
            'status': 'success',
            'score': health_score
        }, ensure_ascii=False)
    except Exception as e:
        print(f"Error in get_user_score: {str(e)}")  # 添加错误日志
        return json.dumps({
            'status': 'error',
            'message': str(e)
        }, ensure_ascii=False)


if __name__ == "__main__":
    if len(sys.argv) > 1:
        try:
            user_id = int(sys.argv[1])
            result = get_user_score(user_id)
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