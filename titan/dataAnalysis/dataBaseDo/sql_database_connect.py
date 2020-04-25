import pymysql


# 数据库连接
class DatabaseConnect:

    # 连接数据库
    def __init__(self):
        self.db = pymysql.connect(host="152.136.116.121", port=3306, user='titan', passwd="liu35667", db="titan",charset="utf8")

    # 获取对象
    def get_cursor(self):
        return self.db.cursor()

    # 关闭
    def close_database(self):
        self.db.cursor().close()
        self.db.close()