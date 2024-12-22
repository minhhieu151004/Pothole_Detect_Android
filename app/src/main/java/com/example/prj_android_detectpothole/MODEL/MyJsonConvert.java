package com.example.prj_android_detectpothole.MODEL;

import com.example.prj_android_detectpothole.OBJECT.MyMarker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MyJsonConvert {
    private static final Gson gson = new Gson();

    public static String listToJson(List<MyMarker> list) {
        return gson.toJson(list);
    }

    // Chuyển chuỗi JSON thành List
    public static List<MyMarker> jsonToList(String json) {
        Type type = new TypeToken<List<MyMarker>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
