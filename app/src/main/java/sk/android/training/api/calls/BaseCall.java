package sk.android.training.api.calls;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.HashMap;

import sk.android.training.BaseActivity;
import sk.android.training.utils.Utils;
import sk.android.training.utils.listeners.OnTaskCompleteListener;

/**
 * @author Jano
 * Base call for each API call
 */
public abstract class BaseCall extends AsyncTask<Void, Integer, Object> {

    protected HashMap<String, Object> params;
    protected ProgressDialog progress;
    protected BaseActivity activity;
    protected Context context;
    protected Fragment fragment;
    protected boolean isSuccess = false;
    protected Resources res;
    protected String url;

    protected OnTaskCompleteListener mListener;

//    protected EasyTracker easyTracker;

    public BaseCall(Activity activity) {
        // TODO Auto-generated constructor stubf
        this.activity = (BaseActivity) activity;
        this.res = this.activity.getResources();

//        easyTracker = EasyTracker.getInstance(activity);

        try {
            initProgressBar();
        } catch (RuntimeException e) {

        }
    }

    public BaseCall(Activity activity, HashMap<String, Object> params) {
        // TODO Auto-generated constructor stub
        this.activity = (BaseActivity) activity;
        this.params = params;
        this.res = this.activity.getResources();

//        easyTracker = EasyTracker.getInstance(activity);

        try {
            initProgressBar();
        } catch (RuntimeException e) {
            Utils.appendLog(BaseCall.class.getSimpleName(), e.toString());
        }
    }

    public BaseCall(Fragment fragment) {
        // TODO Auto-generated constructor stub
        this.activity = (BaseActivity) fragment.getActivity();
        this.fragment = fragment;
        this.res = this.activity.getResources();

//        easyTracker = EasyTracker.getInstance(activity);

        try {
            initProgressBar();
        } catch (RuntimeException e) {
            Utils.appendLog(BaseCall.class.getSimpleName(), e.toString());
        }
    }

    public BaseCall(Fragment fragment, HashMap<String, Object> params) {
        // TODO Auto-generated constructor stub
        this.activity = (BaseActivity) fragment.getActivity();
        this.fragment = (Fragment) fragment;
        this.params = params;
        this.res = this.activity.getResources();

//        easyTracker = EasyTracker.getInstance(activity);

        try {
            initProgressBar();
        } catch (RuntimeException e) {
            Utils.appendLog(BaseCall.class.getSimpleName(), e.toString());
        }
    }

    private void initProgressBar() {

        progress = new ProgressDialog(this.activity);
        progress.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                cancel(true);
            }
        });

        setProgressDialog();

    }

    public BaseCall(Context context, HashMap<String, Object> params) {
        // TODO Auto-generated constructor stub

        this.params = params;
        this.context = context;
        this.res = context.getResources();

    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        toggleProgress();
    }

    @Override
    protected Object doInBackground(Void... params) {
        // TODO Auto-generated method stub
        return executeCall();
    }

    @Override
    protected void onPostExecute(Object result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        toggleProgress();

        if (mListener != null) mListener.OnTaskComplete(result);
    }

    protected abstract Object executeCall();

    protected abstract void setProgressDialog();

    public void toggleProgress() {

        if (progress == null) return;

        if (!progress.isShowing()) {
            progress.show();
        } else {
            progress.cancel();
        }
    }

    public void setOnTaskCompleteListener(OnTaskCompleteListener listener) {
        this.mListener = listener;
    }
}
