package com.xxhhxhh.adminback.user;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.user.UserInfo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetUserMAinMessageAction extends ActionSupport
{
    /**此action用于获取用户的全部个人信息
     * @param nowItem /*当前获取位置
     * @return json数据
     * */
    private JSONObject data = new JSONObject();
    public String getData() { return data.toString(); }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        int nowItem = servletRequest.getParameter("nowItem") != null ?
                Integer.parseInt(servletRequest.getParameter("nowItem")) : 0;


        Session session = GetSessionFactory.sessionFactory.openSession();

        List<UserInfo> userInfoList = session.createQuery("from UserInfo", UserInfo.class)
                .setFirstResult(nowItem).setMaxResults(10).getResultList();

        if(userInfoList != null && userInfoList.size() > 0)
        {
            int i = 0;
            for(UserInfo userInfo : userInfoList)
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", userInfo.getUsername());
                jsonObject.put("user_article", userInfo.getUser_article());
                jsonObject.put("user_suiji", userInfo.getUser_suiji());
                jsonObject.put("user_fans", userInfo.getUser_fans());
                jsonObject.put("user_focus", userInfo.getUser_focus());
                jsonObject.put("user_eyi", userInfo.getUser_eyi());
                data.put(String.valueOf(i), jsonObject);
                i++;
            }
        }



        return SUCCESS;
    }
}
