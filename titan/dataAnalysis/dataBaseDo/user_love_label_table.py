import threading
from dataBaseDo import sql_database_connect, get_user
import dataAnalysis.user_love_label_select
import time


# 确定用户喜爱标签
class SetUserLoveLabel(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        # sql数据库对象
        self.cursor = sql_database_connect.DatabaseConnect().get_cursor()
        # 获取用户信息
        self.users = get_user.GetUsers().get_username()
        # 日志
        self.log = open("../doLog/user_love_label_log.txt", "a+", encoding="utf-8")

    # 按用户获取喜爱标签,mongodb表名，用户名
    def get_love_label(self, type, username):
        # 一个对象设置
        one_cursor = dataAnalysis.user_love_label_select.SelectUserLoveLabels()
        one_cursor.set_type(type)
        one_cursor.set_select_id(username)
        one_cursor.select_third()
        # 获取数据
        return one_cursor.get_finally()

    # 用户喜爱标签存入
    def save_love_label(self):
        # 每个用户
        for user in self.users:
            suiji_love = self.get_love_label(0, user[0])
            article_love = self.get_love_label(1, user[0])
            # 判空
            self.cursor.execute("select * from UserLabels where username = '" + user[0] + "';")
            # 不为空，更新
            if self.cursor.fetchone():
                self.cursor.execute("update UserLabels set "
                                    " suiji_love1 = '" + str(suiji_love[0]) + "', "
                                    "suiji_love2 = '" + str(suiji_love[1]) + "', "
                                    "suiji_love3 = '" + str(suiji_love[2]) + "', "
                                    "article_love1 = '" + str(article_love[0]) + "',"
                                    " article_love2 = '" + str(article_love[1]) + "', "
                                    "article_love3 = '" + str(article_love[2]) + "' where username = '" + str(user[0]) + "';")
                self.cursor.connection.commit()
                self.log.write(time.strftime("%Y-%m-%d : %H:%M:%S") + " --成功更新标签,用户:" + user[0] + "\n")
            # 为空，创建
            else:
                self.cursor.execute("insert into UserLabels values( '" + str(user[0]) + "','"
                                    + article_love[0] + "','"
                                    + article_love[1] + "','"
                                    + article_love[2] + "','"
                                    + suiji_love[0] + "','"
                                    + suiji_love[1] + "','"
                                    + suiji_love[2] + "');")
                self.cursor.connection.commit()
                self.log.write(time.strftime("%Y-%m-%d : %H:%M:%S") + " --成功插入标签,用户:" + user[0] + "\n")

    # 关闭
    def finsih_work(self):
        self.log.close()
        self.cursor.close()


# 测试用
a = SetUserLoveLabel()
a.save_love_label()
a.finsih_work()