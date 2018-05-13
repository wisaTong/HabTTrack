package com.example.wisa.hbt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SumAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> activities;
    private DBHandler dbHandler;

    public SumAdapter(Context c) {
        this.dbHandler = new DBHandler(c, null, null, 1);
        this.activities = dbHandler.getActivities();
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(){
        this.activities = dbHandler.getActivities();
    }

    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public Object getItem(int position) {
        return activities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String actv = activities.get(position);
        View v = mInflater.inflate(R.layout.summary_detail, null);
        TextView name = v.findViewById(R.id.nameTextView);
        TextView record = v.findViewById(R.id.recordTextView);

        name.setText("- " + actv);
        String format = String.format("%2d/%2d days", dbHandler.howManyDone(actv), dbHandler.daysFromStart(actv));
        record.setText(format);
        return v;
    }
}
