package com.xxhhxhh.titanback.datasave.goodorreport;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.MongodbUtil;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class ToReportAction extends ActionSupport
{
    /**此action负责存储用户被举报信息
     * @param username /*用户名
     * @param type /*0不喜欢用户、1不喜欢标签、2不喜欢内容、3举报
     * @param messageType /* 举报内容, 0恶意内容 1社会危害 2内容非法
     * @param message /*信息，包括：用户名、标签
     * */
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        String username = servletRequest.getParameter("username") != null ?
                 servletRequest.getParameter("username") : "";
        int type = servletRequest.getParameter("type") != null ?
                 Integer.parseInt(servletRequest.getParameter("type")) : 4;
        int messageType = servletRequest.getParameter("messageType") != null ?
                 Integer.parseInt(servletRequest.getParameter("messageType")) : 3;
        String message = servletRequest.getParameter("message") != null ?
                 servletRequest.getParameter("message") : "";
        if (!username.equals(""))
        {
            MongodbUtil mongodbUtil = new MongodbUtil();
            mongodbUtil.setCollection("user_report");
            //查询是否存在
            DBCollection dbCollection = mongodbUtil.getCollection();
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("_id", username);
            DBCursor cursor = dbCollection.find(basicDBObject);
            List<DBObject> dbObjects = cursor.toArray();

            if (dbObjects.size() == 0)
            {
                dbObjects.add(basicDBObject);
                dbCollection.insert(dbObjects);
            }

            for (DBObject dbObject : dbObjects)
            {
                if (type < 3)
                {
                    //是否存在
                    BasicDBObject object = new BasicDBObject();
                    if(dbObject.keySet().contains("dislike"))
                    {
                        object = (BasicDBObject) dbObject.get("dislike");
                    }
                    else
                    {
                        dbObject.put("dislike", object);
                    }
                    //二级内容存储
                    switch (type)
                    {
                        case 0:
                            {
                            if (object.keySet().contains("disuser"))
                            {
                                BasicDBObject object1 = (BasicDBObject) object.get("disuser");
                                if (object1.keySet().contains(message))
                                {
                                    int a = object1.getInt(message);
                                    object1.put(message, a + 1);
                                    object.put("disuser", object1);
                                }
                                else if (object1.keySet().size() > 3)
                                {
                                    List<Map.Entry<String, Integer>> list = new ArrayList<>();
                                    Collections.sort(list, Comparator.comparingInt(Map.Entry::getValue));
                                    object1.remove(list.get(0).getKey());
                                    BasicDBObject basicDBObject1 = new BasicDBObject();
                                    basicDBObject1.put(message, 1);
                                    object.put("disuser", basicDBObject1);
                                }
                                else
                                    {
                                    BasicDBObject basicDBObject1 = new BasicDBObject();
                                    basicDBObject1.put(message, 1);
                                    object.put("disuser", basicDBObject1);
                                }
                            }
                            else
                                {
                                BasicDBObject basicDBObject1 = new BasicDBObject();
                                basicDBObject1.put(message, 1);
                                object.put("disuser", basicDBObject1);
                            }
                        }
                        break;
                        case 1:
                            {
                            if (object.keySet().contains("dislabel"))
                            {
                                BasicDBObject object1 = (BasicDBObject) object.get("dislabel");
                                if (object1.keySet().contains(message))
                                {
                                    int a = object1.getInt(message);
                                    object1.put(message, a + 1);
                                    object.put("dislabel", object1);
                                }
                                else if (object1.keySet().size() > 3)
                                {
                                    List<Map.Entry<String, Integer>> list = new ArrayList<>();
                                    Collections.sort(list, Comparator.comparingInt(Map.Entry::getValue));
                                    object1.remove(list.get(0).getKey());
                                    BasicDBObject basicDBObject1 = new BasicDBObject();
                                    basicDBObject1.put(message, 1);
                                    object.put("dislabel", basicDBObject1);
                                }
                                else
                                    {
                                    BasicDBObject basicDBObject1 = new BasicDBObject();
                                    basicDBObject1.put(message, 1);
                                    object.put("dislabel", basicDBObject1);
                                }
                            }
                            else
                                {
                                BasicDBObject basicDBObject1 = new BasicDBObject();
                                basicDBObject1.put(message, 1);
                                object.put("dislabel", basicDBObject1);
                            }
                        }
                        break;
                        case 2:
                            {
                            if (object.keySet().contains("discontent"))
                            {

                            }
                            else
                                {

                            }
                        }
                        break;
                    }

                }
                else if(type == 3)
                {
                    BasicDBObject object = new BasicDBObject();
                   if(dbObject.keySet().contains("report"))
                   {
                       object = (BasicDBObject) dbObject.get("report");
                   }
                   else
                    {
                       dbObject.put("report", object);
                    }

                   switch (messageType)
                   {
                       case 0:
                       {
                           if(object.keySet().contains("0"))
                           {
                               int a = object.getInt("0");
                               object.put("0", a+1);
                           }
                           else
                           {
                               object.put("0", 1);
                           }
                       }break;
                       case 1:
                       {
                           if(object.keySet().contains("1"))
                           {
                               int a = object.getInt("1");
                               object.put("1", a+1);
                           }
                           else
                           {
                               object.put("1", 1);
                           }
                       }break;
                       case 2:
                       {
                           if(object.keySet().contains("2"))
                           {
                               int a = object.getInt("2");
                               object.put("2", a+1);
                           }
                           else
                           {
                               object.put("2", 1);
                           }
                       }break;
                   }

               }

                //更新
                BasicDBObject basicDBObject1 = new BasicDBObject();
                basicDBObject1.put("_id", username);
                dbCollection.update(basicDBObject1, dbObject);
            }
        }
        return SUCCESS;
    }
}
