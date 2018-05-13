package com.example.wisa.hbt;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HabitAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> activity;
    private DBHandler dbHandler;

    public HabitAdapter(Context c) {
        this.dbHandler = new DBHandler(c, null, null, 1);
        this.activity = dbHandler.getActivities();
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /** Update activities in database */
    public void updateData(){
        activity = dbHandler.getActivities();
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
        View v = mInflater.inflate(R.layout.tracker_detail, null);
        TextView nameText = v.findViewById(R.id.habitNameTextView);
        LinearLayout tracker = v.findViewById(R.id.trackerLinearH);
        final HorizontalScrollView hScroll = v.findViewById(R.id.hScroll);

        final String name = activity.get(position);
        nameText.setText(name);
        addRecord(name, tracker);

        createCheckBox(v, name);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), InDepthActivity.class);
                in.putExtra(MainActivity.EXTRA_MESSAGE, name);
                v.getContext().startActivity(in);
            }
        });
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        hScroll.post(new Runnable() {
            @Override
            public void run() {
                hScroll.fullScroll(View.FOCUS_RIGHT); // pull all the way to right
            }
        });
        return v;
    }

    /**
     * Create CheckBox and setOnClickListener to pop a snackBar
     * asking use if they are sure that they did the activity
     * then use DBHandler to add dateCheck to the database.
     * also notify listViews after activity is checked.
     * @param v is a Parent View containing this CheckBox
     * @param name is String name of activity associate with this CheckBox
     * @return CheckBox created
     */
    private CheckBox createCheckBox(View v, final String name){
        CheckBox check = v.findViewById(R.id.checkBox);
        if (dbHandler.isDoneToday(name)) {
            check.setChecked(true);
            check.setEnabled(false);
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox box = (CheckBox) v;
                Snackbar snack = Snackbar.make(v, "Are you sure?", 3000);
                snack.setAction("Sure", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        dbHandler.addDateCheck(cal.getTime(), dbHandler.getActivityID(name));
                        notifyDataSetChanged();
                    }
                });
                snack.addCallback(new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        box.setChecked(false);
                    }
                });
                if (box.isChecked()) snack.show();
            }
        });
        return check;
    }

    /**
     * Loop over Dates from earliest date recorded to current date
     * and check whether that date is in the record if it is then add
     * a specific rectangle accordingly.
     * @param activity String name of activity
     * @param tracker LinearLayout for convenience
     */
    private void addRecord(String activity, LinearLayout tracker) {
        List<Date> dateList = dbHandler.getDateDone(activity);

        Date earliest = dbHandler.getEarliestDate();
        Calendar start = Calendar.getInstance();
        start.setTime(earliest);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);

        SimpleDateFormat formatter = new SimpleDateFormat(DBHandler.DATE_FOMAT);
        while (start.before(today)){
            ImageView rect = new ImageView(tracker.getContext());
            rect.setImageResource(R.drawable.not_done_rectangle);

            if (dateList.size() <= 0) {
                tracker.addView(rect);
                start.add(Calendar.DATE, 1);
                continue;
            }

            String date1 = formatter.format(dateList.get(0));
            String date2 = formatter.format(start.getTime());

            if (date1.equals(date2)) {
                rect.setImageResource(R.drawable.colored_rectangle);
                dateList.remove(0);
            }

            tracker.addView(rect);
            start.add(Calendar.DATE, 1);
        }
    }
}
