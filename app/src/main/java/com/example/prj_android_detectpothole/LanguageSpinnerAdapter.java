package com.example.prj_android_detectpothole;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.prj_android_detectpothole.language.language;

import java.util.List;

public class LanguageSpinnerAdapter extends BaseAdapter {
    private Fragment fragment;
    private List<language> languageList;

    public LanguageSpinnerAdapter(Fragment fragment, List<language> languageList) {
        this.fragment = fragment;
        this.languageList = languageList;
    }

    @Override
    public int getCount() {
        return languageList != null ? languageList.size():0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(fragment.getContext())
                .inflate(R.layout.language_spinner_item,viewGroup,false);

        TextView txtCountry = rootView.findViewById(R.id.country_textView);
        ImageView imageView = rootView.findViewById(R.id.image_imgView);

        txtCountry.setText(languageList.get(i).getCountry());
        imageView.setImageResource(languageList.get(i).getImage());

        return rootView;
    }
}
