import dataBaseDo.sql_database_connect
import time


# 获取用户信息
class GetUsers:

    def __init__(self):
        self.cursor = dataBaseDo.sql_database_connect.DatabaseConnect().get_cursor()
        # 结果
        self.result_tuple = ()
        # 上次位置
        self.last_number = 0

    # 获取总行数
    def get_all_rows(self):
        self.cursor.execute("select count(*) from UserInfo;")
        return self.cursor.fetchone()[0]

    # 获取用户名，number为数量,默认500,数据返回格式((,),)元组嵌套
    def get_username(self, number=500):
        if self.last_number <= self.get_all_rows():
            self.cursor.execute("select username from UserInfo limit " + str(self.last_number) + "," + str(number) + ";")
            self.result_tuple = self.cursor.fetchall()
            # 修改下次起始位置
            self.last_number = number
            # 日志
            with open("../doLog/userinfo_get_log.txt", "a+", encoding="utf-8") as f:
                f.write(time.strftime("%Y-%m-%d : %H:%M:%S") + " --成功获取用户名信息，数量" + str(self.last_number) + "\n")
        return self.result_tuple

# 测试用
# a = GetUsers()
# print(a.get_username(10))
