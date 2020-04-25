package com.xxhhxhh.mainthing.Factory;

import java.util.UUID;

public class MakeUUID {

    public static String makeUUID(String canshu)
    {
        return canshu + UUID.randomUUID().toString();
    }
}
