package com.xxhhxhh.titanback;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.article.Articles;
import com.xxhhxhh.database.suiji.SuiJis;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchAction  extends ActionSupport
{
    /**此action用于获取查询结果
     * @return 包含id和主要信息
     * **/

    private JSONObject finallyResult = new JSONObject();
    public String getfinallyResult(){return finallyResult.toString().replaceAll("\\\\", "");}


    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        String message = servletRequest.getParameter("message") != null ?
                 servletRequest.getParameter("message") : "";

       message += "%";
        if(!message.equals("%"))
        {
            Session session = GetSessionFactory.sessionFactory.openSession();

            Query query = session.createSQLQuery("select suiji_id, main_message from SuiJis where main_message like '" + message + "'")
                .setMaxResults(5).addScalar("suiji_id", StandardBasicTypes.INTEGER)
                    .addScalar("main_message", StandardBasicTypes.STRING);
            List list = query.getResultList();

            int i = 0;
            i = doResult(list, i, 0);

            Query query1 = session.createSQLQuery("select article_id, article_title from Articles where article_title like '" + message + "'")
                    .setMaxResults(5).addScalar("article_id", StandardBasicTypes.INTEGER)
                    .addScalar("article_title", StandardBasicTypes.STRING);
            List list1 = query1.getResultList();

            doResult(list1, i, 1);
            session.close();
        }

        return SUCCESS;
    }

    private int doResult(List list, int i, int type)
    {
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Object[] objects = (Object[]) iterator.next();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", objects[0]);
            jsonObject.put("message", objects[1]);
            jsonObject.put("type", type);
            finallyResult.put(String.valueOf(i), jsonObject);
            i++;
        }

        return i;
    }
}
