package com.xxhhxhh.titanback.suiji;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//保存随记
public class SaveSuiJiAction extends ActionSupport
{
    //需求数据
    private String username;
    private String basePath = "C:\\Users\\1\\Desktop\\Titan\\theTitanBack\\src\\main\\resources";
//    private String basePath = "/opt/tomcat/webapps/suijis";
    private String dirPath;
    private Session session;
    private int suiji_id;//id
    private Map<Integer, String> photoStrings = new HashMap<>();//图片字符串i
    private String fileType = "0";
    private Map<Integer, String> mp4File = new HashMap<>();
    private Transaction transaction;
    //返回数据
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }
    public void setIsSuccess(int isSuccess) { this.isSuccess = isSuccess; }

    @Override
    public String execute() throws Exception
    {
        session = GetSessionFactory.sessionFactory.openSession();
        transaction = session.beginTransaction();

        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        //模式
        int mode = servletRequest.getParameter("mode") != null ?
                Integer.parseInt(servletRequest.getParameter("mode")) : 2;

        if(mode == 0)
        {
            suiji_id = MessageSave.makeID(session, "SuiJis", "suiji_id");
        }
       else if(mode == 1)
        {
            suiji_id = servletRequest.getParameter("suiji_id") != null ?
                    Integer.parseInt(servletRequest.getParameter("suiji_id")) : 0;
        }
        //内容
        String main_message = servletRequest.getParameter("mainMessage");
        //位置
        String location = servletRequest.getParameter("location");
        username = servletRequest.getParameter("username");
        //文件存储位置
        dirPath = username + "-" + suiji_id;
        //文件数据
        String aa = servletRequest.getParameter("fileStrings");
        fileType = servletRequest.getParameter("fileType") != null ?
                servletRequest.getParameter("fileType") : "0";
        if(aa != null && !aa.equals(""))
        {
            if(fileType.equals("0"))
            {
                JSONArray jsonArray = new JSONArray(aa);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    photoStrings.put(i, jsonArray.getString(i));
                }
            }
            else if(fileType.equals("1"))
            {
                JSONArray jsonArray = new JSONArray(aa);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    mp4File.put(i, jsonArray.getString(0));
                }
            }
        }
        //文件个数
        int haveFile = photoStrings != null && photoStrings.size() > 0 ? photoStrings.size() : 0;
        Map<String, Object> data = new HashMap<>();
        data.put("main_message", main_message);
        data.put("location", location);
        data.put("username", username);
        data.put("have_file", haveFile);
        data.put("file_type", fileType);

        //开始存储
        setIsSuccess(MessageSave.insertMessage(session, 0, data, suiji_id, mode));

        if(mode == 0)
        {
            //标签存储
            String labels1 = servletRequest.getParameter("labels");
            if(labels1 != null && !labels1.equals(""))
            {
                JSONArray jsonArray = new JSONArray(labels1);
                String theIdString = "suiji_id";
                MessageSave.saveLabels(jsonArray, session, suiji_id,
                        "SuiJiLabels", "EverySuiJiLabels", theIdString, mode);
            }
        }

        //清空文件
        if(mode == 1)
        {
            resetFiles();
        }

        saveFiles();
        return SUCCESS;
    }

    //清空源文件
    private void resetFiles()
    {

        Query query1 = session.createSQLQuery("select file_location from SuiJiFiles " +
                " where suiji_id = :suiji_id ").setParameter("suiji_id", suiji_id);

        List<String> result = query1.getResultList();
        if(result != null && result.size() > 0)
        {
            for(String key : result)
            {
                File file = new File(key);
                file.delete();
            }
        }

        Query query = session.createSQLQuery("delete from SuiJiFiles where suiji_id= :suiji_id ")
                .setParameter("suiji_id", suiji_id);
        query.executeUpdate();
        session.getTransaction().commit();
    }

    //存入文件
    private void saveFiles()
    {
        new Thread(() ->
        {
            String dirsPath = basePath + "/suiji/" + dirPath;
            //创建必备文件夹
            File file = new File(dirsPath);
            if(!file.exists())
            {
                file.mkdirs();
            }
            if(fileType.equals("0"))
            {
                //存储图片附件
                for(Integer key : photoStrings.keySet())
                {
                    SaveFileUtil.saveFile(dirsPath + "/" + MakeUUID.makeUUID(username) + ".png", photoStrings.get(key), session, suiji_id, key);
                }
            }
            else if(fileType.equals("1"))
            {
                //存储视频
                for(Integer key : mp4File.keySet())
                {
                    SaveFileUtil.saveFile(dirsPath + "/" + MakeUUID.makeUUID(username) + ".mp4", mp4File.get(key), session, suiji_id, key);
                }
            }
        }).start();
    }
}
