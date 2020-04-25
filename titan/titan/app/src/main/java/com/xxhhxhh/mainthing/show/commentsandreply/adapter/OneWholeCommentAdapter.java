package com.xxhhxhh.mainthing.show.commentsandreply.adapter;


import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.databean.ArticleReplyBean;
import com.xxhhxhh.mainthing.databean.SuiJiReplyBean;
import com.xxhhxhh.mainthing.show.commentsandreply.holder.OneWholeCommentViewHolder;

import java.util.HashMap;
import java.util.Map;

public class OneWholeCommentAdapter extends RecyclerView.Adapter<OneWholeCommentViewHolder>
{
    private Fragment fragment;
    private int type;
    public Map<Integer, SuiJiReplyBean> suiJiReplyBeanMap = new HashMap<>();
    public Map<Integer, ArticleReplyBean> articleReplyBeanMap = new HashMap<>();

    public OneWholeCommentAdapter(Fragment fragment, int type)
    {
        this.fragment = fragment;
        this.type = type;
    }

    @NonNull
    @Override
    public OneWholeCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LinearLayout main = new LinearLayout(parent.getContext());
        TextView textView = new TextView(parent.getContext());
        textView.setId(R.id.comment);
        textView.setTextSize(20);
        main.addView(textView);
        textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setMinHeight(100);
        main.setBackground(parent.getContext().getDrawable(R.drawable.bottom_border));
        return new OneWholeCommentViewHolder(main, fragment, type);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull OneWholeCommentViewHolder holder, int position)
    {
        int thePosition = holder.getAdapterPosition();
        switch (type)
        {
            case 0:
            {
                if(suiJiReplyBeanMap != null && suiJiReplyBeanMap.size() > thePosition)
                {
                    holder.getItemView().setTag(suiJiReplyBeanMap.get(thePosition));
                    holder.getTextView().setText(suiJiReplyBeanMap.get(thePosition).getReply_message());
                }
            }break;
            case 1:
            {
                if(articleReplyBeanMap != null && articleReplyBeanMap.size() > thePosition)
                {
                    holder.getItemView().setTag(articleReplyBeanMap.get(thePosition));
                    holder.getTextView().setText(articleReplyBeanMap.get(thePosition).getReply_message());
                }
            }break;
        }
    }

    @Override
    public int getItemCount() {

        switch (type)
        {
            case 0:
            {
                return Math.max(3, suiJiReplyBeanMap.size());
            }
            case 1:
            {
                return Math.max(3, articleReplyBeanMap.size());
            }
        }
        return 3;

    }
}
