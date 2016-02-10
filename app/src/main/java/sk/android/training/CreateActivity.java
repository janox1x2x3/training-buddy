package sk.android.training;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import sk.android.training.database.TableDbAdapter;
import sk.android.training.utils.TimeDateFormatter;

public class CreateActivity extends AppCompatActivity {
	
	private String _date;
	private String _time;
	
	private TextView dateEdit;	
	private TextView timeEdit;
	private EditText totalTimeEdit;
	private EditText distanceEdit;
	private EditText altitudeEdit;
	private EditText avgHR;
	private EditText maxHR;
	private EditText infoEdit;
	private Spinner s;
	
	private TextView totalTimeUnits;
	private TextView distanceUnits;
	private TextView altitudeUnits;
	
	private TableDbAdapter db;
	
	private TimeDateFormatter format;
	
	private String _day;
	private String _month;
	private String _year;
	private String _hour;
	private String _minute;
	private String _sport = "1";
	private String _totalTime;
	private String _distance;
	private String _altitude;
	private String _info;
	private String _speed;
	private String _pace;
	private String _avgHR;
	private String _maxHR;
	
	private Date today_date;
	private Calendar cal;
	
	private LayoutInflater layoutInflater;

	private Calendar time;
	private String _currentTime;
		
	public boolean FirstLoad = true;
	
	private Cursor sportType;
	private Cursor c;
	
	private int idSportType;
	
	private SharedPreferences prefs;
	private String prefName = "UserPref";
	
	private static final int DATE_DIALOG_ID = 0;
	private static final int TIME_DIALOG_ID = 1;
	
	private static final int ENDURANCE_ID = 1;
	private static final int BASIC_ID = 2;
	private static final int CUSTOM_ID = 3;
	
	private static final String UNITS_KEY = "units";
	private static final String ACTIVE_KEY = "active";
	
	private DatePickerDialog.OnDateSetListener _DateSetListener =
            new DatePickerDialog.OnDateSetListener() {
 
                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                	
                	_day = Integer.toString(dayOfMonth);
                	_month = Integer.toString(monthOfYear + 1);
                	_year = Integer.toString(year);
                    
                    _date = (dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                    
                    cal.set(year, monthOfYear, dayOfMonth);
                    today_date.setTime(cal.getTimeInMillis());
                    dateEdit.setText(DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(today_date));
                }
            };
            
    private TimePickerDialog.OnTimeSetListener _TimeSetListener = 
    		new TimePickerDialog.OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					
					_hour = Integer.toString(hourOfDay);
					_minute = Integer.toString(minute);
					
					_currentTime = (format.formatTime(hourOfDay + ":" + minute, getApplicationContext()));
					
