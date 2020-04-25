package com.xxhhxhh.mainthing.index.adpter;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.index.dataload.ArticleDataLoad;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForArticleFragment;
import com.xxhhxhh.mainthing.index.holder.ArticleViewHolder;
import com.example.titan.R;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder>
    implements View.OnClickListener
{


    public int getSize() {
        return dataBeans.size() <= 6 ? dataBeans.size() : 7;
    }
    public boolean isFinally = false;//上一次最后一个
    private Fragment fragment;
    private View rootView;
    private Context context;
    private RecyclerView recyclerView;
    public int position;
    public Map<Integer, ArticleDataBean> dataBeans = new HashMap<>();

    public ArticleAdapter(Context context, RecyclerView recyclerView, Fragment fragment) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == getItemCount() - 1)
        {
            return 1;
        }
        else
            {
            return 0;
        }

    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
        {

            rootView = LayoutInflater.from(context).inflate(R.layout.article, parent, false);
        }
        else if (viewType == 1)
        {
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_line, parent, false);
            ((ProgressBar) rootView.findViewById(R.id.loadingProgress)).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.loadEndTipText)).setVisibility(View.GONE);
        }
        return new ArticleViewHolder(rootView, viewType, fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position)
    {
        int thePosition = holder.getAdapterPosition();

        if (getItemViewType(position) == 1 && isFinally)
        {
            isFinally = false;
            holder.getItemView().findViewById(R.id.loadingProgress).setVisibility(View.GONE);
            holder.getItemView().findViewById(R.id.loadEndTipText).setVisibility(View.VISIBLE);
            holder.getItemView().findViewById(R.id.loadEndTipText).setOnClickListener(this);
        }
        else if (getItemViewType(thePosition) == 0)
         {
            //赋值
             if(dataBeans != null && dataBeans.size() > thePosition  && dataBeans.size() > position && dataBeans.get(thePosition) != null)
             {
                 holder.getItemView().setTag(dataBeans.get(thePosition));
                 holder.getCommentNumber().setText("评论" + String.valueOf(dataBeans.get(thePosition).getComment_number()));
                 if(dataBeans.get(thePosition).getNickName().equals(""))
                 {
                     holder.getShowUsername().setText(dataBeans.get(thePosition).getUsername());
                 }
                 else
                 {
                     holder.getShowUsername().setText(dataBeans.get(thePosition).getNickName());
                 }
                 holder.getArticleTitle().setText(dataBeans.get(thePosition).getArticle_title());
                 holder.getDateAndLabel().setText(dataBeans.get(thePosition).getThe_date());
             }

        }
    }

    @Override
    public int getItemCount()
    {
        return Math.max(3, getSize());
    }


    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.loadEndTipText)
        {
            v.setVisibility(View.GONE);
            isFinally = false;
            recyclerView.scrollToPosition(0);
            notifyItemRangeRemoved(0, getItemCount());
            dataBeans.clear();
            ((OneLabelForArticleFragment)fragment).executePools.
                    execute(new ArticleDataLoad(this, ((OneLabelForArticleFragment)fragment).username, 3, 0));
            ((OneLabelForArticleFragment) fragment).handler.setI(0);
            ((OneLabelForArticleFragment) fragment).loadTimer = new OneLabelForArticleFragment.LoadTimer(5000, 1000, this);
            ((OneLabelForArticleFragment) fragment).loadTimer.start();
        }
    }
}
