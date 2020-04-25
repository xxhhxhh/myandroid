package com.xxhhxhh.mainthing.mine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.mine.MineFragment;
import com.xxhhxhh.mainthing.mine.holder.ArticleShowViewHolder;

import java.util.HashMap;
import java.util.Map;

public class ArticleShowAdapter extends RecyclerView.Adapter<ArticleShowViewHolder>
{
    public Map<Integer, ArticleDataBean> dataBeans = new HashMap<>();



    @NonNull
    @Override
    public ArticleShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_show_line, parent, false);

        return new ArticleShowViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleShowViewHolder holder, int position)
    {
        int thePosition = holder.getAdapterPosition();
        if(dataBeans != null && dataBeans.size() > thePosition && dataBeans.get(thePosition) != null)
        {
            holder.getItemView().setTag(dataBeans.get(thePosition));
            holder.getArticleTitle().setText(dataBeans.get(thePosition).getArticle_title());
            holder.getCommentNumber().setText("评论" + dataBeans.get(thePosition).getComment_number());
            holder.getDate().setText(dataBeans.get(thePosition).getThe_date());
        }
    }

    @Override
    public int getItemCount() {
        return dataBeans.size();
    }
}
