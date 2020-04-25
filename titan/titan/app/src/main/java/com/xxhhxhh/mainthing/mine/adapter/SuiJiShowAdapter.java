package com.xxhhxhh.mainthing.mine.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBeanSimple;
import com.xxhhxhh.mainthing.mine.MineFragment;
import com.xxhhxhh.mainthing.mine.holder.SuiJiShowViewHolder;

import java.util.HashMap;
import java.util.Map;

public class SuiJiShowAdapter extends RecyclerView.Adapter<SuiJiShowViewHolder>
{

    public Map<Integer, SuiJiDataBeanSimple> dataBeans = new HashMap<>();

    public SuiJiShowAdapter()
    {
        dataBeans = new HashMap<>();
    }

    @NonNull
    @Override
    public SuiJiShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LinearLayout main = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.suiji_show_line, parent, false);

        return new SuiJiShowViewHolder(main);
    }

    @Override
    public void onBindViewHolder(@NonNull SuiJiShowViewHolder holder, int position)
    {
        int thePosition = holder.getAdapterPosition();
        if(dataBeans != null && dataBeans.size() > thePosition)
        {
            holder.getItemView().setTag(dataBeans.get(thePosition));
            holder.getMessageShow().setText(dataBeans.get(thePosition).getMain_message());
            holder.getCommentNumber().setText("评论" + dataBeans.get(thePosition).getComment_number());
            holder.getTheDate().setText(dataBeans.get(thePosition).getThe_date());
        }
    }

    @Override
    public int getItemCount()
    {
        return dataBeans.size();
    }



}
