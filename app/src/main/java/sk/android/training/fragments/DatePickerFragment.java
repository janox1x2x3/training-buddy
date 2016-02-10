package sk.android.training.fragments;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sk.android.training.R;
import sk.android.training.database.TableDbAdapter;
import sk.android.training.utils.TimeDateFormatter;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {
	
	private SharedPreferences prefs;
	private String prefName = "UserPref";
	private static final String BIRTH_KEY = "date_of_birth";	

	private static final int AGE_SET_DIALOG_ID = 12;
	
	private TimeDateFormatter format;
	private int day;
	private int month;
	private int year;
	
	private TextView title;
	private int id;
	
	private Date dateObj;
	
	private Calendar c;
	private TableDbAdapter db;
	
	public static DatePickerFragment newInstance(int id) {
		
	   DatePickerFragment f = new DatePickerFragment();
		
       Bundle args = new Bundle();
       args.putInt("id", id);
       f.setArguments(args);
		
       return f;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		db = new TableDbAdapter(getActivity());
		id = getArguments().getInt("id");
		
		if(id == AGE_SET_DIALOG_ID) {
			prefs = getActivity().getSharedPreferences(prefName, getActivity().MODE_PRIVATE);
				
			try {
				day = Integer.parseInt(prefs.getString(BIRTH_KEY, "").substring(0, 2));
				month = (Integer.parseInt(prefs.getString(BIRTH_KEY, "").substring(3, 5))-1);
				year = Integer.parseInt(prefs.getString(BIRTH_KEY, "").substring(6, 10));
				} catch (NumberFormatException e) {
					day = 1;
					month = 0;
					year = 1980;
				}
			
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		return null;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		
		if(id == AGE_SET_DIALOG_ID) {			
			String _date = (dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
			SharedPreferences.Editor editor = prefs.edit();
			
	        editor.putString(BIRTH_KEY, formatDate(_date));
	        editor.commit();    
	        
	        String update = "UPDATE USER SET date = '" + prefs.getString(BIRTH_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
			db.open();
			db.insertSyncQuery(update);
			db.close();
			
			TextView age = (TextView)getActivity().findViewById(R.id.ageEdit);
			age.setText(getAge(year, monthOfYear, dayOfMonth) + " years");
			age.setTextColor(Color.WHITE);
		}

   	}	
	
	private String formatDate(String date) {
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy"); 
		try {
			dateObj = curFormater.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
        
        return (curFormater.format(dateObj));
	}
	
	private String getAge(int year, int month, int day){
	    Calendar dob = Calendar.getInstance();
	    Calendar today = Calendar.getInstance();

	    dob.set(year, month, day); 

	    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

	    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
	        age--; 
	    }
	
	    String ageS = Integer.toString(age);

	    return ageS;  
	}
}
