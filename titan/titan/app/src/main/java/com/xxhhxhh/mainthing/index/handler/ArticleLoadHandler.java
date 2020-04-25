package com.xxhhxhh.mainthing.index.handler;

import android.os.Handler;
import com.xxhhxhh.mainthing.index.dataload.ArticleDataLoad;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForArticleFragment;

public class ArticleLoadHandler extends Handler
        implements Runnable {
    public int start;//起始位置
    public int loadNumber;//加载数量
    private  int i = 0;
    public void setI(int i) { this.i = i; }
    public int getLoadNumber() {
        return loadNumber;
    }
    public int getStart() {
        return start;
    }
    public void setLoadNumber(int loadNumber) {
        this.loadNumber = loadNumber;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public OneLabelForArticleFragment oneLabelForArticleFragment;

    //ui更新
    @Override
    public void run()
    {
        if (i <= getLoadNumber())
        {
            oneLabelForArticleFragment.executePools.execute(new ArticleDataLoad(oneLabelForArticleFragment.articleAdapter,
                    oneLabelForArticleFragment.username, 1,  getStart() + i));
            oneLabelForArticleFragment.articleAdapter.notifyItemInserted(getStart() + i);
            oneLabelForArticleFragment.articleAdapter.notifyItemChanged(getStart() + i);
            i++;
        }
        else
            {
            setI(0);
        }
    }
}