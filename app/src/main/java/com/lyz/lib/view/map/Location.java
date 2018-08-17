package com.lyz.lib.view.map;

/**
 * Created by lyz on 2018/8/17.
 *
 */

public class Location {


    /**
     * deviceId : 77422
     * deviceNo : -1
     * height : 51
     * pvBarcode : 062-2
     * pvstringId : 62
     * solarId : 1238
     * solarX : 5817.5
     * solarY : 459.5
     * state : 0
     * width : 30
     */

    private int deviceId;
    private int deviceNo;
    private int height;
    private String pvBarcode;
    private int pvstringId;
    private int solarId;
    private double solarX;
    private double solarY;
    private int state;
    private int width;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(int deviceNo) {
        this.deviceNo = deviceNo;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPvBarcode() {
        return pvBarcode;
    }

    public void setPvBarcode(String pvBarcode) {
        this.pvBarcode = pvBarcode;
    }

    public int getPvstringId() {
        return pvstringId;
    }

    public void setPvstringId(int pvstringId) {
        this.pvstringId = pvstringId;
    }

    public int getSolarId() {
        return solarId;
    }

    public void setSolarId(int solarId) {
        this.solarId = solarId;
    }

    public double getSolarX() {
        return solarX;
    }

    public void setSolarX(double solarX) {
        this.solarX = solarX;
    }

    public double getSolarY() {
        return solarY;
    }

    public void setSolarY(double solarY) {
        this.solarY = solarY;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
