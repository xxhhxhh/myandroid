package com.xxhhxhh.mainthing.holder;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.fragment.AddSuiJiFragment;

public class NinePictureTableHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener,
        View.OnLongClickListener
{

    public ImageView imageView1;
    public ImageView imageView2;
    public ImageView imageView3;
    public ImageView imageView4;
    public ImageView imageView5;
    public ImageView imageView6;
    public int nowItem;//当前item
    public AddSuiJiFragment addSuiJiFragment;

    public NinePictureTableHolder(@NonNull View itemView, AddSuiJiFragment addSuiJiFragment) {
        super(itemView);
        setAddSuiJiFragment(addSuiJiFragment);
        setImageView1((ImageView) itemView.findViewById(R.id.image1));
        setImageView2((ImageView)itemView.findViewById(R.id.image2));
        setImageView3((ImageView)itemView.findViewById(R.id.image3));
        setImageView4((ImageView)itemView.findViewById(R.id.image4));
        setImageView5((ImageView)itemView.findViewById(R.id.image5));
        setImageView6((ImageView)itemView.findViewById(R.id.image6));
        getImageView2().setVisibility(View.INVISIBLE);
        getImageView4().setVisibility(View.INVISIBLE);
        getImageView6().setVisibility(View.INVISIBLE);
        getImageView1().setOnLongClickListener(this);
        getImageView1().setOnClickListener(this);
        getImageView2().setOnClickListener(this);
        getImageView3().setOnLongClickListener(this);
        getImageView3().setOnClickListener(this);
        getImageView4().setOnClickListener(this);
        getImageView5().setOnLongClickListener(this);
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

    //长按选中添加至删除，点击取消后从列表移除
    @Override
    public void onClick(View v) {


        switch (v.getId())
        {
            case R.id.image2:
            {
                addOrRemoveFromWillDelete(getNowItem() * 3, 1,getImageView1(), getImageView2());
            }break;
            case R.id.image4:
            {
                addOrRemoveFromWillDelete(getNowItem() * 3 + 1, 1,getImageView3(), getImageView4());

            }break;
            case R.id.image6:
            {
                addOrRemoveFromWillDelete(getNowItem() * 3 + 2, 1,getImageView5(), getImageView6());
            }break;
        }
    }

    //添加进入即将删除列表,item计数、view的值1，3，5、类型：0添加1删除
    public void addOrRemoveFromWillDelete(int key, int type, ImageView imageView, ImageView imageView2)
    {
        int a = getAddSuiJiFragment().getWillDeleteSize();
        if(a <= 9)
        {
            if(type == 0)
            {
                getAddSuiJiFragment().willDelete.put(a, key);
                if(imageView.getDrawable() != null)
                    imageView2.setVisibility(View.VISIBLE);
            }
            else if(type == 1)
            {
                getAddSuiJiFragment().willDelete.remove(key);
                if(imageView.getDrawable() != null)
                    imageView2.setVisibility(View.INVISIBLE);
            }
        }
    }




    @Override
    public boolean onLongClick(View v) {
        switch (v.getId())
        {
            case R.id.image1:
            {
                addOrRemoveFromWillDelete(getNowItem() * 3, 0, getImageView1(), getImageView2());
                return true;
            }
            case R.id.image2:
            {

                addOrRemoveFromWillDelete(getNowItem() * 3, 1, getImageView1(), getImageView2());
                return true;
            }
            case R.id.image3:
            {

                addOrRemoveFromWillDelete(getNowItem() * 3 + 1, 0, getImageView3(), getImageView4());
                return true;
            }
            case R.id.image4:
            {

                addOrRemoveFromWillDelete(getNowItem() * 3 + 1, 1, getImageView3(), getImageView4());
                return true;
            }
            case R.id.image5:
            {

                addOrRemoveFromWillDelete(getNowItem() * 3 + 2, 0, getImageView5(), getImageView6());
                return true;
            }
            case R.id.image6:
            {

                addOrRemoveFromWillDelete(getNowItem() * 3 + 2, 1, getImageView5(), getImageView6());
                return true;
            }
        }
        return false;
    }
}
