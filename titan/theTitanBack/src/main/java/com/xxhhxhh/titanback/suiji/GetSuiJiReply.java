package com.xxhhxhh.titanback.suiji;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.suiji.SuiJiReply;
import com.xxhhxhh.database.user.UserInfo;
import com.xxhhxhh.titanback.util.CommentAndReplyDo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetSuiJiReply extends ActionSupport
{

    //返回数据
    private JSONObject jsonObject = new JSONObject();
    private JSONObject finallyResult = new JSONObject();
    public String getFinallyResult() { return finallyResult.toString().replaceAll("\\\\", ""); }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();

        int comment_id = servletRequest.getParameter("comment_id") != null ?
                Integer.parseInt(servletRequest.getParameter("comment_id")) : 0;

        if(comment_id != 0)
        {
            Session session = GetSessionFactory.sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            List<Object> result = CommentAndReplyDo.getReply(session, comment_id, "suiji_comment_id",
                    "SuiJiReply", SuiJiReply.class, "reply_eyi", 3);

            //生成数据
            int i = 0;
            for(Object iterator : result)
            {
                SuiJiReply suiJiReply = (SuiJiReply) iterator;
                jsonObject.put("suiji_comment_id", suiJiReply.getSuiji_comment_id().getSuiji_comment_id());
                jsonObject.put("reply_message", suiJiReply.getReply_message());
                jsonObject.put("username", suiJiReply.getReply_username().getUsername());
                jsonObject.put("nickName", UserInfoDoUtil.getNickName(suiJiReply.getReply_username().getUsername(), session));
                jsonObject.put("to_username", suiJiReply.getTo_username());
                jsonObject.put("to_nickName", UserInfoDoUtil.getNickName(suiJiReply.getTo_username(), session));
                jsonObject.put("id", suiJiReply.getReply_id());
                finallyResult.put(String.valueOf(i), jsonObject);
                jsonObject = new JSONObject();
                i++;
            }
            session.close();
        }

        return SUCCESS;
    }



}
