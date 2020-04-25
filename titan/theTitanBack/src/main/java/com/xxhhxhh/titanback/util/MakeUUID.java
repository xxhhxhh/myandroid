package com.xxhhxhh.titanback.util;

import java.util.UUID;

public class MakeUUID
{
    public static String makeUUID(String canshu)
    {
        return canshu + UUID.randomUUID().toString().replaceAll("-", "");
    }
}
