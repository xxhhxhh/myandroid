package com.xxhhxhh.titanback.article;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.MakeUUID;
import com.xxhhxhh.titanback.util.MessageSave;
import com.xxhhxhh.titanback.util.SaveFileUtil;
import net.sf.json.JSONArray;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveOneArticleAction extends ActionSupport
{
    //需求数据
    private String username;//用户名
    private String mainHtml;//html文本主体
    private String path;//文件路径
    //发送数据
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }
    public void setIsSuccess(int isSuccess) { this.isSuccess = isSuccess; }

    @Override
    public String execute() throws Exception
    {
        //会话
        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String theIdString = "article_id";
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        //模式
        int mode = servletRequest.getParameter("mode")  != null ?
                Integer.parseInt(servletRequest.getParameter("mode")) : 2;

        int article_id = 0;
        if(mode == 0)
        {
            article_id = MessageSave.makeID(session, "Articles", theIdString);
        }
        else if(mode == 1)
        {
            article_id = servletRequest.getParameter("article_id") != null ?
                    Integer.parseInt(servletRequest.getParameter("article_id")) : 0;
        }

        if(article_id != 0)
        {
            //路径
            String basePath = "C:\\Users\\1\\Desktop\\Titan\\theTitanBack\\src\\main\\resources";
//            String basePath = "/opt/tomcat/webapps/articles";
            //标题
            String articleTitle = servletRequest.getParameter("title");
            //位置
            String location = servletRequest.getParameter("location");
            username = servletRequest.getParameter("username");
            String dirsPath = basePath + "/article/" + username + "-" + article_id;
            String uuid = MakeUUID.makeUUID(username);
            String path1 = "/" + uuid + ".html";
            path = dirsPath + "/" + uuid + ".html";
            //url
            String url = "/images/article/" + username + "-" + article_id + path1;
            mainHtml = servletRequest.getParameter("mainMessage");
            //数据
            Map<String, Object> data = new HashMap<>();
            data.put("articleTitle", articleTitle);
            data.put("location", location);
            data.put("username", username);
            data.put("url", url);
            data.put("file_location", path);
            //信息存储
            setIsSuccess(MessageSave.insertMessage(session, 1, data, article_id, mode));


            //标签存储
            String aa = servletRequest.getParameter("labels");
            if(aa != null && !aa.equals(""))
            {
                JSONArray jsonArray = new JSONArray(aa);
                MessageSave.saveLabels(jsonArray, session, article_id, "ArticleLabels",
                        "EveryArticleLabels", theIdString, mode);
            }

            if(mode == 1)
            {
                resetFiles(session, article_id);
            }

            saveFiles(dirsPath);
            session.close();
        }

        return SUCCESS;
    }

    //清空源文件
    private void resetFiles( Session session, int article_id)
    {
        Query query = session.createSQLQuery("select file_location from Articles " +
                " where article_id= :article_id ").setParameter("article_id", article_id).setMaxResults(1);
        List<String> result = query.getResultList();
        if(result != null && result.size() > 0)
        {
            for(String key : result)
            {
                File file = new File(key);
                file.delete();
            }
        }
    }

    //存入文件
    private void saveFiles(String dirsPath)
    {
        new Thread(() ->
        {
            //创建必备文件夹
            File file = new File(dirsPath);
            if(!file.exists())
            {
                file.mkdirs();
            }
            //存储html主体
            SaveFileUtil.saveHtml(path, mainHtml);
        }).start();
    }
}
