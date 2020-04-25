package com.xxhhxhh.titanback.datasave;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

public class SaveLookNumberAction extends ActionSupport
{
    /**此action保存查看信息*/
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        int id = servletRequest.getParameter("id") != null ?
                Integer.parseInt(servletRequest.getParameter("id")) : 0;
        int type = servletRequest.getParameter("type") != null ?
                Integer.parseInt(servletRequest.getParameter("type")) : 2;

        if(id != 0 && type != 2)
        {
            Session session = GetSessionFactory.sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            switch (type)
            {
                case 0:
                {
                    Query query = session.createSQLQuery("update SuiJis set looks_number = looks_number + 1" +
                            " where suiji_id= :id ").setParameter("id", id);
                    isSuccess = query.executeUpdate();
                    transaction.commit();
                    session.close();
                }break;
                case 1:
                {
                    Query query = session.createSQLQuery("update Articles set looks_number = looks_number + 1" +
                            " where article_id= :id ").setParameter("id", id);
                    isSuccess = query.executeUpdate();
                    transaction.commit();
                    session.close();
                }break;
            }
        }

        return SUCCESS;
    }
}
