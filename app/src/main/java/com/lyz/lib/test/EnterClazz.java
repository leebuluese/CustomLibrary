package com.lyz.lib.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by lyz on 2018/8/31.
 *
 */

public class EnterClazz {

    public static void main(String[] args) {


        RealSubject realSubject = new RealSubject();

        System.out.println("RealSubject : " + realSubject);

        InvocationHandler invocationHandler = new DynamicProxy(realSubject);

        Subject subject = (Subject) Proxy.newProxyInstance(realSubject.getClass().getClassLoader(), realSubject.getClass().getInterfaces(), invocationHandler);

        System.out.println("Subject : " + subject.getClass().getName());

        subject.rent();
        subject.print("Hello Proxy and InvocationHandler");
    }
}
