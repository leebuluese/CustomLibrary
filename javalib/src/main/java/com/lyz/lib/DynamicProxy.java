package com.lyz.lib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by lyz on 2018/8/31.
 *
 */

public class DynamicProxy implements InvocationHandler{

    private Object subject;

    public DynamicProxy(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("Before rent my house");

        System.out.println("Method : " + method);
        System.out.println("Method name : " + method.getName());

        method.invoke(subject, args);

        System.out.println("After rent my house");
        return null;
    }
}
