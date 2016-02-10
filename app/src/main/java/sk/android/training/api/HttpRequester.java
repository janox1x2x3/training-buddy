package sk.android.training.api;

import com.goebl.david.Request;
import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sk.android.training.utils.Constants;
import sk.android.training.utils.Utils;

/**
 * Created by jano on 25.11.2015.
 */
public class HttpRequester {

    private static Webb webb;

    private JSONObject jsonBody;

    public HttpRequester(String agentName) {
        getInstance().setDefaultHeader(Webb.HDR_USER_AGENT, agentName);
    }

    private Webb getInstance() {

        if (webb == null) {
            webb = Webb.create();
            webb.setBaseUri(Constants.APP_URL);
        }

        return webb;
    }

    public void setJsonBody(JSONObject jsonBody) {
        this.jsonBody = jsonBody;
    }

    public JSONObject makeGetRequest(String callUri) {

        Response<JSONObject> response = getInstance().get(callUri)
                .asJsonObject();
        printOutResponse(response);
        return response.getBody();
    }

    public JSONObject makePostRequest(String callUri, HashMap<String, Object> params, HashMap<String, String> headers) {
        return execute(getInstance().post(callUri), params, headers);
    }

    public JSONObject makePutRequest(String callUri, HashMap<String, Object> params, HashMap<String, String> headers) {
        return execute(getInstance().put(callUri), params, headers);
    }

    public JSONObject makeDeleteRequest(String callUri, HashMap<String, Object> params, HashMap<String, String> headers) {
        return execute(getInstance().delete(callUri), params, headers);
    }

    private JSONObject execute(Request request, HashMap<String, Object> params, HashMap<String, String> headers) {
        request.ensureSuccess();

        if (params != null && params.size() > 0) {
            request.params(params);
            printOutRequestParams(params);
        }

        if (headers != null && headers.size() > 0) {
            setHeaders(request, headers);
        }

        if (jsonBody != null) {
            request.body(jsonBody);
            Utils.appendLog(Constants.TAG_RETRIEVER, "JSON BODY: " + jsonBody.toString());
        }

        Response<JSONObject> response = request.asJsonObject();
        printOutResponse(response);

        return response.getBody();
    }

    private void setHeaders(Request request, HashMap<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.header(entry.getKey(), entry.getValue());
            Utils.appendLog(Constants.TAG_RETRIEVER, entry.getKey() + ": " + entry.getValue());
        }
    }

    private void printOutRequestParams(HashMap<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Utils.appendLog(Constants.TAG_RETRIEVER, entry.getKey() + ": " + entry.getValue());
        }
    }

    private void printOutResponse(Response response) {
        Utils.appendLog(Constants.TAG_RETRIEVER, response.getStatusLine());
        Utils.appendLog(Constants.TAG_RETRIEVER,
                "---------------- ResponseBody --------------------");
        Utils.appendLog(Constants.TAG_RETRIEVER, response.isSuccess() ? response.getBody().toString() : response.getErrorBody().toString());
        response.ensureSuccess();
    }
}
