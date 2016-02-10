package sk.android.training.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import sk.android.training.R;
import sk.android.training.utils.TimeDateFormatter;


public class FragmentDetails extends Fragment {
	
	private SharedPreferences prefs;
	private String prefName = "UserPref";
	
	TimeDateFormatter format;
	
	private Bitmap bmp;
		
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
	
	private static final int DATE_DIALOG_ID = 0;
	private static final int NAME_DIALOG_ID = 1;
	private static final int SPORT_DIALOG_ID = 2;
	private static final int HOMETOWN_DIALOG_ID = 3;
	private static final int COUNTRY_DIALOG_ID = 4;
	private static final int WEIGHT_DIALOG_ID = 5;
	private static final int HEIGHT_DIALOG_ID = 6;
	private static final int HRMAX_DIALOG_ID = 7;
	private static final int HRBASAL_DIALOG_ID = 8;
	private static final int VO2_DIALOG_ID = 9;
	private static final int FAT_DIALOG_ID = 10;
	private static final int INFO_DIALOG_ID = 11;
	private static final int AGE_SET_DIALOG_ID = 12;
	
	//private SharedPreferences.Editor editor = prefs.edit();
	
	int day;
	int month;
	int year;
	
	String _date;
	
	private View view;
	
	private ImageView avatar;
	
	private TextView age;
	private TextView name;
	private TextView sport;
	private TextView hometown;
	private TextView country;
	private TextView weight;
	private TextView height;
	private TextView hrMax;
	private TextView hrBasal;
	private TextView vo2;
	private TextView fat;
	private TextView info;
		
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		prefs = getActivity().getSharedPreferences(prefName, getActivity().MODE_PRIVATE);
		
		age  = (TextView)view.findViewById(R.id.ageEdit);
		
		try {
			day = Integer.parseInt(prefs.getString(BIRTH_KEY, "").substring(0, 2));
			month = Integer.parseInt(prefs.getString(BIRTH_KEY, "").substring(3, 5));
			year = Integer.parseInt(prefs.getString(BIRTH_KEY, "").substring(6, 10));
			
			age.setText(getAge(year, month, day) + " years");
		} catch (Exception e) {
			age.setText("Not set");
			age.setTextColor(Color.GRAY);
		}
		
		
		name = (TextView)view.findViewById(R.id.nameEdit);

		sport = (TextView)view.findViewById(R.id.sportEdit);
		hometown = (TextView)view.findViewById(R.id.homeEdit);
		country = (TextView)view.findViewById(R.id.countryEdit);
		weight = (TextView)view.findViewById(R.id.weightEdit);
		height = (TextView)view.findViewById(R.id.heightEdit);
		hrMax = (TextView)view.findViewById(R.id.HRmaxEdit);
		hrBasal = (TextView)view.findViewById(R.id.HRbasalEdit);
		vo2 = (TextView)view.findViewById(R.id.VO2edit);
		fat = (TextView)view.findViewById(R.id.fatEdit);
		info = (TextView)view.findViewById(R.id.detailsEdit);
		
		if(prefs.getString(FIRST_NAME_KEY, "").length() != 0){
			name.setText(prefs.getString(FIRST_NAME_KEY, "") + " " + prefs.getString(LAST_NAME_KEY, ""));
		} else {
			name.setText("Not set");
			name.setTextColor(Color.GRAY);
		}
		if(prefs.getString(SPORT_KEY, "").length() != 0){
			sport.setText(prefs.getString(SPORT_KEY, ""));
		} else {
			sport.setText("Not set");
			sport.setTextColor(Color.GRAY);
		}
		
		if(prefs.getString(HOMETOWN_KEY, "").length() != 0){
			hometown.setText(prefs.getString(HOMETOWN_KEY, ""));
		} else {
			hometown.setText("Not set");
			hometown.setTextColor(Color.GRAY);
		}
		
		if(prefs.getString(COUNTRY_KEY, "").length() != 0){
			country.setText(prefs.getString(COUNTRY_KEY, ""));
		} else {
			country.setText("Not set");
			country.setTextColor(Color.GRAY);
		}
		
		if(prefs.getString(WEIGHT_KEY, "").length() != 0){
			weight.setText(prefs.getString(WEIGHT_KEY, ""));
		} else {
			weight.setText("--");
			weight.setTextColor(Color.GRAY);
		}
		
		if(prefs.getString(HEIGHT_KEY, "").length() != 0){
			height.setText(prefs.getString(HEIGHT_KEY, ""));
		} else {
			height.setText("--");
			height.setTextColor(Color.GRAY);
		}
		
		if(prefs.getString(HR_MAX_KEY, "").length() != 0){
			hrMax.setText(prefs.getString(HR_MAX_KEY, ""));
		} else {
			hrMax.setText("--");
			hrMax.setTextColor(Color.GRAY);
		}
		
