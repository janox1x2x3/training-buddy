package sk.android.training;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sk.android.training.syncTasks.SyncLoginTask;
import sk.android.training.utils.listeners.TouchActionListener;
import sk.android.training.views.FontableTextView;

/**
 * @author Jano
 */
public class LoginActivity extends BaseActivity {

    private SharedPreferences prefs;
    private String prefName = "UserPref";

    private FontableTextView register;
    private int REG_FINISH = 0;

    private EditText _username;
    private EditText _password;

    private static final String USERNAME_KEY = "username";
    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
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
    private static final String UNITS_KEY = "units";
    private static final String SYNCED_KEY = "synced";

    private static final int DIALOG_WELCOME = 1;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(prefName, MODE_PRIVATE);

        register = (FontableTextView) findViewById(R.id.btn_register);

        _username = (EditText) findViewById(R.id.username);
        _password = (EditText) findViewById(R.id.password);

        register.registerClickableTouchListener(new TouchActionListener() {
            @Override
            public void onTouch() {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                i.putExtra("username", _username.getText().toString());
                startActivityForResult(i, REG_FINISH);
            }
        });

        findViewById(R.id.menu_login).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if ((_username.getText().toString().length() != 0) && (_password.getText().toString().length() != 0)) {
                    doLogin();
                } else {
                    Toast.makeText(getApplicationContext(), "Some fields are empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doLogin() {

        HashMap<String, String> params = new HashMap<>();
        params.put("username", _username.getText().toString());
        params.put("password", _password.getText().toString());


    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        Dialog dialog;

        switch (id) {
            case DIALOG_WELCOME:

                LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = layoutInflater.inflate(R.layout.update_notes, null);

                return new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("UPDATE RELEASE NOTES")
                        .setView(v)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismissDialog(DIALOG_WELCOME);
                            }
                        })
                        .create();

            default:
                dialog = null;
        }

        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REG_FINISH) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    public class LoginTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        InputStream is = null;
        boolean conSuccess = false;
        String result = "";
        String userDetails = "";

        protected void onPreExecute() {

            progressDialog.setMessage("Logging in...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    LoginTask.this.cancel(true);
                }
            });
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            InputStream is = null;

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", _username.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password", _password.getText().toString()));


            //http post
            try {
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 10000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient httpclient = new DefaultHttpClient(httpParameters);
                HttpPost httppost = new HttpPost("http://trainingbuddy.comuv.com/login.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                conSuccess = true;

            } catch (Exception e) {
                e.printStackTrace();
                this.progressDialog.dismiss();
            }

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString().substring(0, 1);
                userDetails = sb.toString().substring(1);


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
            if (conSuccess) {
                if (result.equals("0")) {
                    Toast.makeText(getApplicationContext(), "User not found!", Toast.LENGTH_SHORT).show();
                } else if (result.equals("1")) {
                    Toast.makeText(getApplicationContext(), "User found, but password is incorrect!", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = prefs.edit();

                    try {
                        JSONArray Jarray = new JSONArray(userDetails);

                        JSONObject Jasonobject = null;
                        Jasonobject = Jarray.getJSONObject(0);

                        editor.putString(FIRST_NAME_KEY, Jasonobject.getString("first_name"));
                        editor.putString(LAST_NAME_KEY, Jasonobject.getString("last_name"));
                        editor.putString(EMAIL_KEY, Jasonobject.getString("email"));
                        editor.putString(BIRTH_KEY, Jasonobject.getString("date"));
                        editor.putString(HOMETOWN_KEY, Jasonobject.getString("hometown"));
                        editor.putString(COUNTRY_KEY, Jasonobject.getString("country"));
                        editor.putString(SPORT_KEY, Jasonobject.getString("sport"));
                        editor.putString(WEIGHT_KEY, Jasonobject.getString("weight"));
                        editor.putString(HEIGHT_KEY, Jasonobject.getString("height"));
                        editor.putString(HR_MAX_KEY, Jasonobject.getString("hrMax"));
                        editor.putString(HR_BASAL_KEY, Jasonobject.getString("hrRest"));
                        editor.putString(VO2_KEY, Jasonobject.getString("vo2"));
                        editor.putString(FAT_KEY, Jasonobject.getString("fat"));
                        editor.putString(INFO_KEY, Jasonobject.getString("info"));
                        editor.putInt(UNITS_KEY, Jasonobject.getInt("units"));

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }

                    editor.putString(USERNAME_KEY, _username.getText().toString());
                    editor.putString(PASSWORD_KEY, _password.getText().toString());

                    editor.commit();

                    SyncLoginTask syncLoginTask = new SyncLoginTask(LoginActivity.this);
                    syncLoginTask.execute();

                }
            } else {
                Toast.makeText(getApplicationContext(), "Connection Failed!", Toast.LENGTH_SHORT).show();

            }
        }
    }
}