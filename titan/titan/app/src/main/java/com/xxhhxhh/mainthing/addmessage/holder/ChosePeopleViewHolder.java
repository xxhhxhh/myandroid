package com.xxhhxhh.mainthing.addmessage.holder;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.fragment.AddLabelFragment;
import com.xxhhxhh.mainthing.addmessage.fragment.AddPeopleFragment;
import com.xxhhxhh.mainthing.addmessage.fragment.AddSuiJiFragment;

public class ChosePeopleViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{
    private TextView textView;
    private ImageView imageView;
    public TextView getTextView() { return textView; }
    public ImageView getImageView() { return imageView; }
    public void setImageView(ImageView imageView) { this.imageView = imageView; }
    public void setTextView(TextView textView) { this.textView = textView; }
    private int nowLocation;
    public void setNowLocation(int nowLocation) { this.nowLocation = nowLocation; }
    private AddSuiJiFragment addSuiJiFragment;
    public void setAddSuiJiFragment(AddSuiJiFragment addSuiJiFragment) { this.addSuiJiFragment = addSuiJiFragment; }

    public ChosePeopleViewHolder(@NonNull View itemView, AddSuiJiFragment addSuiJiFragment)
    {
        super(itemView);
        setImageView(itemView.findViewById(R.id.isChose));
        setTextView(itemView.findViewById(R.id.choseMessage));
        setAddSuiJiFragment(addSuiJiFragment);
        getImageView().setOnClickListener(this);
        getTextView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.isChose)
        {
            chose();
        }
        else if(v.getId() == R.id.choseMessage)
        {
            chose();
        }
    }

    //数据选择
    private void chose()
    {
        int length = addSuiJiFragment.mainMessageAdd.length();
        String aa = "@" + getTextView().getText();
        int length1 = aa.length() + length;
        addSuiJiFragment.mainMessageAdd.append(aa);
        setAddPeople(length, length1);
        addSuiJiFragment.getFragmentManager().popBackStack();
    }

    private void setAddPeople(int length, int length1)
    {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget)
            {
                Toast.makeText(addSuiJiFragment.getContext(), "此处被点击", Toast.LENGTH_SHORT);
            }
        };
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0000ff"));
        addSuiJiFragment.mainMessageAdd.setSpan(colorSpan, length, length1, Spanned.SPAN_MARK_MARK);
        addSuiJiFragment.mainMessageAdd.setSpan( clickableSpan, length, length1, Spanned.SPAN_MARK_MARK);
    }
}
