package com.xxhhxhh.mainthing.addmessage.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.fragment.AddLabelFragment;

public class ChoseLabelsViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{
    private TextView textView;
    private ImageView imageView;
    public TextView getTextView() { return textView; }
    public ImageView getImageView() { return imageView; }
    public void setImageView(ImageView imageView) { this.imageView = imageView; }
    public void setTextView(TextView textView) { this.textView = textView; }
    private AddLabelFragment addLabelFragment;
    public void setAddLabelFragment(AddLabelFragment addLabelFragment) { this.addLabelFragment = addLabelFragment; }

    public ChoseLabelsViewHolder(@NonNull View itemView, AddLabelFragment addLabelFragment)
    {
        super(itemView);
        setImageView(itemView.findViewById(R.id.isChose));
        setTextView(itemView.findViewById(R.id.choseMessage));
        setAddLabelFragment(addLabelFragment);
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
        if(getImageView().getVisibility() == View.VISIBLE)
        {
            addLabelFragment.chosedLabels.remove(getTextView().getText().toString());
            getImageView().setVisibility(View.INVISIBLE);

        }
        else if(getImageView().getVisibility() == View.INVISIBLE)
        {
            addLabelFragment.chosedLabels.put(getTextView().getText().toString(), getTextView().getText().toString());
            getImageView().setVisibility(View.VISIBLE);
        }
    }
}
