package sk.android.training;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import sk.android.training.utils.listeners.OnBackPressedListener;


public abstract class BaseActivity extends AppCompatActivity {

    protected final static int ANIM_FADE = 0;
    protected final static int ANIM_VERTICAL = 1;
    protected final static int ANIM_HORIZONTAL = 2;
    protected Resources res;
    protected boolean justCreated = true;
    protected boolean enableBackButton = true;

    protected OnBackPressedListener backpressedListener;

    protected Integer navigationIcon;

    private boolean isPaused = false;

    public boolean isPaused() {
        return isPaused;
    }

    protected Bundle savedInstanceState;

    public Integer getNavigationIcon() {
        return navigationIcon;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResource());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.savedInstanceState = savedInstanceState;
        enableBackButton = true;

        justCreated = true;
        isPaused = false;

        res = getResources();
    }

    protected abstract int getLayoutResource();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        isPaused = true;

        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        isPaused = false;

        super.onResume();

        if (justCreated) {
            justCreated = false;
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

//        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

//        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

//        if (apiCall != null) {
//            apiCall.cancel(true);
//        }
    }

    public void toggleHomeBackButton(boolean enable) {

        if (enable) {
            enableBackButton = true;
        } else {
            enableBackButton = false;
        }

    }

    /**
     * Swaps fragments in the main content view
     */
    protected void performTransaction(Fragment frag, boolean addToBackStack, String TAG, int anim) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (anim) {
            case ANIM_FADE:
                ft.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                break;

            case ANIM_HORIZONTAL:
                ft.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right);
                break;

            case ANIM_VERTICAL:
                ft.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, R.anim.slide_in_bottom, R.anim.slide_out_top);
                break;

            default:
                break;
        }

        if (frag.getTag() != null) {
            ft.remove(frag);
        }

//        ft.replace(R.id.fragment_container, frag, TAG);
//        if (addToBackStack) {
//            ft.addToBackStack(TAG);
//        }
//        ft.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (backpressedListener == null) {
            super.onBackPressed();
        } else {
            backpressedListener.onBackPressed();
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.backpressedListener = onBackPressedListener;
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setStatusBarColor(color);
        }
    }

    /**
     * Switch between the views
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final View toShow, final View toHide) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            toShow.setVisibility(View.VISIBLE);
            toShow.animate().setDuration(shortAnimTime).alpha(1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            toShow.setVisibility(View.VISIBLE);
                        }
                    });

            toHide.setVisibility(View.VISIBLE);
            toHide.animate().setDuration(shortAnimTime).alpha(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            toHide.setVisibility(View.GONE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            toShow.setVisibility(View.VISIBLE);
            toHide.setVisibility(View.GONE);
        }
    }
}
