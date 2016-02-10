package sk.android.training.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.UUID;

import sk.android.training.R;

public class Utils {

    public static final float MILES_IN_METER = 0.000621371192f;
    public static final float METRES_IN_MILE = 1609.344000614692f;
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final String EMPTY_STRING = "";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";

    public static final boolean DEV_BUILD = false;
    // ========================
    // Decimal formatting
    // ========================
    public final static DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();

    static {
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
    }

    private static final DecimalFormat DECIMAL0 = new DecimalFormat("0", otherSymbols);
    private static final DecimalFormat DECIMAL1 = new DecimalFormat("0.0", otherSymbols);
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static String TAG = "Google Play Services";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    public static void appendLog(String TAG, String text) {

        if (!DEV_BUILD) return;

        if (text.length() > 4000) {
            Log.v(TAG, "sb.length = " + text.length());
            int chunkCount = text.length() / 4000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= text.length()) {
                    Log.i(TAG, "chunk " + i + " of " + chunkCount + ":" + text.substring(4000 * i));
                } else {
                    Log.i(TAG, "chunk " + i + " of " + chunkCount + ":" + text.substring(4000 * i, max));
                }
            }
        } else {
            Log.i(TAG, text);
        }

    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

	/*
     * public static boolean isOk(ResultError error) { return error!=null &&
	 * error.isOk(); }
	 * 
	 * public static void showError(Context context, ResultError error) {
	 * if(error!=null) showToast(context, error.getText()); else
	 * showToast(context, context.getString(R.string.error)); }
	 */

    public static String getCurrencySymbol() {
        return "$";
    }

    public static String MD5Hash(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    public static void hideKeyboard(Context context, View editText) {
        if (editText == null) return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();

    }

    public static void hideKeyboard(Context context, View editText, View requestFocusView) {
        if (editText != null && requestFocusView != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            requestFocusView.requestFocus();
        }
    }

    public static void showKeyboard(Context context, View editText) {
        if (editText == null) return;

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        imm.restartInput(editText);
    }

    public static void showToast(Context context, int testRedId) {
        Toast.makeText(context, context.getString(testRedId), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String text, boolean longer) {
        int duration = longer ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();
    }

    public static void showToast(Context context, int textResId, boolean longer) {
        int duration = longer ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(context, context.getResources().getString(textResId), duration).show();
    }

    public static int getPixels(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (metrics.density * dp + 0.5f);
    }

    public static int getSpPixels(Context context, int sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context
                .getResources().getDisplayMetrics()) + 0.5f);
    }

    public static String getKey() {

        String[] values = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3",
                "4", "5", "6", "7", "8", "9"};

        String out = values[0] + values[18] + values[3] + values[27] + values[0] + values[18]
                + values[32] + values[27] + values[32] + values[18] + values[3] + values[5]
                + values[27] + values[32] + values[18] + values[27] + values[0] + values[3]
                + values[18] + values[27] + values[0] + values[18] + values[31] + values[3];

        return out;
    }

    public static String getFirstName(String name) {

        String firstName = name;

        if (name.contains(" ")) {
            int breakspace = name.indexOf(" ");

            firstName = name.substring(0, breakspace);
        }

        return firstName;
    }

    public static String getLastName(String name) {

        String lastName = "";

        if (name.contains(" ")) {
            int breakspace = name.indexOf(" ");

            lastName = name.substring(breakspace);
        }

        return lastName;
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean valid = false;
        if (!TextUtils.isEmpty(email)) {
            valid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return valid;
    }

    public static void sendEmail(String email, Activity activity) {


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Paynut");

        // TODO Auto-generated method stub
        activity.startActivity(Intent.createChooser(emailIntent, activity.getResources().getString(R.string.dialog_choose_app)));

    }

    public static void navigateTo(Context context, String lat, String lng) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lng + ""));
        context.startActivity(intent);
    }

    public static void sendCSVEmail(String path, String subject, Activity activity) {

        String body = "CSV in attachment.";

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("plain/text");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject + " CSV");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);

        // TODO Auto-generated method stub
        activity.startActivity(Intent.createChooser(sendIntent, activity.getResources().getString(R.string.dialog_choose_app)));

    }

    public static int getStatusBarHeight(Context context) {

        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }

    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    public static int getScreenHeightWithoutStatusBar(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int statusBarHeight = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }

        return size.y - statusBarHeight;
    }

    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.y;
    }

    public static int getRemaingingScreenSpace(Activity activity, int height) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int statusBarHeight = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }

        return size.y - statusBarHeight - getPixels(activity, height);
    }

    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;

        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }

        return actionBarHeight;
    }

    public static Drawable getSelectableBackground(Context context) {

        int[] attrs = new int[]{android.R.attr.selectableItemBackground};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0);
        ta.recycle();

        return drawableFromTheme;
    }

    public static Drawable getSelectableBackgroundBorderless(Context context) {

        int[] attrs = new int[]{android.R.attr.selectableItemBackgroundBorderless};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0);
        ta.recycle();

        return drawableFromTheme;
    }

    public static Intent getOpenFacebookIntent(Context context, String id) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + id)); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sentiapps")); //catches and opens a url to the desired page
        }
    }

    public static void disableTextSelection(EditText editText) {
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    public static String getSimMMC(Context context) {

        String mmc = "";

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null && tm.getSimOperator() != null && tm.getSimOperator().length() > 0) {
            mmc = tm.getSimOperator().substring(0, 3);
        }

        return mmc;

    }

    public static JSONArray concatArray(JSONArray arr1, JSONArray arr2)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr1.length(); i++) {
            result.put(arr1.get(i));
        }
        for (int i = 0; i < arr2.length(); i++) {
            result.put(arr2.get(i));
        }

        return result;
    }

    public static String round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.toString();
    }

    public static String getUniqueId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

        return deviceUuid.toString();
    }

}