		if(prefs.getString(HR_BASAL_KEY, "").length() != 0){
			hrBasal.setText(prefs.getString(HR_BASAL_KEY, ""));
		} else {
			hrBasal.setText("--");
			hrBasal.setTextColor(Color.GRAY);
		}
		
		if(prefs.getString(VO2_KEY, "").length() != 0){
			vo2.setText(prefs.getString(VO2_KEY, ""));
		} else {
			vo2.setText("--");
			vo2.setTextColor(Color.GRAY);
		}
		
		if(prefs.getString(FAT_KEY, "").length() != 0){
			fat.setText(prefs.getString(FAT_KEY, ""));
		} else {
			fat.setText("--");
			fat.setTextColor(Color.GRAY);
		}
		if(prefs.getString(INFO_KEY, "").length() != 0){
			info.setText(prefs.getString(INFO_KEY, ""));
		} else {
			info.setText("write something...");
			info.setTextColor(Color.GRAY);
		}
		
		age.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePickerDialog(AGE_SET_DIALOG_ID);
			}
		});
		
		name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(NAME_DIALOG_ID);
			}
		});
		
		sport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(SPORT_DIALOG_ID);
			}
		});
		
		hometown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(HOMETOWN_DIALOG_ID);
			}
		});
		
		country.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(COUNTRY_DIALOG_ID);
			}
		});
		
		weight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(WEIGHT_DIALOG_ID);
			}
		});
		
		height.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(HEIGHT_DIALOG_ID);
			}
		});
		
		hrMax.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(HRMAX_DIALOG_ID);
			}
		});
		
		hrBasal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(HRBASAL_DIALOG_ID);
			}
		});
		
		vo2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(VO2_DIALOG_ID);
			}
		});
		
		fat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(FAT_DIALOG_ID);
			}
		});
		
		info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(INFO_DIALOG_ID);
			}
		});
		
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.details_fragment, container, false);
		
		avatar = (ImageView)v.findViewById(R.id.avatar);
		
		InputStream is = null;
		try {
			is = getActivity().openFileInput("avatar.png");
			Bitmap b = BitmapFactory.decodeStream(is);
			avatar.setBackgroundResource(0);
			avatar.setImageBitmap(b);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		avatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		        photoPickerIntent.setType("image/*");
		        startActivityForResult(photoPickerIntent, 1);
			}
		});
		
		return v;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultcode, Intent intent)
	  {
	      super.onActivityResult(requestCode, resultcode, intent);
	      if (requestCode == 1) 
	      {
	    	  System.out.println(resultcode);
 	          if (intent != null && resultcode == Activity.RESULT_OK) {
 	    
	                Uri selectedImage = intent.getData();
	              	                
	                Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
	                cropIntent.setDataAndType(selectedImage, "image/*");
	                cropIntent.putExtra("crop", "true");
	                cropIntent.putExtra("aspectX", 10);
	                cropIntent.putExtra("aspectY", 12);
	                cropIntent.putExtra("outputX", 200);
	                cropIntent.putExtra("outputY", 240);
	                cropIntent.putExtra("return-data", true); 
	                startActivityForResult(cropIntent, 2);	             	            
            
	          } else {
	              //Log.d("Status:", "Photopicker canceled");            
	          }
	      } else if (requestCode == 2 && intent != null) {
	    	  
	    	  try {                           

		    	  Bundle extras = intent.getExtras();
		    	  
		    	  Bitmap thePic = extras.getParcelable("data");
	              avatar.setBackgroundResource(0);
	              avatar.setImageBitmap(thePic); 

	              try {
	            	  
	            	  FileOutputStream out = getActivity().openFileOutput("avatar.png", Context.MODE_WORLD_READABLE);
	                   thePic.compress(Bitmap.CompressFormat.PNG, 90, out);
	                   out.flush();
	                   out.close();
	              } catch (Exception e) {
	                   e.printStackTrace();
	              }
	    	  } catch (NullPointerException e) {
	    		  e.printStackTrace();
	    	  }
	      }
	  }

	
	public void showDialog(int id) {
		
	    // DialogFragment.show() will take care of adding the fragment
	    // in a transaction.  We also want to remove any currently showing
	    // dialog, so make our own transaction and take care of that here.
	   /* FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	    Fragment prev = (DialogFragment) getSupportFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);*/

	    // Create and show the dialog.
        DialogFragment newFragment = MyDialogFragment.newInstance(id);
        newFragment.show(getActivity().getSupportFragmentManager(), "dialog");
        
	}
	
	public void showDatePickerDialog(int id) {

	    DialogFragment newFragment = DatePickerFragment.newInstance(id);
	    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

	}
	
	public void update() {
		System.out.println(getView());
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