package com.example.wisa.hbt;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    LinearLayout sumLinearV;
    List<String> activity;
    LayoutInflater inflater;

    DBHandler dbHandler;

    //TODO REMOVE THIS
    TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this, null, null, 1);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //TEST
        testView = findViewById(R.id.testView);
        testView.setText(dbHandler.recordToString());
        //TEST

        listView = (ListView) findViewById(R.id.listView);
        activity = dbHandler.getActivities();

        listView.getLayoutParams().height = ListView.LayoutParams.WRAP_CONTENT;
        HabitAdapter adapter = new HabitAdapter(this, activity);
        listView.setAdapter(adapter);

        sumLinearV = (LinearLayout) findViewById(R.id.summLinearV);
        for (int i = 0; i < activity.size(); i++) {
            String actvName = activity.get(i);
            View v = inflater.inflate(R.layout.summary_detail, null);
            TextView name = v.findViewById(R.id.nameTextView);
            name.setText("- " + actvName);
            TextView record = v.findViewById(R.id.recordTextView);
            String format = String.format("%d/%d", dbHandler.howManyDone(actvName), dbHandler.daysFromStart(actvName));
            record.setText(format);

            sumLinearV.addView(v);
        }
    }
}
