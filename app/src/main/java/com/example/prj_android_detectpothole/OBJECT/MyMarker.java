package com.example.prj_android_detectpothole.OBJECT;

import android.graphics.Bitmap;

public class MyMarker {
    int idMark;
    String level;
    double latitude, longitude;
    String addr;

    public MyMarker(int idMark, double latitude, double longitude, String addr, String level) {
        this.idMark = idMark;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addr = addr;
        this.level = level;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getIdMark() {
        return idMark;
    }

    public void setIdMark(int idMark) {
        this.idMark = idMark;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
