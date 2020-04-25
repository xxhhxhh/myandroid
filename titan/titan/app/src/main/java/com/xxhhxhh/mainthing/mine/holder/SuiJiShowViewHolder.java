package com.xxhhxhh.mainthing.mine.holder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.AddSuiJiActivity;
import com.xxhhxhh.mainthing.databean.SuiJiDataBeanSimple;

public class SuiJiShowViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{
    private TextView messageShow;
    private TextView theDate;
    private TextView commentNumber;
    public void setCommentNumber(TextView commentNumber) { this.commentNumber = commentNumber; }
    public TextView getCommentNumber() { return commentNumber; }
    public TextView getMessageShow() { return messageShow; }
    public TextView getTheDate() { return theDate; }
    public void setMessageShow(TextView messageShow) { this.messageShow = messageShow; }
    public void setTheDate(TextView theDate) { this.theDate = theDate; }
    private View ItemView;
    public void setItemView(View itemView) { ItemView = itemView; }
    public View getItemView() { return ItemView; }

    public SuiJiShowViewHolder(@NonNull View itemView)
    {
        super(itemView);
        setItemView(itemView);
        setCommentNumber(itemView.findViewById(R.id.commentNumber));
        setMessageShow(itemView.findViewById(R.id.messageSuiJi));
        setTheDate(itemView.findViewById(R.id.theDate));
        getMessageShow().setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.messageSuiJi)
        {
            SuiJiDataBeanSimple suiJiDataBeanSimple = (SuiJiDataBeanSimple) getItemView().getTag();

            if(suiJiDataBeanSimple != null)
            {
                Intent intent = new Intent(getItemView().getContext(), AddSuiJiActivity.class);
                intent.putExtra("data", suiJiDataBeanSimple);
                intent.putExtra("type", 1);
                getItemView().getContext().startActivity(intent);
            }
        }
    }
}
