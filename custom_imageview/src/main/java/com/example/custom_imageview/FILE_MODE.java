package com.example.custom_imageview;

//设置模式
public enum FILE_MODE {

    DEFAULT,//默认，提取layout文件background的内容
    URI,//uri设置。-》uri.toFile(),BitmapFactory.decodeFile()
    PATH//文件路径BitmapFactory.decodeFile()

}
