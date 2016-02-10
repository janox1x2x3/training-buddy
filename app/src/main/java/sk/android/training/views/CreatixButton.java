package sk.android.training.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sk.android.training.R;
import sk.android.training.utils.Utils;

public class CreatixButton extends RelativeLayout {

    private int defaultColor;
    private TextView buttonText;
    private Drawable mDefaultButtonIcon;

    public CreatixButton(Context context) {
        super(context);
    }

    public CreatixButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.layout_creatix_button, this);
        buttonText = (TextView) findViewById(R.id.button_text);
        ImageView buttonIcon = (ImageView) findViewById(R.id.button_icon);
        LinearLayout innerLayout = (LinearLayout) findViewById(R.id.inner_layout);

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CreatixButton);

        buttonText.setText(a.getString(R.styleable.CreatixButton_buttonText));

        mDefaultButtonIcon = a.getDrawable(R.styleable.CreatixButton_buttonIcon);
        if (mDefaultButtonIcon != null) {
            buttonIcon.setImageDrawable(mDefaultButtonIcon);
            buttonIcon.setVisibility(VISIBLE);
        }

        defaultColor = a.getResourceId(R.styleable.CreatixButton_buttonColor, R.layout.button);
        setBackgroundResource(defaultColor);

        findViewById(R.id.creatix_button).setEnabled(a.getBoolean(R.styleable.CreatixButton_buttonColor, true));

        if (a.getBoolean(R.styleable.CreatixButton_centerInnerLayout, false)) {
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(CENTER_IN_PARENT);
            innerLayout.setLayoutParams(lp);
        }

        if (a.getColor(R.styleable.CreatixButton_buttonTextColor, 0) != 0) {
            buttonText.setTextColor(a.getColor(R.styleable.CreatixButton_buttonTextColor, 0));
            buttonText.setPadding(0, Utils.getPixels(context, 10), 0, 0);
        }

        setPadding(0, 0, 0, 0);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        findViewById(R.id.creatix_button).setOnClickListener(l);
        setEnabled(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        findViewById(R.id.creatix_button).setEnabled(enabled);

        disableButton(this, enabled);
    }

    private void disableButton(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                disableButton((ViewGroup) view, enabled);
            }
        }
    }

    public void toggleButton(boolean enable) {

        setEnabled(enable);
        if (enable) {
            buttonText.setTextColor(getResources().getColor(R.color.white));
            setBackgroundRecourseWithPadding(defaultColor);
            if (mDefaultButtonIcon != null) {
                ((ImageView) findViewById(R.id.button_icon)).setImageDrawable(mDefaultButtonIcon);
            }
        } else {
            buttonText.setTextColor(getResources().getColor(android.R.color.tertiary_text_light));
//            setBackgroundRecourseWithPadding(R.drawable.btn_inactive_white);
//            ((ImageView) findViewById(R.id.button_icon)).setImageResource(R.drawable.ico_settleup_gray);
        }
    }

    private void setBackgroundRecourseWithPadding(int recourse) {
        int bottom = getPaddingBottom();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int left = getPaddingLeft();
        setBackgroundResource(recourse);
        setPadding(left, top, right, bottom);
    }

    public void setButtonText(int resId) {
        buttonText.setText(resId);
    }

    public void setButtonText(String text) {
        buttonText.setText(text);
    }
}