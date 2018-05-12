package com.example.wisa.hbt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InDepthActivity extends AppCompatActivity {

    //VIEWS
    BarChart barChart;

    //OTHER
    DBHandler dbHandler;
    String[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_depth);

        //INITIALIZE
        dbHandler = new DBHandler(this, null, null, 1);
        barChart = (BarChart) findViewById(R.id.barChart);
        colors = getResources().getStringArray(R.array.Color);

        Intent in = getIntent();
        String name = in.getStringExtra(MainActivity.EXTRA_MESSAGE);

        BarDataSet dataSet = new BarDataSet(setBars(name), "day");
        BarData barData = new BarData(dataSet);
        dataSet.setColors(new int[] {
                R.color.color_MON, R.color.color_TUE, R.color.color_WED,
                R.color.color_THU, R.color.color_FRI, R.color.color_SAT, R.color.color_SUN
        }, this);
        barChart.setData(barData);
        barChart.invalidate();
    }

    private List<BarEntry> setBars(String name){
        List<Date> dateList = dbHandler.getDateDone(name);
        int daysDone = dateList.size();

        List<BarEntry> entries = new ArrayList<>();

        for (int i = 1; i <= 7; i++){
            int count = 0;
            for (Date date : dateList){
                DateTime dt = new DateTime(date);
                if (dt.getDayOfWeek() == i) count++;
            }
            float percent = (float) (count * 100.0 / daysDone);
            entries.add(new BarEntry(i, percent));
        }
        return entries;
    }
}
