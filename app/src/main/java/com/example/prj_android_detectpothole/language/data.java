package com.example.prj_android_detectpothole.language;

import com.example.prj_android_detectpothole.R;

import java.util.ArrayList;
import java.util.List;

public class data {
    public static List<language> getLanguageList() {
        List<language> languageList = new ArrayList<>();
        // English
        language UnitedKingdom = new language();
        UnitedKingdom.setCountry("English");
        UnitedKingdom.setLanguageCode("en"); // ISO 639-1 code for English
        UnitedKingdom.setImage(R.drawable.uk_flag_ic);
        languageList.add(UnitedKingdom);

        // Vietnamese
        language VietNam = new language();
        VietNam.setCountry("Vietnamese");
        VietNam.setLanguageCode("vi"); // ISO 639-1 code for Vietnamese
        VietNam.setImage(R.drawable.vietnam_flag_ic);
        languageList.add(VietNam);

        return languageList;
    }
}
