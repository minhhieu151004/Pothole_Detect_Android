package com.example.prj_android_detectpothole.OBJECT;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class MyMarker {
    @SerializedName("_id")
    String idMark;
    String level;
    double latitude, longitude;
    @SerializedName("street")
    String addr;
    String img;

    public MyMarker(String idMark, double latitude, double longitude, String addr, String level, String img) {
        this.idMark = idMark;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addr = addr;
        this.level = level;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getIdMark() {
        return idMark;
    }

    public void setIdMark(String idMark) {
        this.idMark = idMark;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
