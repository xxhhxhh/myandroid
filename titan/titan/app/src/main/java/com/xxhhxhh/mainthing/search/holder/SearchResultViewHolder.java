package com.xxhhxhh.mainthing.search.holder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.ShowArticleActivity;
import com.xxhhxhh.mainthing.ShowSuiJiActivity;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBeanParceable;
import com.xxhhxhh.transdata.requestcode.RequestCode;

public class SearchResultViewHolder extends RecyclerView.ViewHolder
{
    private TextView resultText;
    private SuiJiDataBean suiJiDataBean;
    private ArticleDataBean articleDataBean;
    private int type;

    public void setType(int type) { this.type = type; }
    public TextView getResultText() { return resultText; }
    public void setResultText(TextView resultText) { this.resultText = resultText; }

    public SearchResultViewHolder(@NonNull View itemView)
    {
        super(itemView);
        setResultText(itemView.findViewById(R.id.resultText));
        itemView.setOnClickListener(v -> {
            switch (type)
            {
                //随记
                case 0:
                {
                    Intent intent = new Intent(itemView.getContext(), ShowSuiJiActivity.class);
                    if(itemView.getTag() != null)
                    {
                        intent.putExtra("type", 1);
                        intent.putExtra("id", (Integer) itemView.getTag());
                        itemView.getContext().startActivity(intent);
                    }

                }break;
                //文章
                case 1:
                {
                    Intent intent = new Intent(itemView.getContext(), ShowArticleActivity.class);
                    if(itemView.getTag() != null)
                    {
                        intent.putExtra("type", 1);
                        intent.putExtra("id", (Integer)itemView.getTag());
                        itemView.getContext().startActivity(intent);
                    }

                }break;
            }
        });
    }
}
