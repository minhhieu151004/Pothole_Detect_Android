package com.example.prj_android_detectpothole;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.prj_android_detectpothole.OBJECT.MyMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;

public class MyIn4WindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;
    private RoundedImageView in4window_img;
    private TextView in4window_txt_level, in4window_txt_addr;
    private ConstraintLayout contrain;
    View view;

    public MyIn4WindowAdapter(Activity context){
        this.context = context;
    }


    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        view = context.getLayoutInflater().inflate(R.layout.layout_info_window, null);
        contrain = view.findViewById(R.id.in4window_constrain);
        in4window_img = view.findViewById(R.id.in4window_img);
        in4window_txt_addr = view.findViewById(R.id.in4window_txt_addr);
        in4window_txt_level = view.findViewById(R.id.in4window_txt_level);

        //Obj obj = marker.getTag();
        //obj lưu dữ liệu của 1 marker
        MyMarker mMarker = (MyMarker) marker.getTag();
        if(Objects.equals(mMarker.getLevel(), "HIGH")){
            contrain.setBackground(ContextCompat.getDrawable(context, R.drawable.bground_in4win_high));
            in4window_txt_level.setText("HIGH");
            in4window_txt_level.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        else if (Objects.equals(mMarker.getLevel(), "MEDIUM")) {
            contrain.setBackground(ContextCompat.getDrawable(context, R.drawable.bground_in4win_medium));
            in4window_txt_level.setText("MEDIUM");
            in4window_txt_level.setTextColor(ContextCompat.getColor(context, R.color.orange));
        }
        else if (Objects.equals(mMarker.getLevel(), "LOW")) {
            contrain.setBackground(ContextCompat.getDrawable(context, R.drawable.bground_in4win_low));
            in4window_txt_level.setText("LOW");
            in4window_txt_level.setTextColor(ContextCompat.getColor(context, R.color.green));
        }
        in4window_txt_addr.setText(mMarker.getAddr());
        return view;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }


}
