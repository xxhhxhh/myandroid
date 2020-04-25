package com.xxhhxhh.titanback.user.infoset;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.user.UserInfo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

public class ResetUserPhone extends ActionSupport
{

    //返回数据
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest httpServletRequest = ServletActionContext.getRequest();

        String username = httpServletRequest.getParameter("username");
        String oldUsername = httpServletRequest.getParameter("oldUsername");
        String idNumber = httpServletRequest.getParameter("idNumber");
        String realName = httpServletRequest.getParameter("realName");

        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //不存在用户名
        if(!UserInfoDoUtil.findUsername(username, session))
        {
            //查询信息正确
            Query query = session.createSQLQuery("select username from UserInfo " +
                    "where username=:oldusername and id_number=:idNumber and real_name=:realName  limit 1")
                    .setParameter("oldusername", oldUsername).setParameter("idNumber", idNumber)
                    .setParameter("realName", realName);
            //正确则更新
            if(query.getResultList() != null && !query.getResultList().isEmpty())
            {
                Query query1 = session.createSQLQuery("update UserInfo set username='" + username + "'" +
                        "where username='" + oldUsername + "'");
                query1.executeUpdate();
                transaction.commit();
                //二次查询
                if(UserInfoDoUtil.findUsername(username, session))
                {
                    isSuccess = 1;
                }
            }
        }
        session.close();
        return SUCCESS;
    }
}
