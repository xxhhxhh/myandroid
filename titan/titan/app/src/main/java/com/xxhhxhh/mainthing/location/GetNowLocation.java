package com.xxhhxhh.mainthing.location;

import android.location.LocationManager;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class GetNowLocation
    implements AMapLocationListener
{
    public String nowLocation;


    public GetNowLocation(LocationManager manager, AMapLocationClient client)
    {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setInterval(3000);
        option.setHttpTimeOut(20000);
        //gps打开并且可网络定位
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
           option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        }
        //gps打开
        else if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        }
        //网络模式打开
        else if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        }
        client.setLocationOption(option);
        client.setLocationListener(this);
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation)
    {
        if (aMapLocation.getErrorCode() == 0)
        {
            setNowLocation(aMapLocation.getCity());
        }
    }

    //设置位置
    public  void setNowLocation(String nowLocation)
    {
        this.nowLocation = nowLocation;
    }

    //获取位置,城市
    public String getNowLocation()
    {
        return  nowLocation;
    }


}
