package sk.android.training.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimerTask;

import sk.android.training.R;
import sk.android.training.database.TableDbAdapter;
import sk.android.training.utils.TimeDateFormatter;

public class FragmentWeekView extends Fragment {

    private View layout;
    private Bundle extras;
    private Activity context;

    private Calendar today;

    private int _day;
    private int _month;
    private int _year;

    protected int current_week;

    private TextView date_title_first;
    private TextView date_title_last;

    private LinearLayout days_holder;

    private static final int RUNNING_ID = 1;
    private static final int CYCLING_ID = 2;
    private static final int XCOUNTRY_ID = 3;
    private static final int SWIMMING_ID = 4;
    private static final int SKATE_ID = 5;
    private static final int WALKING_ID = 6;
    private static final int CANOE_ID = 7;
    private static final int FOOTBALL_ID = 8;
    private static final int HOCKEY_ID = 9;
    private static final int VOLLEY_ID = 10;
    private static final int BASKET_ID = 11;
    private static final int TENNIS_ID = 12;
    private static final int SKIING_ID = 13;
    private static final int ATHLETICS_ID = 14;
    private static final int WEIGHT_ID = 15;
    private static final int OTHER_ID = 16;

    private ImageButton nextWeek;
    private ImageButton previousWeek;

    private TimeDateFormatter format;
    private TableDbAdapter db;

    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        context = getActivity();

        setHasOptionsMenu(true);

        DisplayMetrics metrics = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (metrics.heightPixels < 500 && metrics.widthPixels < 400) {
            layout = inflater.inflate(R.layout.week_view_layout_low, container, false);
        } else {
            layout = inflater.inflate(R.layout.week_view_layout, container, false);
        }

        extras = context.getIntent().getExtras();

        db = new TableDbAdapter(context);
        db.open();

        days_holder = (LinearLayout) layout.findViewById(R.id.days_holder);

        date_title_first = (TextView) layout.findViewById(R.id.date_title_first);
        date_title_last = (TextView) layout.findViewById(R.id.date_title_last);

