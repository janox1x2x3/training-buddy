package sk.android.training;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sk.android.training.database.TableDbAdapter;
import sk.android.training.syncTasks.SyncRegisterTask;


/**
 * @author Jano
 */
public class RegistrationActivity extends BaseActivity {

    private SharedPreferences prefs;
    private String prefName = "UserPref";

    private EditText _usernameEdit;
    private EditText _emailEdit;
    private EditText _passwordEdit;
    private EditText _passwordCheckEdit;
    private EditText _firstNameEdit;
    private EditText _lastNameEdit;
    private TextView _birthEdit;
    private EditText _hometownEdit;
    private EditText _countryEdit;
    private TextView _sportEdit;
    private EditText _weightEdit;
    private EditText _heightEdit;
    private EditText _HRmaxEdit;
    private EditText _HRbasalEdit;
    private EditText _VO2edit;
    private EditText _fatEdit;
    private EditText _infoEdit;
    private RadioButton _radioMetric;
    private RadioButton _radioImperial;

    int units_id;

    private Bundle extras;
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
    private static final String ACTIVE_KEY = "active";

    private static final int DATE_DIALOG_ID = 0;
    private static final int SPORT_DIALOG_ID = 1;

    private TableDbAdapter db;

    private String _date;
    private Date dateObj;

    private Button submit;

    private DatePickerDialog.OnDateSetListener _DateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    _date = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    formatDate(_date);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        db = new TableDbAdapter(this);
        db.open();

//        _birthEdit = (TextView) findViewById(R.id.birthEdit);
//        _birthEdit.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                showDialog(DATE_DIALOG_ID);
//            }
//        });
//
//        _usernameEdit = (EditText) findViewById(R.id.usernameEdit);
//        _emailEdit = (EditText) findViewById(R.id.emailEdit);
//        _passwordEdit = (EditText) findViewById(R.id.passEdit);
//        _passwordCheckEdit = (EditText) findViewById(R.id.passCheckEdit);
//        _firstNameEdit = (EditText) findViewById(R.id.firstNameEdit);
//        _lastNameEdit = (EditText) findViewById(R.id.lastNameEdit);
//        _hometownEdit = (EditText) findViewById(R.id.homeEdit);
//        _countryEdit = (EditText) findViewById(R.id.countryEdit);
//        _weightEdit = (EditText) findViewById(R.id.weightEdit);
//        _heightEdit = (EditText) findViewById(R.id.heightEdit);
//        _HRmaxEdit = (EditText) findViewById(R.id.HRmaxEdit);
//        _HRbasalEdit = (EditText) findViewById(R.id.HRbasalEdit);
//        _VO2edit = (EditText) findViewById(R.id.VO2edit);
//        _fatEdit = (EditText) findViewById(R.id.fatEdit);
//        _infoEdit = (EditText) findViewById(R.id.detailsEdit);
//
//        _radioMetric = (RadioButton) findViewById(R.id.radioMetric);
//        _radioImperial = (RadioButton) findViewById(R.id.radioImperial);

        prefs = getSharedPreferences(prefName, MODE_PRIVATE);

        Intent intent = getIntent();
        String usernameLogin = intent.getStringExtra("username");
        _usernameEdit.setText(usernameLogin);

        _radioMetric.setChecked(true);

        _sportEdit = (TextView) findViewById(R.id.sportEdit);
        _sportEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(SPORT_DIALOG_ID);
            }
        });

