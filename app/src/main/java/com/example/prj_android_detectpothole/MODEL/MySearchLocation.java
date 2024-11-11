package com.example.prj_android_detectpothole.MODEL;

import java.util.List;

public class MySearchLocation {

    public List<Features> features;

    public class Features{
        public Properties properties;
    }

    public class Properties{
        public double lat;
        public double lon;
        public String formatted;

        public Properties(double lat, double lon, String formatted) {
            this.lat = lat;
            this.lon = lon;
            this.formatted = formatted;
        }
    }
}
