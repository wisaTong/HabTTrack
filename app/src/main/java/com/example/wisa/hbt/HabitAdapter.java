package com.example.wisa.hbt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HabitAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    List<String> activity;
    DBHandler dbHandler;

    public HabitAdapter(Context c, List<String> activity) {
        this.activity = activity;
        this.mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHandler = new DBHandler(c, null, null, 1);
    }

    @Override
    public int getCount() {
        return activity.size();
    }

    @Override
    public Object getItem(int position) {
        return activity.get(position);
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
        name.setText(activity.get(position));
        checkDone(activity.get(position), tracker);
        return v;
    }

    private void checkDone(String activity, LinearLayout tracker) {
        List<Date> dateList = dbHandler.getDateDone(activity);

        Calendar start = Calendar.getInstance();
        start.setTime(dateList.get(0));
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);

        SimpleDateFormat formatter = new SimpleDateFormat(DBHandler.DATE_FOMAT);
        while (start.before(today)){

            String date1 = formatter.format(dateList.get(0));
            String date2 = formatter.format(start.getTime());
            ImageView rect = new ImageView(tracker.getContext());
            if (date1.equals(date2))
                rect.setImageResource(R.drawable.colored_rectangle);
            else
                rect.setImageResource(R.drawable.empty_rectangle);

            tracker.addView(rect);
            start.add(Calendar.DATE, 1);
        }
    }
}
