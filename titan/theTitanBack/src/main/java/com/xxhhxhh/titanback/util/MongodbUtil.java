package com.xxhhxhh.titanback.util;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MongodbUtil
{
    public static DB db;//数据库
    public static MongoClient client;//连接
    static final String id = "_id";
    static
    {
        String ip = "152.136.116.121";
        int port = 27017;
        //添加验证
        List<ServerAddress> addressList = new ArrayList<ServerAddress>();
        addressList.add(new ServerAddress(ip, port));
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(MongoCredential.createCredential("titan1", "titan", "liu35667".toCharArray()));
        //创建
        client = new MongoClient(addressList, credentials);
        //数据库
        db = client.getDB("titan");
    }


    //获取表
    private DBCollection collection;
    public void setCollection(String name) { collection = db.getCollection(name); }
    public DBCollection getCollection() { return collection; }

    //更新文档
    public void updateOne(String _idValue, Map<String, Object> data)
    {
        //过滤器
        Bson filter = Filters.eq(id, _idValue);
        Document document = new Document();
        //加参
        for(String key : data.keySet())
        {
            document.append(key, data.get(key));
        }
        Document document1 = new Document("$set", document);
    }
}
