package sk.android.training.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sk.android.training.CreateActivity;
import sk.android.training.R;
import sk.android.training.database.TableDbAdapter;
import sk.android.training.utils.TimeDateFormatter;

public class FragmentDayView extends Fragment {

    private String date;
    private TextView dayLog;
    private TextView editId;
    private TextView editTime;
    private TextView editTotalTime;
    private TextView editDistance;
    private TextView editInfo;
    private TextView editSport;
    private TextView editAltitude;
    private TextView editAvgHR;
    private TextView editMaxHR;

    private ImageView editActivity;
    private ImageView deleteActivity;

    private TimeDateFormatter format;

    private int _day;
    private int _month;
    private int _year;
    private String _date;

    private Calendar today;
    private Date today_date;
    Bundle extras;

    private SharedPreferences prefs;
    private String prefName = "UserPref";

    private TableDbAdapter db;

    private Cursor activity;
    private Cursor c;
    private Cursor totals;

    private LinearLayout linearLayout;
    private LayoutInflater layoutInflater;


    static final int DIALOG_EDIT = 0;
    static final int DIALOG_DELETE = 1;
    static final int TIME_PICKER_DIALOG = 2;

    private static final int ENDURANCE_ID = 1;
    private static final int BASIC_ID = 2;

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

    private static final String UNITS_KEY = "units";

    private Activity context;
    private View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = getActivity();
        setHasOptionsMenu(true);
//        getActivity().getActionBar().setDisplayShowHomeEnabled(true);

        layout = inflater.inflate(R.layout.day_log, container, false);

        super.onCreate(savedInstanceState);

        prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        db = new TableDbAdapter(context);
        db.open();
        format = new TimeDateFormatter();

        extras = context.getIntent().getExtras();

        linearLayout = (LinearLayout) layout.findViewById(R.id.dayLogItemsLayout);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setDate();
        getActivitySessions(3);

        today_date = new Date(today.getTimeInMillis());

        dayLog = (TextView) layout.findViewById(R.id.titleDay);
        dayLog.setText(DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(today_date));

        deleteActivity = (ImageView) layout.findViewById(R.id.deleteIcon);
        editActivity = (ImageView) layout.findViewById(R.id.editIcon);

        ImageButton nextDay = (ImageButton) layout.findViewById(R.id.nextDay);
        nextDay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (_day == today.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    if (_month == today.getActualMaximum(Calendar.MONTH)) {
                        _day = 1;
                        _month = 0;
                        _year = today.get(Calendar.YEAR) + 1;

                    } else {
                        _day = 1;
                        _month = today.get(Calendar.MONTH) + 1;
                        _year = today.get(Calendar.YEAR);
                    }
                } else {

                    _day = today.get(Calendar.DAY_OF_MONTH) + 1;
                    _month = today.get(Calendar.MONTH);
                    _year = today.get(Calendar.YEAR);

                }

                today.set(_year, _month, _day);

                today_date.setTime(today.getTimeInMillis());
                dayLog.setText(String.format(DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(today_date)));

                date = (_day + "/" + (_month + 1) + "/" + _year);

