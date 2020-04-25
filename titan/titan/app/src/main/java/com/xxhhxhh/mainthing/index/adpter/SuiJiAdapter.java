package com.xxhhxhh.mainthing.index.adpter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.index.dataload.ArticleDataLoad;
import com.xxhhxhh.mainthing.index.dataload.SuiJiDataLoad;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForArticleFragment;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForSuiJiFragment;
import com.xxhhxhh.mainthing.index.holder.SuiJiViewHolder;
import com.example.titan.R;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SuiJiAdapter extends RecyclerView.Adapter<SuiJiViewHolder>
    implements View.OnClickListener
{

    public Context context;//上下文
    public int getSize() { return dataBeans.size() <= 6 ? dataBeans.size() : 7;}
    public boolean isFinally = false;//是不是最后一个
    public boolean isBottom;
    public void setBottom(boolean bottom) { isBottom = bottom; }
    public LinearLayout one;
    public void setOne(LinearLayout one) { this.one = one; }
    private RecyclerView recyclerView;
    private Fragment fragment;
    public Map<Integer, SuiJiDataBean> dataBeans = new HashMap<>();
    public Map<Integer, SuiJiDataBean> getDataBeans() { return dataBeans; }

    public SuiJiAdapter(Context context, RecyclerView recyclerView, Fragment fragment)
    {
        this.recyclerView = recyclerView;
        this.context = context;
        this.fragment = fragment;
    }


    @Override
    public int getItemViewType(int thePostition)
    {
        if(thePostition == getItemCount() - 1)
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
    public SuiJiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       //根据viewtype设置
        if(viewType == 0)
        {
            //每一个随记
            one = new LinearLayout(parent.getContext());
            one.setId(R.id.theSuiJi);
            one.setOrientation(LinearLayout.VERTICAL);
            //用户信息栏
            LinearLayout userinfo = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.userinfo_row, parent, false);
            one.addView(userinfo);
            //随记栏
            //
            LinearLayout suiJi = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.suiji, parent, false);
            one.addView(suiJi);
        }
        else if(viewType == 1)
        {
            one = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_line, parent, false);
            ((ProgressBar)one.findViewById(R.id.loadingProgress)).setVisibility(View.VISIBLE);
            ((TextView)one.findViewById(R.id.loadEndTipText)).setVisibility(View.GONE);
        }
        return new SuiJiViewHolder(one, viewType, fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull SuiJiViewHolder holder, int postition)
    {
        int thePosition = postition;
        if(getItemViewType(thePosition) == 1 && isFinally)
        {
            isFinally = false;
            holder.getItemView().findViewById(R.id.loadingProgress).setVisibility(View.GONE);
            holder.getItemView().findViewById(R.id.loadEndTipText).setVisibility(View.VISIBLE);
            holder.getItemView().findViewById(R.id.loadEndTipText).setOnClickListener(this);
        }
        else if(getItemViewType(postition) == 0)
        {

            if(dataBeans != null && dataBeans.size() > postition && dataBeans.get(thePosition) != null)
            {
                holder.itemView.setTag(dataBeans.get(thePosition));
                //生成数据
                holder.getMessageSuiJi().setText(dataBeans.get(thePosition).getMain_message());
                holder.getGood().setText("赞" + dataBeans.get(thePosition).getGoods());
                holder.getComment().setText("评论" + dataBeans.get(thePosition).getComment_number());
                if(dataBeans.get(thePosition).getNickName() == null || dataBeans.get(thePosition).getNickName().equals(""))
                {
                    holder.getUsername().setText(dataBeans.get(thePosition).getUsername());
                }
                else
                {
                    holder.getUsername().setText(dataBeans.get(thePosition).getNickName());
                }
                holder.getDate().setText(dataBeans.get(thePosition).getThe_date());

                if(dataBeans.get(thePosition).getUserHead() != null)
                {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                            Base64.decode(dataBeans.get(thePosition).getUserHead(), Base64.NO_WRAP));
                    holder.getHeadImage().setImageBitmap(BitmapFactory.decodeStream(byteArrayInputStream));
                }

                //设置标签
                if (dataBeans.get(thePosition).getLabels().length() > 0)
                {
                    try
                    {
                        holder.getLabel().setText(String.valueOf(dataBeans.get(thePosition).getLabels().get(0)));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }


                if(dataBeans.get(thePosition).getHave_file() > 0 && dataBeans.get(thePosition).getFileType() == 0)
                {
                    holder.getTablePicture().setVisibility(View.VISIBLE);

                    if(dataBeans.get(thePosition).getBitmapMap() != null && dataBeans.size() > thePosition)
                    {
                        switch (dataBeans.get(thePosition).getBitmapMap().size())
                        {

                            case 1:
                            {
                                holder.getTablePicture().findViewById(R.id.row1).setVisibility(View.VISIBLE);
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage1)).
                                        setImageBitmap(
                                                dataBeans.get(thePosition).getBitmapMap().get(0));
                            }break;
                            case 2:
                            {
                                holder.getTablePicture().findViewById(R.id.row1).setVisibility(View.VISIBLE);
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage1)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(0));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage2)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(1));
                            }break;
                            case 3:
                            {
                                holder.getTablePicture().findViewById(R.id.row1).setVisibility(View.VISIBLE);
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage1)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(0));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage2)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(1));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage3)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(2));
                            }break;
                            case 4:
                            {
                                holder.getTablePicture().findViewById(R.id.row1).setVisibility(View.VISIBLE);
                                holder.getTablePicture().findViewById(R.id.row2).setVisibility(View.VISIBLE);
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage1)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(0));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage2)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(1));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage3)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(2));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage4)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(3));
                            }break;
                            case 5:
                            {
                                holder.getTablePicture().findViewById(R.id.row1).setVisibility(View.VISIBLE);
                                holder.getTablePicture().findViewById(R.id.row2).setVisibility(View.VISIBLE);
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage1)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(0));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage2)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(1));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage3)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(2));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage4)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(3));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage5)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(4));
                            }break;
                            case 6:
                            {
                                holder.getTablePicture().findViewById(R.id.row1).setVisibility(View.VISIBLE);
                                holder.getTablePicture().findViewById(R.id.row2).setVisibility(View.VISIBLE);
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage1)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(0));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage2)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(1));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage3)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(2));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage4)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(3));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage5)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(4));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage6)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(5));
                            }break;
                            case 7:
                            {
                                holder.getTablePicture().findViewById(R.id.row1).setVisibility(View.VISIBLE);
                                holder.getTablePicture().findViewById(R.id.row2).setVisibility(View.VISIBLE);
                                holder.getTablePicture().findViewById(R.id.row3).setVisibility(View.VISIBLE);
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage1)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(0));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage2)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(1));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage3)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(2));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage4)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(3));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage5)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(4));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage6)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(5));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage7)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(6));
                            }break;
                            case 8:
                            {
                                holder.getTablePicture().findViewById(R.id.row1).setVisibility(View.VISIBLE);
                                holder.getTablePicture().findViewById(R.id.row2).setVisibility(View.VISIBLE);
                                holder.getTablePicture().findViewById(R.id.row3).setVisibility(View.VISIBLE);
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage1)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(0));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage2)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(1));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage3)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(2));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage4)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(3));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage5)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(4));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage6)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(5));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage7)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(6));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage8)).
                                    setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(7));

                            }break;
                            case 9:
                            {
                                holder.getTablePicture().findViewById(R.id.row1).setVisibility(View.VISIBLE);
                                holder.getTablePicture().findViewById(R.id.row2).setVisibility(View.VISIBLE);
                                holder.getTablePicture().findViewById(R.id.row3).setVisibility(View.VISIBLE);
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage1)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(0));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage2)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(1));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage3)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(2));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage4)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(3));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage5)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(4));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage6)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(5));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage7)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(6));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage8)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(7));
                                ((ImageView)holder.getTablePicture().findViewById(R.id.mainImage9)).
                                        setImageBitmap(dataBeans.get(thePosition).getBitmapMap().get(8));
                            }break;
                        }
                    }

                }
                else
                {
                    holder.getTablePicture().setVisibility(View.GONE);
                }

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
            recyclerView.scrollToPosition(0);
            notifyItemRangeRemoved(0, getItemCount());
            dataBeans.clear();
            isFinally = false;
            ((OneLabelForSuiJiFragment)fragment).executorService.
                    execute(new SuiJiDataLoad(this, ((OneLabelForSuiJiFragment)fragment).username, 3, 0));
            ((OneLabelForSuiJiFragment) fragment).handler.setI(0);
            ((OneLabelForSuiJiFragment) fragment).loadTimer = new OneLabelForSuiJiFragment.
                    LoadTimer(5000, 1000, this);
            ((OneLabelForSuiJiFragment) fragment).loadTimer.start();
        }
    }
}
