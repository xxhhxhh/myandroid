package com.xxhhxhh.titanback.article;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.article.Articles;
import com.xxhhxhh.database.suiji.SuiJis;
import com.xxhhxhh.interfaces.GetMessageShow;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.MakeUUID;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import javassist.bytecode.Descriptor;
import net.sf.json.JSONArray;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

//返回一个完整的网页url,和其它数据
public class GetOneArticleAction extends ActionSupport
    implements GetMessageShow
{
    //返回数据
    private String theUrl;//文章url
    private String username;
    private String articleTitle;//标题
    private JSONArray label = new JSONArray();//文章标签
    private Timestamp theDate;//发表时间
    private int commentNumber;//评论数量
    private int goods;//赞数
    private String location;//位置
    private int the_id;//当前查到的id
    private int looksNumber;
    private String nickName;

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public int getLooksNumber() { return looksNumber;}
    public void setLooksNumber(int looksNumber) { this.looksNumber = looksNumber; }
    public int getThe_id() { return the_id; }
    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
    public String getLocation() { return location; }
    public void setGoods(int goods) { this.goods = goods; }
    public int getGoods() { return goods; }
    public void setLabel(JSONArray label) { this.label = label; }
    public String getLabel() { return label.toString() != null ? label.toString() : ""; }
    public void setArticleTitle(String articleTitle) { this.articleTitle = articleTitle; }
    public void setCommentNumber(int commentNumber) { this.commentNumber = commentNumber; }
    public int getCommentNumber() { return commentNumber; }
    public String getArticleTitle() { return articleTitle; }
    public void setLocation(String location) { this.location = location; }
    public Timestamp getTheDate() { return theDate; }
    public void setTheDate(Timestamp theDate) { this.theDate = theDate; }
    public String getTheUrl() { return theUrl; }
    public void setTheUrl(String theUrl) { this.theUrl = theUrl; }
    //需求数据
    private String theUsername;//查看信息的用户
    private Session session;
    private int start;
    private String the_label;

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        //得到过期标签
        start = servletRequest.getParameter("start") != null ?
                Integer.parseInt(servletRequest.getParameter("start")) : 0;
        int article_id = servletRequest.getParameter("article_id") != null ?
                Integer.parseInt(servletRequest.getParameter("article_id")) : 0;
        theUsername = servletRequest.getParameter("username");
        //获取类型,0推荐、1最新、2热门,3其它,4用户，5单独
        int type = Integer.parseInt(servletRequest.getParameter("type") != null ? servletRequest.getParameter("type") : "0");
        the_label = servletRequest.getParameter("the_label") != null ?
        servletRequest.getParameter("the_label") : "";
        session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        switch (type)
        {
            //推荐
            case 0:
            {
                getMessageRecommend();
            }break;
            //最新
            case 1:
            {
                getMessageLatest();
            }break;
            //热门
            case 2:
            {
                getMessageHot();
            }break;
            //其他标签
            case 3:
            {
                if(the_label != null && !the_label.equals(""))
                {
                    getMessageWithLabel(the_label);
                }
            }break;
            //用户获取
            case 4:
            {
                if(theUsername != null && !theUsername.equals(""))
                {
                    getMessageForUser(theUsername);
                }
            }break;
            //单独获取
            case 5:
            {
                getMessageSingleSearch(article_id);
            }break;
        }

        session.close();

        if(getTheUrl() != null && !getTheUrl().equals(""))
        return SUCCESS;
        else
        {
            return ERROR;
        }
    }


    //获取主页显示内容
    @Override
    public void getMessageRecommend()
    {
        if(theUsername != null && !theUsername.equals(""))
        {

            Query query = session.createSQLQuery("select Articles.article_id from Articles, UserInfo, UserLabels, EveryArticleLabels, Related " +
                    "  where (UserLabels.article_love1 = EveryArticleLabels.label_name and EveryArticleLabels.article_id = Articles.article_id\n" +
                    "  or UserLabels.article_love2 = EveryArticleLabels.label_name and EveryArticleLabels.article_id = Articles.article_id\n" +
                    "  or UserLabels.article_love3 = EveryArticleLabels.label_name and EveryArticleLabels.article_id = Articles.article_id)\n" +
                    "  and UserLabels.username = '" + theUsername + "' and (" +
                    "allow_detail = 0 and allow_look_article = 0) " +
                    "  and (Related.username = Articles.username and Related.type != 1) limit :start ,1;")
                    .setParameter("start", start);

            List<Integer> result = query.getResultList();
            if (query.getResultList() != null && !query.getResultList().isEmpty())
            {
                for (int i : result)
                {
                    doResult(i);
                }
            }
            else
                {
               getRandom();
            }
        }
        //随机获取
        else
        {
            getRandom();
        }

    }

    //随机获取
    public void getRandom()
    {
        //随机选出id
        Query query2 = session.createSQLQuery("select article_id from Articles,Related " +
                " where (Related.username = Articles.username and Related.type != 1) " +
                " order by rand() desc limit :start ,1").setParameter("start", start);
        List<Integer> result1 = query2.getResultList();
        //
        if(result1 != null && !result1.isEmpty())
        {
            for(int i : result1)
            {
                doResult(i);
            }
        }
    }

    @Override
    public void getMessageHot()
    {

        Query query = session.createSQLQuery("select article_id from Articles, ArticleLabels,Related " +
                " where  (Related.username = Articles.username and Related.type != 1) " +
                " order by ArticleLabels.label_looks_number desc limit :start ,1").setParameter("start", start);

        List<Integer> result = query.getResultList();
        if(result != null && !result.isEmpty())
        {
            for(Integer key : result)
            {
                doResult(key);
            }
        }
    }

    @Override
    public void getMessageLatest()
    {

        Query query = session.createSQLQuery("select article_id from Articles,Related " +
                " where  (Related.username = Articles.username and Related.type != 1)" +
                " order by the_date desc limit :start ,1").setParameter("start", start);
        List<Integer> result = query.getResultList();
        if(result != null && !result.isEmpty())
        {
            for(Integer key : result)
            {
                doResult(key);
            }
        }
    }

    @Override
    public void getMessageWithLabel(String label)
    {
        Query query = session.createSQLQuery("select Articles.article_id from EveryArticleLabels, Articles, Related " +
                " where (Related.username = Articles.username and Related.type != 1)" +
                " and label_name= :label_name  limit :start ,1").setParameter("start", start)
                .setParameter("label_name", the_label);

        List<Integer> result = query.getResultList();
        if(result != null && !result.isEmpty())
        {
            for(Integer key : result)
            {
                doResult(key);
            }
        }
    }

    @Override
    public void getMessageForUser(String username)
    {

        Query query = session.createSQLQuery("select article_id from Articles" +
                " where username= :username " +
                "  limit :start ,1").setParameter("start", start).setParameter("username", username);

        List<Integer> result = query.getResultList();
        if(result != null && !result.isEmpty())
        {
            for(Integer key : result)
            {
                doResult(key);
            }
        }
    }

    @Override
    public void getMessageSingleSearch(int id)
    {
        doResult(id);
    }

    //结果处理
    private void doResult(int i)
    {
        //获取主要内容
        Query query1 = session.createQuery("from Articles where article_id = :i ", Articles.class).
                setParameter("i", i).setMaxResults(1);
        List<Articles> resultList = query1.getResultList();
        for(Articles article : resultList)
        {
            if(article.getUrl() != null && !article.getUrl().equals(""))
            {
                the_id = article.getArticle_id();
                setArticleTitle(article.getArticle_title());
                setCommentNumber(article.getComment_number());
                setGoods(article.getGoods());
                setTheUrl(article.getUrl());
                setTheDate(article.getThe_date());
                setLocation(article.getLocation());
                setUsername(article.getUsername().getUsername());
                String nickName = UserInfoDoUtil.getNickName(getUsername(), session);
                if(nickName != null)
                {
                    setNickName(nickName);
                }
                else
                {
                    setNickName("");
                }
                setLooksNumber(article.getLooks_number());
                //获取随记标签
                getArticleLabels(the_id);
            }
            else
            {
                break;
            }
        }
    }


    //获取标签
    public void getArticleLabels(int article_id)
    {
        //获取名称

        Query query2 = session.createSQLQuery("select label_name from EveryArticleLabels " +
                "where article_id = " + article_id + ";");
        List<String> labels_names = query2.getResultList();
        //组合数据
        JSONArray labels = new JSONArray();
        for (String labels_name : labels_names)
        {
            labels.put(labels_name);
        }
        setLabel(labels);
    }
}
