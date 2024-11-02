package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class DashboardFragment extends Fragment {
    PieChart pieChart;
    BarChart barChart;
    LineChart lineChart;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        pieChart = view.findViewById(R.id.pie_chart);
        barChart = view.findViewById(R.id.bar_chart);
        lineChart = view.findViewById(R.id.line_chart);

        ShowPieChar(pieChart, 80f, 60f, 25f);
        ShowBarChart(barChart);
        ShowLineChart(lineChart);

        return view;
    }

    private void ShowPieChar(PieChart pieChart, float low, float medium, float high) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(low, "Low"));
        entries.add(new PieEntry(medium, "Medium"));
        entries.add(new PieEntry(high, "High"));

        PieDataSet pieDataSet = new PieDataSet(entries, "Statistics on the number of potholes");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setDrawValues(false);

        PieData pieData = new PieData(pieDataSet);

        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setData(pieData);
    }

    private void ShowBarChart(BarChart barChart) {
        ArrayList<BarEntry> potholesEntries = new ArrayList<>();
        potholesEntries.add(new BarEntry(0, 3));   // Monday
        potholesEntries.add(new BarEntry(2, 5));   // Tuesday
        potholesEntries.add(new BarEntry(4, 2));   // Wednesday
        potholesEntries.add(new BarEntry(6, 4));   // Thursday
        potholesEntries.add(new BarEntry(8, 6));   // Friday
        potholesEntries.add(new BarEntry(10, 3));  // Saturday
        potholesEntries.add(new BarEntry(12, 1));  // Sunday

        BarDataSet barDataSet = new BarDataSet(potholesEntries, "Number of Potholes");
        barDataSet.setColor(Color.BLUE);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.setFitBars(true);

        final String[] daysOfWeek = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = Math.round(value / 2); // Điều chỉnh để phù hợp với khoảng cách
                if (index >= 0 && index < daysOfWeek.length) {
                    return daysOfWeek[index];
                }
                return "";
            }
        };

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        barChart.invalidate();
    }

    private void ShowLineChart(LineChart lineChart) {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 1));
        entries.add(new Entry(1, 3));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 5));
        entries.add(new Entry(4, 4));

        LineDataSet lineDataSet = new LineDataSet(entries, "Sample Data");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueTextColor(Color.BLACK); // Màu chữ hiển thị các giá trị điểm dữ liệu

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        // Tắt hoàn toàn trục X
        lineChart.getXAxis().setEnabled(false);

        // Tắt hoàn toàn trục Y
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);

        // Tắt mô tả của biểu đồ
        lineChart.getDescription().setEnabled(false);

        // Tắt chú giải nếu không cần thiết
        lineChart.getLegend().setEnabled(false);

        // Cập nhật lại biểu đồ
        lineChart.invalidate();
    }
}
