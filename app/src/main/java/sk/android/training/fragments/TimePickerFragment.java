package sk.android.training.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sk.android.training.R;
import sk.android.training.utils.TimeDateFormatter;


public class TimePickerFragment extends DialogFragment implements OnTimeSetListener{
	
	private static final int TIME_EDIT_DIALOG = 1;
	private int id;
	private Date dateObj;
	private Calendar cal;
	private String _time;
	private TextView editTime;
	private TimeDateFormatter format;
	
	public static TimePickerFragment newInstance(int id, String time) {
		
		   TimePickerFragment f = new TimePickerFragment();
			
	       Bundle args = new Bundle();
	       args.putInt("id", id);
	       args.putString("time", time);
	       f.setArguments(args);
	   			
	       return f;
		}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		id = getArguments().getInt("id");
		
		format = new TimeDateFormatter();
		
		if(id == TIME_EDIT_DIALOG) {
			
			SimpleDateFormat curFormater;
			editTime = (TextView)getActivity().getSupportFragmentManager().findFragmentByTag("EditDialog").getView().findViewById(R.id.timeEdit);
			
        	_time = editTime.getText().toString();
        	if(android.text.format.DateFormat.is24HourFormat(getActivity()) == true) {
        		curFormater = new SimpleDateFormat("HH:mm"); 
        	} else {
        		curFormater = new SimpleDateFormat("hh:mm aa");
        	}
        	
        	try {
				dateObj = curFormater.parse(_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}       	      	
        	cal = Calendar.getInstance();
        	cal.setTimeInMillis(dateObj.getTime());
			
		    return new TimePickerDialog(getActivity(), 
                     this,
                     cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getActivity()));
		}
		return null;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		editTime.setText(format.formatTimeToDayLog(String.format("%02d", hourOfDay) + "" + String.format("%02d", minute), getActivity()));
	}

}
