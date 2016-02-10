package sk.android.training.api.calls;

import android.app.Activity;

import java.util.HashMap;

import sk.android.training.R;
import sk.android.training.api.HttpRetriever;

/**
 * Created by jano on 25.11.2015.
 */
public class LoginCall extends BaseCall {

    public LoginCall(Activity activity, HashMap<String, Object> params) {
        super(activity, params);

        url = ApiUrl.API_LOGIN;
    }

    @Override
    protected Boolean executeCall() {

        boolean success = false;

        HttpRetriever httpRetriever = new HttpRetriever(HttpRetriever.HttpMethod.POST);
        httpRetriever.addCallParams(params);


        return success;
    }

    @Override
    protected void setProgressDialog() {
        progress.setMessage(res.getString(R.string.progress_logging));
    }
}
