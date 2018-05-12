package com.example.wisa.hbt;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //VIEWS
    ListView listView;
    ListView sumListView;
    LinearLayout sumLinearV;
    FloatingActionButton addButton;

    //OTHER
    LayoutInflater inflater;
    HabitAdapter habitAdapter;
    SumAdapter sumAdapter;

    DBHandler dbHandler;

    //TODO REMOVE THIS
    TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INITIALIZATION
        dbHandler = new DBHandler(this, null, null, 1);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listView = (ListView) findViewById(R.id.listView);
        sumListView = (ListView) findViewById(R.id.sumListView);
        addButton = (FloatingActionButton) findViewById(R.id.floatingAddButton);

        //TEST
        dbHandler.deleteActivity("testing");
        testView = findViewById(R.id.testView);
        testView.setText(dbHandler.recordToString());
        //TEST

        listView.getLayoutParams().height = ListView.LayoutParams.WRAP_CONTENT;
        habitAdapter = new HabitAdapter(this);
        listView.setAdapter(habitAdapter);

        sumListView.getLayoutParams().height= ListView.LayoutParams.WRAP_CONTENT;
        sumAdapter = new SumAdapter(this);
        sumListView.setAdapter(sumAdapter);

    }

    public void addButtonClicked(View view) {
        final Dialog mDialog = new Dialog(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_add_activity, null);
        mDialog.setContentView(v);

        final EditText nameET = v.findViewById(R.id.nameEditText);
        Button add = v.findViewById(R.id.addButton);
        Button cancel = v.findViewById(R.id.cancelButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString().trim();
                dbHandler.addActivity(name);
                habitAdapter.updateData();
                habitAdapter.notifyDataSetChanged();
                mDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }
}
