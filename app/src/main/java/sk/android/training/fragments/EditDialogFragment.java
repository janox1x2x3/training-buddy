package sk.android.training.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import sk.android.training.DayLogActivity;
import sk.android.training.R;
import sk.android.training.database.TableDbAdapter;
import sk.android.training.utils.TimeDateFormatter;

public class EditDialogFragment extends DialogFragment {
	
	private static final int TIME_EDIT_DIALOG = 1;
	
	private TextView timeEditDialog;
	private EditText totalTimeEditDialog;
	private EditText distanceEditDialog;
	private EditText altitudeEditDialog;
	private EditText avgHREditDialog;
	private EditText maxHREditDialog;
	private EditText infoEditDialog;
	private Spinner s;
	
	private String _time;
	private int idSportType;
	
	private SharedPreferences prefs;
	private String prefName = "UserPref";
	
	private String _sport = "1";
	private String _totalTime;
	private String _distance;
	private String _altitude;
	private String _info;
	private String _speed;
	private String _pace;
	private String _avgHR;
	private String _maxHR;	

	private static final int ENDURANCE_ID = 1;
	private static final int BASIC_ID = 2;
	
	private static final String UNITS_KEY = "units";
	private static final String ACTIVE_KEY = "active";
	
	private TableDbAdapter db;
	
	private Calendar cal;
	
	private Cursor c;
	
	private Dialog dialog;
	
	private LayoutInflater layoutInflater;
	
