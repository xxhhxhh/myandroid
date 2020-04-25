package com.xxhhxhh.titanback.datasave.goodorreport;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

public class GoodDoAction extends ActionSupport
{
    /** 此action负责处理点赞事宜
     *需求数据
     * @param id /*被操作的id，可以是文章、随记、评论
     * @param type /*类型：0随记、1文章、2随记评论、3文章评论
     * @return isSuccess /*返回数据，被修改行数
     * **/
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        int id = servletRequest.getParameter("id") != null ?
                Integer.parseInt(servletRequest.getParameter("id")) : 0;
        int type = servletRequest.getParameter("type") != null ?
                Integer.parseInt(servletRequest.getParameter("type")) : 4;

        if(id != 0 && type != 3)
        {
            Session session = GetSessionFactory.sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            switch (type)
            {
                case 0:
                {
                    isSuccess = session.createSQLQuery("update SuiJis set goods = goods + 1 " +
                            "where suiji_id= :id ").setParameter("id", id).executeUpdate();
                    transaction.commit();
                }break;
                case 1:
                {
                    isSuccess = session.createSQLQuery("update Articles set goods = goods + 1 " +
                        "where article_id= :id ").setParameter("id", id).executeUpdate();
                    transaction.commit();
                }break;
                case 2:
                {
                    isSuccess = session.createSQLQuery("update SuiJiComments set goods = goods + 1 " +
                            "where suiji_comment_id= :id ").setParameter("id", id).executeUpdate();
                    transaction.commit();
                }break;
                case 3:
                {
                    isSuccess = session.createSQLQuery("update ArticleComments set goods = goods + 1 " +
                            "where article_comment_id= :id ").setParameter("id", id).executeUpdate();
                    transaction.commit();
                }break;
            }
        }

        return SUCCESS;
    }
}
