package com.xxhhxhh.mainthing.mine.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.databean.RelatedBean;
import com.xxhhxhh.mainthing.mine.MineFragment;
import com.xxhhxhh.mainthing.mine.holder.InfoLineViewHolder;

import java.util.HashMap;
import java.util.Map;

public class FocusAdapter extends RecyclerView.Adapter<InfoLineViewHolder>
{
    private Map<Integer, RelatedBean> beanMap = new HashMap<>();
    public Map<Integer, RelatedBean> getBeanMap() { return beanMap; }
    private Map<Integer, Integer> clickedThings = new HashMap<>();
    public Map<Integer, Integer> getClickedThings() { return clickedThings; }

    private int type;//类型标识
    public FocusAdapter(int type)
    {
        this.type = type;
    }

    @Override
    public int getItemViewType(int position)
    {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public InfoLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout one = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.info_show_line, parent, false);

        return new InfoLineViewHolder(one, type, this);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoLineViewHolder holder, int position)
    {
        int thePosition = holder.getAdapterPosition();
        if(beanMap != null && !beanMap.isEmpty())
        {
            if(beanMap.size() > thePosition)
            {
                holder.getUsername().setText(beanMap.get(thePosition).getToUsername());
            }
        }
    }

    @Override
    public int getItemCount() {
        return beanMap.size();
    }

}
