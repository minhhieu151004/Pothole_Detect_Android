package com.example.prj_android_detectpothole.language;

import com.example.prj_android_detectpothole.R;

import java.util.ArrayList;
import java.util.List;

public class data {
    public static List<language> getLanguageList() {
        List<language> languageList = new ArrayList<>();

        language VietNam = new language();
        VietNam.setCountry("Vietnamese");
        VietNam.setImage(R.drawable.vietnam_flag_ic);
        languageList.add(VietNam);

        language UnitedKingdom = new language();
        UnitedKingdom.setCountry("English");
        UnitedKingdom.setImage(R.drawable.uk_flag_ic);
        languageList.add(UnitedKingdom);

        return languageList;
    }
}
