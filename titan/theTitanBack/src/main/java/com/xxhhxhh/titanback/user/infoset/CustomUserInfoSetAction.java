package com.xxhhxhh.titanback.user.infoset;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CustomUserInfoSetAction extends ActionSupport
{
    private int IsSuccess;
    public int getIsSuccess() { return IsSuccess; }
    public void setIsSuccess(int isSuccess) { IsSuccess = isSuccess; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();

        Map<String, String> map = new HashMap<String, String>();
        map.put("my_love", servletRequest.getParameter("myLove"));
        map.put("my_other", servletRequest.getParameter("myOther"));
        map.put("nick_name", servletRequest.getParameter("nickName"));
        setIsSuccess(UserInfoDoUtil.setUserInfoCustom(servletRequest.getParameter("username"), map, GetSessionFactory.sessionFactory.openSession()));

        return SUCCESS;
    }
}
