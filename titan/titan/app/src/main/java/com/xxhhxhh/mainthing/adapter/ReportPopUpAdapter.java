package com.xxhhxhh.mainthing.adapter;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.holder.ReportPopUpHolder;

import java.util.HashMap;
import java.util.Map;

public class ReportPopUpAdapter extends RecyclerView.Adapter<ReportPopUpHolder>
{
    private Map<Integer, String> theData = new HashMap<>();
    private String username;

    public ReportPopUpAdapter(Map<Integer, String> theData, String username)
    {
        this.theData = theData;
        this.username = username;
    }

    @NonNull
    @Override
    public ReportPopUpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        TextView textView = new TextView(parent.getContext());
        textView.setWidth(400);
        textView.setHeight(120);
        textView.setTextSize(25);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setId(R.id.choseMessage);
        return new ReportPopUpHolder(textView, username);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportPopUpHolder holder, int position)
    {
        holder.getTextView().setTag(theData.get(position));
        holder.getTextView().setText(theData.get(position));
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
