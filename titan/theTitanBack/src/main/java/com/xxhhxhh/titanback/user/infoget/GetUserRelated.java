package com.xxhhxhh.titanback.user.infoget;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.user.infoset.UserRelated;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import net.sf.json.JSONArray;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetUserRelated extends ActionSupport
{
    /** 此action返回用户关系
     * /*需求数据
     * @param username /*关系用户名
     * @param type /*模式，0关注，1拉黑
     * @param serachType /* 0全部，1查找
     * */

    /** 返回数据
     * @return  toUsername /*被关系用户,返回json数组
     * */
    private JSONArray toUsernames = new JSONArray();
    public String getToUsernames() { return toUsernames.toString(); }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();

        String username = servletRequest.getParameter("username");
        String toUsername = servletRequest.getParameter("toUsername") != null ?
                servletRequest.getParameter("toUsername") : "";
        int type = servletRequest.getParameter("type") != null ?
                Integer.parseInt(servletRequest.getParameter("type")) : 2;
        int searchType = servletRequest.getParameter("searchType") != null ?
                Integer.parseInt(servletRequest.getParameter("searchType")) : 2;

        if(type != 2 && username != null && !username.equals("") && searchType != 2)
        {
            switch (searchType)
            {
                case 0:
                {
                    Session session = GetSessionFactory.sessionFactory.openSession();
                    Transaction transaction = session.beginTransaction();

                    Query query = session.createSQLQuery("select to_username from Related " +
                            " where username=:username and type= :type limit 30 ;")
                            .setParameter("username", username).setParameter("type", type);
                    List<String> list = query.getResultList();
                    session.close();
                    //数据存储
                    if(list != null && list.size() > 0)
                    {
                        for(String key :list)
                        {
                            toUsernames.put(key);
                        }
                    }
                }break;
                case 1:
                {
                    if(toUsername != null && !toUsername.equals(""))
                    {
                        Session session = GetSessionFactory.sessionFactory.openSession();
                        Query query = session.createSQLQuery("select to_username from Related " +
                                " where username=:username and type=0 and to_username= :toUsername limit 1 ;")
                                .setParameter("username", username).setParameter("toUsername", toUsername);
                        List<String> list = query.getResultList();
                        session.close();
                        //数据存储
                        if(list != null && list.size() > 0)
                        {
                            for(String key :list)
                            {
                                toUsernames.put(key);
                            }
                        }
                    }
                }

            }
        }

        return SUCCESS;
    }
}
