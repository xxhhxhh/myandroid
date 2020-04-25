package com.xxhhxhh.mainthing.search;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.search.adapter.SearchResultAdapter;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchFragment extends Fragment
    implements SearchView.OnQueryTextListener
{

    private SearchResultAdapter searchResultAdapter;
    private SearchView searchView;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search, container, false);
        //搜索框处理
        searchView = rootView.getRootView().findViewById(R.id.search);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getResources().getString(R.string.findTipText));

        RecyclerView recyclerView = rootView.getRootView().findViewById(R.id.showSearchResult);
        searchResultAdapter = new SearchResultAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(searchResultAdapter);
        recyclerView.setLayoutManager(manager);

        return rootView;
    }


    @Override
    public boolean onQueryTextSubmit(String query)
    {

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                handleMessage(query);
            }
        };
        executorService.execute(thread);
        new LoadTimer(5000, 1000, searchResultAdapter).start();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        searchResultAdapter.getSuiJiDataBeanMap().clear();
        searchResultAdapter.getArticleDataBeanMap().clear();
        searchResultAdapter.notifyDataSetChanged();
        return false;
    }




        public void handleMessage(String text)
        {
                //获取数据
                HttpsUtil httpsUtil = new HttpsUtil(TheUrls.RESULT_FIND);
                Map<String, String> data = new HashMap<>();
                data.put("message", text);

                httpsUtil.setSendData(data);

                try
                {
                    httpsUtil.sendPost();
                    String reusult = httpsUtil.getResponse().body().string();
                    JSONObject jsonObject = new JSONObject(reusult);
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("finallyResult"));

                    int i = 0;
                    for (Iterator<String> it = jsonObject1.keys(); it.hasNext();)
                    {
                        String key = (String) it.next();
                        jsonObject = new JSONObject(jsonObject1.getString(key));
                        //随记
                        if(jsonObject.getInt("type") == 0)
                        {
                            SuiJiDataBean suiJiDataBean = new SuiJiDataBean();
                            suiJiDataBean.setSuiji_id(jsonObject.getInt("id"));
                            suiJiDataBean.setMain_message(jsonObject.getString("message"));
                            searchResultAdapter.getSuiJiDataBeanMap().put(i, suiJiDataBean);
                        }
                        //文章
                        else if(jsonObject.getInt("type") == 1)
                        {
                            ArticleDataBean articleDataBean = new ArticleDataBean();
                            articleDataBean.setArticle_id(jsonObject.getInt("id"));
                            articleDataBean.setArticle_title(jsonObject.getString("message"));
                            searchResultAdapter.getArticleDataBeanMap().put(i, articleDataBean);
                        }
                        i++;
                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }



    static class LoadTimer extends CountDownTimer
    {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        private SearchResultAdapter searchResultAdapter;
        public LoadTimer(long millisInFuture, long countDownInterval, SearchResultAdapter searchResultAdapter)
        {
            super(millisInFuture, countDownInterval);
            this.searchResultAdapter = searchResultAdapter;
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            searchResultAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFinish() {

        }
    }
}
