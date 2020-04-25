package com.xxhhxhh.mainthing.mine.popwindow;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.example.titan.R;
import com.xxhhxhh.transdata.requestcode.RequestCode;

import java.io.File;

public class ChoseHeadImageModePopWindow extends PopupWindow
    implements View.OnClickListener
{
    private Fragment fragment;

    public ChoseHeadImageModePopWindow(Fragment fragment)
    {
        this.fragment = fragment;
        LinearLayout main = new LinearLayout(fragment.getContext());
        main.setOrientation(LinearLayout.VERTICAL);
        main.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        Button chosePicture = new Button(fragment.getContext());
        chosePicture.setOnClickListener(this);
        chosePicture.setHeight(100);
        chosePicture.setText("拍照");
        chosePicture.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        chosePicture.setId(R.id.openCamera);
        Button openCamera = new Button(fragment.getContext());
        openCamera.setOnClickListener(this);
        openCamera.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        openCamera.setId(R.id.chosePicture);
        openCamera.setText("相册选择");
        openCamera.setHeight(100);
        main.addView(openCamera);
        main.addView(chosePicture);
        setContentView(main);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.chosePicture:
            {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                fragment.getActivity().startActivityForResult(intent, RequestCode.GET_PICTURE);
                this.dismiss();
            }break;
            case R.id.openCamera:
            {
                File outFile = new File(fragment.getActivity().getFilesDir(), "aHead.png");
                Uri fileProvider = FileProvider.getUriForFile(fragment.getContext(), "com.fileProvider"
                        , outFile);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                fragment.getActivity().startActivityForResult(intent, RequestCode.OPEN_CAMERA);
                this.dismiss();
            }break;
        }
    }
}
