package com.example.prj_android_detectpothole;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.prj_android_detectpothole.API.API_Pothole;
import com.example.prj_android_detectpothole.OBJECT.MyMarker;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    TextView pickedDateTextView;
    ImageButton pickDateButton;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch modeViewSwitch;
    PieChart pieChart;
    TextView username, email, totalPotholes, lowPotholes, mediumPotholes, highPotholes;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        username = view.findViewById(R.id.username_txt);
        email = view.findViewById(R.id.email_txt);
        totalPotholes = view.findViewById(R.id.total_potholes_txt);
        lowPotholes = view.findViewById(R.id.low_potholes_txt);
        mediumPotholes = view.findViewById(R.id.medium_potholes_txt);
        highPotholes = view.findViewById(R.id.high_potholes_txt);
        pieChart = view.findViewById(R.id.pieChart);
        modeViewSwitch = view.findViewById(R.id.switch_mode_view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean isChecked = sharedPreferences.getBoolean("mode_view_switch", false);

        modeViewSwitch.setChecked(isChecked);

        modeViewSwitch.setOnCheckedChangeListener((buttonView, isCheckedSwitch) -> {
            sharedPreferences.edit().putBoolean("mode_view_switch", isCheckedSwitch).apply();

            if (isCheckedSwitch) {
                getAllPotholes();
            } else {
                getMyPotholes();
            }
        });

        setTitleDashboard();

        if (isChecked) {
            getAllPotholes();
        } else {
            getMyPotholes();
        }
        return view;
    }

    //Function
    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                    pickedDateTextView.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    public void setTitleDashboard() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String usernameValue = sharedPreferences.getString("username", "");
        String emailValue = sharedPreferences.getString("email", "");
        username.setText(usernameValue);
        email.setText(emailValue);
    }

    public void getMyPotholes() {
        List<MyMarker> myMarkerList = new ArrayList<>();
        List<MyMarker> lowList = new ArrayList<>();
        List<MyMarker> mediumList = new ArrayList<>();
        List<MyMarker> highList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String usernameValue = sharedPreferences.getString("username", "");
        API_Pothole.api_pothole.get_all_pothole().enqueue(new Callback<List<MyMarker>>() {
            @Override
            public void onResponse(Call<List<MyMarker>> call, Response<List<MyMarker>> response) {
                List<MyMarker> list = response.body();
                if(list!=null && !list.isEmpty()){
                    for (MyMarker myMarker : list) {
                        try {
                            if (myMarker.getContributor().equals(usernameValue)) {
                                switch (myMarker.getLevel()) {
                                    case "LOW":
                                        lowList.add(myMarker);
                                        break;
                                    case "MEDIUM":
                                        mediumList.add(myMarker);
                                        break;
                                    case "HIGH":
                                        highList.add(myMarker);
                                        break;
                                    default:
                                        Log.e(TAG, "Get pothole error: " + myMarker.getLevel());
                                        break;
                                }
                                myMarkerList.add(myMarker);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Get pothole error: " + e.getMessage());
                        }
                    }
                    lowPotholes.setText(String.valueOf(lowList.size()));
                    mediumPotholes.setText(String.valueOf(mediumList.size()));
                    highPotholes.setText(String.valueOf(highList.size()));
                    totalPotholes.setText(String.valueOf(myMarkerList.size()));
                    CreatePieChart(lowList, mediumList, highList);

                }
            }

            @Override
            public void onFailure(Call<List<MyMarker>> call, Throwable throwable) {
                Log.e(TAG,"Fail to get Potholes: " + throwable.getMessage());
            }
        });
    }

    public void getAllPotholes(){
        List<MyMarker> myMarkerList = new ArrayList<>();
        List<MyMarker> lowList = new ArrayList<>();
        List<MyMarker> mediumList = new ArrayList<>();
        List<MyMarker> highList = new ArrayList<>();
        API_Pothole.api_pothole.get_all_pothole().enqueue(new Callback<List<MyMarker>>() {
            @Override
            public void onResponse(Call<List<MyMarker>> call, Response<List<MyMarker>> response) {
                List<MyMarker> list = response.body();
                if(list!=null && !list.isEmpty()){
                    for (MyMarker myMarker : list) {
                        try {
                            switch (myMarker.getLevel()) {
                                case "LOW":
                                    lowList.add(myMarker);
                                    break;
                                case "MEDIUM":
                                    mediumList.add(myMarker);
                                    break;
                                case "HIGH":
                                    highList.add(myMarker);
                                    break;
                                default:
                                    Log.e(TAG, "Get pothole error: " + myMarker.getLevel());
                                    break;
                            }
                            myMarkerList.add(myMarker);
                        } catch (Exception e) {
                            Log.e(TAG, "Get pothole error: " + e.getMessage());
                        }
                    }
                    lowPotholes.setText(String.valueOf(lowList.size()));
                    mediumPotholes.setText(String.valueOf(mediumList.size()));
                    highPotholes.setText(String.valueOf(highList.size()));
                    totalPotholes.setText(String.valueOf(myMarkerList.size()));
                    CreatePieChart(lowList, mediumList, highList);

                }
            }

            @Override
            public void onFailure(Call<List<MyMarker>> call, Throwable throwable) {
                Log.e(TAG,"Fail to get Potholes: " + throwable.getMessage());
            }
        });
    }

    public void CreatePieChart(List<MyMarker> lowList, List<MyMarker> mediumList, List<MyMarker> highList) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Dữ liệu mẫu
        entries.add(new PieEntry(lowList.size(), "LOW"));
        entries.add(new PieEntry(mediumList.size(), "MEDIUM"));
        entries.add(new PieEntry(highList.size(), "HIGH"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(20f);

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.animateY(1000);
        Legend legend = pieChart.getLegend();
        legend.setTextSize(16f);
        Typeface boldTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        pieChart.getLegend().setTypeface(boldTypeface);
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setXEntrySpace(50f);
        legend.setYOffset(10f);
        legend.setDrawInside(false);

        pieChart.invalidate();
    }


}
