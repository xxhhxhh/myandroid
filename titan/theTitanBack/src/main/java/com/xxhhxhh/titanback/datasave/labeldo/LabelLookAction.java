package com.xxhhxhh.titanback.datasave.labeldo;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.MongodbUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class LabelLookAction extends ActionSupport
{
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }


    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        String data = servletRequest.getParameter("data");
        String username = servletRequest.getParameter("username");
        //随记还是文章，0随记1文章
        int type = servletRequest.getParameter("type") != null ?
                Integer.parseInt(servletRequest.getParameter("type")) : 2;
        //发表还是观看,0观看，1发表
        int type2 = servletRequest.getParameter("type2") != null ?
                Integer.parseInt(servletRequest.getParameter("type2")) : 2;

        JSONArray jsonArray = new JSONArray();
        if(data != null && !data.equals(""))
        {
              jsonArray = new JSONArray(data);
        }

        if(username == null || username.equals(""))
        {
            saveLabelLook(jsonArray, type, type2);
            return SUCCESS;
        }

        MongodbUtil mongodbUtil = new MongodbUtil();

        if(type == 0)
        {
            mongodbUtil.setCollection("user_suiji_label_look");
        }
        else if(type == 1)
        {
            mongodbUtil.setCollection("user_article_label_look");
        }

        //查询
        DBCollection collection = mongodbUtil.getCollection();
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("_id", username);
        DBCursor cursor = collection.find(basicDBObject);
        List<DBObject> dbObjects = cursor.toArray();

        //长度存在
        if(jsonArray.length() > 0 && type2 == 0)
        {

            //存在
            if (dbObjects.size() > 0)
            {
                for (DBObject dbObject1 : dbObjects)
                {
                    BasicDBObject dbObject = (BasicDBObject) dbObject1.get("label_look");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        if (dbObject.keySet().contains(jsonArray.getString(i)))
                        {
                            int j = dbObject.getInt(jsonArray.getString(i));

                            dbObject.put(jsonArray.getString(i), j + 1);

                        }
                        else
                            {
                            dbObject.put(jsonArray.getString(i), 1);
                        }
                    }

                    BasicDBObject basicDBObject1 = new BasicDBObject();
                    basicDBObject1.put("_id", username);
                    basicDBObject1.put("label_look", dbObject);

                    mongodbUtil.getCollection().update(basicDBObject, basicDBObject1);
                }
            }
            //不存在，插入
            else
                {
                BasicDBObject basicDBObject1 = new BasicDBObject();
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    basicDBObject1.put(jsonArray.getString(i), 1);
                }

                //插入新数据
                BasicDBObject basicDBObject2 = new BasicDBObject();
                basicDBObject2.put("_id", username);
                basicDBObject2.put("label_look", basicDBObject1);
                mongodbUtil.getCollection().insert(basicDBObject2);
            }
        }
        saveLabelLook(jsonArray, type, type2);

        return SUCCESS;
    }

    //保存标签到总数据库
    public void saveLabelLook(JSONArray data, int type, int type2)
    {
        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        if(type == 0 && type2 == 0)
        {

            Query query = session.createSQLQuery("update SuiJiLabels set " +
                    "label_looks_number = label_looks_number + 1 where label_name in ( :list )")
                    .setParameterList("list", data.toArray());
            isSuccess = query.executeUpdate();
            transaction.commit();
            session.close();
        }
        else if(type == 1 && type2 == 0)
        {
            Query query = session.createSQLQuery("update ArticleLabels set " +
                    "label_looks_number = label_looks_number + 1 where label_name in ( :list )")
                    .setParameterList("list", data.toArray());
            isSuccess = query.executeUpdate();
            transaction.commit();
            session.close();
        }
        else if(type == 0 && type2 == 1)
        {
            Query query = session.createSQLQuery("update ArticleLabels set " +
                    "label_fabiao_number = label_fabiao_number + 1 where label_name in ( :list )")
                    .setParameterList("list", data.toArray());
            isSuccess = query.executeUpdate();
            transaction.commit();
            session.close();
        }
        else if(type == 1 && type2 == 1)
        {
            Query query = session.createSQLQuery("update ArticleLabels set " +
                    "label_fabiao_number = label_fabiao_number + 1 where label_name in ( :list )")
                    .setParameterList("list", data.toArray());
            isSuccess = query.executeUpdate();
            transaction.commit();
            session.close();
        }
    }
}
