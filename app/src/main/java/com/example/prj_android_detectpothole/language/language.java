package com.example.prj_android_detectpothole.language;

import java.io.Serializable;

public class language implements Serializable {
    private String country;
    private String languageCode; // Mã ngôn ngữ
    private int image;

    // Constructor mặc định
    public language() {
    }

    // Constructor đầy đủ
    public language(String country, String languageCode, int image) {
        this.country = country;
        this.languageCode = languageCode;
        this.image = image;
    }

    // Getter và Setter cho country
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Getter và Setter cho languageCode
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    // Getter và Setter cho image
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