//        submit = (Button) findViewById(R.id.registerButton);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = _usernameEdit.getText().toString();
                String password = _passwordEdit.getText().toString();
                String passwordCheck = _passwordCheckEdit.getText().toString();
                String email = _emailEdit.getText().toString();
                String firstName = _firstNameEdit.getText().toString();
                String lastName = _lastNameEdit.getText().toString();
                String dateOfBirth = _birthEdit.getText().toString();
                String hometown = _hometownEdit.getText().toString();
                String country = _countryEdit.getText().toString();
                String sport = _sportEdit.getText().toString();

                if (password.equals(passwordCheck) && password.length() >= 6 && username.length() >= 4) {

                    if (email.length() > 0 && firstName.length() > 0 && lastName.length() > 0
                            && !dateOfBirth.equals("DD/MM/YYYY") && hometown.length() > 0
                            && country.length() > 0 && !sport.equals("Click to set")) {
                        new RegisterTask().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some of the required fields are empty!", Toast.LENGTH_SHORT).show();
                    }

                } else if (!password.equals(passwordCheck)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else if (!(password.length() >= 6)) {
                    Toast.makeText(getApplicationContext(), "Passwords must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
                } else if (!(username.length() >= 4)) {
                    Toast.makeText(getApplicationContext(), "Usernam must be at least 4 characters long!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.registration;
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        _DateSetListener,
                        1980, 0, 1);

            case SPORT_DIALOG_ID:
                Cursor c = db.getAllSports();
                startManagingCursor(c);

                return new AlertDialog.Builder(this)
                        .setTitle("Choose new value")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setCursor(c, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                Cursor cursor = db.getSport(which + 1);

                                _sportEdit.setText(cursor.getString(0));
                            }
                        }, TableDbAdapter.KEY_SPORT_TITLE)
                        .create();
        }
        return null;
    }

    private void formatDate(String date) {
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateObj = curFormater.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        _birthEdit.setText(curFormater.format(dateObj));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class RegisterTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
        InputStream is = null;
        boolean conSuccess = false;
        String result = "";

        protected void onPreExecute() {

            progressDialog.setMessage("Creating User...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    RegisterTask.this.cancel(true);
                }
            });
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            InputStream is = null;
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", _usernameEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("email", _emailEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password", _passwordEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("first_name", _firstNameEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("last_name", _lastNameEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("dateOfBirth", _birthEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("hometown", _hometownEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("country", _countryEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("sport", _sportEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("units", String.valueOf(units_id)));
            nameValuePairs.add(new BasicNameValuePair("weight", _weightEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("height", _heightEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("hrMax", _HRmaxEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("hrRest", _HRbasalEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("vo2", _VO2edit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("fat", _fatEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("info", _infoEdit.getText().toString()));

            //http post
            try {
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 10000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient httpclient = new DefaultHttpClient(httpParameters);
                HttpPost httppost = new HttpPost("http://trainingbuddy.comuv.com/registerUser.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                conSuccess = true;

                units_id = 0;

//                if (_radioMetric.isChecked()) {
//                    units_id = 0;
//                } else if (_radioImperial.isChecked()) {
//                    units_id = 1;
//                }

            } catch (Exception e) {
                e.printStackTrace();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
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
                if (result.equals("1")) {

                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(USERNAME_KEY, _usernameEdit.getText().toString());
                    editor.putString(EMAIL_KEY, _emailEdit.getText().toString());
                    editor.putString(PASSWORD_KEY, _passwordEdit.getText().toString());
                    editor.putString(FIRST_NAME_KEY, _firstNameEdit.getText().toString());
                    editor.putString(LAST_NAME_KEY, _lastNameEdit.getText().toString());
                    editor.putString(BIRTH_KEY, _birthEdit.getText().toString());
                    editor.putString(HOMETOWN_KEY, _hometownEdit.getText().toString());
                    editor.putString(COUNTRY_KEY, _countryEdit.getText().toString());
                    editor.putString(SPORT_KEY, _sportEdit.getText().toString());
                    editor.putString(WEIGHT_KEY, _weightEdit.getText().toString());
                    editor.putString(HEIGHT_KEY, _heightEdit.getText().toString());
                    editor.putString(HR_MAX_KEY, _HRmaxEdit.getText().toString());
                    editor.putString(HR_BASAL_KEY, _HRbasalEdit.getText().toString());
                    editor.putString(VO2_KEY, _VO2edit.getText().toString());
                    editor.putString(FAT_KEY, _fatEdit.getText().toString());
                    editor.putString(INFO_KEY, _infoEdit.getText().toString());
                    editor.putInt(UNITS_KEY, units_id);

                    editor.commit();

                    SyncRegisterTask syncRegisterTask = new SyncRegisterTask(RegistrationActivity.this);
                    syncRegisterTask.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Username already in use!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
