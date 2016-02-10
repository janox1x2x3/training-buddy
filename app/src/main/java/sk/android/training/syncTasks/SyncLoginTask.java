package sk.android.training.syncTasks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import sk.android.training.CalendarViewActivity;
import sk.android.training.LoginActivity;
import sk.android.training.database.TableDbAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

public class SyncLoginTask extends AsyncTask<String, Integer, Void> {	
	

    InputStream is = null ;
    boolean conSuccess = false;
    String result = "";
    Context ctx;
	private ProgressDialog progressDialog; 
	private TableDbAdapter db;
	
	private SharedPreferences prefs;
	private String prefName = "UserPref";
	
    public SyncLoginTask(Context context) {
		// TODO Auto-generated constructor stub
    	super();
    	this.ctx = context;
    	progressDialog =  new ProgressDialog(this.ctx);
		db = new TableDbAdapter(ctx);

	}
    
    protected void onPreExecute() {
    	progressDialog.setMessage("Synchronizing...");
    	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	progressDialog.show();
    	progressDialog.setOnCancelListener(new OnCancelListener() {
    	   @Override
    	   public void onCancel(DialogInterface arg0) {
    		   SyncLoginTask.this.cancel(true);
    	   }
       });
    }

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		int lastSync;
		prefs = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("username", prefs.getString("username", "")));
	    
		//http post
		try{
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
		    HttpClient httpclient = new DefaultHttpClient(httpParameters);
		    HttpPost httppost = new HttpPost("http://trainingbuddy.comuv.com/syncLog.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
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

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		db.open();
		
		try {
			JSONArray Jarray = new JSONArray(result);
			JSONObject Jasonobject;
			for(int i=0;i<Jarray.length();i++)
			{
				Jasonobject = Jarray.getJSONObject(i);
                String time = Jasonobject.getString("time");
                String date = Jasonobject.getString("date");
                String total_time = Jasonobject.getString("total_time");
                String id_sport = Jasonobject.getString("id_sport");
                String info = Jasonobject.getString("info");
                String distance = Jasonobject.getString("distance");
                String altitude = Jasonobject.getString("altitude");
                String speed = Jasonobject.getString("speed");
                String pace = Jasonobject.getString("pace");
                String avgHR = Jasonobject.getString("avgHR");
                String maxHR = Jasonobject.getString("maxHR");
                String modified = Jasonobject.getString("modified");
                String created = Jasonobject.getString("created");
                
                float percentage = ((float)i / (float)Jarray.length()) * 100;
                publishProgress(Float.valueOf(percentage).intValue());                  
                
                db.createSyncActivity(time, date, total_time, id_sport, info, distance, altitude, speed, pace, avgHR, maxHR, modified, created);             
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		db.close();	
		
		return null;
		
	}
	protected void onProgressUpdate(Integer...progress){

        progressDialog.setProgress(progress[0]);
    }
	
	protected void onPostExecute(Void v) {
        if (progressDialog.isShowing()) {
	           progressDialog.dismiss();
	        }
		if(conSuccess){
			   
			Intent i = new Intent(ctx, CalendarViewActivity.class);
 			i.putExtra("type", "login");
			ctx.startActivity(i);	

			//Toast.makeText(getApplicationContext(), "You are successfully logged in as " + _username.getText().toString() + "!", Toast.LENGTH_LONG).show();
			((Activity)ctx).finish();
			Toast.makeText(ctx, "Synchronization Successfull!", Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(ctx, "Connection Failed!", Toast.LENGTH_SHORT).show();
		}
	}
}