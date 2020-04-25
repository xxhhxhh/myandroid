package com.xxhhxhh.titanback.util;

import org.hibernate.SessionFactory;

public class GetSessionFactory
{
    public static SessionFactory sessionFactory;
    public  final void setSessionFactory(SessionFactory sessionFactory) { GetSessionFactory.sessionFactory = sessionFactory; }
}
