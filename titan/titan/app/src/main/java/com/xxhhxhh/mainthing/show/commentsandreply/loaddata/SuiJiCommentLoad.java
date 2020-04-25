package com.xxhhxhh.mainthing.show.commentsandreply.loaddata;

import android.graphics.Bitmap;
import android.util.Base64;
import com.xxhhxhh.mainthing.databean.SuiJiCommentDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.show.commentsandreply.adapter.ShowCommentsAdapter;
import com.xxhhxhh.transdata.util.CommentAndReplyAndResetDoUtil;
import com.xxhhxhh.transdata.util.GetUserHeadUtil;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class SuiJiCommentLoad implements Runnable
{
    private ShowCommentsAdapter showCommentsAdapter;
    private int suiji_id;

    public SuiJiCommentLoad(ShowCommentsAdapter showCommentsAdapter, int suiji_id)
    {
        this.showCommentsAdapter = showCommentsAdapter;
        this.suiji_id = suiji_id;
    }

    @Override
    public void run()
    {
      Map<Integer, JSONObject> data = CommentAndReplyAndResetDoUtil.getComment(suiji_id, 0);

      for(int i = 0; i < data.size(); i++)
      {
          try
          {
              SuiJiCommentDataBean suiJiCommentDataBean = new SuiJiCommentDataBean();
              suiJiCommentDataBean.setComment_message(data.get(i).getString("commentMessage"));
              suiJiCommentDataBean.setComment_username(data.get(i).getString("username"));
              suiJiCommentDataBean.setNickName(data.get(i).getString("nickName"));
              suiJiCommentDataBean.setGoods(data.get(i).getInt("goods"));
              suiJiCommentDataBean.setReply_number(data.get(i).getInt("replyNumber"));
              suiJiCommentDataBean.setSuiji_comment_id(data.get(i).getInt("suiji_comment_id"));
              getHead(suiJiCommentDataBean.getComment_username(), suiJiCommentDataBean);
              showCommentsAdapter.getSuiJiCommentDataBeanMap().put(i, suiJiCommentDataBean);
          }
          catch (Exception e)
          {
              e.printStackTrace();
          }
      }

    }

    //获取头像
    private void getHead(String username, SuiJiCommentDataBean suiJiCommentDataBean)
    {
        Bitmap bitmap =  GetUserHeadUtil.getHead(username);
        if(bitmap != null)
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
            String data1 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
            suiJiCommentDataBean.setUserHead(data1);
        }
    }
}
