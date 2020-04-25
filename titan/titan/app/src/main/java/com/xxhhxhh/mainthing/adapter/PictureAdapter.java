package com.xxhhxhh.mainthing.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.xxhhxhh.mainthing.holder.NinePictureTableHolder;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.fragment.AddSuiJiFragment;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class PictureAdapter extends RecyclerView.Adapter<NinePictureTableHolder>
{

    private Context context;
    private NinePictureTableHolder ninePictureTableHolder;
    private int itemCount;//item计数
    private AddSuiJiFragment fragment;
    private int type;//0为添加，1为观看
    public List<Map<Integer, Bitmap>> bitmaps = new ArrayList<>();
    public int files = 0;
    public int suiji_id = 0;

    public PictureAdapter(AddSuiJiFragment fragment, int type)
    {
        this.fragment = fragment;
        this.type = type;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
        if(type == 0)
        {
            itemCountUpdate();
        }
        else if(type == 1)
        {

        }
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @NonNull
    @Override
    public NinePictureTableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        context = parent.getContext();
        FrameLayout one = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.picture_line, null);
        ninePictureTableHolder = new NinePictureTableHolder(one, fragment);

        return ninePictureTableHolder;
    }


    //计数更新
    public void itemCountUpdate()
    {
        //计算item个数
        if (fragment.getAddList().size() <= 9)
        {
            if (fragment.getAddList().size() <= 3)
            {
                itemCount = 1;
            }
            else if (fragment.getAddList().size()<= 6)
            {
                itemCount = 2;
            }
            else
            {
                itemCount = 3;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NinePictureTableHolder holder, int position)
    {
        //当前item
        holder.setNowItem(position);
        //添加模式
        if(type == 0)
        {

            if(fragment.getAddList() != null)
            {
                System.out.println(fragment.getAddList().size());
               switch (fragment.getAddList().size())
               {
                   case 0:
                   {

                   }break;
                   case 1:
                   {
                       setImage(0, 0, holder);
                   }break;
                   case 2:
                   {
                        setImage(0, 0, holder);
                        setImage(1, 1, holder);
                   }break;
                   case 3:
                   {
                       setImage(0, 0, holder);
                       setImage(1, 1, holder);
                       setImage(2, 2, holder);
                   }break;
                   case 4:
                   {
                       if(position == 0)
                       {
                           setImage(0, 0, holder);
                           setImage(1, 1, holder);
                           setImage(2, 2, holder);
                       }
                       else if(position == 1)
                       {
                           setImage(3, 0, holder);
                       }
                   }break;
                   case 5:
                   {
                       if(position == 0)
                       {
                           setImage(0, 0, holder);
                           setImage(1, 1, holder);
                           setImage(2, 2, holder);
                       }
                       else if(position == 1)
                        {
                           setImage(3, 0, holder);
                           setImage(4, 1, holder);
                         }
                   }break;
                   case 6:
                   {
                       if(position == 0)
                       {
                           setImage(0, 0, holder);
                           setImage(1, 1, holder);
                           setImage(2, 2, holder);
                       }
                       else if(position == 1)
                       {
                           setImage(3, 0, holder);
                           setImage(4, 1, holder);
                           setImage(5, 2, holder);
                       }
                   }break;
                   case 7:
                   {
                       if(position == 0)
                       {
                           setImage(0, 0, holder);
                           setImage(1, 1, holder);
                           setImage(2, 2, holder);
                       }
                       else if(position == 1)
                       {
                           setImage(3, 0, holder);
                           setImage(4, 1, holder);
                           setImage(5, 2, holder);
                       }
                       else if(position == 2)
                       {
                           setImage(6, 0, holder);
                       }
                   }break;
                   case 8:
                   {
                       if(position == 0)
                       {
                           setImage(0, 0, holder);
                           setImage(1, 1, holder);
                           setImage(2, 2, holder);
                       }
                       else if(position == 1)
                       {
                           setImage(3, 0, holder);
                           setImage(4, 1, holder);
                           setImage(5, 2, holder);
                       }
                       else if(position == 2)
                       {
                           setImage(6, 0, holder);
                           setImage(7, 1, holder);
                       }
                   }break;
                   case 9:
                   {
                       if(position == 0)
                       {
                           setImage(0, 0, holder);
                           setImage(1, 1, holder);
                           setImage(2, 2, holder);
                       }
                       else if(position == 1)
                       {
                           setImage(3, 0, holder);
                           setImage(4, 1, holder);
                           setImage(5, 2, holder);
                       }
                       else if(position == 2)
                       {
                           setImage(6, 0, holder);
                           setImage(7, 1, holder);
                           setImage(8, 2, holder);
                       }
                   }break;
               }

            }
        }
    }

    //观看模式赋值
    public void setLookImage(NinePictureTableHolder holder, int key, int type)
    {
        //总长度
        if(bitmaps.size() >= key)
        {
            //内部长度
            if(bitmaps.get(key).size() <= type)
            {
                switch (type)
                {
                    case 1:
                    {
                        holder.getImageView1().setImageBitmap(bitmaps.get(key).get(0));
                    }break;
                    case 2:
                    {
                        holder.getImageView3().setImageBitmap(bitmaps.get(key).get(1));
                    }break;
                    case 3:
                    {
                        holder.getImageView5().setImageBitmap(bitmaps.get(key).get(2));
                    }break;
                }
            }
        }
    }

    //添加图片
    public void setImage(Integer location,Integer key, NinePictureTableHolder holder)
    {
        if(fragment.getAddList().get(location) != null)
        {
            if(key == 0)
            {
                ninePictureTableHolder.getImageView1().setImageBitmap(fragment.getAddList().get(location));
                holder.getImageView1().setLongClickable(true);
                holder.getImageView2().setLongClickable(true);
                holder.getImageView2().setClickable(true);
            }
            else if(key == 1)
            {
                ninePictureTableHolder.getImageView3().setImageBitmap(fragment.getAddList().get(location));
                holder.getImageView3().setLongClickable(true);
                holder.getImageView4().setLongClickable(true);
                holder.getImageView4().setClickable(true);
            }
            else if(key == 2)
            {
                ninePictureTableHolder.getImageView5().setImageBitmap(fragment.getAddList().get(location));
                holder.getImageView5().setLongClickable(true);
                holder.getImageView6().setLongClickable(true);
                holder.getImageView6().setClickable(true);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        if(type == 0)
        {
            return itemCount;
        }
        else if(type == 1)
        {
            return Math.max(1, files / 3);
        }
        return 0;
    }


}
