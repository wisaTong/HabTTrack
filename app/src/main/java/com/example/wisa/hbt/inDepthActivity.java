package com.example.wisa.hbt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import java.util.ArrayList;
import java.util.List;

public class inDepthActivity extends AppCompatActivity {

    //VIEWS
    BarGraph barGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_depth);


    }

    private void setBars(){
        List<Bar> percent = new ArrayList<>();

    }
}
