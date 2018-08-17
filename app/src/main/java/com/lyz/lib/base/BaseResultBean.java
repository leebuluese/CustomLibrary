package com.lyz.lib.base;

import java.io.Serializable;

/**
 * Created by 80002796 on 2018/8/17.
 * Created by lyz on 2018/8/17.
 */

public class BaseResultBean implements Serializable {

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
