package com.xxhhxhh.mainthing.mine.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.mine.MineFragment;
import com.xxhhxhh.mainthing.mine.adapter.BlackListAdapter;
import com.xxhhxhh.mainthing.mine.adapter.FocusAdapter;

import java.util.ArrayList;
import java.util.List;

public class InfoLineViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{
    public TextView username;
    public Button toDo;
    public Button getToDo() { return toDo; }
    public void setToDo(Button toDo) { this.toDo = toDo; }
    public TextView getUsername() { return username; }
    public void setUsername(TextView username) { this.username = username; }
    private int type;//显示类型
    private int clickType = 0;//按钮单击状态
    private FocusAdapter focusAdapter;
    private BlackListAdapter blackListAdapter;

    public InfoLineViewHolder(@NonNull View itemView, int type, RecyclerView.Adapter<InfoLineViewHolder> adapter)
    {
        super(itemView);
        setToDo(itemView.findViewById(R.id.toDoButton));
        setUsername(itemView.findViewById(R.id.username));
        getToDo().setOnClickListener(this);
        this.type = type;
        switch (type)
        {
            //关注
            case 3:
            {
                focusAdapter = (FocusAdapter) adapter;
                getToDo().setText("取消关注");

            }break;
            //黑名单
            case 4:
            {
                blackListAdapter = (BlackListAdapter) adapter;
                getToDo().setText("取消拉黑");
            }break;
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.toDoButton)
        {
            setClickType(type);

        }
    }

    //设置按钮单击状态
    public void setClickType(int type)
    {
        switch (type)
        {
            //关注
            case 3:
            {
                if(clickType % 2 == 1)
                {
                    getToDo().setText("取消关注");
                    focusAdapter.getClickedThings().remove(getAdapterPosition());
                    clickType = 0;
                }
                else if(clickType % 2 == 0)
                {
                    getToDo().setText("关注");
                    focusAdapter.getClickedThings().put(getAdapterPosition(), getAdapterPosition());
                    clickType = 1;
                }

            }break;
            //黑名单
            case 4:
            {
                if(clickType % 2 == 1)
                {
                    getToDo().setText("取消拉黑");
                    blackListAdapter.getClickedThings().remove(getAdapterPosition());
                    clickType = 0;
                }
                else if(clickType % 2 == 0)
                {
                    getToDo().setText("拉黑");
                    blackListAdapter.getClickedThings().put(getAdapterPosition(), getAdapterPosition());
                    clickType = 1;
                }
            }break;
        }
    }
}