        nextWeek = (ImageButton) layout.findViewById(R.id.nextDay);
        nextWeek.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                nextWeek(today);
            }
        });

        previousWeek = (ImageButton) layout.findViewById(R.id.previousDay);
        previousWeek.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                previousWeek(today);
            }
        });

        format = new TimeDateFormatter();

        setDate();

        return layout;
    }

    private void setDate() {

        _day = Integer.parseInt(extras.getString("day"));
        _month = extras.getInt("month");
        _year = Integer.parseInt(extras.getString("year"));

        current_week = extras.getInt("week");
        if (current_week == 0) {
            current_week = getWeek(extras.getInt("position"));
        }

        today = Calendar.getInstance();
        today.set(Calendar.WEEK_OF_MONTH, current_week);

        today.set(_year, _month, _day);

        fillDays(today);
        setTotals(today);

    }

    private void fillDays(Calendar week) {

        week.setFirstDayOfWeek(2);

        week.set(Calendar.DAY_OF_WEEK, 2);
        date_title_first.setText(format.formatDate(week.getTime()));

        week.set(Calendar.DAY_OF_WEEK, 1);
        date_title_last.setText(format.formatDate(week.getTime()));

        today = week;

        days_holder.removeAllViews();

        for (int i = 2; i <= 7; i++) {
            week.set(Calendar.DAY_OF_WEEK, i);
            days_holder.addView(new DayLayout(context, week, i));
        }

        week.set(Calendar.DAY_OF_WEEK, 1);
        days_holder.addView(new DayLayout(context, week, 1));

    }

    private int getWeek(int position) {

        System.out.println("POSITION " + position);

        if (position <= 6) {
            return 1;
        } else if (6 < position && position <= 13) {
            return 2;
        } else if (13 < position && position <= 20) {
            return 3;
        } else if (20 < position && position <= 27) {
            return 4;
        } else if (27 < position && position <= 34) {
            return 5;
        } else if (34 < position && position <= 41) {
            return 6;
        }

        return 0;
    }

    private void nextWeek(Calendar current) {

        current_week = current.get(Calendar.WEEK_OF_MONTH) + 1;
        current.set(Calendar.WEEK_OF_MONTH, current_week);

        fillDays(current);
        setTotals(current);
    }

    private void previousWeek(Calendar current) {

        current_week = current.get(Calendar.WEEK_OF_MONTH) - 1;
        current.set(Calendar.WEEK_OF_MONTH, current_week);

        fillDays(current);
        setTotals(current);
    }

    private void setTotals(Calendar week) {

        week.set(Calendar.DAY_OF_WEEK, 2);
        int start = format.formatDateToQuery(week.getTime());
        week.set(Calendar.DAY_OF_WEEK, 1);
        int end = format.formatDateToQuery(week.getTime());

        System.out.println(start + "   " + end);

        Cursor month_totals = db.getActivityWeekTotals(start, end);
        if (month_totals != null && month_totals.getCount() > 0) {
            if (month_totals.moveToFirst()) {
                // loop until it reach the end of the cursor
                do {
                    TextView totalsTimeTitle = (TextView) layout.findViewById(R.id.activityTotalTimeMonthLog);
                    if (month_totals.getString(month_totals.getColumnIndexOrThrow("TOTAL_TIME")) == null) {
                        totalsTimeTitle.setText("0");
                    } else {
                        String _hours = null;
                        DecimalFormat df = new DecimalFormat("0.0");
                        if (!month_totals.getString(month_totals.getColumnIndexOrThrow("TOTAL_TIME")).equals("0")) {
                            float hours = Float.parseFloat(month_totals.getString(month_totals.getColumnIndexOrThrow("TOTAL_TIME"))) / 60;
                            _hours = String.valueOf(df.format(hours));
                        } else {
                            _hours = "0";
                        }
                        totalsTimeTitle.setText(_hours);

                    }

                    TextView totalsDaysTitle = (TextView) layout.findViewById(R.id.activityTotalDayMonthLog);
                    totalsDaysTitle.setText(month_totals.getString(month_totals.getColumnIndexOrThrow("TOTAL_DAYS")));

                    TextView totalsUnitsTitle = (TextView) layout.findViewById(R.id.activityTotalUnitsMonthLog);
                    totalsUnitsTitle.setText(month_totals.getString(month_totals.getColumnIndexOrThrow("TOTAL_UNITS")));
                    // do something here
                } while (month_totals.moveToNext());
            }
            // make sure to close the cursor
            // month_totals.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                context.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    protected class DayLayout extends FrameLayout implements OnClickListener, OnTouchListener {

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        protected class LayoutHolder extends LinearLayout {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            public LayoutHolder(Context context) {
                super(context);
                // TODO Auto-generated constructor stub
                setWillNotDraw(false);
                addView(LinearLayout.inflate(getContext(), R.layout.week_layout_holder, null), lp);
            }

            @Override
            protected void onDraw(Canvas canvas) {
                // TODO Auto-generated method stub
                super.onDraw(canvas);
                if (highlighted) {
                    canvas.drawColor(0x20ffffff, PorterDuff.Mode.SRC_OVER);
                }
            }
        }

        LinearLayout layout;
        TextView day_title;
        LinearLayout activity_holder;
        TextView day_date;
        int day;
        int month;
        int week;
        Calendar cal;

        public DayLayout(Context context, Calendar cal, int i) {
            super(context);
            // TODO Auto-generated constructor stub

            layout = new LayoutHolder(context);
            this.day = cal.get(Calendar.DAY_OF_MONTH);
            this.month = cal.get(Calendar.MONTH);
            this.week = cal.get(Calendar.WEEK_OF_MONTH);
            this.cal = cal;

            addView(layout, lp);
            setOnClickListener(this);
            setOnTouchListener(this);

            day_title = (TextView) findViewById(R.id.day_title);
            day_date = (TextView) findViewById(R.id.day_date);
            activity_holder = (LinearLayout) findViewById(R.id.activity_holder);


            day_date.setText(format.formatDateToDay(cal.getTime()));
            setDayTitle(i);
            setActivities();
        }

        private void setDayTitle(int i) {
            switch (i) {
                case 1:
                    day_title.setText("SUN");
                    break;
                case 2:
                    day_title.setText("MON");
                    break;
                case 3:
                    day_title.setText("TUE");
                    break;
                case 4:
                    day_title.setText("WED");
                    break;
                case 5:
                    day_title.setText("THU");
                    break;
                case 6:
                    day_title.setText("FRI");
                    break;
                case 7:
                    day_title.setText("SAT");
                    break;

                default:
                    break;
            }
        }

        private void setActivities() {
            Cursor cursor = db.getActivity(format.formatDateToQuery(cal.getTime()));
            activity_holder.removeAllViews();

            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    // loop until it reach the end of the cursor
                    do {
//			    		context.startManagingCursor(cursor);

                        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        View v = inflater.inflate(R.layout.week_activity_layout, null);
                        TextView sport = (TextView) v.findViewById(R.id.week_view_item_name);
                        TextView time = (TextView) v.findViewById(R.id.week_view_item_time);
                        TextView info = (TextView) v.findViewById(R.id.week_view_item_info);
                        ImageView sportIcon = (ImageView) v.findViewById(R.id.sport_icon);

                        sport.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT_TITLE)));
                        time.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableDbAdapter.KEY_TOTAL_TIME)) + " min");
                        info.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableDbAdapter.KEY_INFO)));
                        int id_sport = cursor.getInt(cursor.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT));

                        if (id_sport == RUNNING_ID) {
                            sportIcon.setImageResource(R.drawable.sports_running);
                        }
                        if (id_sport == CYCLING_ID) {
                            sportIcon.setImageResource(R.drawable.sports_cycling);
                        }
                        if (id_sport == XCOUNTRY_ID) {
                            sportIcon.setImageResource(R.drawable.sports_x_country);
                        }
                        if (id_sport == SWIMMING_ID) {
                            sportIcon.setImageResource(R.drawable.sports_swimming);
                        }
                        if (id_sport == SKATE_ID) {
                            sportIcon.setImageResource(R.drawable.sports_skate);
                        }
                        if (id_sport == WALKING_ID) {
                            sportIcon.setImageResource(R.drawable.sports_walking);
                        }
                        if (id_sport == CANOE_ID) {
                            sportIcon.setImageResource(R.drawable.sports_canoe);
                        }
                        if (id_sport == FOOTBALL_ID) {
                            sportIcon.setImageResource(R.drawable.sports_football);
                        }
                        if (id_sport == HOCKEY_ID) {
                            sportIcon.setImageResource(R.drawable.sports_hockey);
                        }
                        if (id_sport == VOLLEY_ID) {
                            sportIcon.setImageResource(R.drawable.sports_volleyball);
                        }
                        if (id_sport == BASKET_ID) {
                            sportIcon.setImageResource(R.drawable.sports_basketball);
                        }
                        if (id_sport == TENNIS_ID) {
                            sportIcon.setImageResource(R.drawable.sports_tennis);
                        }
                        if (id_sport == SKIING_ID) {
                            sportIcon.setImageResource(R.drawable.sports_skiing);
                        }
                        if (id_sport == ATHLETICS_ID) {
                            sportIcon.setImageResource(R.drawable.sports_athletics);
                        }
                        if (id_sport == WEIGHT_ID) {
                            sportIcon.setImageResource(R.drawable.sports_weightlifting);
                        }
                        if (id_sport == OTHER_ID) {
                            sportIcon.setImageResource(R.drawable.sports_other);
                        }

                        activity_holder.addView(v);

                    } while (cursor.moveToNext());
                }
            }
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            Intent i = context.getIntent();
            i.putExtra("day", String.valueOf(day));
            i.putExtra("tabSelected", 1);
            i.putExtra("week", week);
            i.putExtra("month", month);
            startActivityForResult(i, 0);
            context.overridePendingTransition(0, 0);
            context.finish();
        }

        private boolean highlighted = false;
        private HightlightTask highlightTask;

        private class HightlightTask extends TimerTask {

            @Override
            public void run() {
                if (!highlighted) {
                    highlighted = true;
                    layout.postInvalidate();
                }
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN) {

                if (!highlighted && highlightTask == null) {

                    System.out.println("HIGHLIGHT");

                    highlightTask = new HightlightTask();
                    highlightTask.run();
                }

            } else {
                if (highlightTask != null) {
                    highlightTask.cancel();
                    highlightTask = null;
                }

                if (highlighted) {
                    System.out.println("DEHIGHLIGHT");
                    highlighted = false;
                    layout.invalidate();
                }
            }
            return false;
        }

    }


}
