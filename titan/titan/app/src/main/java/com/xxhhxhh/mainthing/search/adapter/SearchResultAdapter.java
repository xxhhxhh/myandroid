package com.xxhhxhh.mainthing.search.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.search.holder.SearchResultViewHolder;

import java.util.HashMap;
import java.util.Map;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder>
{
    private Map<Integer, SuiJiDataBean> suiJiDataBeanMap = new HashMap<>();
    private Map<Integer, ArticleDataBean> articleDataBeanMap = new HashMap<>();

    public Map<Integer, ArticleDataBean> getArticleDataBeanMap() { return articleDataBeanMap; }
    public Map<Integer, SuiJiDataBean> getSuiJiDataBeanMap() { return suiJiDataBeanMap; }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LinearLayout one = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.result, parent, false);

        return new SearchResultViewHolder(one);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position)
    {
        int thePosition = holder.getAdapterPosition();

        if(suiJiDataBeanMap != null && suiJiDataBeanMap.size() > thePosition)
        {
            holder.setType(0);
            holder.itemView.setTag(suiJiDataBeanMap.get(thePosition).getSuiji_id());
            holder.getResultText().setText(suiJiDataBeanMap.get(thePosition).getMain_message());
        }
        else if(articleDataBeanMap != null && articleDataBeanMap.size() > 0)
        {
            holder.setType(1);
            holder.itemView.setTag(articleDataBeanMap.get(thePosition).getArticle_id());
            holder.getResultText().setText(articleDataBeanMap.get(thePosition).getArticle_title());
        }

    }

    @Override
    public int getItemCount()
    {

        return suiJiDataBeanMap.size() + articleDataBeanMap.size();
    }
}
