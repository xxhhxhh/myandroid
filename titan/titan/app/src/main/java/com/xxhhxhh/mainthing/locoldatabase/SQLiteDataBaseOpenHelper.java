package com.xxhhxhh.mainthing.locoldatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.xxhhxhh.mainthing.MainActivity;

public class SQLiteDataBaseOpenHelper extends SQLiteOpenHelper
{
    //初始标签
    public static String[] firstStartSuiJiLabels =
            {
                    "推荐", "关注", "热门", "明星", "生活", "趣闻"
            };
    public static String[] firstStartArticleLabels =
            {
                    "推荐", "关注", "热门", "生活", "城市", "科技", "游戏"
            };
    //更新标签
    public String[] newSuiJiLabels;
    public String[] newArticleLabels;

    public SQLiteDataBaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建用户信息表
        db.execSQL("create table UserInfo(" +
                "username varchar(20) primary key," +
                "login_date datetime DEFAULT CURRENT_TIMESTAMP," +
                "isallow_login int DEFAULT 0);");
        //标签栏
        db.execSQL("create table SuiJiLabels(label_name varchar(10) primary key);");
        db.execSQL("create table ArticleLabels( label_name varchar(10) primary key);");
        //标签插入
        addData(db, firstStartSuiJiLabels, firstStartArticleLabels);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        newSuiJiLabels = MainActivity.suiJiLabels;
        newArticleLabels = MainActivity.articleLabels;
        if(newSuiJiLabels != null && newSuiJiLabels.length >0)
        db.execSQL("delete from SuiJiLabels;");
        if(newArticleLabels != null && newArticleLabels.length > 0)
        db.execSQL("delete from ArticleLabels;");
        //标签插入
        addData(db, newSuiJiLabels, newArticleLabels);
    }

    private void addData(SQLiteDatabase db, String[] newSuiJiLabels, String[] newArticleLabels)
    {
        if(newSuiJiLabels != null && newSuiJiLabels.length > 0)
        {
            for(int i = 0; i < newSuiJiLabels.length; i++)
            {
                db.execSQL("insert into SuiJiLabels values('" + newSuiJiLabels[i] + "');");
            }
        }

        if(newArticleLabels != null && newArticleLabels.length > 0)
        {
            for(int j = 0; j < newArticleLabels.length; j++)
            {
                db.execSQL("insert into ArticleLabels values('" + newArticleLabels[j] + "');");
            }
        }
    }
}
