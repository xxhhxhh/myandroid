package com.xxhhxhh.mainthing.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.xxhhxhh.mainthing.adapter.ReportPopUpAdapter;

import java.util.HashMap;
import java.util.Map;

public class ToReportDialog extends AlertDialog.Builder
{
    private Context context;
    private Map<Integer, String> theData = new HashMap<>();
    private String username;//被举报者

    public ToReportDialog(Context context, String username)
    {
        super(context);
        this.context = context;
        this.username = username;
        init();
    }


    //初始化
    private void init()
    {
        LinearLayout main = new LinearLayout(context);
        RecyclerView recyclerView = new RecyclerView(context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutManager(manager);
        theData.put(0, "恶意内容");
        theData.put(1, "社会危害");
        theData.put(2, "非法内容");
        ReportPopUpAdapter reportPopUpAdapter = new ReportPopUpAdapter(theData, username);
        recyclerView.setAdapter(reportPopUpAdapter);
        main.addView(recyclerView);
        setView(main);
        setTitle("举报选项");
        setNegativeButton("取消", null);
    }

}
