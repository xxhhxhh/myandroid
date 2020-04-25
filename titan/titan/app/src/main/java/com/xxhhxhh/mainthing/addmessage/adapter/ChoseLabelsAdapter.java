package com.xxhhxhh.mainthing.addmessage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.fragment.AddArticleFragment;
import com.xxhhxhh.mainthing.addmessage.fragment.AddLabelFragment;
import com.xxhhxhh.mainthing.addmessage.holder.ChoseLabelsViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChoseLabelsAdapter extends RecyclerView.Adapter<ChoseLabelsViewHolder>
{

    private Map<Integer, String> data = new HashMap<>();
    public void setData(Map<Integer, String> data) { this.data = data; }
    private int itemCounts;
    public void setItemCounts(int itemCounts) { this.itemCounts = itemCounts; }
    private AddLabelFragment addLabelFragment;
    public void setAddLabelFragment(AddLabelFragment addLabelFragment) { this.addLabelFragment = addLabelFragment; }


    @NonNull
    @Override
    public ChoseLabelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.choseing_line, parent, false);
        return new ChoseLabelsViewHolder(linearLayout, addLabelFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoseLabelsViewHolder holder, int position)
    {
        holder.getTextView().setText(data.get(position));
        if(addLabelFragment.chosedLabels.get(holder.getTextView().getText().toString()) != null)
        {
            holder.getImageView().setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount()
    {
        return itemCounts;
    }
}
