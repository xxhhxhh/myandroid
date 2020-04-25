package com.xxhhxhh.titanback.suiji;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

public class DoOneMediaAction extends ActionSupport
{

    private String result;
    public String getResult() { return result; }
    private int type;
    public int getType() { return type; }

    //需求数据
    private int group_id = 0;//当前数量位置
    private int suiji_id;//随记id

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();

        group_id = Integer.parseInt(servletRequest.getParameter("group_id") != null ?
                servletRequest.getParameter("group_id") : "0");
        suiji_id = Integer.parseInt(servletRequest.getParameter("suiji_id") != null ?
                servletRequest.getParameter("suiji_id") : "0");

        String file_location = getFileLocation();
        result = uploadFile(file_location);

        return SUCCESS;
    }

    //获取文件
    private String getFileLocation()
    {
        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery("select file_location from SuiJiFiles" +
                " where suiji_id=:suiji_id and group_id=:group_id limit 1;")
                .setParameter("suiji_id", suiji_id).setParameter("group_id", group_id);
        return query.getResultList() != null && query.getResultList().size() > 0 ? (String) query.getResultList().get(0) : null;
    }

    public String uploadFile(String path)
    {
        FileInputStream inputStream;
        try
        {
            if(path != null && !path.equals(""))
            {
                inputStream =  new FileInputStream(new File(path));
//                //判断文件类型
//                BufferedInputStream stream = new BufferedInputStream(inputStream);
//                byte[] bytes = new byte[4];
//                StringBuilder builder = new StringBuilder();
//                stream.read(bytes, 0, bytes.length);
//                //转化
//                for (int i = 0; i < bytes.length; i++)
//                {
//                    int v = bytes[i] & 0xff;
//                    String hv = Integer.toHexString(v);
//                    builder.append(hv);
//                }
//                if(builder.toString().toUpperCase().equals("89504E47"))
//                {
//                    type = 0;
//                }
//                else
//                {
//                    type = 1;
//                }

                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                String aa = Base64.getEncoder().encodeToString(bytes);
                return aa;
            }
        }
        catch (Exception e)
        {
            return null;
        }

        return null;
    }



}
