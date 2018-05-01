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

public class MainActivity extends AppCompatActivity {

    ListView listView;
    LinearLayout linearInTable;
    TableLayout tableLayout;
    String[] habits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        listView = (ListView) findViewById(R.id.listView);
        linearInTable = (LinearLayout) findViewById(R.id.linearInTable);
        habits = getResources().getStringArray(R.array.habits);

        listView.getLayoutParams().height = ListView.LayoutParams.WRAP_CONTENT;
        tableLayout.getLayoutParams().height = TableLayout.LayoutParams.WRAP_CONTENT;

        for (String name : habits) {
            TextView tv = new TextView(this);
            tv.setText(name);
            linearInTable.addView(tv);
        }

        HabitAdapter adapter = new HabitAdapter(this, habits);
        listView.setAdapter(adapter);


    }
}