					timeEdit.setText(_currentTime);
				}
			};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	    DisplayMetrics metrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    if (metrics.heightPixels < 500 && metrics.widthPixels < 400){
	    	getSupportActionBar().hide();
	    } else {
	    	getSupportActionBar().setDisplayUseLogoEnabled(true);
		    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		    getSupportActionBar().setDisplayShowTitleEnabled(false);
	    } 
		
		setContentView(R.layout.activity_add);
		
		prefs = getSharedPreferences(prefName, MODE_PRIVATE);
		
		setResult(1);
		
		cal = Calendar.getInstance();
		today_date = new Date();
		
		format = new TimeDateFormatter();
		db = new  TableDbAdapter(this);
		db.open();
				
		fillSpinner();
		setTimeDate();		
				
		dateEdit = (TextView)findViewById(R.id.dateEdit);
		dateEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
		dateEdit.setText(DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(today_date));		
		
		timeEdit = (TextView) findViewById(R.id.timeEdit);
		timeEdit.setText(_currentTime);
		timeEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID);
			}
		});
		
		layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		
		Button addActivity = (Button) findViewById(R.id.submitBtn);
		addActivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!totalTimeEdit.getText().toString().equals("")) {
					getValues();
					setResult(0);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "You have to set at least training duration!", Toast.LENGTH_SHORT).show();
				}				
			}
		});
		s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
			    /* if(FirstLoad){
		                FirstLoad = false;
		                return;                         
		        }*/
			    _sport = Long.toString(s.getSelectedItemId());
			    sportType = db.getSportType(_sport);
			    idSportType  = sportType.getInt(0);
			    
			    LinearLayout detailsLayout = (LinearLayout)findViewById(R.id.activityDetails);
			    
			    if(idSportType == ENDURANCE_ID) {
			    	detailsLayout.removeAllViews();
			    	
			    	View activityView = layoutInflater.inflate(R.layout.type_endurance_activity, null);
			    	detailsLayout.addView(activityView);	
			    	
			    	totalTimeEdit = (EditText)findViewById(R.id.totalTimeEdit);
			    	distanceEdit = (EditText)findViewById(R.id.DistanceEdit);
			    	altitudeEdit = (EditText)findViewById(R.id.altitudeEdit);
			    	infoEdit = (EditText)findViewById(R.id.detailsEdit);
			    	avgHR = (EditText)findViewById(R.id.avgHREdit);
			    	maxHR = (EditText)findViewById(R.id.maxHREdit);
			    	
			    	distanceUnits = (TextView)findViewById(R.id.DistanceUnits);
			    	altitudeUnits = (TextView)findViewById(R.id.altitudeUnits);
			    	
			    	if(prefs.getInt(UNITS_KEY, 0) != 0) {
			    		distanceUnits.setText("mi");
			    		altitudeUnits.setText("yd");
			    	} else {
			    		distanceUnits.setText("km");
			    		altitudeUnits.setText("m");
			    	}
			    	
			    }

			    if(idSportType == BASIC_ID) {
			    	detailsLayout.removeAllViews();
			    	
			    	View activityView = layoutInflater.inflate(R.layout.type_basic_activity, null);
			    	detailsLayout.addView(activityView);		
			    	
			    	totalTimeEdit = (EditText)findViewById(R.id.totalTimeEdit);
			    	infoEdit = (EditText)findViewById(R.id.detailsEdit);
			    }

			    if(idSportType == CUSTOM_ID) {
			    	detailsLayout.removeAllViews();
			    	
			    	View activityView = layoutInflater.inflate(R.layout.type_custom_activity, null);
			    	detailsLayout.addView(activityView);	
			    	
			    	totalTimeEdit = (EditText)findViewById(R.id.totalTimeEdit);
			    	infoEdit = (EditText)findViewById(R.id.detailsEdit);
			    }
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				return;
			}
		});
}
	
	private void getValues() {
		// TODO Auto-generated method stub

		_date = format.formatDateDatabase(_date);;
		_time = format.formatTimeDatabase(_currentTime, this);
		String insert = null;
		String  username = prefs.getString("username", "");
		
    	Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	sdf.setTimeZone(TimeZone.getTimeZone("EST5EDT"));
    	String now = sdf.format(date);
		
		if(idSportType == ENDURANCE_ID) {
			_altitude = altitudeEdit.getText().toString();
			_info = infoEdit.getText().toString();
			_avgHR = avgHR.getText().toString();
			_maxHR = maxHR.getText().toString();			
			
			try {
				DecimalFormat df = new DecimalFormat("#.00");
				_totalTime = totalTimeEdit.getText().toString();
				_distance = distanceEdit.getText().toString();
				_speed = df.format(((float) (Float.parseFloat(_distance)/((float) Integer.parseInt(_totalTime)/60))));
				_pace = String.valueOf(Math.round((Integer.parseInt(_totalTime) * 60) / Float.parseFloat(_distance)));
			} catch(NumberFormatException e){
				
				_speed = ("null");
				_pace = ("null");
			}
			
			
			
			db.createActivity(_time, _date, _totalTime, _sport, _info, _distance, _altitude, _speed, _pace, _avgHR, _maxHR, now);
			insert = "INSERT INTO activity(time, date, total_time, id_sport, info, distance, altitude, speed, pace, avgHR, maxHR, modified, created, username) " +
					"VALUES('" + _time + "','" + _date + "','" + _totalTime + "','" + _sport + "','" + _info + "','" + _distance + "','" + _altitude + "','" + _speed + 
							"','" + _pace + "','" + _avgHR + "','" + _maxHR + "','" + now + "','" + now + "','" + username + "');";
						
		}
		
		if(idSportType == BASIC_ID) {

			_info = infoEdit.getText().toString();
			_totalTime = totalTimeEdit.getText().toString();

			db.createActivity(_time, _date, _totalTime, _sport, _info, null, null, null, null, null, null, now);
			insert = "INSERT INTO activity(time, date, total_time, id_sport, info, modified, created, username) " +
					"VALUES('" + _time + "','" + _date + "','" + _totalTime + "','" + _sport + "','" + _info + "','" + now + "','" + now + "','" + username + "');";
		}

		db.insertSyncQuery(insert);
		
		prefs = getSharedPreferences(prefName, MODE_PRIVATE);			
		SharedPreferences.Editor editor = prefs.edit();    		
        editor.putString(ACTIVE_KEY, now);	    	  
        editor.commit();
	}

	private void fillSpinner(){
		 
		c = db.getAllSports();
		startManagingCursor(c);
		
		String[] from = new String[]{TableDbAdapter.KEY_SPORT_TITLE};
		int[] to = new int[]{android.R.id.text1};
		
		SimpleCursorAdapter adapter =
		  new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
		
		s = (Spinner) findViewById(R.id.sportSelectSpinner);
		s.setAdapter(adapter);
		}
	
	private void setTimeDate() {
		
		Bundle extras = getIntent().getExtras();
		_day = extras.getString("day");
		_month = extras.getString("month");
		_year = extras.getString("year");
		
        _date = (_day + "/" + _month + "/" + _year);
		
        cal.set(Integer.parseInt(_year), Integer.parseInt(_month) - 1, Integer.parseInt(_day));
        today_date.setTime(cal.getTimeInMillis());
		
		time = Calendar.getInstance();	
		
		_hour = Integer.toString(time.get(Calendar.HOUR_OF_DAY));
		_minute = Integer.toString(time.get(Calendar.MINUTE));
		_currentTime = format.formatTime((time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE)), this);
	}
	
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this, 
                        _DateSetListener,
                        Integer.parseInt(_year), Integer.parseInt(_month)-1, Integer.parseInt(_day));
        case TIME_DIALOG_ID:
            return new TimePickerDialog(this, 
                        _TimeSetListener,
                        time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(this));
        }
        return null;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
		}		
		return super.onOptionsItemSelected(item);
	}
	
}
		 
	

