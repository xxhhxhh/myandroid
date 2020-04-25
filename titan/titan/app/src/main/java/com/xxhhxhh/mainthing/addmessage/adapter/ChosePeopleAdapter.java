package com.xxhhxhh.mainthing.addmessage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.fragment.AddPeopleFragment;
import com.xxhhxhh.mainthing.addmessage.fragment.AddSuiJiFragment;
import com.xxhhxhh.mainthing.addmessage.holder.ChosePeopleViewHolder;
import java.util.HashMap;
import java.util.Map;

public class ChosePeopleAdapter extends RecyclerView.Adapter<ChosePeopleViewHolder>
{
    private Map<Integer, String> data = new HashMap<>();
    public void setData(Map<Integer, String> data) { this.data = data; }
    private int itemCounts;
    public void setItemCounts(int itemCounts) { this.itemCounts = itemCounts; }
    private AddSuiJiFragment addSuiJiFragment;
    public void setAddSuiJiFragment(AddSuiJiFragment addSuiJiFragment) { this.addSuiJiFragment = addSuiJiFragment; }

    @NonNull
    @Override
    public ChosePeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.choseing_line, parent, false);
        return new ChosePeopleViewHolder(linearLayout, addSuiJiFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull ChosePeopleViewHolder holder, int position)
    {
        holder.setNowLocation(position);
        holder.getTextView().setText(data.get(position));
    }

    @Override
    public int getItemCount()
    {
        return itemCounts;
    }
}