package com.example.prj_android_detectpothole;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {
    private final List<Object> slideData; // Danh sách dữ liệu cho các biểu đồ

    public SlideAdapter(List<Object> slideData) {
        this.slideData = slideData;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        Object data = slideData.get(position);
        if (data instanceof PieData) {
            holder.showPieChart((PieData) data);
        } else if (data instanceof LineData) {
            holder.showLineChart((LineData) data);
        } else if (data instanceof BarData) {
            holder.showBarChart((BarData) data);
        }
    }

    @Override
    public int getItemCount() {
        return slideData.size();
    }

    static class SlideViewHolder extends RecyclerView.ViewHolder {
        private final PieChart pieChart;
        private final LineChart lineChart;
        private final BarChart barChart;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            pieChart = itemView.findViewById(R.id.pieChart);
            lineChart = itemView.findViewById(R.id.lineChart);
            barChart = itemView.findViewById(R.id.barChart);
        }

        public void showPieChart(PieData data) {
            pieChart.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
            barChart.setVisibility(View.GONE);
            pieChart.setData(data);
            pieChart.invalidate();
        }

        public void showLineChart(LineData data) {
            lineChart.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
            barChart.setVisibility(View.GONE);
            lineChart.setData(data);
            lineChart.invalidate();
        }

        public void showBarChart(BarData data) {
            barChart.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.GONE);
            barChart.setData(data);
            barChart.invalidate();
        }
    }
}
