package com.xxhhxhh.titanback.util;

import net.sf.json.JSONArray;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentAndReplyDo
{
    //获取评论
    public static List<Object> backComment(Session session, int id, String id_name, String tableName, Class theClass, String eyiname)
    {
        List result;

        Transaction transaction = session.getTransaction().isActive() ? session.getTransaction() : session.beginTransaction();
        //查询评论
        Query query = session.createQuery("from " + tableName + " where " + id_name + "=" + id  + " " +
                " and " + eyiname + " < 20", theClass)
                .setMaxResults(10);
        result = query.getResultList();

        return result != null && result.size() > 0 ? result : new ArrayList<>();
    }


    //获取回复
    public static List<Object> getReply(Session session, int comment_id, String comment_name, String tableName, Class theClass, String eyiname, int max)
    {

        Transaction transaction = session.getTransaction().isActive() ? session.getTransaction() : session.beginTransaction();
        Query query = session.createQuery("from " + tableName + " where " + comment_name + "=" + comment_id
        + " and " + eyiname + " < 20", theClass).setMaxResults(max);

        List result = query.getResultList();//结果

        return result != null && result.size() > 0 ? result : new ArrayList<>();
    }

}
