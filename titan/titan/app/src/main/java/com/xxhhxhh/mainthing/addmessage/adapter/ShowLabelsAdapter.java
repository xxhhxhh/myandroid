package com.xxhhxhh.mainthing.addmessage.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.fragment.AddArticleFragment;
import com.xxhhxhh.mainthing.addmessage.fragment.AddSuiJiFragment;
import com.xxhhxhh.mainthing.addmessage.holder.ShowLabelsHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowLabelsAdapter extends RecyclerView.Adapter<ShowLabelsHolder>
{
    public int itemCounts;
    public AddSuiJiFragment addSuiJifragment;
    public AddArticleFragment addArticleFragment;
    private Object[] labels;
    public int type;
    public void setItemCounts(int itemCounts) { this.itemCounts = itemCounts; }

    public ShowLabelsAdapter(int type, Fragment fragment)
    {
        this.type = type;
        if(type == 0)
        {
            addSuiJifragment = (AddSuiJiFragment) fragment;
            labels = addSuiJifragment.chosedLabel.keySet().toArray();
        }
        else if(type == 1)
        {
            addArticleFragment = (AddArticleFragment) fragment;
            labels = addArticleFragment.chosedLabel.keySet().toArray();
        }
    }

    @NonNull
    @Override
    public ShowLabelsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LinearLayout one = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.show_or_delete_label, parent, false);
        if(type == 0)
        {
            ShowLabelsHolder holder = new ShowLabelsHolder(one, addSuiJifragment, type, this);
            return holder;
        }
        else if(type == 1)
        {
            ShowLabelsHolder holder = new ShowLabelsHolder(one, addArticleFragment, type, this);
            return holder;
        }
        return new ShowLabelsHolder(one, null, 3, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowLabelsHolder holder, int position)
    {
        if(type == 0)
        {
            if(addSuiJifragment.chosedLabel != null && !addSuiJifragment.chosedLabel.isEmpty())
            {
                holder.getTextView().setText((String)labels[position]);
            }
        }
        else if(type == 1)
        {
            if(addArticleFragment.chosedLabel != null && !addArticleFragment.chosedLabel.isEmpty())
            {
                holder.getTextView().setText((String)labels[position]);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return Math.min(itemCounts, 5);
    }
}
