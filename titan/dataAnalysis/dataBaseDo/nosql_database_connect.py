# mongodb数据库连接
import pymongo


class DataBaseConnect:

    def __init__(self):
        self.client = pymongo.mongo_client.MongoClient("mongodb://titan1:liu35667@152.136.116.121:27017/titan")
        self.db = self.client.titan

    # 获取对象
    def get_db(self):
        return self.db

    # 关闭
    def close_to(self):
        self.client.close()
