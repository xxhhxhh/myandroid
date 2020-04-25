package com.xxhhxhh.mainthing.addmessage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.xxhhxhh.mainthing.addmessage.fragment.PictureLookFrgment;
import com.xxhhxhh.mainthing.addmessage.holder.PictureLookHolder;
import com.example.titan.R;

public class PictureLookAdapter extends RecyclerView.Adapter<PictureLookHolder>
{

    public static  int itemCounts = 6;//行数计数
    private PictureLookHolder holder;
    private Button confirm;
    private Context context;
    private Fragment fragment;
    private int type;
    private FragmentTransaction transaction;

    public PictureLookAdapter(Button confirm, Context context, Fragment fragment, int type, FragmentTransaction transaction)
    {
        this.confirm = confirm;
        this.context = context;
        this.fragment = fragment;
        this.type = type;
        this.transaction = transaction;
    }

    @NonNull
    @Override
    public PictureLookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        FrameLayout rootView = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_line, null);
        holder = new PictureLookHolder(rootView, confirm, context, fragment, type, transaction);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PictureLookHolder holder, int position)
    {
        holder.setNowItem(position);
        for(int i = 0; i <= 2; i++)
        {
            switch (i)
            {
                case 0:
                {
                    if(position == 0)
                    {
                        setPhoto(position, holder.getImageView1());
                        holder.getImageView1().setClickable(true);
                    }
                    else
                    {
                        setPhoto(position * 3, holder.getImageView1());
                        holder.getImageView1().setLongClickable(true);
                        holder.getImageView2().setLongClickable(true);
                        holder.getImageView2().setClickable(true);
                    }

                }break;
                case 1:
                {
                    setPhoto(position * 3 + 1, holder.getImageView3());
                    holder.getImageView3().setLongClickable(true);
                    holder.getImageView4().setLongClickable(true);
                    holder.getImageView4().setClickable(true);
                }break;
                case 2:
                {
                    setPhoto(position * 3 + 2, holder.getImageView5());
                    holder.getImageView5().setLongClickable(true);
                    holder.getImageView6().setLongClickable(true);
                    holder.getImageView6().setClickable(true);
                }break;
            }
        }

    }

    //设置图片
    private void setPhoto(int position, ImageView imageView)
    {
        if(PictureLookFrgment.photosMap.get(position) != null)
        {
            imageView.setImageBitmap(PictureLookFrgment.photosMap.get(position));
        }
    }



    @Override
    public int getItemCount() {
        return itemCounts;
    }


}
