package com.example.wisa.hbt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<String> activity;

    DBHandler dbHandler;

    //TODO REMOVE THIS
    TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TEST
        dbHandler = new DBHandler(this, null, null, 1);
//        dbHandler.addActivity("Grieving");
//        dbHandler.addActivity("Crying");

        dbHandler.deleteActivity("Crying");

        Date now = new Date();
        now.setHours(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

//        dbHandler.addDateCheck(cal.getTime(), dbHandler.getActivityID("Grieving"));
//        dbHandler.addDateCheck(now, dbHandler.getActivityID("Crying"));

        testView = findViewById(R.id.testView);
        testView.setText(dbHandler.recordToString());
        //TEST

        listView = (ListView) findViewById(R.id.listView);
        activity = dbHandler.getActivities();

        listView.getLayoutParams().height = ListView.LayoutParams.WRAP_CONTENT;

        HabitAdapter adapter = new HabitAdapter(this, activity);
        listView.setAdapter(adapter);
    }
}
