package com.xxhhxhh.titanback.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SaveFileUtil
{
    //保存图片
    public static void saveFile(String path, String inputString, Session session, int id, int group_id)
    {

        Transaction transaction = session.getTransaction().isActive() ? session.getTransaction() : session.beginTransaction();
        try
        {
            //保存文件
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(inputString.replace("\r\n", "").getBytes(StandardCharsets.UTF_8)));
            FileOutputStream outputStream = new FileOutputStream(new File(path));
            inputStream.transferTo(outputStream);
            outputStream.close();
        }
        catch (Exception e)
        {

        }
        //插入文件位置
        Query query = session.createSQLQuery("insert into SuiJiFiles (file_location, group_id, suiji_id)  " +
                "values( :path , :position , :suiji_id ) ;").setParameter("path", path)
                .setParameter("position", group_id).setParameter("suiji_id", id);
        query.executeUpdate();
        transaction.commit();
    }

    //保存html
    public static void saveHtml(String path, String value)
    {
        try
        {
            value = value.substring(1, value.length() - 1);
            value = value.replaceAll("(\\\\u003C)", "<");
            value = value.replaceAll("\\\\n", "");
            value = value.replaceAll("\\\\", "");
            //保存文件
            FileOutputStream outputStream = new FileOutputStream(new File(path));
            byte[] bytes;
            bytes = value.getBytes();
            int b = bytes.length;
            outputStream.write(bytes, 0 , b);
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