                getActivitySessions(1);
            }
        });

        ImageButton previousDay = (ImageButton) layout.findViewById(R.id.previousDay);
        previousDay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (_day == 1) {
                    if (_month == 0) {
                        _year = today.get(Calendar.YEAR) - 1;
                        today.set(_year, _month, _day);
                        _month = 11;
                        _day = 31;

                    } else {
                        _month = today.get(Calendar.MONTH) - 1;

                        today.set(_year, _month, _day);

                        _day = today.getActualMaximum(Calendar.DAY_OF_MONTH);
                        _year = today.get(Calendar.YEAR);

                    }
                } else {
                    _day = today.get(Calendar.DAY_OF_MONTH) - 1;
                    _month = today.get(Calendar.MONTH);
                    _year = today.get(Calendar.YEAR);
                }

                today.set(_year, _month, _day);

                today_date.setTime(today.getTimeInMillis());
                dayLog.setText(String.format(DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(today_date)));

                date = (_day + "/" + (_month + 1) + "/" + _year);

                getActivitySessions(0);

            }
        });
        return layout;
    }

    private void getActivityDetails(View v) {
        // TODO Auto-generated method stub
        editId = (TextView) v.findViewById(R.id.id_activity);
        editTime = (TextView) v.findViewById(R.id.activityTimeDayLog);
        editTotalTime = (TextView) v.findViewById(R.id.activityTotalTimeDayLog);
        editDistance = (TextView) v.findViewById(R.id.DistanceDayLog);
        editInfo = (TextView) v.findViewById(R.id.activityDetailsDayLog);
        editSport = (TextView) v.findViewById(R.id.sportTypeDayLog);
        editAltitude = (TextView) v.findViewById(R.id.altitudeDayLog);
        editAvgHR = (TextView) v.findViewById(R.id.avgHRDayLog);
        editMaxHR = (TextView) v.findViewById(R.id.maxHRDayLog);
    }

    private void setDate() {

        _day = Integer.parseInt(extras.getString("day"));
        _month = extras.getInt("month");
        _year = Integer.parseInt(extras.getString("year"));

        today = Calendar.getInstance();
        today.set(_year, _month, _day);

        date = (_day + "/" + (_month + 1) + "/" + _year);
    }

    private void showDialog(int id) {
        Dialog dialog;

        switch (id) {
            case DIALOG_DELETE:
                dialog = new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(editSport.getText().toString() + " " + editTime.getText().toString())
                        .setMessage("Are you sure you want to delete this activity?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String delete = "DELETE FROM activity WHERE created = " + Long.parseLong(editId.getText().toString()) + " AND " +
                                        "username = '" + prefs.getString("username", "") + "';";

                                db.insertSyncQuery(delete);
                                db.deleteActivity(Long.parseLong(editId.getText().toString()));
                                Toast.makeText(context.getBaseContext(), "Activity Deleted!", Toast.LENGTH_SHORT).show();
                                getActivitySessions(3);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.cancel();
                            }
                        })
                        .show();
        }

    }

    private void showEditDialog(long editDialogId, String editDialogTime, String editDialogTotalTime, String editDialogDistance,
                                String editDialogInfo, String editDialogSport, String editDialogAlt, String editDialogAvgHR, String editDialogMaxHR,
                                int day, int month, int year) {

        DialogFragment newFragment = EditDialogFragment.newInstance(editDialogId, editDialogTime, editDialogTotalTime,
                editDialogDistance, editDialogInfo, editDialogSport, editDialogAlt, editDialogAvgHR, editDialogMaxHR, day, month, year);
        newFragment.show(getFragmentManager(), "EditDialog");
    }

    public void getActivitySessions(int side) {
        int offset = 0;
        linearLayout.removeAllViews();
        _date = format.formatDateDatabase(date);

        activity = db.getActivity(Integer.parseInt(_date));
        if (activity != null && activity.getCount() > 0) {
            if (activity.moveToFirst()) {
                // loop until it reach the end of the cursor
                do {
                    context.startManagingCursor(activity);
                    if (activity.getInt(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT_TYPE)) == ENDURANCE_ID) {

                        final View view = layoutInflater.inflate(R.layout.day_log_item_endurance, null);
//					   	    runSlideAnimationOn(this, view, offset, side);
                        linearLayout.addView(view);
                        offset += 100;

                        editActivity = (ImageView) view.findViewById(R.id.editIcon);
                        deleteActivity = (ImageView) view.findViewById(R.id.deleteIcon);

                        deleteActivity.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                getActivityDetails(view);
                                showDialog(DIALOG_DELETE);
                            }
                        });

                        editActivity.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                getActivityDetails(view);

                                String editDialogDistance = null;
                                String editDialogAlt = null;
                                String editDialogAvgHR = null;
                                String editDialogMaxHR = null;

                                long editDialogId = Long.parseLong(editId.getText().toString());
                                String editDialogTime = editTime.getText().toString();
                                String editDialogTotalTime = editTotalTime.getText().toString();
                                String editDialogInfo = editInfo.getText().toString();
                                String editDialogSport = editSport.getText().toString();
                                try {
                                    editDialogDistance = editDistance.getText().toString();
                                    editDialogAlt = editAltitude.getText().toString();
                                    editDialogAvgHR = editAvgHR.getText().toString();
                                    editDialogMaxHR = editMaxHR.getText().toString();
                                } catch (NullPointerException e) {

                                }

                                showEditDialog(editDialogId, editDialogTime, editDialogTotalTime, editDialogDistance,
                                        editDialogInfo, editDialogSport, editDialogAlt, editDialogAvgHR, editDialogMaxHR, _day, _month, _year);
                            }
                        });

                        registerForContextMenu(view);

                        TextView id = (TextView) view.findViewById(R.id.id_activity);
                        TextView sport = (TextView) view.findViewById(R.id.sportTypeDayLog);
                        TextView time = (TextView) view.findViewById(R.id.activityTimeDayLog);
                        TextView totalTime = (TextView) view.findViewById(R.id.activityTotalTimeDayLog);
                        TextView details = (TextView) view.findViewById(R.id.activityDetailsDayLog);
                        TextView distance = (TextView) view.findViewById(R.id.DistanceDayLog);
                        TextView altitude = (TextView) view.findViewById(R.id.altitudeDayLog);
                        TextView speed = (TextView) view.findViewById(R.id.speedDayLog);
                        TextView pace = (TextView) view.findViewById(R.id.paceDayLog);
                        TextView avgHR = (TextView) view.findViewById(R.id.avgHRDayLog);
                        TextView maxHR = (TextView) view.findViewById(R.id.maxHRDayLog);
                        ImageView sportIcon = (ImageView) view.findViewById(R.id.training_icon);

                        TableRow distanceRow = (TableRow) view.findViewById(R.id.distanceRow);
                        TableRow totalClimbRow = (TableRow) view.findViewById(R.id.totalClimbRow);
                        TableRow speedRow = (TableRow) view.findViewById(R.id.speedRow);
                        TableRow paceRow = (TableRow) view.findViewById(R.id.paceRow);
                        TableRow detailsTitleRow = (TableRow) view.findViewById(R.id.detailsTitleRow);
                        LinearLayout detailsHRlayout = (LinearLayout) view.findViewById(R.id.detailsHRlayout);

                        id.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_CREATED)));
                        id.setVisibility(View.INVISIBLE);
                        sport.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT_TITLE)));

                        time.setText(format.formatTimeToDayLog(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_TIME)), context));

                        if (activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_TOTAL_TIME)).length() != 0) {

                            totalTime.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_TOTAL_TIME)) + " min");
                        } else {
                            totalTime.setText("N/A");
                        }
                        if (activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_DISTANCE)).length() != 0 &&
                                !activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_DISTANCE)).equals("0")) {
                            if (prefs.getInt(UNITS_KEY, 0) == 0) {
                                distance.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_DISTANCE)) + " km");
                            } else {
                                distance.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_DISTANCE)) + " mi");
                            }
                        } else {
                            distanceRow.setVisibility(View.GONE);
                        }
                        if ((activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_ALTITUDE))).length() != 0 &&
                                !activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_ALTITUDE)).equals("0")) {
                            if (prefs.getInt(UNITS_KEY, 0) == 0) {
                                altitude.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_ALTITUDE)) + " m");
                            } else {
                                altitude.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_ALTITUDE)) + " yd");
                            }
                        } else {
                            totalClimbRow.setVisibility(View.GONE);
                        }
                        try {

                            if (!activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_DISTANCE)).equals("0")) {
                                if (prefs.getInt(UNITS_KEY, 0) == 0) {
                                    speed.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_SPEED)) + " km/h");
                                } else {
                                    speed.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_SPEED)) + " mph");
                                }
                                int _pace = Integer.parseInt(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_PACE)));
                                int _min = _pace / 60;
                                int _sec = _pace % 60;

                                if (prefs.getInt(UNITS_KEY, 0) == 0) {
                                    pace.setText(_min + ":" + String.format("%02d", _sec) + " min/km");
                                } else {
                                    pace.setText(_min + ":" + String.format("%02d", _sec) + " min/mi");
                                }
                            } else {
                                speedRow.setVisibility(View.GONE);
                                paceRow.setVisibility(View.GONE);
                            }
                        } catch (NumberFormatException e) {
                            speedRow.setVisibility(View.GONE);
                            paceRow.setVisibility(View.GONE);
                        }
                        if (!(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_AVGHR))).equals("") &&
                                !activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_AVGHR)).equals("0")) {
                            avgHR.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_AVGHR)));
                        } else {
                            detailsHRlayout.setVisibility(View.GONE);
                        }
                        if (!(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_MAXHR))).equals("") &&
                                !activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_MAXHR)).equals("0")) {
                            maxHR.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_MAXHR)));
                        } else {
                            detailsHRlayout.setVisibility(View.GONE);
                        }
                        details.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_INFO)));

                        int id_sport = activity.getInt(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT));

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
                    }

                    if (activity.getInt(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT_TYPE)) == BASIC_ID) {

                        final View view = layoutInflater.inflate(R.layout.day_log_item_basic, null);
//					   	    runSlideAnimationOn(this, view, offset, side);
                        linearLayout.addView(view);
                        offset += 100;

                        editActivity = (ImageView) view.findViewById(R.id.editIcon);
                        deleteActivity = (ImageView) view.findViewById(R.id.deleteIcon);

                        deleteActivity.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                getActivityDetails(view);
                                showDialog(DIALOG_DELETE);
                            }
                        });

                        editActivity.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                getActivityDetails(view);

                                String editDialogDistance = null;
                                String editDialogAlt = null;
                                String editDialogAvgHR = null;
                                String editDialogMaxHR = null;

                                long editDialogId = Long.parseLong(editId.getText().toString());
                                String editDialogTime = editTime.getText().toString();
                                String editDialogTotalTime = editTotalTime.getText().toString();
                                String editDialogInfo = editInfo.getText().toString();
                                String editDialogSport = editSport.getText().toString();
                                try {
                                    editDialogDistance = editDistance.getText().toString();
                                    editDialogAlt = editAltitude.getText().toString();
                                    editDialogAvgHR = editAvgHR.getText().toString();
                                    editDialogMaxHR = editMaxHR.getText().toString();
                                } catch (NullPointerException e) {

                                }

                                showEditDialog(editDialogId, editDialogTime, editDialogTotalTime, editDialogDistance,
                                        editDialogInfo, editDialogSport, editDialogAlt, editDialogAvgHR, editDialogMaxHR, _day, _month, _year);
                            }
                        });

                        TextView id = (TextView) view.findViewById(R.id.id_activity);
                        TextView sport = (TextView) view.findViewById(R.id.sportTypeDayLog);
                        TextView time = (TextView) view.findViewById(R.id.activityTimeDayLog);
                        TextView totalTime = (TextView) view.findViewById(R.id.activityTotalTimeDayLog);
                        TextView details = (TextView) view.findViewById(R.id.activityDetailsDayLog);
                        ImageView sportIcon = (ImageView) view.findViewById(R.id.training_icon);

                        id.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_CREATED)));
                        id.setVisibility(View.INVISIBLE);
                        sport.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT_TITLE)));
                        time.setText(format.formatTimeToDayLog(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_TIME)), context));
                        if (activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_TOTAL_TIME)).length() != 0) {
                            totalTime.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_TOTAL_TIME)) + " min");
                        } else {
                            totalTime.setText("N/A");
                        }
                        details.setText(activity.getString(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_INFO)));

                        int id_sport = activity.getInt(activity.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT));

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

                        registerForContextMenu(view);
                    }

                    // do something here
                } while (activity.moveToNext());
            }
            // make sure to close the cursor
            //activity.close();
        } else {
            TextView noRecord = new TextView(context);
            noRecord.setText("No Record!");
            noRecord.setTextAppearance(context, R.style.SubTitleText);
            noRecord.setPadding(20, 5, 10, 10);
            linearLayout.addView(noRecord);
        }
        setTotals();

    }

    private void setTotals() {
        totals = db.getActivityDayTotals(Integer.parseInt(_date));
        if (totals != null && totals.getCount() > 0) {
            if (totals.moveToFirst()) {
                // loop until it reach the end of the cursor
                do {
                    context.startManagingCursor(totals);

                    TextView totalsTimeTitle = (TextView) layout.findViewById(R.id.activityTotalTime_DayLog);
                    if (totals.getString(totals.getColumnIndexOrThrow("TOTAL_TIME")) == null) {
                        totalsTimeTitle.setText("0" + " min");
                    } else {
                        totalsTimeTitle.setText(totals.getString(totals.getColumnIndexOrThrow("TOTAL_TIME")) + " min");
                    }

                    TextView totalsUnitsTitle = (TextView) layout.findViewById(R.id.activityTotalUnits_DayLog);
                    totalsUnitsTitle.setText(totals.getString(totals.getColumnIndexOrThrow("TOTAL_UNITS")));

                } while (totals.moveToNext());
            }

            //totals.close();
        }
    }

    private void createActivity() {

        Intent i = new Intent(context, CreateActivity.class);
        i.putExtra("day", Integer.toString(_day));
        i.putExtra("month", Integer.toString(_month + 1));
        i.putExtra("year", Integer.toString(_year));
        startActivityForResult(i, 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub		@Override

        inflater.inflate(R.menu.add_activity_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.menu_insert:
                createActivity();
                return true;
            case android.R.id.home:
                context.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Action:");
        menu.add(0, v.getId(), 0, "Edit Activity");
        menu.add(0, v.getId(), 0, "Delete Activity");
        getActivityDetails(v);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getTitle() == "Edit Activity") {

            String editDialogDistance = null;
            String editDialogAlt = null;
            String editDialogAvgHR = null;
            String editDialogMaxHR = null;

            long editDialogId = Long.parseLong(editId.getText().toString());
            String editDialogTime = editTime.getText().toString();
            String editDialogTotalTime = editTotalTime.getText().toString();
            String editDialogInfo = editInfo.getText().toString();
            String editDialogSport = editSport.getText().toString();
            try {
                editDialogDistance = editDistance.getText().toString();
                editDialogAlt = editAltitude.getText().toString();
                editDialogAvgHR = editAvgHR.getText().toString();
                editDialogMaxHR = editMaxHR.getText().toString();
            } catch (NullPointerException e) {

            }

            showEditDialog(editDialogId, editDialogTime, editDialogTotalTime, editDialogDistance,
                    editDialogInfo, editDialogSport, editDialogAlt, editDialogAvgHR, editDialogMaxHR, _day, _month, _year);
        } else if (item.getTitle() == "Delete Activity") {
            showDialog(DIALOG_DELETE);
        } else {
            return false;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {
            Toast.makeText(context, "Activity Created!", Toast.LENGTH_SHORT).show();
            getActivitySessions(3);
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        context.stopManagingCursor(totals);
        context.stopManagingCursor(c);
        context.stopManagingCursor(activity);
    }

}
