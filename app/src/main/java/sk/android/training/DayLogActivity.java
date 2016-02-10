package sk.android.training;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import sk.android.training.fragments.FragmentDayView;
import sk.android.training.fragments.FragmentWeekView;
import sk.android.training.fragments.MyTabsListener;


public class DayLogActivity extends AppCompatActivity {
	
	private ActionBar actionBar;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.main);		

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab tabWeek = actionBar.newTab().setText("Week Details");
        ActionBar.Tab tabDay = actionBar.newTab().setText("Day Details");
        
        Fragment fragmentWeek = new FragmentWeekView();
        Fragment fragmentDay = new FragmentDayView();
        
        tabWeek.setTabListener(new MyTabsListener(fragmentWeek));  
        tabDay.setTabListener(new MyTabsListener(fragmentDay));
        
        actionBar.addTab(tabWeek);
        actionBar.addTab(tabDay);
        
        try {
        	actionBar.setSelectedNavigationItem(getIntent().getExtras().getInt("tabSelected"));
        } catch (NullPointerException e) {
//        	e.printStackTrace();
        }	
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
        	setResult(0);
        	finish();
        }
         return super.onKeyDown(keyCode, event);
    }
    
    
}
