package sk.android.training.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sk.android.training.R;
import sk.android.training.database.TableDbAdapter;

public class FragmentTotals extends Fragment {

    private Spinner spinnerYear;
    private Spinner spinnerSport;
    private TableDbAdapter db;

    private TextView januaryDays;
    private TextView januarySessions;
    private TextView januaryTime;
    private TextView januaryDistance;
    private TextView januaryAltitude;

    private TextView februaryDays;
    private TextView februarySessions;
    private TextView februaryTime;
    private TextView februaryDistance;
    private TextView februaryAltitude;

    private TextView marchDays;
    private TextView marchSessions;
    private TextView marchTime;
    private TextView marchDistance;
    private TextView marchAltitude;

    private TextView aprilDays;
    private TextView aprilSessions;
    private TextView aprilTime;
    private TextView aprilDistance;
    private TextView aprilAltitude;

    private TextView mayDays;
    private TextView maySessions;
    private TextView mayTime;
    private TextView mayDistance;
    private TextView mayAltitude;

    private TextView juneDays;
    private TextView juneSessions;
    private TextView juneTime;
    private TextView juneDistance;
    private TextView juneAltitude;

    private TextView julyDays;
    private TextView julySessions;
    private TextView julyTime;
    private TextView julyDistance;
    private TextView julyAltitude;

    private TextView augustDays;
    private TextView augustSessions;
    private TextView augustTime;
    private TextView augustDistance;
    private TextView augustAltitude;

    private TextView septemberDays;
    private TextView septemberSessions;
    private TextView septemberTime;
    private TextView septemberDistance;
    private TextView septemberAltitude;

    private TextView octoberDays;
    private TextView octoberSessions;
    private TextView octoberTime;
    private TextView octoberDistance;
    private TextView octoberAltitude;

    private TextView novemberDays;
    private TextView novemberSessions;
    private TextView novemberTime;
    private TextView novemberDistance;
    private TextView novemberAltitude;

    private TextView decemberDays;
    private TextView decemberSessions;
    private TextView decemberTime;
    private TextView decemberDistance;
    private TextView decemberAltitude;

    private TextView totalsDays;
    private TextView totalsSessions;
    private TextView totalsTime;
    private TextView totalsDistance;
    private TextView totalsAltitude;

    private int _sport;
    private int _year;

    private static int january = 100;
    private static int february = 200;
    private static int march = 300;
    private static int april = 400;
    private static int may = 500;
    private static int june = 600;
    private static int july = 700;
    private static int august = 800;
    private static int september = 900;
    private static int october = 1000;
    private static int november = 1100;
    private static int decemeber = 1200;

    private Cursor c;
    private Cursor totalsCursor;
    private Cursor januaryCursor;
    private Cursor februaryCursor;
    private Cursor marchCursor;
    private Cursor aprilCursor;
    private Cursor mayCursor;
    private Cursor juneCursor;
    private Cursor julyCursor;
    private Cursor augustCursor;
    private Cursor septemberCursor;
    private Cursor octoberCursor;
    private Cursor novemberCursor;
    private Cursor decemberCursor;

    private DecimalFormat df;

