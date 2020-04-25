package com.xxhhxhh.adminback.suiji;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.suiji.SuiJis;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetSimpleSuiJiAction extends ActionSupport
{

    /**此action用于获取随记基本信息
     * @param username /*用户名
     * @return 随记信息数据，包含:随记标识、评论数量、赞数、发表位置、主要信息、日期、用户
     * */

    private JSONObject result = new JSONObject();
    public String getResult() { return result.toString(); }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        String username = servletRequest.getParameter("username") != null ?
                servletRequest.getParameter("username") : "";
        int nowItem = servletRequest.getParameter("nowItem") != null ?
                Integer.parseInt(servletRequest.getParameter("nowItem")) : 0;
        if(!username.equals(""))
        {
            Session session = GetSessionFactory.sessionFactory.openSession();

            Query query = session.createQuery("from SuiJis where username= '" + username + "' ", SuiJis.class)
                    .setFirstResult(nowItem).setMaxResults(10);

            List<SuiJis> suiJis = query.getResultList();

            int i = 0;
            for(SuiJis suiJis1 : suiJis)
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("suiji_id", suiJis1.getSuiji_id());
                jsonObject.put("comment_number", suiJis1.getComment_number());
                jsonObject.put("location", suiJis1.getLocation());
                jsonObject.put("looks_number", suiJis1.getLooks_number());
                jsonObject.put("the_date", suiJis1.getThe_date().toString());
                jsonObject.put("goods", suiJis1.getGoods());
                result.put(String.valueOf(i), jsonObject);
                i++;
            }
        session.close();
        }

        return SUCCESS;
    }
}
