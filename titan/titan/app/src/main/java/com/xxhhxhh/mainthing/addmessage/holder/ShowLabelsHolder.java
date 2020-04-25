package com.xxhhxhh.mainthing.addmessage.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.adapter.ShowLabelsAdapter;
import com.xxhhxhh.mainthing.addmessage.fragment.AddArticleFragment;
import com.xxhhxhh.mainthing.addmessage.fragment.AddSuiJiFragment;

import java.util.Map;

public class ShowLabelsHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{

    public TextView textView;
    public Button delete;
    public Button getDelete() { return delete; }
    public TextView getTextView() { return textView; }
    public void setDelete(Button delete) { this.delete = delete; }
    public void setTextView(TextView textView) { this.textView = textView; }
    public AddArticleFragment addArticleFragment;
    public AddSuiJiFragment addSuiJiFragment;
    private int type;


    public ShowLabelsHolder(@NonNull View itemView, Fragment fragment,int type, ShowLabelsAdapter showLabelsAdapter)
    {
        super(itemView);
        setDelete(itemView.findViewById(R.id.deleteLabel));
        setTextView(itemView.findViewById(R.id.showOneLabel));
        getDelete().setOnClickListener(this);
        this.type = type;
        if(type == 0)
        {
            addSuiJiFragment = (AddSuiJiFragment) fragment;
        }
        else if(type == 1)
        {
            addArticleFragment = (AddArticleFragment) fragment;
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.deleteLabel)
        {
            getTextView().setVisibility(View.GONE);
            getDelete().setVisibility(View.GONE);
            if(type == 0)
            {
                addSuiJiFragment.chosedLabel.remove(getTextView().getText().toString());
            }
            else if(type == 1)
            {
                addArticleFragment.chosedLabel.remove(getTextView().getText().toString());
            }
        }
    }
}
