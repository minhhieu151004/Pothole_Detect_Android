package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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

public class DashboardFragment extends Fragment {
    TextView pickedDateTextView;
    ImageButton pickDateButton;
    TextView username, email;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        username = view.findViewById(R.id.username_txt);
        email = view.findViewById(R.id.email_txt);
        setTitleDashboard();
        //Example View Page
        List<Object> slideData = new ArrayList<>();
        slideData.add(new PieData(new PieDataSet(new ArrayList<>(Arrays.asList(
                new PieEntry(30, "Low"),
                new PieEntry(40, "Medium"),
                new PieEntry(30, "High")
        )), "Statistics on the number of potholes")));

        ArrayList<BarEntry> potholesEntries = new ArrayList<>(Arrays.asList(
                new BarEntry(0, 3), new BarEntry(2, 5), new BarEntry(4, 2),
                new BarEntry(6, 4), new BarEntry(8, 6), new BarEntry(10, 3), new BarEntry(12, 1)
        ));
        slideData.add(new BarData(new BarDataSet(potholesEntries, "Number of Potholes")));

        ArrayList<Entry> lineEntries = new ArrayList<>(Arrays.asList(
                new Entry(0, 1), new Entry(1, 3), new Entry(2, 2), new Entry(3, 5), new Entry(4, 4)
        ));
        slideData.add(new LineData(new LineDataSet(lineEntries, "Sample Data")));
        ViewPager2 viewPager2 = view.findViewById(R.id.viewPageChart);
        SlideAdapter adapter = new SlideAdapter(slideData);
        viewPager2.setAdapter(adapter);
        //
        //Example Pick Date
        pickedDateTextView = view.findViewById(R.id.show_picked_date);
        pickDateButton = view.findViewById(R.id.pickDate_btn);
        pickDateButton.setOnClickListener(v -> openDatePicker());
        //
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
}
