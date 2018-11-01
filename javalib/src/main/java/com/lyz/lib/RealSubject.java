package com.lyz.lib;

/**
 * Created by lyz on 2018/8/31.
 *
 */

public class RealSubject implements Subject {
    @Override
    public void rent() {
        System.out.println("RealSubject I want rent my house");
    }

    @Override
    public void print(String s) {
        System.out.println("RealSubject I want to print my house");
    }
}
