import dataBaseDo.sql_database_connect
import time
import threading


# 创建默认随记标签视图
class CreateDefaultLabelsView(threading.Thread):

    def __init__(self, view_type):
        # 初始化
        threading.Thread.__init__(self)
        # 数据库对象
        self.cursor = dataBaseDo.sql_database_connect.DatabaseConnect().get_cursor()
        # 打开文件
        self.file = open("../doLog/default_label_view_log.txt", "a+", encoding="utf-8")
        # 类型标记,0为随记，1为文章
        self.view_type = view_type
        # 开启
        self.create_view()

    # 是否存在视图
    def is_view_exists(self):
        if self.view_type == 0:
            self.cursor.execute("show tables like 'defaultSuiJiLabels';")
        elif self.view_type == 1:
            self.cursor.execute("show tables like 'defaultArticleLabels';")
        # 存在返回true，否则false
        if self.cursor.fetchone():
            return True
        else:
            return False

    # 视图删除
    def delete_view(self):
        try:
            if self.view_type == 0:
                self.cursor.execute("drop view defaultSuiJiLabels;")
            elif self.view_type == 1:
                self.cursor.execute("drop view defaultArticleLabels;")
        except Exception as e:
            self.log_to(" --随记默认标签视图删除异常：" + str(e))
            return False
        else:
            return True

    # 视图创建
    def create_view(self):
        try:
            # 创建标签视图
            if self.view_type == 0:
                # 视图存在删除
                if self.is_view_exists():
                    self.delete_view()
                sql = "create view defaultSuiJiLabels as select SuiJiLabels.label_name from SuiJiLabels " \
                      "where SuiJiLabels.label_level <= 97 order by SuiJiLabels.label_looks_number desc limit 3;"
                self.cursor.execute(sql)
                # 判断并写入日志
                if self.is_view_exists():
                    self.log_to(" --随记默认标签视图成功：成功创建视图")
                else:
                    self.log_to(" --随记默认标签视图失败：失败创建视图")
                self.close_this()
            # 创建文章视图
            elif self.view_type == 1:
                # 视图存在删除
                if self.is_view_exists():
                    self.delete_view()
                sql = "create view defaultArticleLabels as select ArticleLabels.label_name from ArticleLabels " \
                      "where ArticleLabels.label_level <= 97 order by ArticleLabels.label_looks_number desc limit 3;"
                self.cursor.execute(sql)
                # 判断并写入日志
                if self.is_view_exists():
                    self.log_to(" --文章默认标签视图成功：成功创建视图")
                else:
                    self.log_to(" --文章默认标签视图失败：失败创建视图")
                self.close_this()
        # 异常
        except Exception as e:
            if self.view_type == 0:
                self.log_to(" --随记默认标签视图异常：" + str(e))
            elif self.view_type == 1:
                self.log_to(" --文章默认标签视图异常：" + str(e))
            self.close_this()

    # 日志写入
    def log_to(self, message):
        self.file.write(time.strftime("%Y-%m-%d : %H:%M:%S") + message + "\n")

    # 关闭日志文件
    def close_this(self):
        self.cursor.close()
        self.file.close()


CreateDefaultLabelsView(0).start()
CreateDefaultLabelsView(1).start()