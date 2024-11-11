package com.example.prj_android_detectpothole.MODEL;

import java.util.List;

public class MyRouting {

    public List<Features> features;


    public class Features{
        public Geometry geometry;
        public Properties properties;
    }
    public class Geometry{
        public List<List<List<Double>>> coordinates;
    }
    public class Properties{
        public double distance;
        public double time;
    }
}
