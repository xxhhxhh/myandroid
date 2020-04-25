package com.xxhhxhh.titanback.datasave.labeldo;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import net.sf.json.JSONArray;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;

public class GetLabelsAction extends ActionSupport
{

    //返回数据
    private JSONArray suiJiLabels;
    private JSONArray articleLabels;
    public String getArticleLabels() { return articleLabels.toString(); }
    public String getSuiJiLabels() { return suiJiLabels.toString(); }

    @Override
    public String execute() throws Exception
    {
        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery("select label_name from SuiJiLabels;");
        Query query1 = session.createSQLQuery("select label_name from ArticleLabels;");
        suiJiLabels = new JSONArray();
        //随记标签
        for(Object key : query.getResultList())
        {
            suiJiLabels.put((String) key);
        }
        articleLabels = new JSONArray();
        //文章标签
        for(Object key : query1.getResultList())
        {
            articleLabels.put((String)key);
        }


        return SUCCESS;
    }
}
