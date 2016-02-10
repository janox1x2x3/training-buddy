package sk.android.training.api;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.creatix.cratefree.BaseActivity;
import com.creatix.cratefree.R;
import com.creatix.cratefree.db.VersionUtils;
import com.creatix.cratefree.utils.Constants;
import com.creatix.cratefree.utils.Utils;
import com.goebl.david.WebbException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class HttpRetriever {

    JSONArray errors = null;
    private String error;
    private Activity activity;
    /**
     * Default error runnable
     */
    private Runnable FailedMsg = new Runnable() {

        @Override
        public void run() {
            Utils.showToast(activity, error);
        }
    };

    private HttpMethod httpMethodType;

    private HashMap<String, Object> callParams = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();
    private JSONObject jsonBody;

    /**
     * Default constructor
     *
     * @param httpMethodType
     */
    public HttpRetriever(HttpMethod httpMethodType) {
        super();

        this.httpMethodType = httpMethodType;
    }

    /**
     * Make apiCall
     *
     * @param callUri
     * @return null if request failed, or requestbody with jsonObject
     */
    public JSONObject makeApiCall(Context context, String callUri) {

        error = context.getResources().getString(R.string.server_error_message);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        if (!this.hasConnection(context)) {

            error = context.getResources().getString(R.string.no_connection_error_message);
            showFailedMsgToast();

            Utils.appendLog(Constants.TAG_RETRIEVER, "No connection! Call aborted.");

            return null;
        }

        JSONObject response = null;

        try {
            HttpRequester requester = new HttpRequester(getAgentName(activity));
            if (jsonBody != null) {
                requester.setJsonBody(jsonBody);
            }

            Utils.appendLog(Constants.TAG_RETRIEVER, "Making " + httpMethodType + " to " + Constants.APP_URL + callUri);

            switch (httpMethodType) {

                case GET:
                    response = requester.makeGetRequest(callUri);
                    break;
                case POST:
                    response = requester.makePostRequest(callUri, callParams, headers);
                    break;
                case PUT:
                    response = requester.makePutRequest(callUri, callParams, headers);
                    break;
                case DELETE:
                    response = requester.makeDeleteRequest(callUri, callParams, headers);
                    break;
                default:
                    break;
            }

        } catch (NullPointerException | IllegalArgumentException e) {
            showFailedMsgToast();
            e.printStackTrace();
            return null;
        } catch (WebbException e) {
            // Workaround for some Android phones
            if (e.getMessage().equals("java.io.InterruptedIOException")) {
                Utils.appendLog(Constants.TAG_RETRIEVER, "Retrying request because of OKIO lib bug.");
                return makeApiCall(activity, callUri);
            }

            showFailedMsgToast();
            e.printStackTrace();
            return null;
        }

        return response;
    }

    private void showFailedMsgToast() {
        if (activity != null && activity instanceof BaseActivity && !((BaseActivity) activity).isPaused()) {
            activity.runOnUiThread(FailedMsg);
        }
    }

    /**
     * @param ctx
     * @return true if connection is available
     */
    public boolean hasConnection(Context ctx) {
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public void addCallParam(String paramName, String value) {
        callParams.put(paramName, value);
    }

    public void addCallParams(HashMap<String, Object> params) {
        callParams.putAll(params);
    }

    public void addHeader(String paramName, String value) {
        headers.put(paramName, value);
    }

    public void addHeaders(HashMap<String, String> params) {
        headers.putAll(params);
    }

    public void setJsonBody(JSONObject jsonBody) {
        this.jsonBody = jsonBody;
    }

    public void clearCallParams() {
        callParams.clear();
    }

    private String getAgentName(Context context) {

        String agentName;
        int versionName = 0;

        try {
            versionName = VersionUtils.getVersionCode(context);
        } catch (NameNotFoundException | NullPointerException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
        }

        String os = "CrateFree IL Android ";
        String myVersion = android.os.Build.VERSION.RELEASE;
        int sdkVersion = android.os.Build.VERSION.SDK_INT;

        agentName = os + "(v" + versionName + ") @ Android OS " + myVersion + "(" + sdkVersion + ")";
        return agentName;
    }

    public enum HttpMethod {
        POST,
        GET,
        PUT,
        DELETE
    }
}
