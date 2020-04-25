package com.xxhhxhh.mainthing.Factory;


import android.content.Context;
import android.widget.Button;
import android.widget.Switch;
import com.example.titan.R;

public class ButtonFactory
{
    public static final int ORDINARY_BUTTON = 50;
    public static final int SWITCH_BUTTON = 51;
    //传入按钮参数(类型，style，id)
    public static Object buttonFactory(Context context,int BUTTONTYPE, int settings, int id)
    {
        //生产按钮
        if(BUTTONTYPE == context.getResources().getInteger(R.integer.ORDINARY_BUTTON))
        {
            return ordinaryButton(context, settings, id);
        }
        else if(BUTTONTYPE == context.getResources().getInteger(R.integer.SWITCH_BUTTON))
        {
            return switchButton(context, settings, id);
        }
        else
        {
            return null;
        }
    }

    //普通按钮
    private static Button ordinaryButton(Context context,int settings, int id)
    {
        Button button = new Button(context);
        button.setTextAppearance(settings);
        button.setId(id);
        return button;
    }

    //状态开关
    private static Switch switchButton(Context context,int settings, int id)
    {
        Switch button = new Switch(context);
        button.setTextAppearance(settings);
        button.setId(id);
        return button;
    }

}
