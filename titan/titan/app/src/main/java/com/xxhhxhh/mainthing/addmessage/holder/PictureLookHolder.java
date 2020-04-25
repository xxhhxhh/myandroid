package com.xxhhxhh.mainthing.addmessage.holder;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.fragment.AddArticleFragment;
import com.xxhhxhh.mainthing.addmessage.fragment.AddSuiJiFragment;
import com.xxhhxhh.mainthing.addmessage.fragment.CameraPreviewFragment;
import com.xxhhxhh.mainthing.addmessage.fragment.PictureLookFrgment;

public class PictureLookHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{

    public ImageView imageView1;
    public ImageView imageView2;
    public ImageView imageView3;
    public ImageView imageView4;
    public ImageView imageView5;
    public ImageView imageView6;
    public View rootView;
    public int nowItem;//当前item
    private Button confirm;
    private Context context;
    public AddSuiJiFragment addSuiJiFragment;
    public AddArticleFragment addArticleFragment;
    public FragmentTransaction transaction;
    public int type;

    public PictureLookHolder(@NonNull View itemView, Button confirm, Context context, Fragment fragment, int type, FragmentTransaction transaction) {
        super(itemView);
        setRootView(itemView.getRootView());
        setType(type);
        setTransaction(transaction);
        if(getType() == 0)
        {
            setAddSuiJiFragment((AddSuiJiFragment) fragment);
        }
        else if(getType() == 1)
        {
            setAddArticleFragment((AddArticleFragment)fragment);
        }
        this.confirm = confirm;
        this.context = context;
        setImageView1((ImageView) itemView.findViewById(R.id.image1));
        setImageView2((ImageView)itemView.findViewById(R.id.image2));
        setImageView3((ImageView)itemView.findViewById(R.id.image3));
        setImageView4((ImageView)itemView.findViewById(R.id.image4));
        setImageView5((ImageView)itemView.findViewById(R.id.image5));
        setImageView6((ImageView)itemView.findViewById(R.id.image6));
        getImageView2().setVisibility(View.INVISIBLE);
        getImageView4().setVisibility(View.INVISIBLE);
        getImageView6().setVisibility(View.INVISIBLE);
        getImageView1().setOnClickListener(this);
        getImageView2().setOnClickListener(this);
        getImageView3().setOnClickListener(this);
        getImageView4().setOnClickListener(this);
        getImageView5().setOnClickListener(this);
        getImageView6().setOnClickListener(this);
        getImageView2().setClickable(false);
        getImageView6().setClickable(false);
        getImageView4().setClickable(false);
        getImageView1().setLongClickable(false);
        getImageView2().setLongClickable(false);
        getImageView3().setLongClickable(false);
        getImageView4().setLongClickable(false);
        getImageView5().setLongClickable(false);
        getImageView6().setLongClickable(false);
    }

