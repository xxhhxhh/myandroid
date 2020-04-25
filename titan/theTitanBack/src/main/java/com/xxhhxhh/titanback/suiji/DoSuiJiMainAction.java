package com.xxhhxhh.titanback.suiji;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.suiji.SuiJis;
import com.xxhhxhh.interfaces.GetMessageShow;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import net.sf.json.JSONArray;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

//获取随记文本
public class DoSuiJiMainAction extends ActionSupport
    implements GetMessageShow
{
    private String username;//用户名
    private int goods;//赞数
    private int commentNumber;//评论数
    private String mainMessage;//内容
    private int haveFile;//文件个数
    private String location;//位置
    private String the_date;//日期
    private int suiji_id;
    private int looks_number;
    private JSONArray labels = new JSONArray();
    private int fileType;//文件类型
    private String nickName;

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public int getFileType() { return fileType; }
    public void setFileType(int fileType) { this.fileType = fileType; }
    public void setLooks_number(int looks_number) { this.looks_number = looks_number; }
    public int getLooks_number() { return looks_number; }
    public int getSuiji_id() { return suiji_id; }
    public String getThe_date() { return the_date; }
    public void setThe_date(String the_date) { this.the_date = the_date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getHaveFile() { return haveFile; }
    public void setHaveFile(int haveFile) { this.haveFile = haveFile; }
    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
    public int getGoods() { return goods; }
    public void setGoods(int goods) { this.goods = goods; }
    public int getCommentNumber() { return commentNumber; }
    public void setCommentNumber(int commentNumber) { this.commentNumber = commentNumber; }
    public void setLabels(JSONArray labels) { this.labels = labels; }
    public String getLabels() { return labels.toString(); }
    public String getMainMessage() { return mainMessage; }
    public void setMainMessage(String mainMessage) { this.mainMessage = mainMessage; }

    //需求数据
    private Session session;
    private String the_label;
    private String theUsername;//用户名
    private int start;//分页起始位置

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        theUsername = servletRequest.getParameter("username");
        //得到过期标签
        start = servletRequest.getParameter("start") != null ?
                Integer.parseInt(servletRequest.getParameter("start")) : 0;
        the_label = servletRequest.getParameter("the_label") != null ?
                servletRequest.getParameter("the_label") : "";
        int suiji_id = servletRequest.getParameter("suiji_id") != null ?
                Integer.parseInt(servletRequest.getParameter("suiji_id")) : 0;
        //获取类型,0推荐、1最新、2热门,3其它,4用户，5单独
        int type = Integer.parseInt(servletRequest.getParameter("type") != null ? servletRequest.getParameter("type") : "0");
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
            //指定获取
            case 5:
            {
                getMessageSingleSearch(suiji_id);
            }break;
        }

        session.close();
        if(getMainMessage() != null && !getMainMessage().equals(""))
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

            Query query = session.createSQLQuery("select SuiJis.suiji_id from SuiJis, Related, UserLabels, UserInfo, EverySuiJiLabels\n" +
                    "where (UserLabels.suiji_love1 = EverySuiJiLabels.label_name and EverySuiJiLabels.suiji_id = SuiJis.suiji_id\n" +
                    "    or UserLabels.suiji_love2 = EverySuiJiLabels.label_name and EverySuiJiLabels.suiji_id = SuiJis.suiji_id\n" +
                    "or UserLabels.suiji_love3 = EverySuiJiLabels.label_name and EverySuiJiLabels.suiji_id = SuiJis.suiji_id)\n" +
                    "    and UserLabels.username = '" + theUsername + "'" +
                    " and (Related.username = SuiJis.username and Related.type != 1) and (" +
                    "allow_detail = 0 and allow_look_suiji = 0) limit :start ,1;")
                    .setParameter("start", start);

            List<Integer> result = query.getResultList();
            //
            if(result != null && !result.isEmpty())
            {
                for (int i : result) {
                    doResult(i);
                }
            }
            //随机获取
            else
            {
               getRandom();
             }
        }
        else
        {
            getRandom();
        }


    }

    //随机获取数据
    public void getRandom()
    {
        //随机选出id
        Query query2 = session.createSQLQuery("select SuiJis.suiji_id from SuiJis, Related, defaultSuiJiLabels, UserInfo, EverySuiJiLabels" +
                "           where (defaultSuiJiLabels.label_name = EverySuiJiLabels.label_name and EverySuiJiLabels.suiji_id = SuiJis.suiji_id" +
                "       or defaultSuiJiLabels.label_name  = EverySuiJiLabels.label_name and EverySuiJiLabels.suiji_id = SuiJis.suiji_id " +
                "    or defaultSuiJiLabels.label_name  = EverySuiJiLabels.label_name and EverySuiJiLabels.suiji_id = SuiJis.suiji_id) " +
                "  and (Related.username = SuiJis.username and Related.type != 1) " +
                " order by rand() limit :start ,1").setParameter("start", start);
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
        Query query = session.createSQLQuery("select suiji_id from SuiJis, SuiJiLabels,Related" +
                " where  " +
                "  (Related.username = SuiJis.username and Related.type != 1)" +
                "order by SuiJiLabels.label_looks_number desc limit :start ,1").setParameter("start", start);

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
        Query query = session.createSQLQuery("select suiji_id from SuiJis,Related " +
                " where " +
                "   (Related.username = SuiJis.username and Related.type != 1)" +
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
        Query query = session.createSQLQuery("select SuiJis.suiji_id from EverySuiJiLabels, Related, SuiJis" +
                " where " +
                "  (Related.username = SuiJis.username and Related.type != 1) " +
                " and label_name= :label_name limit :start ,1").setParameter("start", start)
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
        Query query = session.createSQLQuery("select suiji_id from SuiJis" +
                " where  username= :username " +
                " limit :start ,1").setParameter("username", username).setParameter("start", start);

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
        //获取随记主要内容
        Query query1 = session.createQuery("from SuiJis where suiji_id =:i", SuiJis.class).
                setParameter("i", i).setMaxResults(1);
        List<SuiJis> resultList = query1.getResultList();
        for(SuiJis suiJi : resultList)
        {
            if(suiJi.getMain_message() != null && !suiJi.getMain_message().equals(""))
            {
                suiji_id = suiJi.getSuiji_id();
                setCommentNumber(suiJi.getComment_number());
                setGoods(suiJi.getGoods());
                setMainMessage(suiJi.getMain_message());
                setHaveFile(suiJi.getHave_file());
                setLocation(suiJi.getLocation());
                setThe_date(suiJi.getThe_date().toString());
                setUsername(suiJi.getUsername().getUsername());
                String nickName = UserInfoDoUtil.getNickName(suiJi.getUsername().getUsername(), session);
                if(nickName != null)
                {
                    setNickName(nickName);
                }
                else
                {
                    setNickName("");
                }
                setLooks_number(suiJi.getLooks_number());
                setFileType(suiJi.getFile_type());
                //获取随记标签
                getSuiJiLabels(suiji_id);
            }
            else
            {
                break;
            }
        }
    }


    //获取随记标签
    public void getSuiJiLabels(int suiji_id)
    {
        //获取名称
        Query query2 = session.createSQLQuery("select label_name from EverySuiJiLabels " +
                "where suiji_id = " + suiji_id + ";");
        List<String> label_names = query2.getResultList();
        //组合数据
        JSONArray labels = new JSONArray();
        for (String label_name : label_names)
        {
            labels.put(label_name);
        }
        setLabels(labels);
    }
}
