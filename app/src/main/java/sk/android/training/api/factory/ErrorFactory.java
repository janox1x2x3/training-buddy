package sk.android.training.api.factory;

import android.app.Activity;
import android.widget.LinearLayout;

import org.json.JSONArray;

public class ErrorFactory {

    public static LinearLayout createDialogLayout(JSONArray errors, Activity activity) {

        LinearLayout layout = new LinearLayout(activity);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        layout.setLayoutParams(lp);
//
//        if (errors.length() != 0) {
//            int len = errors.length();
//
//            for (int i = 0; i < len; i++) {
//                try {
//                    ErrorItem item = new ErrorItem(activity, errors.getJSONObject(i));
//                    layout.addView(item, i);
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }

        return layout;
    }
}
