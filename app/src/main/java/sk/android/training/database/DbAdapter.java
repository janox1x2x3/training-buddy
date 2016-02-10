package sk.android.training.database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import sk.android.training.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

public abstract class DbAdapter {

    protected static final String TAG = "TrainOseDbAdapter";
    protected DatabaseHelper mDbHelper;
    protected SQLiteDatabase mDb;

    protected static final String TABLE_CREATE_ACTIVITY =
        "create table activity (_id integer primary key autoincrement, " +
        "time text not null, " +
        "date integer not null, " +
        "total_time integer not null, " +
        "id_sport text not null, " +
        "info text not null, " +
        "distance real, " +
        "altitude real, " +
        "speed real, " +
        "pace integer, " +
        "avgHR integer, " +
        "maxHR integer, " +
        "modified integer, " +
        "created integer);";
    protected static final String TABLE_CREATE_QUERY =
        "create table query (_id integer primary key autoincrement, " +
        "query text)";
    protected static final String TABLE_CREATE_SPORT =    
        "create table sport (_id integer primary key autoincrement, " +
        "sport_title text not null, " +
        "sport_type integer);"; 
    protected static final String TABLE_CREATE_SPORT_TYPE =    
            "create table sport_type (_id integer primary key autoincrement, " +
            "sport_type text not null);"; 

    protected static final String DATABASE_NAME = "training_log";
    protected static final int DATABASE_VERSION = 2;

    protected final Context mCtx;

    protected static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_ACTIVITY);
            db.execSQL(TABLE_CREATE_SPORT);
            db.execSQL(TABLE_CREATE_SPORT_TYPE);
            db.execSQL(TABLE_CREATE_QUERY);
            db.execSQL("INSERT INTO sport_type(sport_type) VALUES ('Endurance')");
            db.execSQL("INSERT INTO sport_type(sport_type) VALUES ('Basic')");
            db.execSQL("INSERT INTO sport_type(sport_type) VALUES ('Custom')");
            db.execSQL("INSERT INTO SPORT(sport_title, sport_type) VALUES ('Running', 1)");
            db.execSQL("INSERT INTO SPORT(sport_title, sport_type) VALUES ('Cycling', 1)");
            db.execSQL("INSERT INTO SPORT(sport_title, sport_type) VALUES ('X-country Ski', 1)");
            db.execSQL("INSERT INTO SPORT(sport_title, sport_type) VALUES ('Swimming', 1)");
            db.execSQL("INSERT INTO SPORT(sport_title, sport_type) VALUES ('Inline Skating', 1)");
            db.execSQL("INSERT INTO SPORT(sport_title, sport_type) VALUES ('Walking', 1)");
            db.execSQL("INSERT INTO sport(sport_title, sport_type) VALUES ('Canoeing', 2)");
            db.execSQL("INSERT INTO sport(sport_title, sport_type) VALUES ('Football', 2)");
            db.execSQL("INSERT INTO sport(sport_title, sport_type) VALUES ('Ice Hockey', 2)");
            db.execSQL("INSERT INTO sport(sport_title, sport_type) VALUES ('Volleyball', 2)");
            db.execSQL("INSERT INTO sport(sport_title, sport_type) VALUES ('Basketball', 2)");
            db.execSQL("INSERT INTO sport(sport_title, sport_type) VALUES ('Tennis', 2)");
            db.execSQL("INSERT INTO sport(sport_title, sport_type) VALUES ('Skiing', 2)");
            db.execSQL("INSERT INTO sport(sport_title, sport_type) VALUES ('Athletics', 2)");
            db.execSQL("INSERT INTO SPORT(sport_title, sport_type) VALUES ('Weight Training', 2)");
            db.execSQL("INSERT INTO SPORT(sport_title, sport_type) VALUES ('Other', 2)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	Date now = new Date();
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
        	sdf.setTimeZone(TimeZone.getTimeZone("EST5EDT"));

            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion);
            db.execSQL("ALTER TABLE activity ADD modified integer");
            db.execSQL("ALTER TABLE activity ADD created integer");
            db.execSQL(TABLE_CREATE_QUERY);
            db.execSQL("UPDATE activity SET modified = " + sdf.format(now));
                  
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open or create the routes database.
     * 
     * @return this
     * @throws SQLException if the database could be neither opened or created
     */
    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

}
