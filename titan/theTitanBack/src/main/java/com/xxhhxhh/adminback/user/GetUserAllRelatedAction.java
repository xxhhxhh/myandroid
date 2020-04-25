package com.xxhhxhh.adminback.user;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.user.Related;
import com.xxhhxhh.titanback.user.infoset.UserRelated;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetUserAllRelatedAction extends ActionSupport
{
    /**此action获取单个用户的全部关系信息
     * @param  username /*用户名
     * @return 包含数据:用户名、被关系用户名、关系模式0关注、1拉黑
     * */

    private JSONObject result = new JSONObject();
    public String getResult() { return result.toString(); }

    @Override
    public String execute() throws Exception
    {

        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        String username = servletRequest.getParameter("username") != null ?
                 servletRequest.getParameter("username") : "";

        if(!username.equals(""))
        {
            Session session = GetSessionFactory.sessionFactory.openSession();
            List<Related> relateds = session.createQuery("from Related  " +
                    " where username= '" + username + "' ", Related.class)
                    .setMaxResults(30).getResultList();

            int i = 0;
            for(Related userRelated : relateds)
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", userRelated.getUsername().getUsername());
                jsonObject.put("to_username", userRelated.getTo_username().getUsername());
                jsonObject.put("type", userRelated.getType());
                result.put(String.valueOf(i), jsonObject);
                i++;
            }

        }


        return SUCCESS;
    }
}
