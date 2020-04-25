package com.xxhhxhh.mainthing.index.holder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.ShowArticleActivity;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.popup.MenuPopUpWindow;

import java.util.HashMap;
import java.util.Map;

public class ArticleViewHolder extends RecyclerView.ViewHolder
    implements View.OnLongClickListener,
        RecyclerView.OnClickListener
{

    public View itemView;//整体
    public TextView articleTitle;//文章标题
    public TextView dateAndLabel;//时间和标题
    public Button report;//举报按钮
    public TextView commentNumber;//评论数
    private int viewType;//当前视图类型
    private TextView showUsername;
    //
    public ArticleDataBean articleDataBean;
    private Fragment fragment;

    public ArticleViewHolder(@NonNull View itemView, int viewType, Fragment fragment)
    {
        super(itemView);
        this.viewType = viewType;
        this.fragment = fragment;
        if(viewType == 0)
        {
            setArticleTitle((TextView) itemView.findViewById(R.id.articleTitle));
            setCommentNumber((TextView) itemView.findViewById(R.id.commentNumber));
            setDateAndLabel((TextView) itemView.findViewById(R.id.theDate));
            setShowUsername((TextView)itemView.findViewById(R.id.showUsername));
            setItemView(itemView);
            setReport((Button) itemView.findViewById(R.id.report));
            getReport().setOnClickListener(this);
            getItemView().setOnLongClickListener(this);
            getArticleTitle().setOnLongClickListener(this);
            getArticleTitle().setOnClickListener(this);
            getItemView().setOnClickListener(this);
        }
        else if(viewType == 1)
        {
            setItemView(itemView);
        }
        else if(viewType == 2)
        {
            setItemView(itemView);
        }
    }


    public Button getReport() { return report; }
    public TextView getArticleTitle() { return articleTitle; }
    public TextView getCommentNumber() { return commentNumber; }
    public TextView getDateAndLabel() { return dateAndLabel; }
    public View getItemView() { return itemView; }
    public void setArticleTitle(TextView articleTitle) { this.articleTitle = articleTitle; }
    public void setCommentNumber(TextView commentNumber) { this.commentNumber = commentNumber; }
    public void setDateAndLabel(TextView dateAndLabel) { this.dateAndLabel = dateAndLabel; }
    public void setItemView(View itemView) { this.itemView = itemView; }
    public void setReport(Button report) { this.report = report; }
    public TextView getShowUsername() { return showUsername; }
    public void setShowUsername(TextView showUsername) { this.showUsername = showUsername; }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId())
        {
            case R.id.articleTitle:
            case R.id.theArticle:
            {
                articleDataBean = (ArticleDataBean) getItemView().getTag();
                Map<String, String> data = new HashMap<>();
                data.put("username", articleDataBean.getUsername());
                data.put("label", articleDataBean.getLabels());
                String username = ((UserInfoApplocation)fragment.getActivity().getApplication()).getUsername();
                MenuPopUpWindow popUpWindow = new MenuPopUpWindow(getItemView().getContext(), data, username);
                popUpWindow.showAsDropDown(getReport());
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.report:
            {
                articleDataBean = (ArticleDataBean) getItemView().getTag();
                Map<String, String> data = new HashMap<>();
                data.put("username", articleDataBean.getUsername());
                data.put("label", articleDataBean.getLabels());
                String username = ((UserInfoApplocation)fragment.getActivity().getApplication()).getUsername();
                MenuPopUpWindow popUpWindow = new MenuPopUpWindow(getItemView().getContext(), data, username);
                popUpWindow.showAsDropDown(getReport());
            }break;
            case R.id.theArticle:
            case R.id.articleTitle:
            {
                Intent intent = new Intent(getItemView().getContext(), ShowArticleActivity.class);
                intent.putExtra("data", (ArticleDataBean)getItemView().getTag());
                getItemView().getContext().startActivity(intent);
            }break;
        }
    }
}
