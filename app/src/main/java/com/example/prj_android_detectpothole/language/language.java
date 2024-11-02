package com.example.prj_android_detectpothole.language;

import java.io.Serializable;

public class language implements Serializable {
    private String country;
    private int image;

    public language() {

    }

    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}
    public int getImage() {return image;}
    public void setImage(int image) {this.image = image;}
}