    private Map<Long, Integer> sportsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.totals_fragment, container, false);

        db = new TableDbAdapter(getActivity());

        df = new DecimalFormat("0.0");

        // getSherlockActivity().startManagingCursor(sportCursor);
        // getSherlockActivity().startManagingCursor(c);

        spinnerYear = (Spinner) v.findViewById(R.id.spinnerYear);
        spinnerSport = (Spinner) v.findViewById(R.id.spinnerSports);

        januaryDays = (TextView) v.findViewById(R.id.januaryDays);
        januarySessions = (TextView) v.findViewById(R.id.januarySessions);
        januaryTime = (TextView) v.findViewById(R.id.januaryTime);
        januaryDistance = (TextView) v.findViewById(R.id.januaryDistance);
        januaryAltitude = (TextView) v.findViewById(R.id.januaryAltitude);

        februaryDays = (TextView) v.findViewById(R.id.februaryDays);
        februarySessions = (TextView) v.findViewById(R.id.februarySessions);
        februaryTime = (TextView) v.findViewById(R.id.februaryTime);
        februaryDistance = (TextView) v.findViewById(R.id.februaryDistance);
        februaryAltitude = (TextView) v.findViewById(R.id.februaryAltitude);

        marchDays = (TextView) v.findViewById(R.id.marchDays);
        marchSessions = (TextView) v.findViewById(R.id.marchSessions);
        marchTime = (TextView) v.findViewById(R.id.marchTime);
        marchDistance = (TextView) v.findViewById(R.id.marchDistance);
        marchAltitude = (TextView) v.findViewById(R.id.marchAltitude);

        aprilDays = (TextView) v.findViewById(R.id.aprilDays);
        aprilSessions = (TextView) v.findViewById(R.id.aprilSessions);
        aprilTime = (TextView) v.findViewById(R.id.aprilTime);
        aprilDistance = (TextView) v.findViewById(R.id.aprilDistance);
        aprilAltitude = (TextView) v.findViewById(R.id.aprilAltitude);

        mayDays = (TextView) v.findViewById(R.id.mayDays);
        maySessions = (TextView) v.findViewById(R.id.maySessions);
        mayTime = (TextView) v.findViewById(R.id.mayTime);
        mayDistance = (TextView) v.findViewById(R.id.mayDistance);
        mayAltitude = (TextView) v.findViewById(R.id.mayAltitude);

        juneDays = (TextView) v.findViewById(R.id.juneDays);
        juneSessions = (TextView) v.findViewById(R.id.juneSessions);
        juneTime = (TextView) v.findViewById(R.id.juneTime);
        juneDistance = (TextView) v.findViewById(R.id.juneDistance);
        juneAltitude = (TextView) v.findViewById(R.id.juneAltitude);

        julyDays = (TextView) v.findViewById(R.id.julyDays);
        julySessions = (TextView) v.findViewById(R.id.julySessions);
        julyTime = (TextView) v.findViewById(R.id.julyTime);
        julyDistance = (TextView) v.findViewById(R.id.julyDistance);
        julyAltitude = (TextView) v.findViewById(R.id.julyAltitude);

        augustDays = (TextView) v.findViewById(R.id.augustDays);
        augustSessions = (TextView) v.findViewById(R.id.augustSessions);
        augustTime = (TextView) v.findViewById(R.id.augustTime);
        augustDistance = (TextView) v.findViewById(R.id.augustDistance);
        augustAltitude = (TextView) v.findViewById(R.id.augustAltitude);

        septemberDays = (TextView) v.findViewById(R.id.septemberDays);
        septemberSessions = (TextView) v.findViewById(R.id.septemberSessions);
        septemberTime = (TextView) v.findViewById(R.id.septemberTime);
        septemberDistance = (TextView) v.findViewById(R.id.septemberDistance);
        septemberAltitude = (TextView) v.findViewById(R.id.septemberAltitude);

        octoberDays = (TextView) v.findViewById(R.id.octoberDays);
        octoberSessions = (TextView) v.findViewById(R.id.octoberSessions);
        octoberTime = (TextView) v.findViewById(R.id.octoberTime);
        octoberDistance = (TextView) v.findViewById(R.id.octoberDistance);
        octoberAltitude = (TextView) v.findViewById(R.id.octoberAltitude);

        novemberDays = (TextView) v.findViewById(R.id.novemberDays);
        novemberSessions = (TextView) v.findViewById(R.id.novemberSessions);
        novemberTime = (TextView) v.findViewById(R.id.novemberTime);
        novemberDistance = (TextView) v.findViewById(R.id.novemberDistance);
        novemberAltitude = (TextView) v.findViewById(R.id.novemberAltitude);

        decemberDays = (TextView) v.findViewById(R.id.decemberDays);
        decemberSessions = (TextView) v.findViewById(R.id.decemberSessions);
        decemberTime = (TextView) v.findViewById(R.id.decemberTime);
        decemberDistance = (TextView) v.findViewById(R.id.decemberDistance);
        decemberAltitude = (TextView) v.findViewById(R.id.decemberAltitude);

        totalsDays = (TextView) v.findViewById(R.id.totalsDays);
        totalsSessions = (TextView) v.findViewById(R.id.totalsSessions);
        totalsTime = (TextView) v.findViewById(R.id.totalsTime);
        totalsDistance = (TextView) v.findViewById(R.id.totalsDistance);
        totalsAltitude = (TextView) v.findViewById(R.id.totalsAltitude);

        fillSpinner();

        _year = 20120000;
        _sport = 0;

        spinnerYear.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                // TODO Auto-generated method stub
                _year = Integer.parseInt(String.valueOf(spinnerYear.getSelectedItem() + "0000"));
                setTotals(_year, _sport);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        spinnerSport.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                // TODO Auto-generated method stub
                _sport = Integer.parseInt(String.valueOf(sportsList.get(spinnerSport.getSelectedItemId())));
                setTotals(_year, _sport);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        return v;
    }

    private void setTotals(int year, int sport) {
        db.open();

        if (sport == 0) {

            totalsCursor = db.getActivityYearTotals(year);
            januaryCursor = db.getActivityMonthTotals(year + january);
            februaryCursor = db.getActivityMonthTotals(year + february);
            marchCursor = db.getActivityMonthTotals(year + march);
            aprilCursor = db.getActivityMonthTotals(year + april);
            mayCursor = db.getActivityMonthTotals(year + may);
            juneCursor = db.getActivityMonthTotals(year + june);
            julyCursor = db.getActivityMonthTotals(year + july);
            augustCursor = db.getActivityMonthTotals(year + august);
            septemberCursor = db.getActivityMonthTotals(year + september);
            octoberCursor = db.getActivityMonthTotals(year + october);
            novemberCursor = db.getActivityMonthTotals(year + november);
            decemberCursor = db.getActivityMonthTotals(year + decemeber);

        } else {
            totalsCursor = db.getActivityYearTotals(year, sport);
            januaryCursor = db.getActivityMonthTotals(year + january, sport);
            februaryCursor = db.getActivityMonthTotals(year + february, sport);
            marchCursor = db.getActivityMonthTotals(year + march, sport);
            aprilCursor = db.getActivityMonthTotals(year + april, sport);
            mayCursor = db.getActivityMonthTotals(year + may, sport);
            juneCursor = db.getActivityMonthTotals(year + june, sport);
            julyCursor = db.getActivityMonthTotals(year + july, sport);
            augustCursor = db.getActivityMonthTotals(year + august, sport);
            septemberCursor = db.getActivityMonthTotals(year + september, sport);
            octoberCursor = db.getActivityMonthTotals(year + october, sport);
            novemberCursor = db.getActivityMonthTotals(year + november, sport);
            decemberCursor = db.getActivityMonthTotals(year + decemeber, sport);
        }

        // totals
        totalsDays.setText(totalsCursor.getString(totalsCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        totalsSessions.setText(totalsCursor.getString(totalsCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            totalsTime
                    .setText(df.format(Float.parseFloat(totalsCursor
                            .getString(totalsCursor
                                    .getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            totalsTime.setText("0");
        }
        try {
            totalsDistance.setText(df.format(Float.parseFloat(totalsCursor
                    .getString(totalsCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            totalsDistance.setText("0");
        }
        try {
            totalsAltitude.setText(String.valueOf(totalsCursor
                    .getInt(totalsCursor.getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            totalsAltitude.setText("0");
        }

        // january
        januaryDays.setText(januaryCursor.getString(januaryCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        januarySessions.setText(januaryCursor.getString(januaryCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            januaryTime.setText(df.format(Float.parseFloat(januaryCursor
                    .getString(januaryCursor
                            .getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            januaryTime.setText("0");
        }
        try {
            januaryDistance.setText(df.format(Float.parseFloat(januaryCursor
                    .getString(januaryCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            januaryDistance.setText("0");
        }
        try {
            januaryAltitude.setText(String.valueOf(januaryCursor
                    .getInt(januaryCursor.getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            januaryAltitude.setText("0");
        }

        // february
        februaryDays.setText(februaryCursor.getString(februaryCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        februarySessions.setText(februaryCursor.getString(februaryCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            februaryTime.setText(df.format(Float.parseFloat(februaryCursor
                    .getString(februaryCursor
                            .getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            februaryTime.setText("0");
        }
        try {
            februaryDistance.setText(df.format(Float.parseFloat(februaryCursor
                    .getString(februaryCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            februaryDistance.setText("0");
        }
        try {
            februaryAltitude
                    .setText(String.valueOf(februaryCursor
                            .getInt(februaryCursor
                                    .getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            februaryAltitude.setText("0");
        }

        // march
        marchDays.setText(marchCursor.getString(marchCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        marchSessions.setText(marchCursor.getString(marchCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            marchTime
                    .setText(df.format(Float.parseFloat(marchCursor
                            .getString(marchCursor
                                    .getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            marchTime.setText("0");
        }
        try {
            marchDistance.setText(df.format(Float.parseFloat(marchCursor
                    .getString(marchCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            marchDistance.setText("0");
        }
        try {
            marchAltitude.setText(String.valueOf(marchCursor.getInt(marchCursor
                    .getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            marchAltitude.setText("0");
        }

        // april
        aprilDays.setText(aprilCursor.getString(aprilCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        aprilSessions.setText(aprilCursor.getString(aprilCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            aprilTime
                    .setText(df.format(Float.parseFloat(aprilCursor
                            .getString(aprilCursor
                                    .getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            aprilTime.setText("0");
        }
        try {
            aprilDistance.setText(df.format(Float.parseFloat(aprilCursor
                    .getString(aprilCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            aprilDistance.setText("0");
        }
        try {
            aprilAltitude.setText(String.valueOf(aprilCursor.getInt(aprilCursor
                    .getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            aprilAltitude.setText("0");
        }

        // may
        mayDays.setText(mayCursor.getString(mayCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        maySessions.setText(mayCursor.getString(mayCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            mayTime.setText(df.format(Float.parseFloat(mayCursor
                    .getString(mayCursor.getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            mayTime.setText("0");
        }
        try {
            mayDistance.setText(df.format(Float.parseFloat(mayCursor
                    .getString(mayCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            mayDistance.setText("0");
        }
        try {
            mayAltitude.setText(String.valueOf(mayCursor.getInt(mayCursor
                    .getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            mayAltitude.setText("0");
        }

        // june
        juneDays.setText(juneCursor.getString(juneCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        juneSessions.setText(juneCursor.getString(juneCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            juneTime.setText(df.format(Float.parseFloat(juneCursor
                    .getString(juneCursor.getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            juneTime.setText("0");
        }
        try {
            juneDistance.setText(df.format(Float.parseFloat(juneCursor
                    .getString(juneCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            juneDistance.setText("0");
        }
        try {
            juneAltitude.setText(String.valueOf(juneCursor.getInt(juneCursor
                    .getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            juneAltitude.setText("0");
        }

        // july
        julyDays.setText(julyCursor.getString(julyCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        julySessions.setText(julyCursor.getString(julyCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            julyTime.setText(df.format(Float.parseFloat(julyCursor
                    .getString(julyCursor.getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            julyTime.setText("0");
        }
        try {
            julyDistance.setText(df.format(Float.parseFloat(julyCursor
                    .getString(julyCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            julyDistance.setText("0");
        }
        try {
            julyAltitude.setText(String.valueOf(julyCursor.getInt(julyCursor
                    .getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            julyAltitude.setText("0");
        }

        // august
        augustDays.setText(augustCursor.getString(augustCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        augustSessions.setText(augustCursor.getString(augustCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            augustTime
                    .setText(df.format(Float.parseFloat(augustCursor
                            .getString(augustCursor
                                    .getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            augustTime.setText("0");
        }
        try {
            augustDistance.setText(df.format(Float.parseFloat(augustCursor
                    .getString(augustCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            augustDistance.setText("0");
        }
        try {
            augustAltitude.setText(String.valueOf(augustCursor
                    .getInt(augustCursor.getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            augustAltitude.setText("0");
        }

        // september
        septemberDays.setText(septemberCursor.getString(septemberCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        septemberSessions.setText(septemberCursor.getString(septemberCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            septemberTime.setText(df.format(Float.parseFloat(septemberCursor
                    .getString(septemberCursor
                            .getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            septemberTime.setText("0");
        }
        try {
            septemberDistance.setText(df.format(Float
                    .parseFloat(septemberCursor.getString(septemberCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            septemberDistance.setText("0");
        }
        try {
            septemberAltitude
                    .setText(String.valueOf(septemberCursor
                            .getInt(septemberCursor
                                    .getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            septemberAltitude.setText("0");
        }

        // october
        octoberDays.setText(octoberCursor.getString(octoberCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        octoberSessions.setText(octoberCursor.getString(octoberCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            octoberTime.setText(df.format(Float.parseFloat(octoberCursor
                    .getString(octoberCursor
                            .getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            octoberTime.setText("0");
        }
        try {
            octoberDistance.setText(df.format(Float.parseFloat(octoberCursor
                    .getString(octoberCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            octoberDistance.setText("0");
        }
        try {
            octoberAltitude.setText(String.valueOf(octoberCursor
                    .getInt(octoberCursor.getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            octoberAltitude.setText("0");
        }

        // november
        novemberDays.setText(novemberCursor.getString(novemberCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        novemberSessions.setText(novemberCursor.getString(novemberCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            novemberTime.setText(df.format(Float.parseFloat(novemberCursor
                    .getString(novemberCursor
                            .getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            novemberTime.setText("0");
        }
        try {
            novemberDistance.setText(df.format(Float.parseFloat(novemberCursor
                    .getString(novemberCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            novemberDistance.setText("0");
        }
        try {
            novemberAltitude
                    .setText(String.valueOf(novemberCursor
                            .getInt(novemberCursor
                                    .getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            novemberAltitude.setText("0");
        }

        // december
        decemberDays.setText(decemberCursor.getString(decemberCursor
                .getColumnIndexOrThrow("TOTAL_DAYS")));
        decemberSessions.setText(decemberCursor.getString(decemberCursor
                .getColumnIndexOrThrow("TOTAL_UNITS")));
        try {
            decemberTime.setText(df.format(Float.parseFloat(decemberCursor
                    .getString(decemberCursor
                            .getColumnIndexOrThrow("TOTAL_TIME"))) / 60));
        } catch (NullPointerException e) {
            decemberTime.setText("0");
        }
        try {
            decemberDistance.setText(df.format(Float.parseFloat(decemberCursor
                    .getString(decemberCursor
                            .getColumnIndexOrThrow("TOTAL_DISTANCE")))));
        } catch (NullPointerException e) {
            decemberDistance.setText("0");
        }
        try {
            decemberAltitude
                    .setText(String.valueOf(decemberCursor
                            .getInt(decemberCursor
                                    .getColumnIndexOrThrow("TOTAL_ALT"))));
        } catch (NullPointerException e) {
            decemberAltitude.setText("0");
        }
        db.close();
    }

    private void fillSpinner() {

        String[] years = null;

        // Year Spinner
        db.open();
        try {
            years = db.getTotalsYears();
        } finally {
            db.close();
        }

        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, years);
        spinnerYear.setAdapter(adapterYear);

        // Sport Spinner

        ArrayList<String> entries = new ArrayList<String>();

        ArrayAdapter<String> adapterSport = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item,
                entries);

        adapterSport.setNotifyOnChange(true);
        adapterSport.add("All Sports");

        db.open();

        c = db.getSportsForTotalsSpinner();
        long position = 1L;

        sportsList = new HashMap<>();
        sportsList.put(0L, 0);

        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                // loop until it reach the end of the cursor
                do {
                    sportsList.put(position++, c.getInt(c.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT_ROWID)));
                    adapterSport.add(c.getString(c.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT_TITLE)));
                } while (c.moveToNext());
            }
        }

        spinnerSport.setAdapter(adapterSport);

        db.close();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
}