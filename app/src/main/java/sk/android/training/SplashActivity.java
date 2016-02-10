package sk.android.training;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

public class SplashActivity extends BaseActivity {

    private SharedPreferences prefs;
    private String prefName = "UserPref";

    private static final String USERNAME_KEY = "username";
    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String FIRST_NAME_KEY = "first_name";
    private static final String LAST_NAME_KEY = "last_name";
    private static final String BIRTH_KEY = "date_of_birth";
    private static final String HOMETOWN_KEY = "hometown";
    private static final String COUNTRY_KEY = "country";
    private static final String SPORT_KEY = "sport";
    private static final String WEIGHT_KEY = "weight";
    private static final String HEIGHT_KEY = "height";
    private static final String HR_MAX_KEY = "HR_max";
    private static final String HR_BASAL_KEY = "HR_basal";
    private static final String VO2_KEY = "VO2";
    private static final String FAT_KEY = "fat";
    private static final String INFO_KEY = "info";
    private static final String UNITS_KEY = "units";
    private static final String SYNCED_KEY = "synced";

    private String username;

    @Override
    protected int getLayoutResource() {
        return R.layout.welcome_screen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        prefs = getSharedPreferences(prefName, MODE_PRIVATE);

        username = prefs.getString(USERNAME_KEY, "");

        if (!prefs.contains(FIRST_NAME_KEY)) {

            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(EMAIL_KEY, "");
            editor.putString(FIRST_NAME_KEY, "");
            editor.putString(LAST_NAME_KEY, "");
            editor.putString(EMAIL_KEY, "");
            editor.putString(BIRTH_KEY, "");
            editor.putString(HOMETOWN_KEY, "");
            editor.putString(COUNTRY_KEY, "");
            editor.putString(SPORT_KEY, "");
            editor.putString(WEIGHT_KEY, "");
            editor.putString(HEIGHT_KEY, "");
            editor.putString(HR_MAX_KEY, "");
            editor.putString(HR_BASAL_KEY, "");
            editor.putString(VO2_KEY, "");
            editor.putString(FAT_KEY, "");
            editor.putString(INFO_KEY, "");
            editor.putInt(UNITS_KEY, 0);
            editor.putInt(SYNCED_KEY, 0);

            editor.commit();
        }

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 1000) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Intent i;
                    if (username.length() == 0) {
                        i = new Intent(SplashActivity.this, LoginActivity.class);
                    } else {
                        i = new Intent(SplashActivity.this, CalendarViewActivity.class);
                        i.putExtra("type", "usercheck");
                    }
                    proceed(i);
                }
            }
        };
        splashThread.start();
    }

    private void proceed(Intent intent) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent, options.toBundle());
        ActivityCompat.finishAfterTransition(this);
    }
}
