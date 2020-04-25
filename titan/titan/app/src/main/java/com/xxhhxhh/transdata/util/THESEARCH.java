package com.xxhhxhh.transdata.util;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class THESEARCH
{
    /**此类用于字符串内容非法监测
     * @param  name /*要查询字符串
     * @param nowFind /*已查询到非法内容个数， 大于5个内容将无法发送,并提升用户恶意等级
     * @param mode /*0找到即失败， 1低于5个成功，否则失败，失败返回字符串 "no"
     * */

    private String name;
    public void setName(String name) { this.name = name; }
    private int mode;
    public void setMode(int mode) { this.mode = mode; }
    private int nowFind = 0;//找到非法内容个数


    public char[] first =
    {
        '我', '艹', '日'
    };

    public char[] second =
            {
              '你', '操', '艹', '草', '日'
            };

    public char[] third =
            {
              '妈', '马', '骂'
            };

    //开始
    public String toStart()
    {
        return firstSearch(name, 0, mode);
    }

    public String firstSearch(String name, int start, int mode)
    {
        if(name != null)
        {
            char[] name1 = name.toCharArray();
            //第一次查找
            for(int i = start; i < name1.length; i++)
            {

                //信息检索
                for(char a : first)
                {
                    //存在一级敏感字符，到达二级
                    if(name1[i] == a && name1[i] == '我')
                    {
                        //长度是否符合要求
                        if(name1.length > i + 1)
                        {
                            return secondSearch(name.substring(i), i + 1, mode);
                        }
                       else
                        {
                            return Arrays.toString(name1);
                        }
                    }
                    //一级强制敏感字符
                    else if(name1[i] == a)
                    {
                        //模式0，关键字存在，内容非法取消
                        if(mode == 0)
                        {
                            return "no";
                        }
                        //模式1，关键字屏蔽
                        else if(mode == 1)
                        {
                            name1[i] = '*';
                            //判断是否需要结束
                            if(isDead(nowFind++))
                            {
                                //强制结束
                                return "no";
                            }
                            else
                            {
                                //长度是否符合要求
                                if(name1.length > i + 1)
                                {
                                    return firstSearch(Arrays.toString(name1), i + 1, mode);
                                }
                                else
                                {
                                    return  Arrays.toString(name1);
                                }
                            }
                        }
                    }
                }
            }
            //检测合格
            return name;
        }
        else
        {
            return null;
        }
    }

    //二级匹配
    public String secondSearch(String name, int start, int mode) {
        char[] name1 = name.toCharArray();
        for (char a : second)
        {
            //匹配二级非强制敏感
            if (name1[start] == a && a == '你')
            {
                return thirdSearch(Arrays.toString(name1), start + 1, mode);
            }
            //二级强制敏感词
            else if (name1[start] == a)
            {
                if(mode == 1)
                {
                    //关键字屏蔽
                    name1[start - 1] = '*';
                    name1[start] = '*';
                    if (isDead(nowFind++))
                    {
                        return "no";
                    }
                    else
                    {
                        //长度是否符合要求
                        if (name1.length > start + 1)
                        {
                            return firstSearch(Arrays.toString(name1), start + 1, mode);
                        }
                        else
                        {
                            return Arrays.toString(name1);
                        }
                    }
                }
                //强制取消
                else if(mode == 0)
                {
                    return "no";
                }
            }
        }
        //长度是否符合要求
        if (name1.length > start + 1)
        {
            //一级继续匹配
            return firstSearch(Arrays.toString(name1), start + 1, mode);
        }
        else
        {
            return Arrays.toString(name1);
        }
    }

    //三级检测
    public String thirdSearch(String name, int start, int mode)
    {
        char[] name1 = name.toCharArray();
        for(char a : third)
        {

            //三级强制敏感词
            if(name1[start] == a)
            {
                //非强制结束
                if(mode == 1)
                {
                    //关键字屏蔽
                    name1[start - 2] = '*';
                    name1[start - 1] = '*';
                    name1[start] = '*';
                    if(isDead(nowFind++))
                    {
                        return "no";
                    }
                    else
                    {
                        //长度是否符合要求
                        if (name1.length > start + 1)
                        {
                            return firstSearch(Arrays.toString(name1), start + 1, mode);
                        }
                        else
                        {
                            return Arrays.toString(name1);
                        }
                    }
                }
                else if(mode == 0)
                {
                    return "no";
                }
            }
        }
        //长度是否符合要求
        if (name1.length > start + 1)
        {
            //一级继续匹配
            return firstSearch(Arrays.toString(name1), start + 1, mode);
        }
        else
        {
            return Arrays.toString(name1);
        }
    }

    //违法状态判断
    public boolean isDead(int nowFind)
    {
        return nowFind >= 5;
    }
}
