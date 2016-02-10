package sk.android.training.database;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class TableDbAdapter extends DbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TIME = "time";
    public static final String KEY_DATE = "date";
    public static final String KEY_TOTAL_TIME = "total_time";
    public static final String KEY_SPORT = "id_sport";
    public static final String KEY_INFO = "info";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_ALTITUDE = "altitude";
    public static final String KEY_SPEED = "speed";
    public static final String KEY_PACE = "pace";
    public static final String KEY_AVGHR = "avgHR";
    public static final String KEY_MAXHR = "maxHR";
    public static final String KEY_MODIFIED = "modified";
    public static final String KEY_CREATED = "created";

    private static final String DATABASE_TABLE_ACTIVITY = "activity";

    public static final String KEY_SPORT_ROWID = "_id";
    public static final String KEY_SPORT_TITLE = "sport_title";
    public static final String KEY_SPORT_TYPE = "sport_type";

    private static final String DATABASE_TABLE_SPORT = "sport";

    public static final String KEY_SPORT_TYPE_ROWID = "_id";
    public static final String KEY_SPORT_TYPE_TITLE = "sport_type";

    private static final String DATABASE_TABLE_SPORT_TYPE = "sport_type";

    private static final String DATABASE_TABLE_QUERY = "query";
    public static final String KEY_QUERY_ID = "_id";
    public static final String KEY_QUERY = "query";


    public TableDbAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    public long createActivity(String time, String date, String total_time,
                               String sport, String info, String distance, String altitude, String speed, String pace, String avgHR, String maxHR, String now) {
        ContentValues args = new ContentValues();

        args.put(KEY_TIME, time);
        args.put(KEY_DATE, Integer.parseInt(date));
        args.put(KEY_TOTAL_TIME, total_time);
        args.put(KEY_SPORT, sport);
        args.put(KEY_INFO, info);
        args.put(KEY_DISTANCE, distance);
        args.put(KEY_ALTITUDE, altitude);
        args.put(KEY_SPEED, speed);
        args.put(KEY_PACE, pace);
        args.put(KEY_AVGHR, avgHR);
        args.put(KEY_MAXHR, maxHR);
        args.put(KEY_MODIFIED, now);
        args.put(KEY_CREATED, now);

        return mDb.insert(DATABASE_TABLE_ACTIVITY, null, args);
    }

    public long createSyncActivity(String time, String date, String total_time,
                                   String sport, String info, String distance, String altitude, String speed, String pace, String avgHR, String maxHR, String modified, String created) {
        ContentValues args = new ContentValues();

        args.put(KEY_TIME, time);
        args.put(KEY_DATE, Integer.parseInt(date));
        args.put(KEY_TOTAL_TIME, total_time);
        args.put(KEY_SPORT, sport);
        args.put(KEY_INFO, info);
        args.put(KEY_DISTANCE, distance);
        args.put(KEY_ALTITUDE, altitude);
        args.put(KEY_SPEED, speed);
        args.put(KEY_PACE, pace);
        args.put(KEY_AVGHR, avgHR);
        args.put(KEY_MAXHR, maxHR);
        args.put(KEY_MODIFIED, modified);
        args.put(KEY_CREATED, created);

        return mDb.insert(DATABASE_TABLE_ACTIVITY, null, args);
    }

    public void deleteActivity(long id) {

        mDb.delete(DATABASE_TABLE_ACTIVITY, KEY_CREATED + " = '" + id + "';", null);
    }

    public Cursor getAllActivitiesSync() {
        Cursor activities = mDb.rawQuery("SELECT * FROM activity", null);
        return activities;
    }

    public Cursor getAllActivitiesSync(int lastSync) {
        Cursor activities = mDb.rawQuery("SELECT * FROM activity WHERE modified > " + lastSync, null);
        return activities;
    }

    public Cursor getSyncQuery() {
        return mDb.rawQuery("SELECT query FROM query;", null);
    }

    public void truncateQuery() {
        mDb.delete(DATABASE_TABLE_QUERY, null, null);
    }

    public void truncateDatabase() {
        mDb.delete(DATABASE_TABLE_QUERY, null, null);
        mDb.delete(DATABASE_TABLE_ACTIVITY, null, null);

    }

    public long insertSyncQuery(String query) {
        ContentValues args = new ContentValues();
        System.out.println(query);
        args.put(KEY_QUERY, query);
        return mDb.insert(DATABASE_TABLE_QUERY, null, args);

    }

    public Cursor getAllActivities() {
        return mDb.query(DATABASE_TABLE_ACTIVITY, new String[]{KEY_ROWID, KEY_TIME,
                KEY_DATE}, null, null, null, null, null);
    }

    public void updateActivity(long id, String time, String total_time, String sport,
                               String info, String distance, String altitude, String speed, String pace, String avgHR, String maxHR, String now) {
        ContentValues args = new ContentValues();

        args.put(KEY_TIME, time);
        args.put(KEY_TOTAL_TIME, total_time);
        args.put(KEY_SPORT, sport);
        args.put(KEY_INFO, info);
        args.put(KEY_DISTANCE, distance);
        args.put(KEY_ALTITUDE, altitude);
        args.put(KEY_SPEED, speed);
        args.put(KEY_PACE, pace);
        args.put(KEY_AVGHR, avgHR);
        args.put(KEY_MAXHR, maxHR);
        args.put(KEY_MODIFIED, now);

        mDb.update(DATABASE_TABLE_ACTIVITY, args, KEY_CREATED + "='" + id + "';", null);

    }

    public void updateRegisterTask(ContentValues args, int id) {

        mDb.update("activity", args, "_id ='" + id + "';", null);
    }

    public String[] getTotalsYears() {

        List<String> years = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String currentYear = String.valueOf(calendar.get(Calendar.YEAR));
        years.add(currentYear);

        Cursor mCursor = mDb.rawQuery("SELECT date FROM activity order by date DESC;", null);
        try {
            if (mCursor != null) {
                mCursor.moveToFirst();
                while (mCursor.moveToNext()) {
                    String year = mCursor.getString(mCursor.getColumnIndex(KEY_DATE)).substring(0, 4);
                    if (!years.contains(year)) {
                        years.add(year);
                    }
                }
            }
        } finally {
            mCursor.close();
        }

        return years.toArray(new String[years.size()]);
    }

    public Cursor getActivity(int date) throws SQLException {
        Cursor mCursor =
                mDb.rawQuery("SELECT a.*, s.sport_title, s.sport_type " +
                        "FROM activity a, sport s WHERE " +
                        "a.date = " + date + " AND " + " a.id_sport = s._id ORDER BY a.time ASC", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getActivityMonthTotals(int date) throws SQLException {
        Cursor mCursor =
                mDb.rawQuery("SELECT SUM(total_time) AS TOTAL_TIME, COUNT(_id) AS TOTAL_UNITS, " +
                        "COUNT(DISTINCT date) AS TOTAL_DAYS, SUM(distance) as TOTAL_DISTANCE, SUM(altitude) as TOTAL_ALT FROM activity " +
                        "WHERE (" + KEY_DATE + " >= " + date + ")" + " AND " + "(" + KEY_DATE + " < " + (date + 100) + ")", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getActivityWeekTotals(int start, int end) throws SQLException {
        Cursor mCursor =
                mDb.rawQuery("SELECT SUM(total_time) AS TOTAL_TIME, COUNT(_id) AS TOTAL_UNITS, " +
                        "COUNT(DISTINCT date) AS TOTAL_DAYS, SUM(distance) as TOTAL_DISTANCE, SUM(altitude) as TOTAL_ALT FROM activity " +
                        "WHERE (" + KEY_DATE + " >= " + start + ")" + " AND " + "(" + KEY_DATE + " <= " + end + ")", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getActivityMonthTotals(int date, int sport) throws SQLException {
        Cursor mCursor =
                mDb.rawQuery("SELECT SUM(total_time) AS TOTAL_TIME, COUNT(_id) AS TOTAL_UNITS, " +
                        "COUNT(DISTINCT date) AS TOTAL_DAYS, SUM(distance) as TOTAL_DISTANCE, SUM(altitude) as TOTAL_ALT FROM activity " +
                        "WHERE (" + KEY_DATE + " >= " + date + ")" + " AND " + "(" + KEY_DATE + " < " + (date + 100) + ")" + " AND " + "(" + KEY_SPORT + " = " + sport + ")", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getActivityYearTotals(int date) throws SQLException {
        Cursor mCursor =
                mDb.rawQuery("SELECT SUM(total_time) AS TOTAL_TIME, COUNT(_id) AS TOTAL_UNITS, " +
                        "COUNT(DISTINCT date) AS TOTAL_DAYS, SUM(distance) as TOTAL_DISTANCE, SUM(altitude) as TOTAL_ALT FROM activity " +
                        "WHERE (" + KEY_DATE + " >= " + date + ")" + " AND " + "(" + KEY_DATE + " < " + (date + 10000) + ")", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getActivityYearTotals(int date, int sport) throws SQLException {
        Cursor mCursor =
                mDb.rawQuery("SELECT SUM(total_time) AS TOTAL_TIME, COUNT(_id) AS TOTAL_UNITS, " +
                        "COUNT(DISTINCT date) AS TOTAL_DAYS, SUM(distance) as TOTAL_DISTANCE, SUM(altitude) as TOTAL_ALT FROM activity " +
                        "WHERE (" + KEY_DATE + " >= " + date + ")" + " AND " + "(" + KEY_DATE + " < " + (date + 10000) + ")" + " AND " + "(" + KEY_SPORT + " = " + sport + ")", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getMonthSessions(int date) throws SQLException {
        Cursor mCursor =
                mDb.rawQuery("SELECT date, id_sport FROM activity " +
                        "WHERE (" + KEY_DATE + " >= " + date + ")" + " AND " + "(" + KEY_DATE + " < " + (date + 100) + ")", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public Cursor getActivityDayTotals(int date) {
        Cursor mCursor =
                mDb.rawQuery("SELECT SUM(total_time) AS TOTAL_TIME, COUNT(_id) AS TOTAL_UNITS, COUNT(DISTINCT date) AS TOTAL_DAYS FROM activity " +
                        "WHERE (" + KEY_DATE + " = " + date + ")", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long createSport(String sport) {
        ContentValues args = new ContentValues();

        args.put(KEY_SPORT_TITLE, sport);
        return mDb.insert(DATABASE_TABLE_SPORT, null, args);
    }

    public Cursor getAllSports() {
        return mDb.query(DATABASE_TABLE_SPORT, new String[]{KEY_ROWID, KEY_SPORT_TITLE}, null, null, null, null, null);
    }

    public Cursor getSportsForTotalsSpinner() {
        return mDb.rawQuery("SELECT DISTINCT s._id, s.sport_title from sport s, activity a where a.id_sport = s._id", null);
    }

    public Cursor getSport(int id) {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_SPORT, new String[]{KEY_SPORT_TITLE}, KEY_SPORT_ROWID + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getSport(String title) {
        Cursor mCursor =
                mDb.rawQuery("SELECT _id, sport_title FROM sport WHERE sport_title = '" + title + "';", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getAllSportTypes() {
        return mDb.query(DATABASE_TABLE_SPORT_TYPE, new String[]{KEY_SPORT_TYPE_ROWID, KEY_SPORT_TYPE_TITLE}, null, null, null, null, null);
    }

    public Cursor getSportType(String id) {
        Cursor mCursor =
                mDb.rawQuery("SELECT sport_type FROM sport WHERE _id = '" + id + "';", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
