package com.xxhhxhh.titanback.util;

import com.xxhhxhh.database.user.UserInfo;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.*;

public class UserInfoDoUtil
{

    //用户名是否存在
    public static boolean findUsername(String username, Session session)
    {
        if(!session.isOpen())
        {
            session = GetSessionFactory.sessionFactory.openSession();
        }

        if(username != null)
        {
            //查询
            Query query = session.createSQLQuery("select username from UserInfo where username = '" + username + "' limit 1;");
            List user = query.getResultList();
            //找到内容并二次确认
            if(user.size() > 0)
            {
                if(user.get(0).equals(username))
                {
                    return true;
                }
            }
        }

        return false;
    }

    //找密码
    public static String findPassword(String username, Session session)
    {
        Query query = session.createSQLQuery("select password from UserInfo where username = '" + username + "' limit 1;");
        List password = query.getResultList();
        session.clear();
        //找到内容并二次确认
        if(password.size() > 0)
        {
            return (String) password.get(0);
        }
        return null;
    }

    //查找用户信息
    public static UserInfo findCustomUserInfo(String username, Session session)
    {
        if (session.getTransaction().isActive())
        {
            session.getTransaction();
        }
        else
            {
            session.beginTransaction();
        }
        //查询
        Query query = session.createQuery("from UserInfo where username=:username1", UserInfo.class).
                setParameter("username1", username).setMaxResults(1);
        List result = query.getResultList();
        session.getTransaction().commit();

        //判空
        if(result != null && result.size() > 0)
        {
            return (UserInfo) result.get(0);
        }

        return null;
    }

    //获取昵称
    public  static  String getNickName(String username, Session session)
    {
        Query query = session.createSQLQuery("select nick_name from UserInfo" +
                " where username= :username ").setParameter("username", username).setMaxResults(1);
        List<String> list = query.getResultList();

        if(list != null && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////
    //信息修改，传入map对象，key为列名，value为要修改的值
    //修改自定义信息
    public static int setUserInfoCustom(String username, Map<String, String> alterMessage, Session session)
    {
        if (session.getTransaction().isActive()) {
            session.getTransaction();
        } else {
            session.beginTransaction();
        }
        StringBuilder sql = new StringBuilder("update UserInfo set ");
        int i = 0;
        //添加修改内容
        for(String key : alterMessage.keySet())
        {
            if(i++ != alterMessage.size() - 1)
            {
                sql.append(key).append(" = '").append(alterMessage.get(key)).append("', ");
            }
            else
            {
                sql.append(key).append(" = '").append(alterMessage.get(key)).append("' where username = '").append(username).append("';");
            }
        }
        //创建查询
        Query query = session.createSQLQuery(sql.toString());
        int result = query.executeUpdate();
        session.getTransaction().commit();
        return result;
    }

    //修改状态控制信息
    public static int setUserInfoSwitch(String username, Map<String, String> alterSwitch, Session session)
    {
        if(!session.getTransaction().isActive())
        {
            session.beginTransaction();
        }
        else
        {
            session.getTransaction();
        }
        StringBuilder sql = new StringBuilder("update UserInfo set ");
        int i = 0;
        //添加修改内容
        for(String key : alterSwitch.keySet())
        {
            if(i++ != alterSwitch.size() - 1)
            {
                sql.append(key).append(" = ").append(alterSwitch.get(key)).append(", ");
            }
            else
            {
                sql.append(key).append(" = ").append(alterSwitch.get(key)).append(" where username = '").append(username).append("';");
            }
        }
        //创建查询
        Query query = session.createSQLQuery(sql.toString());
        int result = query.executeUpdate();
        session.getTransaction().commit();
        return result;
    }

    //修改登录日期
    public static void setLoginDate(String username, Session session)
    {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        Query query = session.createSQLQuery("update UserInfo set " +
                "login_date = '" + date +
                "' where username = '" + username + "';");
        query.executeUpdate();
        session.getTransaction().commit();
    }
}