	public static EditDialogFragment newInstance(long id, String editDialogTime, String editDialogTotalTime, String editDialogDistance,
			String editDialogInfo, String editDialogSport, String editDialogAlt, String editDialogAvgHR, String editDialogMaxHR, int day, int month, int year) {
		 EditDialogFragment f = new EditDialogFragment();
		
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putString("Time", editDialogTime);
        args.putString("TotalTime", editDialogTotalTime);
        args.putString("Distance", editDialogDistance);
        args.putString("Info", editDialogInfo);
        args.putString("Sport", editDialogSport);
        args.putString("Alt", editDialogAlt);
        args.putString("AvgHR", editDialogAvgHR);
        args.putString("MaxHR", editDialogMaxHR);
        args.putInt("day", day);
        args.putInt("month", month);
        args.putInt("year", year);
        f.setArguments(args);
		
		return f;
	}

	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dialog = new Dialog(getActivity());
		dialog.setTitle("Edit Activity");
		return dialog;	

	} 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		final long id = getArguments().getLong("id");
		final String editTime = getArguments().getString("Time");
		final String editTotalTime = getArguments().getString("TotalTime");
		final String editDistance = getArguments().getString("Distance");
		final String editInfo = getArguments().getString("Info");
		final String editSport = getArguments().getString("Sport");
		final String editAltitude = getArguments().getString("Alt");
		final String editAvgHR = getArguments().getString("AvgHR");
		final String editMaxHR = getArguments().getString("MaxHR");
		final int day = getArguments().getInt("day");
		final int month = getArguments().getInt("month");
		final int year = getArguments().getInt("year");
				
		View v = inflater.inflate(R.layout.activity_edit_dialog, container);
		
		prefs = getActivity().getSharedPreferences(prefName, getActivity().MODE_PRIVATE);
		
		
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});				
		
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		Button submitBtn = (Button)v.findViewById(R.id.submitBtn) ;
		
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeDateFormatter format = new TimeDateFormatter();
				
		    	Date date = new Date();
		    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		    	sdf.setTimeZone(TimeZone.getTimeZone("EST5EDT"));
		    	String now = sdf.format(date);
		    	String update = null;
				
				if(idSportType == ENDURANCE_ID) {
					
					_time = format.formatTimeDatabase(timeEditDialog.getText().toString(), getActivity());
					_altitude = altitudeEditDialog.getText().toString();
					_info = infoEditDialog.getText().toString();
					_avgHR = avgHREditDialog.getText().toString();
					_maxHR = maxHREditDialog.getText().toString();
					_sport = Integer.toString(s.getSelectedItemPosition()+1);
					
					try {
						DecimalFormat df = new DecimalFormat("#.00");
						_totalTime = totalTimeEditDialog.getText().toString();
						_distance = distanceEditDialog.getText().toString();
						_speed = df.format(((float) (Float.parseFloat(_distance)/((float) Integer.parseInt(_totalTime)/60))));
						_pace = String.valueOf(Math.round((Integer.parseInt(_totalTime) * 60) / Float.parseFloat(_distance)));
					} catch(NumberFormatException e){
						
						_speed = ("null");
						_pace = ("null");
					}
					update = "UPDATE activity SET time = '" + _time + "', total_time = '" + _totalTime + "', id_sport = '" + _sport + "', info = '" + _info + 
							"', distance = '" + _distance + "', altitude = '" + _altitude + "', speed = '" + _speed + "', pace = '" + _pace + 
							"', avgHR = '" + _avgHR + "', maxHR = '" + _maxHR + "', modified = '" + now + "' WHERE created = '" + id + "' AND " +
									"username = '" + prefs.getString("username", "") + "';";
					db.updateActivity(id, _time, _totalTime, _sport, _info, _distance, _altitude, _speed, _pace, _avgHR, _maxHR, now);
					
				}
				
				if(idSportType == BASIC_ID) {
					
					_time = format.formatTimeDatabase(timeEditDialog.getText().toString(), getActivity());
					_info = infoEditDialog.getText().toString();
					_sport = Integer.toString(s.getSelectedItemPosition()+1);
					_totalTime = totalTimeEditDialog.getText().toString();
					update = "UPDATE activity SET time = '" + _time + "', total_time = '" + _totalTime + "', id_sport = '" + _sport + "', info = '" + _info + 
							"', distance = '', altitude = '', speed = '', pace = '', avgHR = '', maxHR = '', modified = '" + now + "' WHERE created = '" + id + "' AND " +
									"username = '" + prefs.getString("username", "") + "';";
					db.updateActivity(id, _time, _totalTime, _sport, _info, null, null, null, null, null, null, now);
				}
				db.insertSyncQuery(update);
				
				dismiss();				
				Toast.makeText(getActivity(), "Activity Updated!", Toast.LENGTH_SHORT).show();
				
	    		prefs = getActivity().getSharedPreferences(prefName, getActivity().MODE_PRIVATE);			
	    		SharedPreferences.Editor editor = prefs.edit();    		
		        editor.putString(ACTIVE_KEY, now);	    	  
		        editor.commit();
				
				Intent i = new Intent(getActivity(), DayLogActivity.class);
				i.putExtra("day", String.valueOf(day));
	        	i.putExtra("month", month);
	        	i.putExtra("year", String.valueOf(year));
	        	i.putExtra("tabSelected", getActivity().getActionBar().getSelectedNavigationIndex());
	        	
	        	startActivityForResult(i, 0);
	        	getActivity().overridePendingTransition(0, 0);
	        	getActivity().finish();
			}
		});

		
		/////////////////////////////////////////////////////////////////
		///////////////     	implementation 			  ///////////////
		/////////////////////////////////////////////////////////////////
		
		db = new TableDbAdapter(getActivity());
		db.open();
	    layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		
		timeEditDialog = (TextView)v.findViewById(R.id.timeEdit);
		timeEditDialog.setText(editTime);

		timeEditDialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogFragment newFragment = TimePickerFragment.newInstance(TIME_EDIT_DIALOG, editTime);
				newFragment.show(getActivity().getSupportFragmentManager(), "TimeDialog");
			}
		});
		
		final LinearLayout lin = (LinearLayout)v.findViewById(R.id.activityDetailsDialog);		
		
		c = db.getAllSports();
		getActivity().startManagingCursor(c);
		
		String[] from = new String[]{TableDbAdapter.KEY_SPORT_TITLE};
		int[] to = new int[]{android.R.id.text1};
		
		SimpleCursorAdapter adapter =
		  new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, c, from, to );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
		
		s = (Spinner)v.findViewById(R.id.sportSelectSpinner);
		s.setAdapter(adapter);	
		
		Cursor cursor = (Cursor) db.getSport(editSport);
		s.setSelection(cursor.getInt(0)-1);
		
		s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
			    
			    lin.removeAllViews();
			    _sport = Long.toString(s.getSelectedItemId());
			    Cursor sportType = db.getSportType(_sport);
			    idSportType  = sportType.getInt(0);
			    					
				if(idSportType == ENDURANCE_ID) {
					
					View layout = layoutInflater.inflate(R.layout.type_endurance_activity, null);
					lin.addView(layout);		
					
					totalTimeEditDialog = (EditText)layout.findViewById(R.id.totalTimeEdit);
					infoEditDialog = (EditText)layout.findViewById(R.id.detailsEdit);
					
					if(!editTotalTime.equals("N/A")) {
						totalTimeEditDialog.setText(editTotalTime.replace(" min", ""));
					}
					infoEditDialog.setText(editInfo);
					
					try {
						distanceEditDialog = (EditText)layout.findViewById(R.id.DistanceEdit);
						altitudeEditDialog = (EditText)layout.findViewById(R.id.altitudeEdit);
						avgHREditDialog = (EditText)layout.findViewById(R.id.avgHREdit);
						maxHREditDialog = (EditText)layout.findViewById(R.id.maxHREdit);
						
						if(!editDistance.equals("N/A")) {
							if(prefs.getInt(UNITS_KEY, 0) == 0){
								distanceEditDialog.setText(editDistance.replace(" km", ""));
							} else {
								distanceEditDialog.setText(editDistance.replace(" mi", ""));

							}
						}
						if(!editAltitude.equals("N/A")) {
							if(prefs.getInt(UNITS_KEY, 0) == 0){
								altitudeEditDialog.setText(editAltitude.replace(" m", ""));		
							} else {
								altitudeEditDialog.setText(editAltitude.replace(" yd", ""));		

							}
						}
						if(!editAvgHR.equals("N/A")) {
							avgHREditDialog.setText(editAvgHR);
						}
						if(!editMaxHR.equals("N/A")) {
							maxHREditDialog.setText(editMaxHR);	
						}
					} catch (NullPointerException e) {
						 
						distanceEditDialog.setText("");
						altitudeEditDialog.setText("");
						avgHREditDialog.setText("");
						maxHREditDialog.setText("");
					}
				}
				
				if(idSportType == BASIC_ID) {
					
					View layout = layoutInflater.inflate(R.layout.type_basic_activity, null);
					lin.addView(layout);
					
					totalTimeEditDialog = (EditText)layout.findViewById(R.id.totalTimeEdit);
					infoEditDialog = (EditText)layout.findViewById(R.id.detailsEdit);
					if(!editTotalTime.equals("N/A")) {
						totalTimeEditDialog.setText(editTotalTime.replace(" min", ""));
					}
					infoEditDialog.setText(editInfo);
				}  
			}  

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				return;
			}
		}); 
		return v;
	}
	
	

}