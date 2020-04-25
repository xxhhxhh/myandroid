package com.xxhhxhh.mainthing.show.commentsandreply.adapter;

import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.databean.ArticleCommentDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiCommentDataBean;
import com.xxhhxhh.mainthing.show.commentsandreply.holder.OneCommentViewHolder;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class ShowCommentsAdapter extends RecyclerView.Adapter<OneCommentViewHolder>
{
    /**评论适配器
     * @param type /*0随记，1文章
     * @param suijiCmmmentDataBeanMap /*随记评论组
     * @param articleCommentDataBeanMap /*文章评论组
     * */
    private int type;//0随记，1文章
    private Map<Integer, SuiJiCommentDataBean> suiJiCommentDataBeanMap = new HashMap<>();
    public Map<Integer, SuiJiCommentDataBean> getSuiJiCommentDataBeanMap() { return suiJiCommentDataBeanMap; }
    private Map<Integer, ArticleCommentDataBean> articleCommentDataBeanMap = new HashMap<>();
    public Map<Integer, ArticleCommentDataBean> getArticleCommentDataBeanMap() { return articleCommentDataBeanMap; }
    private Fragment fragment;
    private int size;
    public void setSize(int size){this.size = size;}
    //尺寸
    public int getSize()
    {
       switch (type)
       {
           case 0:
           {
               return suiJiCommentDataBeanMap.size();
           }
           case 1:
           {
               return articleCommentDataBeanMap.size();
           }
       }
       return 3;
    }

    public ShowCommentsAdapter(int type, Fragment fragment)
    {
        this.type = type;
        this.fragment = fragment;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @NonNull
    @Override
    public OneCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LinearLayout one = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.one_comment, parent, false);

        return new OneCommentViewHolder(one, type, fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull OneCommentViewHolder holder, int position)
    {
        int thePosition = holder.getAdapterPosition();
        switch (type)
        {
            case 0:
            {
                if(suiJiCommentDataBeanMap != null && suiJiCommentDataBeanMap.size() > thePosition)
                {
                    SuiJiCommentDataBean suiJiCommentDataBean = suiJiCommentDataBeanMap.get(thePosition);
                    holder.getItemView().setTag(suiJiCommentDataBean);
                    holder.getGood().setText("赞"+String.valueOf(suiJiCommentDataBean.getGoods()));
                    holder.getComment().setText(String.valueOf(suiJiCommentDataBean.getComment_message()));
                    holder.getReply().setText("回复" + String.valueOf(suiJiCommentDataBean.getReply_number()));
                    if(suiJiCommentDataBean.getNickName() != null)
                    {
                        holder.getUsername().setText(suiJiCommentDataBean.getNickName());
                    }
                    else
                    {
                        holder.getUsername().setText(suiJiCommentDataBean.getComment_username());
                    }

                    if(suiJiCommentDataBean.getUserHead() != null)
                    {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                                Base64.decode(suiJiCommentDataBean.getUserHead(), Base64.NO_WRAP));
                        holder.getHeadImage().setImageBitmap(BitmapFactory.decodeStream(byteArrayInputStream));
                    }

                }

            }break;
            case 1:
            {
                if(articleCommentDataBeanMap != null && articleCommentDataBeanMap.size() > thePosition)
                {
                    ArticleCommentDataBean articleCommentDataBean = articleCommentDataBeanMap.get(thePosition);
                    holder.getItemView().setTag(articleCommentDataBean);
                    holder.getGood().setText("赞"+String.valueOf(articleCommentDataBean.getGoods()));
                    holder.getComment().setText(String.valueOf(articleCommentDataBean.getComment_message()));
                    holder.getReply().setText("回复" + String.valueOf(articleCommentDataBean.getReply_number()));
                    if(articleCommentDataBean.getNickName() != null)
                    {
                        holder.getUsername().setText(articleCommentDataBean.getNickName());
                    }
                    else
                    {
                        holder.getUsername().setText(articleCommentDataBean.getComment_username());
                    }

                    if(articleCommentDataBean.getUserHead() != null)
                    {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                                Base64.decode(articleCommentDataBean.getUserHead(), Base64.NO_WRAP));
                        holder.getHeadImage().setImageBitmap(BitmapFactory.decodeStream(byteArrayInputStream));
                    }
                }
            }break;
        }
    }

    @Override
    public int getItemCount() {
        return getSize();
    }



}
