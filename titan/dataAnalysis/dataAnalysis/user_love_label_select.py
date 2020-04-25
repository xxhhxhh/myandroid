import dataBaseDo.nosql_database_connect
import dataBaseDo.sql_database_connect
import time


# 选取用户喜爱标签
# 创建实例后先设置表和表内主键
class SelectUserLoveLabels:

    def __init__(self):
        # nosql数据库
        self.db = dataBaseDo.nosql_database_connect.DataBaseConnect().get_db()
        # sql数据库
        self.sql_db = dataBaseDo.sql_database_connect.DatabaseConnect().get_cursor()
        # 最终结果列表
        self.finally_result = []

    # 设置标识，随记还是文章
    def set_type(self, type):
        self.type = type
        self.set_one_collection()

    # 设置表
    def set_one_collection(self):
        # 根据模式设置
        if self.type == 0:
            self.collection = self.db["user_suiji_label_look"]
        elif self.type == 1:
            self.collection = self.db["user_article_label_look"]

    # 设置选择内容
    def set_select_id(self, _id):
        self.select_id = _id

    # 获取对应内容
    def get_labels(self):
        self.labels = self.collection.find({"_id": self.select_id})
        self.labels_length = self.collection.count_documents({"_id": self.select_id})

    # 选择标签，选取三个最多的
    def select_third(self):
        # 获取mongodb标签
        self.get_labels()
        if self.labels_length != 0:
            # 遍历结果对象
            for one in self.labels:
                # 从大到小排序
                result = sorted(one['label_look'].items(), key=lambda item: item[1], reverse=True)
                # 结果长度判断并确定最终结果
                if len(result) >= 3:
                    for i in range(3):
                        self.finally_result.append(result[i][0])
                else:
                    for i in range(len(result)):
                        self.finally_result.append(result[i][0])
                    self.full_the_labels(len(result))
                with open("../doLog/user_love_label_log.txt", "a+", encoding="utf-8") as f:
                    f.write(time.strftime("%Y-%m-%d : %H:%M:%S") + " --成功选择标签\n")
        # 使用默认标签
        else:
            if self.type == 0:
                self.sql_db.execute("select label_name from defaultSuiJiLabels limit 3")
                for i in self.sql_db.fetchall():
                    self.finally_result.append(i[0])
            elif self.type == 1:
                self.sql_db.execute("select label_name from defaultArticleLabels limit 3")
                for i in self.sql_db.fetchall():
                    print(i[0])
                    self.finally_result.append(i[0])
            with open("../doLog/user_love_label_log.txt", "a+", encoding="utf-8") as f:
                f.write(time.strftime("%Y-%m-%d : %H:%M:%S") + " --使用默认标签\n")

    # 标签补充
    def full_the_labels(self, the_len):
        a = 0
        if self.type == 0:
            for i in range(the_len, 3):
                self.sql_db.execute("select label_name from defaultSuiJiLabels limit " + str(a) + ",1")
                one = self.sql_db.fetchone()[0]
                if self.is_exist_label(one):
                    a += 1
                    self.sql_db.execute("select label_name from defaultSuiJiLabels limit " + str(a) + ",1")
                    self.finally_result.append(self.sql_db.fetchone()[0])
                else:
                    self.finally_result.append(self.sql_db.fetchone()[0])
                    a += 1
        elif self.type == 1:
            for i in range(the_len, 3):
                self.sql_db.execute("select label_name from defaultArticleLabels limit " + str(a) + ",1")
                one = self.sql_db.fetchone()[0]
                # 存在标签,下一个
                if self.is_exist_label(one):
                    a += 1
                    self.sql_db.execute("select label_name from defaultArticleLabels limit " + str(a) + ",1")
                    self.finally_result.append(self.sql_db.fetchone()[0])
                else:
                    self.finally_result.append(self.sql_db.fetchone()[0])
                    a += 1

    # 如果标签已经存在
    def is_exist_label(self, label):
        for key in self.finally_result:
            if label == key:
                return True
        return False

    # 确定最终结果
    def get_finally(self):
        return self.finally_result


# 测试用
# a = SelectUserLoveLabels()
# a.set_type(1)
# a.set_select_id("18722112428")
# a.select_third()
# print(a.get_finally())