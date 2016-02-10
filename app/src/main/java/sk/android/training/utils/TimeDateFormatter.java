package sk.android.training.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import sk.android.training.DayLogActivity;

public class TimeDateFormatter {
	
	private Date dateObj;
	
	public String formatDateToDay(String date) {
		SimpleDateFormat curFormater = new SimpleDateFormat("yyyyMMdd"); 
		try {
			dateObj = curFormater.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat("d");
        
        date = postFormater.format(dateObj);
        
        return date;
	}
	
	public String formatDate(String date) {
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy"); 
		try {
			dateObj = curFormater.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");
        
        date = postFormater.format(dateObj);
        
        return date;
	}
	
	public String formatDate(Date date) {

		SimpleDateFormat postFormater = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        
        String result = postFormater.format(date);
        
        return result;
	}
	
	public String formatDateToDay(Date date) {

		SimpleDateFormat postFormater = new SimpleDateFormat("dd");
        
        String result = postFormater.format(date);
        
        return result;
	}
	
	public int formatDateToQuery(Date date) {

		SimpleDateFormat postFormater = new SimpleDateFormat("yyyyMMdd");
        
        int result = Integer.parseInt(postFormater.format(date));
        
        return result;
	}
	
	public String formatDateDatabase(String date) {
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy"); 
		try {
			dateObj = curFormater.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat("yyyyMMdd");
        
        date = postFormater.format(dateObj);
        
        return date;
	}
	
	public String formatDateMonthTotals(String date) {
		SimpleDateFormat curFormater = new SimpleDateFormat("MMMM yyyy", Locale.US); 
		try {
			dateObj = curFormater.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat("yyyyMMdd", Locale.US);
        
        date = postFormater.format(dateObj);
        return date;
	}
	
	public String formatTime(String time, Context ctx) {
    	SimpleDateFormat curFormater;
    	//if(android.text.format.DateFormat.is24HourFormat(ctx) == true) {
    	curFormater = new SimpleDateFormat("HH:mm"); 
    	//} else {
    	//	curFormater = new SimpleDateFormat("hh:mm aa");
    	//}
    	
    	try {
			dateObj = curFormater.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		if(android.text.format.DateFormat.is24HourFormat(ctx) == false) {
			time = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US).format(dateObj.getTime());
		} else {
			time = curFormater.format(dateObj);
		}
		
		return time;
	}
	
	public String formatTimeDatabase(String time, Context ctx) {
		System.out.println(time);
    	SimpleDateFormat curFormater;
    	if(android.text.format.DateFormat.is24HourFormat(ctx) == true) {
    		curFormater = new SimpleDateFormat("HH:mm"); 
    	} else {
    		curFormater = new SimpleDateFormat("hh:mm aa");
    	}
    	
    	try {
			dateObj = curFormater.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat("HHmm");
        
        time = postFormater.format(dateObj);
		
		return time;
	}
	
	public String formatTimeToDayLog(String time, Context ctx) {
		
		System.out.println(time);
		
		if(time.length() == 3){
			time = ("0" + time); 
		}else if(time.length() == 2){
			time = ("00" + time); 
		}else if(time.length() == 1){
			time = ("000" + time); 
		}
		SimpleDateFormat curFormater = new SimpleDateFormat("HHmm"); 
		try {
			dateObj = curFormater.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(android.text.format.DateFormat.is24HourFormat(ctx) == false) {
			time = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US).format(dateObj.getTime());
		} else {
			SimpleDateFormat postFormater = new SimpleDateFormat("HH:mm");
			time = postFormater.format(dateObj);
		}
		
		return time;
	}

}
