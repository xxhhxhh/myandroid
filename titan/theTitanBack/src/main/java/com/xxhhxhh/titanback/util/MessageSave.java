package com.xxhhxhh.titanback.util;

import net.sf.json.JSONArray;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.Query;
import java.util.Map;

public class MessageSave
{
    //生成id
    public static int makeID(Session session, String tableName, String theIdString)
    {
        session = GetSessionFactory.sessionFactory.getCurrentSession().isOpen() ?
                GetSessionFactory.sessionFactory.getCurrentSession() : GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.getTransaction().isActive() ? session.getTransaction() : session.beginTransaction();
        Query query1 = session.createSQLQuery("select " + theIdString + " from " + tableName + " order by " + theIdString + " desc limit 1");
        return query1.getResultList() != null && query1.getResultList().size() > 0 ? (int)query1.getResultList().get(0) + 1 : 0;
    }


    /**此方法用于插入数据
     * @param session /*会话
     * @param type /*0随记、1文章
     * @param data /*传入数据
     * @param id /*对应id
     * @param mode /*0插入，1更新
     * */
    public static int insertMessage(Session session, int type, Map<String, Object> data, int id, int mode)
    {
        Transaction transaction = session.getTransaction().isActive() ? session.getTransaction() : session.beginTransaction();

        int line = 0;//被修改行数
        //插入文章
        if(type == 1)
        {
            if(mode == 0)
            {
                try
                {
                    Query query = session.createSQLQuery("INSERT INTO titan.Articles (article_id, article_title, comment_number, location, looks_number, url, username, goods, the_date, file_location) \n" +
                            "VALUES (" + id + " ,'" + data.get("articleTitle") + "', 0, '" + data.get("location") + "', 0," +
                            " '" + data.get("url") + "', '" + data.get("username") + "', 0, DEFAULT, '" + data.get("file_location") + "') ;");
                    line = query.executeUpdate();
                    transaction.commit();

                    Query query1 = session.createSQLQuery("update UserInfo set user_article = user_article + 1 " +
                            " where username = :username ").setParameter("username", data.get("username"));
                    line = query1.executeUpdate();
                    transaction.commit();
                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }
            }
            //更新文章
            else if(mode == 1)
            {
                try
                {
                    Query query = session.createSQLQuery("update titan.Articles set article_title = :article_title " +
                            ", location = :location , url = :url , " +
                            " the_date = now() , file_location = :file_location " +
                            " where article_id = :article_id")
                            .setParameter("article_id", id).setParameter("article_title", data.get("articleTitle"))
                            .setParameter("location", data.get("location")).setParameter("url", data.get("url"))
                            .setParameter("file_location", data.get("file_location"));
                    line = query.executeUpdate();
                    transaction.commit();

                    Query query1 = session.createSQLQuery("update UserInfo set user_suiji = user_suiji + 1 " +
                            " where username = :username ").setParameter("username", data.get("username"));
                    line = query1.executeUpdate();
                    transaction.commit();
                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }
            }

        }
        //插入随记
        else if(type == 0)
        {
            if(mode == 0)
            {
                try
                {

                    Query query = session.createSQLQuery("INSERT INTO titan.SuiJis (suiji_id, comment_number, have_file, location, looks_number, main_message, username, goods, the_date, file_type) \n" +
                            "VALUES (" + id + " ," + " 0, " + data.get("have_file") + ", '" + data.get("location") + "', 0," +
                            " '" + data.get("main_message") + "', '" + data.get("username") + "', 0, DEFAULT, " + data.get("file_type") + ")");
                    line = query.executeUpdate();
                    transaction.commit();
                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }
            }
            //更新随记
            else if(mode == 1)
            {
                try
                {
                    Query query = session.createSQLQuery("update SuiJis set  main_message = :main_message " +
                            ", location = :location , " +
                            " the_date = default , have_file = :have_file , the_date = now(), file_type = :file_type " +
                            " where  suiji_id= :suiji_id ")
                            .setParameter("suiji_id", id).setParameter("main_message", data.get("main_message"))
                            .setParameter("location", data.get("location")).setParameter("file_type", data.get("file_type"))
                            .setParameter("have_file", data.get("have_file"));
                    line = query.executeUpdate();
                    transaction.commit();
                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }
            }
        }

        return line;
    }

    //标签存入
    public static void saveLabels(JSONArray labels, Session session, int id, String labelsTable, String everyLabelsTable, String theIdString, int mode)
    {
        if(mode == 0)
        {
            for (int i = 0; i < labels.length(); i++)
            {
                Transaction transaction = session.getTransaction().isActive() ? session.getTransaction() : session.beginTransaction();
                //是否存在这个标签
                Query query1 = session.createSQLQuery("select label_name from " + labelsTable +
                        " where label_name = :key limit 1;").setParameter("key", labels.getString(i));
                if (query1.getResultList() != null && query1.getResultList().size() > 0)
                {
                    //插入
                    Query query = session.createSQLQuery("insert into " + everyLabelsTable + " (label_name, " + theIdString + ") " +
                            "values( :key, :id)").setParameter("key", labels.getString(i)).setParameter("id", id);
                    query.executeUpdate();
                    transaction.commit();
                    labelsFabiao(session, labelsTable, labels.getString(i));
                }

            }
        }
        else if(mode == 1)
        {

            for (int i = 0; i < labels.length(); i++)
            {
                Transaction transaction = session.getTransaction().isActive() ? session.getTransaction() : session.beginTransaction();
                //是否存在这个标签
                Query query1 = session.createSQLQuery("select label_name from " + labelsTable +
                        " where label_name = :key limit 1;").setParameter("key", labels.getString(i));
                if (query1.getResultList() != null && query1.getResultList().size() > 0)
                {
                    //插入
                    Query query = session.createSQLQuery("update " + everyLabelsTable + " set label_name = :key where " + theIdString
                            + " = :id ").setParameter("key", labels.getString(i)).setParameter("id", id);
                    query.executeUpdate();
                    transaction.commit();
                    labelsFabiao(session, labelsTable, labels.getString(i));
                }

            }
        }
    }

    //标签发表修改
    public static void labelsFabiao(Session session, String labelsTable, String labelName)
    {
        Transaction transaction = session.getTransaction().isActive() ? session.getTransaction() : session.beginTransaction();
        Query query = session.createSQLQuery("update " + labelsTable + " set label_fabiao_number = label_fabiao_number + 1" +
                " where label_name='" + labelName + "' ;");
        query.executeUpdate();
        transaction.commit();
    }

}
