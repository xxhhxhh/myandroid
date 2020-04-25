package com.xxhhxhh.titanback.suiji;

import com.mysql.cj.xdevapi.JsonArray;
import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.article.ArticleComments;
import com.xxhhxhh.database.suiji.SuiJiComments;
import com.xxhhxhh.titanback.util.CommentAndReplyDo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSuiJiCommentAction extends ActionSupport
{

    public static final String tableName = "SuiJiComments";
    public static final String id_name = "suiji_id";
    ///返回数据
    private JSONObject resultData = new JSONObject();
    private JSONObject finallyResult = new JSONObject();
    public String getfinallyResult(){return finallyResult.toString().replaceAll("\\\\", "");}

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        //需求数据
        int suiji_id = servletRequest.getParameter("suiji_id") != null ?
                Integer.parseInt(servletRequest.getParameter("suiji_id")) : 0;

        if(suiji_id != 0)
        {
            Session session = GetSessionFactory.sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            List<Object> result = CommentAndReplyDo.backComment(session,
                    suiji_id, id_name, tableName, SuiJiComments.class, "eyi_level");

            //迭代对象
            int i = 0;
            for(Object iterator : result)
            {
                SuiJiComments suiJiComments = (SuiJiComments)iterator;
                resultData.put("goods", suiJiComments.getGoods());
                resultData.put("commentMessage", suiJiComments.getComment_message());
                resultData.put("replyNumber", suiJiComments.getReply_number());
                resultData.put("username", suiJiComments.getComment_username().getUsername());
                resultData.put("nickName", UserInfoDoUtil.getNickName(suiJiComments.getComment_username().getUsername(), session));
                resultData.put("suiji_comment_id", suiJiComments.getSuiji_comment_id());
                finallyResult.put(String.valueOf(i), resultData);
                resultData = new JSONObject();
                i++;

            }
            session.close();
        }

        return SUCCESS;
    }
}
