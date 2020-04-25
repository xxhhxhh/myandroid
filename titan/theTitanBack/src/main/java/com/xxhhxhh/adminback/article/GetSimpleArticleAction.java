package com.xxhhxhh.adminback.article;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.article.Articles;
import com.xxhhxhh.database.suiji.SuiJis;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetSimpleArticleAction extends ActionSupport
{
    /**此action用于显示文章信息
     * @param username /*用户名
     * @param nowItem /*起始位置
     * @return 文章基本信息，包含:文章标识、文章标题、评论、赞、查看次数
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

            Query query = session.createQuery("from Articles where username= '"+ username +"' ", Articles.class)
                    .setFirstResult(nowItem).setMaxResults(10);

            List<Articles> articles = query.getResultList();

            int i = 0;
            for(Articles article : articles)
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("article_id", article.getArticle_id());
                jsonObject.put("comment_number", article.getComment_number());
                jsonObject.put("location", article.getLocation());
                jsonObject.put("looks_number", article.getLooks_number());
                jsonObject.put("the_date", article.getThe_date().toString());
                jsonObject.put("goods", article.getGoods());
                result.put(String.valueOf(i), jsonObject);
                i++;
            }
            session.close();
        }

        return SUCCESS;
    }
}
