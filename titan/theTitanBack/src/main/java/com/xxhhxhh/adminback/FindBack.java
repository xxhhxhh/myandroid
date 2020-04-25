package com.xxhhxhh.adminback;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.admin.AdminUserInfo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FindBack extends ActionSupport
{
    /**此方法用于找回账号密码
     * @param identifyText /*识别码
     * @return username /*返回用户名
     *          password /*返回密码
     * */
    private String identifyText;//识别码

    private String username;
    private String password;
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        String identifyText = servletRequest.getParameter("identifyText") != null ?
                servletRequest.getParameter("identifyText") : "";

        if(!identifyText.equals(""))
        {
            Session session = GetSessionFactory.sessionFactory.openSession();

            List<AdminUserInfo> adminUserInf = session.createQuery("from AdminUserInfo " +
                    " where identifyText = :identifyText").setParameter("identifyText", identifyText)
                    .setMaxResults(1).getResultList();

            for(AdminUserInfo adminUserInfo : adminUserInf)
            {
                setUsername(adminUserInfo.getUsername());
                setPassword(adminUserInfo.getPassowrd());
                return SUCCESS;
            }

        }

        return ERROR;
    }
}
