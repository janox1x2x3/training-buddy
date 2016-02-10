package sk.android.training;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import sk.android.training.adapter.CalendarAdapter;
import sk.android.training.database.TableDbAdapter;
import sk.android.training.fragments.FragmentCalendar;
import sk.android.training.fragments.FragmentDetails;
import sk.android.training.fragments.FragmentTotals;
import sk.android.training.fragments.MyTabsListener;
import sk.android.training.syncTasks.SyncTask;

public class CalendarViewActivity extends AppCompatActivity {

	public Calendar month;
	public CalendarAdapter adapter;

	private SharedPreferences prefs;
	private String prefName = "UserPref";

	private ActionBar actionBar;

	private static final String KEY_USERNAME = "username";
	private static final String ACTIVE_KEY = "active";
	private TableDbAdapter db;

	private static final int DIALOG_LOGOUT = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			sdf.setTimeZone(TimeZone.getTimeZone("EST5EDT"));
			String now = sdf.format(date);

			prefs = getSharedPreferences(prefName, MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(ACTIVE_KEY, now);
			editor.commit();
			db = new TableDbAdapter(getApplicationContext());

			try {
				if (getIntent().getExtras().getString("type")
						.equals("usercheck")) {
					Toast.makeText(
							getBaseContext(),
							"Welcome back, "
									+ prefs.getString(KEY_USERNAME, "") + "!",
							Toast.LENGTH_SHORT).show();
				}
			} catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		setContentView(R.layout.main);

		actionBar = getSupportActionBar();

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		if (metrics.heightPixels < 400 && metrics.widthPixels < 300) {
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
		} else {
			actionBar.setDisplayUseLogoEnabled(true);
		}

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tabCalendar = actionBar.newTab().setText("Calendar");
		ActionBar.Tab tabTotals = actionBar.newTab().setText("Totals");
		ActionBar.Tab tabDetails = actionBar.newTab().setText("User Info");

		Fragment fragmentCalendar = new FragmentCalendar();
		Fragment fragmentTotals = new FragmentTotals();
		Fragment fragmentDetails = new FragmentDetails();

		tabCalendar.setTabListener(new MyTabsListener(fragmentCalendar));
		tabTotals.setTabListener(new MyTabsListener(fragmentTotals));
		tabDetails.setTabListener(new MyTabsListener(fragmentDetails));

		actionBar.addTab(tabCalendar);
		actionBar.addTab(tabTotals);
		actionBar.addTab(tabDetails);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Dialog dialog;

		switch (id) {
		case DIALOG_LOGOUT:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Are you sure you want to log out?")
					.setMessage(
							"Logged as: "
									+ prefs.getString("username", "")
									+ " \n\nWARNING: All unsynchronized data will be lost!")
					.setPositiveButton("Logout",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									getSharedPreferences("UserPref", 0).edit()
											.clear().commit();
									db.open();
									db.truncateDatabase();
									db.close();
									Intent loginIntent = new Intent(
											getApplicationContext(),
											LoginActivity.class);
									startActivity(loginIntent);
									finish();
								}
							})
					.setNeutralButton("Synchronize",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									SyncTask syncTask = new SyncTask(
											CalendarViewActivity.this);
									syncTask.execute();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									dismissDialog(DIALOG_LOGOUT);
								}
							}).create();

		default:
			dialog = null;
		}

		return dialog;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_about:
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
			return true;
		case R.id.menu_synchronize:
			SyncTask syncTask = new SyncTask(CalendarViewActivity.this);
			syncTask.execute();
			return true;
		case R.id.menu_logout:
			showDialog(DIALOG_LOGOUT);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
