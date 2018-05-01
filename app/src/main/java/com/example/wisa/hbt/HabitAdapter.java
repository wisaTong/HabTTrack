package com.example.wisa.hbt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HabitAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    String[] habits;

    public HabitAdapter(Context c, String[] habits) {
        this.habits = habits;
        this.mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return habits.length;
    }

    @Override
    public Object getItem(int position) {
        return habits[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflator.inflate(R.layout.tracker_detail, null);
        TextView name = v.findViewById(R.id.habitNameTextView);
        LinearLayout tracker = v.findViewById(R.id.trackerLinearH);
        tracker.addView(new Button(v.getContext()));
        name.setText(habits[position]);
        return v;
    }
}
