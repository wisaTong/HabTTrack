package com.example.wisa.hbt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InDepthActivity extends AppCompatActivity {

    //VIEWS
    BarChart barChart;
    TextView nameTextView;

    //OTHER
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_depth);

        //INITIALIZE
        dbHandler = new DBHandler(this, null, null, 1);
        barChart = (BarChart) findViewById(R.id.barChart);
        nameTextView = (TextView) findViewById(R.id.nameTextView);

        Intent in = getIntent();
        String name = in.getStringExtra(MainActivity.EXTRA_MESSAGE);
        nameTextView.setText(name);

        BarData barData = new BarData();
        for (BarDataSet dtst : setBars(name, this)) {
            barData.addDataSet(dtst);
        }
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getXAxis().setEnabled(false);
        barChart.setData(barData);
        barChart.animateY(1500);
        barChart.invalidate();
    }

    private List<BarDataSet> setBars(String name, Context c){
        List<Date> dateList = dbHandler.getDateDone(name);
        int daysDone = dateList.size();
        int[] colors = {
                R.color.color_MON, R.color.color_TUE, R.color.color_WED,
                R.color.color_THU, R.color.color_FRI, R.color.color_SAT, R.color.color_SUN
        };

        List<BarDataSet> dataSets = new ArrayList<>();

        for (int i = 1; i <= 7; i++){
            int count = 0;
            for (Date date : dateList){
                DateTime dt = new DateTime(date);
                if (dt.getDayOfWeek() == i) count++;
            }
            float percent = (float) (count * 100.0 / daysDone);

            LocalDate date = new LocalDate();
            date = date.withDayOfWeek(i);
            String dayName = DateTimeFormat.forPattern("EEE").print(date);

            BarEntry entry = new BarEntry(i, percent);
            List<BarEntry> entries = new ArrayList<>();
            entries.add(entry);
            BarDataSet dataSet = new BarDataSet(entries, dayName);
            dataSet.setColors(new int[] {colors[i-1]}, c);
            dataSets.add(dataSet);
        }
        return dataSets;
    }
}
