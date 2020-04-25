package com.xxhhxhh.mainthing.show;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.titan.R;

public class ErrorFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        LinearLayout main = (LinearLayout) inflater.inflate(R.layout.error, container, false);

        main.findViewById(R.id.goBack).setOnClickListener((view)->
        {
            getActivity().finish();
        });

        return main;
    }
}