    public ImageView getImageView1() { return imageView1; }
    public ImageView getImageView2() { return imageView2; }
    public ImageView getImageView3() { return imageView3; }
    public ImageView getImageView5() { return imageView5; }
    public ImageView getImageView4() { return imageView4; }
    public ImageView getImageView6() { return imageView6; }
    public View getRootView() { return rootView; }
    public void setRootView(View rootView) { this.rootView = rootView; }
    public void setImageView1(ImageView imageView1) { this.imageView1 = imageView1; }
    public void setImageView3(ImageView imageView3) { this.imageView3 = imageView3; }
    public void setImageView2(ImageView imageView2) { this.imageView2 = imageView2; }
    public void setImageView4(ImageView imageView4) { this.imageView4 = imageView4; }
    public void setImageView5(ImageView imageView5) { this.imageView5 = imageView5; }
    public void setImageView6(ImageView imageView6) { this.imageView6 = imageView6; }
    public int getNowItem() { return nowItem; }
    public void setNowItem(int nowItem) { this.nowItem = nowItem; }
    public AddSuiJiFragment getAddSuiJiFragment() { return addSuiJiFragment; }
    public void setAddSuiJiFragment(AddSuiJiFragment addSuiJiFragment) { this.addSuiJiFragment = addSuiJiFragment; }
    public AddArticleFragment getAddArticleFragment() { return addArticleFragment; }
    public void setAddArticleFragment(AddArticleFragment addArticleFragment) { this.addArticleFragment = addArticleFragment; }
    public void setType(int type){this.type = type;}
    public int getType(){return this.type;}
    public FragmentTransaction getTransaction() { return transaction; }
    public void setTransaction(FragmentTransaction transaction) { this.transaction = transaction; }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.image1:
            {
                if(getNowItem() == 0)
                {
                    CameraPreviewFragment cameraPreviewFragment = new CameraPreviewFragment();
                    //页面切换
                    if(getType() == 0)
                    {
                        cameraPreviewFragment.setType(0);
                        getTransaction().replace(R.id.addMessagePage, cameraPreviewFragment).addToBackStack("addsuiji").commit();
                    }
                    else if(getType() == 1)
                    {
                        cameraPreviewFragment.setType(1);
                        getTransaction().replace(R.id.addMessagePage, cameraPreviewFragment).addToBackStack("addarticle").commit();
                    }
                }
                else
                {
                    addOrDeleteToPictureLook(getAdapterPosition() * 3,0, imageView1, imageView2, confirm, context);
                }
            }break;
            case R.id.image2:
            {
                if(getNowItem() != 0)
                {
                   addOrDeleteToPictureLook(getAdapterPosition() * 3, 1, imageView1, imageView2, confirm, context);
                }
            }break;
            case R.id.image3:
            {
                addOrDeleteToPictureLook(getAdapterPosition() * 3 + 1, 0, imageView3, imageView4, confirm, context);
            }break;
            case R.id.image4:
            {
                addOrDeleteToPictureLook( getAdapterPosition() * 3 + 1,1, imageView3, imageView4, confirm, context);
            }break;
            case R.id.image5:
            {
                addOrDeleteToPictureLook(getAdapterPosition() * 3 + 2,0, imageView5, imageView6, confirm, context);
            }break;
            case R.id.image6:
            {
                addOrDeleteToPictureLook( getAdapterPosition()  * 3 + 2,1, imageView5, imageView6, confirm, context);
            }break;
        }
    }

    //添加或删除图片视图列表
    public void addOrDeleteToPictureLook(int Key, int type, ImageView imageView1, ImageView imageView2, Button confirm, Context context)
    {
        //随记
        if(getType() == 0)
        {
            if(getAddSuiJiFragment().getAddList().size() <= 9 && type == 0)
            {
                //添加
                if(imageView1.getDrawable() != null)
                {
                    PictureLookFrgment.selectPhotos.put(Key, ((BitmapDrawable)imageView1.getDrawable()).getBitmap());
                    imageView2.setVisibility(View.VISIBLE);
                    System.out.println(getAddSuiJiFragment().getAddList().size());
                }

            }
            //删除
            else if(type == 1 && imageView1.getDrawable() != null)
            {
                PictureLookFrgment.selectPhotos.remove(Key);
                imageView2.setVisibility(View.INVISIBLE);
            }
            else
            {
                Toast.makeText(context, "图片选择已达上限", Toast.LENGTH_SHORT).show();
            }
            confirm.setText("(" + (getAddSuiJiFragment().getAddList().size()+ PictureLookFrgment.selectPhotos.size()) + "/9)确定");
        }
        //文章
        else if(getType() == 1)
        {
            if(addArticleFragment.getNumber() <= 30 && type == 0)
            {
                //添加
                if(imageView1.getDrawable() != null)
                {
                    PictureLookFrgment.selectPhotos.put(Key, ((BitmapDrawable)imageView1.getDrawable()).getBitmap());
                    imageView2.setVisibility(View.VISIBLE);
                }

            }
            //删除
            else if(type == 1 && imageView1.getDrawable() != null)
            {
                PictureLookFrgment.selectPhotos.remove(Key);
                imageView2.setVisibility(View.INVISIBLE);
            }
            else
            {
                Toast.makeText(context, "图片选择已达上限", Toast.LENGTH_SHORT).show();
            }
            confirm.setText("(" + (addArticleFragment.getNumber() + PictureLookFrgment.selectPhotos.size()) + "/30)确定");
        }
    }

}
