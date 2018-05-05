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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<String> activity;

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TEST
        dbHandler = new DBHandler(this, null, null, 1);
        dbHandler.addActivity("Grieving");
        dbHandler.addActivity("Crying");
        dbHandler.deleteActivity("Grieving");
        //TEST

        listView = (ListView) findViewById(R.id.listView);
        activity = dbHandler.getActivities();

        listView.getLayoutParams().height = ListView.LayoutParams.WRAP_CONTENT;

        HabitAdapter adapter = new HabitAdapter(this, activity);
        listView.setAdapter(adapter);


    }
}
