package com.xxhhxhh.mainthing.show.commentsandreply;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.xxhhxhh.mainthing.ShowArticleActivity;
import com.xxhhxhh.mainthing.databean.ArticleCommentDataBean;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.show.commentsandreply.adapter.ShowCommentsAdapter;
import com.xxhhxhh.mainthing.show.commentsandreply.loaddata.ArticleCommentLoad;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowCommentsFragment extends Fragment
    implements View.OnClickListener
{
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ShowCommentsAdapter showCommentsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        RecyclerView recyclerView = new RecyclerView(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArticleDataBean articleDataBean = ((ShowArticleActivity) getActivity()).getArticleDataBean();
        showCommentsAdapter = new ShowCommentsAdapter(1, this);
        executorService.execute(new ArticleCommentLoad(showCommentsAdapter, articleDataBean != null ?
                articleDataBean.getArticle_id() : 0));
        new CountDownTimer(20000, 2500)
        {

            @Override
            public void onTick(long millisUntilFinished)
            {
                showCommentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish()
            {

            }
        }.start();
        recyclerView.setAdapter(showCommentsAdapter);
        return recyclerView;
    }

    @Override
    public void onClick(View v)
    {

    }
}
