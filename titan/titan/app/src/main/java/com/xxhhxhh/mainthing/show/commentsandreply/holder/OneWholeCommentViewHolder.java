package com.xxhhxhh.mainthing.show.commentsandreply.holder;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.databean.ArticleReplyBean;
import com.xxhhxhh.mainthing.databean.SuiJiReplyBean;
import com.xxhhxhh.mainthing.show.commentsandreply.popup.ToReplyPopUpWindow;
import com.xxhhxhh.mainthing.show.dialog.ToReplyDialog;
import com.xxhhxhh.whenstart.UserLogin;

public class OneWholeCommentViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{
    private TextView textView;
    private View itemView;
    public void setItemView(View itemView) { this.itemView = itemView; }
    public TextView getTextView() { return textView; }
    public View getItemView() { return itemView; }
    public void setTextView(TextView textView) { this.textView = textView; }
    private Fragment fragment;
    private int type;//0随记、1文章

    public OneWholeCommentViewHolder(@NonNull View itemView, Fragment fragment, int type)
    {
        super(itemView);
        setItemView(itemView);
        this.fragment = fragment;
        this.type = type;
        setTextView(itemView.findViewById(R.id.comment));
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.comment)
        {
            if(UserLogin.IS_LOGIN)
            {
                String username = ((UserInfoApplocation)fragment.getActivity().getApplication()).getUsername();
                if(username != null && !username.equals(""))
                {
                    switch (type)
                    {
                        case 0:
                        {
                            SuiJiReplyBean suiJiReplyBean = (SuiJiReplyBean) getItemView().getTag();
                            ToReplyDialog toReplyDialog = new ToReplyDialog(itemView.getContext(), 1, type,
                                    username, suiJiReplyBean.getTo_username(), suiJiReplyBean.getReply_id());
                            toReplyDialog.show();
                        }break;
                        case 1:
                        {
                            ArticleReplyBean articleReplyBean = (ArticleReplyBean) getItemView().getTag();
                            ToReplyPopUpWindow toReplyPopUpWindow = new ToReplyPopUpWindow(itemView.getContext(),
                                    "回复@" + username,
                                     username, articleReplyBean.getTo_username(), articleReplyBean.getReply_id(), 1);
                            toReplyPopUpWindow.showAtLocation(itemView, Gravity.BOTTOM, 0, 0);
                        }break;
                    }
                }
            }
            else
            {
                UserLogin.requestLogin(itemView.getContext());
            }
        }
    }
}
