package com.xxhhxhh.mainthing.mine.holder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.AddArticleActivity;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;

public class ArticleShowViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{
    public TextView articleTitle;
    public TextView date;
    public TextView commentNumber;
    public View itemView;
    public void setItemView(View itemView) { this.itemView = itemView; }
    public View getItemView() { return itemView; }
    public void setCommentNumber(TextView commentNumber) { this.commentNumber = commentNumber; }
    public void setArticleTitle(TextView articleTitle) { this.articleTitle = articleTitle; }
    public TextView getCommentNumber() { return commentNumber; }
    public TextView getArticleTitle() { return articleTitle; }
    public TextView getDate() { return date; }
    public void setDate(TextView date) { this.date = date; }

    public ArticleShowViewHolder(@NonNull View itemView)
    {
        super(itemView);
        setItemView(itemView);
        setArticleTitle(itemView.findViewById(R.id.articleTitle));
        setCommentNumber(itemView.findViewById(R.id.commentNumber));
        setDate(itemView.findViewById(R.id.theDate));
        getArticleTitle().setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.articleTitle)
        {
            ArticleDataBean articleDataBean = (ArticleDataBean) getItemView().getTag();
            Intent intent = new Intent(getItemView().getContext(), AddArticleActivity.class);
            intent.putExtra("data", articleDataBean);
            intent.putExtra("type", 1);
            getItemView().getContext().startActivity(intent);
        }
    }
}
