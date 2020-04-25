package com.xxhhxhh.titanback.user.infoset;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SwitchUserInfoSetAction extends ActionSupport
{
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }
    public void setIsSuccess(int isSuccess) { this.isSuccess = isSuccess; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        //获取数据
        Map<String,String> map = new HashMap<>();
        map.put("allow_detail", servletRequest.getParameter("allowDetail"));
        map.put("allow_look_article", servletRequest.getParameter("allowLookArticle"));
        map.put("allow_look_suiji", servletRequest.getParameter("allowLookSuiJi"));
        map.put("allow_recommend", servletRequest.getParameter("allowRecommend"));
        //修改
        setIsSuccess(UserInfoDoUtil.setUserInfoSwitch(servletRequest.getParameter("username"), map, GetSessionFactory.sessionFactory.openSession()));
        return SUCCESS;
    }
}
