package sk.android.training.syncTasks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import sk.android.training.CalendarViewActivity;
import sk.android.training.database.TableDbAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

public class SyncRegisterTask extends AsyncTask<String, String, Void> {	
	

    InputStream is = null ;
    boolean conSuccess = false;
    String result = "";
    Context ctx;
	private ProgressDialog progressDialog; 
	private  TableDbAdapter db;
	
	private SharedPreferences prefs;
	private String prefName = "UserPref";
	
    public SyncRegisterTask(Context context) {
		// TODO Auto-generated constructor stub
    	super();
    	this.ctx = context;
    	progressDialog =  new ProgressDialog(this.ctx);
    	db = new TableDbAdapter(ctx);
    	prefs = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
	}
    
    protected void onPreExecute() {
    	progressDialog.setMessage("Synchronizing...");
    	progressDialog.show();
    	progressDialog.setOnCancelListener(new OnCancelListener() {
    	   @Override
    	   public void onCancel(DialogInterface arg0) {
    		   SyncRegisterTask.this.cancel(true);
    	   }
       });
    }

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		int lastSync;
		
       	Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	sdf.setTimeZone(TimeZone.getTimeZone("EST5EDT"));
    	String now = sdf.format(date);
    	
		db.open();
		
        Cursor c = db.getAllActivitiesSync();
        
		if (c != null && c.getCount() > 0 ) {
		    if (c.moveToFirst()) {
		        // loop until it reach the end of the cursor
		        do {
		        	int id = c.getInt(0);
			    	ContentValues args = new ContentValues();    			    	
			    	args.put("created", (Long.parseLong(now) + id));
					db.updateRegisterTask(args, id);	    					
					
					String insert = "INSERT INTO activity(time, date, total_time, id_sport, info, distance, altitude, speed, pace, avgHR, maxHR, modified, created, username) " +
							"VALUES('" + c.getString(1) + "','" + c.getInt(2) + "','" + c.getInt(3) + "','" + c.getString(4) + "','" + c.getString(5) + "','" + c.getDouble(6) + "','" 
							+ c.getDouble(7) + "','" + c.getDouble(8) + "','" + c.getInt(9) + "','" + c.getInt(10) + "','" + c.getInt(11) + "','" + c.getLong(12) +
							"','" + (Long.parseLong(now) + id) + "','" + prefs.getString("username", "") + "');";
					
					db.insertSyncQuery(insert);
		        } while (c.moveToNext());
		    }
		    c.close();
		}
		
		Cursor query = db.getSyncQuery();
	    
        JSONArray Jarray = new JSONArray();
        try {
        	if (query.moveToFirst()) {
		        // loop until it reach the end of the cursor
		        do {
		        	
		        	JSONObject sessions = new JSONObject();
		        	
		        	sessions.put("query", query.getString(0));
		        	
		        	Jarray.put(sessions);
		        } while (query.moveToNext());
		    }
        	
        } catch (Exception e) {
			// TODO: handle exception
		}
        
		InputStream is = null;

		
		//http post
		try{
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
		    HttpClient httpclient = new DefaultHttpClient(httpParameters);
		    HttpPost httppost = new HttpPost("http://trainingbuddy.comuv.com/syncReg.php");
		    httppost.setHeader("json", Jarray.toString());
		    StringEntity entity = new StringEntity(Jarray.toString());
		    entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		    httppost.setEntity(entity);
		    HttpResponse response = httpclient.execute(httppost);			   
            is = response.getEntity().getContent();
            conSuccess = true;
            
		}catch(Exception e){
			e.printStackTrace();
			this.progressDialog.dismiss();
			}
	
		try {
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = "";
			
			while((line=br.readLine())!=null) {
			   sb.append(line+"\n");
			}
				is.close();
				result=sb.toString();	
				System.out.println(result);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
		return null;
		
	}
	
	protected void onPostExecute(Void v) {
        if (progressDialog.isShowing()) {
	           progressDialog.dismiss();
	        }
		if(conSuccess){
			db.truncateQuery();
			db.close();
			
			Intent i;
			i = new Intent(ctx, CalendarViewActivity.class);
 			i.putExtra("type", "register");
			ctx.startActivity(i);
	        ((Activity) ctx).setResult(Activity.RESULT_OK);
			((Activity) ctx).finish();
			Toast.makeText(ctx, "Registration Successfull", Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(ctx, "Connection Failed!", Toast.LENGTH_SHORT).show();
		}
	}
}