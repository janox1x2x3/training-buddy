package sk.android.training.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import sk.android.training.*;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
	static final int FIRST_DAY_OF_WEEK =1; // Sunday = 0, Monday = 1
	
	
	private Context mContext;

    private Calendar month;
    private Calendar nextMonth;
    private Calendar previousMonth;
    private Calendar selectedDate;
    private ArrayList<String[]> dates;
    
	private static final String RUNNING_ID = "1";
	private static final String CYCLING_ID = "2";
	private static final String XCOUNTRY_ID = "3";
	private static final String SWIMMING_ID = "4";
	private static final String SKATE_ID = "5";
	private static final String WALKING_ID = "6";
	private static final String CANOE_ID = "7";	
	private static final String FOOTBALL_ID = "8";
	private static final String HOCKEY_ID = "9";
	private static final String VOLLEY_ID = "10";
	private static final String BASKET_ID = "11";	
	private static final String TENNIS_ID = "12";
	private static final String SKIING_ID = "13";
	private static final String ATHLETICS_ID = "14";
	private static final String WEIGHT_ID = "15";
	private static final String OTHER_ID = "16";
    
    private int _previous;
    private int _current;
        
    public CalendarAdapter(Context c, Calendar monthCalendar) {
    	month = monthCalendar;    
    	
    	previousMonth = (Calendar)monthCalendar.clone();
    	nextMonth = (Calendar)monthCalendar.clone();
    	selectedDate = Calendar.getInstance();
    
    	mContext = c;
    	
        month.set(Calendar.DAY_OF_MONTH, 1);
        nextMonth.set(Calendar.DAY_OF_MONTH, 1);
        previousMonth.set(Calendar.DAY_OF_MONTH, 1);
        
    	nextMonth.set(Calendar.MONTH, month.get(Calendar.MONTH)+1);
    	previousMonth.set(Calendar.MONTH, month.get(Calendar.MONTH)-1);
    	
        refreshDays();
    }
    
    public void setDates(ArrayList<String[]> dates) {
    	this.dates = dates;
    } 

    public int getCount() {
        return days.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
    	TextView dayView;
    	
    	LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	LinearLayout linearLayout;
    	
        if (convertView == null) {  
            v = vi.inflate(R.layout.calendar_item, null);
        }

        linearLayout = (LinearLayout)v.findViewById(R.id.TrainingIconLayout); 
        dayView = (TextView)v.findViewById(R.id.date);

        	// mark current day as focused
        v.setBackgroundResource(R.drawable.list_item_background);
        if(position >= _previous && position < _current){
        	linearLayout.removeAllViews();
	        dayView.setTextColor(Color.BLACK);
	        
	        int i = 0;
	        View view;
	        String[] a;
	        ArrayList<String[]> help;
	        help = dates;
	        try{
	        	do {
			        a = help.get(i);
			        i++;
			        if(a[0].equals(days[position])){
					    v.setBackgroundResource(R.drawable.list_item_background_training);
					    
					    if(a[1].equals(RUNNING_ID)){
						   	view = vi.inflate(R.drawable.training_item_running, null); 
					        linearLayout.addView(view);
						}
					    if(a[1].equals(CYCLING_ID)){
						   	view = vi.inflate(R.drawable.training_item_cycling, null); 
					        linearLayout.addView(view);
						}
					    if(a[1].equals(XCOUNTRY_ID)){
						   	view = vi.inflate(R.drawable.training_item_xcountry, null); 
					        linearLayout.addView(view);
						}
					    if(a[1].equals(SWIMMING_ID)){
						   	view = vi.inflate(R.drawable.training_item_swimming, null); 
					        linearLayout.addView(view);
						}
					    if(a[1].equals(SKATE_ID)){
						   	view = vi.inflate(R.drawable.training_item_skate, null); 
					        linearLayout.addView(view);
						}
					    if(a[1].equals(WALKING_ID)){
						   	view = vi.inflate(R.drawable.training_item_walking, null); 
					        linearLayout.addView(view);
						}						    
					    if(a[1].equals(CANOE_ID)){
						   	view = vi.inflate(R.drawable.training_item_canoe, null); 
					        linearLayout.addView(view);
						}
					    if(a[1].equals(FOOTBALL_ID)){
						   	view = vi.inflate(R.drawable.training_item_football, null); 
					        linearLayout.addView(view);
						}
					    if(a[1].equals(HOCKEY_ID)){
						   	view = vi.inflate(R.drawable.training_item_hockey, null); 
					        linearLayout.addView(view);
						}
					    if(a[1].equals(VOLLEY_ID)){
						   	view = vi.inflate(R.drawable.training_item_volley, null); 
					        linearLayout.addView(view);
						}	
					    if(a[1].equals(BASKET_ID)){
						   	view = vi.inflate(R.drawable.training_item_basket, null); 
					        linearLayout.addView(view);
						}	
					    if(a[1].equals(TENNIS_ID)){
						   	view = vi.inflate(R.drawable.training_item_tennis, null); 
					        linearLayout.addView(view);
						}	
					    if(a[1].equals(SKIING_ID)){
						   	view = vi.inflate(R.drawable.training_item_skiing, null); 
					        linearLayout.addView(view);
						}	
					    if(a[1].equals(ATHLETICS_ID)){
						   	view = vi.inflate(R.drawable.training_item_athletics, null); 
					        linearLayout.addView(view);
						}	
					    if(a[1].equals(WEIGHT_ID)){
						   	view = vi.inflate(R.drawable.training_item_weight, null); 
					        linearLayout.addView(view);
						}	
					    if(a[1].equals(OTHER_ID)){
						   	view = vi.inflate(R.drawable.training_item_other, null); 
					        linearLayout.addView(view);
						}
			        }
	        	} while(!a.equals(days[position]));	        

	        } catch (IndexOutOfBoundsException e) {
	        	
	     }
        }
        
        if(!(position > _current-1 || position < _previous) && 
        		month.get(Calendar.YEAR)== selectedDate.get(Calendar.YEAR) && 
        		month.get(Calendar.MONTH)== selectedDate.get(Calendar.MONTH) && 
        		days[position].equals(""+ selectedDate.get(Calendar.DAY_OF_MONTH))) {
        			v.setBackgroundResource(R.drawable.list_item_background_focused);
        }

        if(position < _previous){
        	dayView.setTextColor(Color.LTGRAY);
        	linearLayout.removeAllViews();
        } 
        
        if(position > _current-1){
        	dayView.setTextColor(Color.LTGRAY);
        	linearLayout.removeAllViews();
        } 
  
        dayView.setText(days[position]);
                
        return v;
    }
    
    public void refreshDays() {
    	
    	int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int)month.get(Calendar.DAY_OF_WEEK);            
        
        int lastDayPrev = previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH);        
        int dec = 5; 
        int dec1 = (int)month.get(Calendar.DAY_OF_WEEK) - 3;
        
        days = new String[42];
        
        int j=FIRST_DAY_OF_WEEK;
        int i;
        
        // populate empty days before first real day
        if(firstDay>1) {
	        for(j=0;j<firstDay-FIRST_DAY_OF_WEEK;j++) {
	        	days[j] = "" + (lastDayPrev - dec1);
	        	dec1--;
	        }
	        _previous = firstDay-FIRST_DAY_OF_WEEK-1;
        }
	    else {
	    	for(j=0;j<FIRST_DAY_OF_WEEK*6;j++) {
	    		days[j] = "" + (lastDayPrev - dec);
	    		dec--;
	        }
	    	
	    	_previous = FIRST_DAY_OF_WEEK*6;
	    	j=FIRST_DAY_OF_WEEK*6+1; // sunday => 1, monday => 7
	    }        
        
        // populate days
        int dayNumber = 1;
        for(i=j-1;i<j+lastDay-1;i++) {
        	days[i] = ""+dayNumber;
        	dayNumber++;
        }
        
        _current = j+lastDay-1;
        
        dayNumber = 1;
        for(i=j+lastDay-1;i<days.length;i++) {
        	days[i] = ""+dayNumber;
        	dayNumber++;
        }
        
    }
    
    public void previousMonth() {    	
    	
		if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) {	
			
			nextMonth.set(Calendar.MONTH, month.get(Calendar.MONTH));
			month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
			previousMonth.set(Calendar.MONTH ,month.get(Calendar.MONTH)-1);
			
		} else {
			
	    	nextMonth.set(Calendar.MONTH, month.get(Calendar.MONTH));
	    	month.set(Calendar.MONTH, previousMonth.get(Calendar.MONTH));
	        previousMonth.set(Calendar.MONTH,previousMonth.get(Calendar.MONTH)-1);
		}  
        
    }
    
    public void nextMonth() {
    	
    	if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) {	
    		
    		previousMonth.set(Calendar.MONTH, month.get(Calendar.MONTH));
			month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
			nextMonth.set(Calendar.MONTH, month.get(Calendar.MONTH)+1);
			
		} else {
			
	    	previousMonth.set(Calendar.MONTH, month.get(Calendar.MONTH));
	        month.set(Calendar.MONTH,nextMonth.get(Calendar.MONTH));
	        nextMonth.set(Calendar.MONTH, nextMonth.get(Calendar.MONTH)+1);
		}
                
    }
    
    @Override 
    public boolean isEnabled(int position) { 
    	
	    if(position < _previous){ 
	    	return false; 
	    } else if (position > _current-1) {
	    	return false; 
	    } else {
	    	return true;
	    }
    } 
    
    
    // references to our items
    public String[] days;
}