package sk.android.training.syncTasks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

public class SyncTask extends AsyncTask<String, String, Void> {	
	

    InputStream is = null ;
    boolean conSuccess = false;
    String result = "";
    Context ctx;
	private ProgressDialog progressDialog; 
	private TableDbAdapter db;
	
	private SharedPreferences prefs;
	private String prefName = "UserPref";
	
    public SyncTask(Context context) {
		// TODO Auto-generated constructor stub
    	super();
    	this.ctx = context;
    	progressDialog =  new ProgressDialog(this.ctx);
		db = new TableDbAdapter(ctx);
		ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);

	}
    
    protected void onPreExecute() {
    	progressDialog.setMessage("Synchronizing...");
    	progressDialog.show();
    	progressDialog.setOnCancelListener(new OnCancelListener() {
    	   @Override
    	   public void onCancel(DialogInterface arg0) {
    		   SyncTask.this.cancel(true);
    	   }
       });
    }

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		int lastSync;
		db.open();
		
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
			Toast.makeText(ctx, "Synchronization Successfull!", Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(ctx, "Connection Failed!", Toast.LENGTH_SHORT).show();
		}
		/*try {
			JSONArray Jarray = new JSONArray(result);
			for(int i=0;i<Jarray.length();i++)
			{
				JSONObject Jasonobject = Jarray.getJSONObject(i);
				
				System.out.println(Jasonobject.toString());
				this.progressDialog.dismiss();                

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/

	}
}