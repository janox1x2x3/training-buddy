package sk.android.training.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import sk.android.training.R;
import sk.android.training.utils.UiUtil;


public class FontableRadioButton extends RadioButton {

    public FontableRadioButton(Context context) {
        super(context);
    }

    public FontableRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        UiUtil.setCustomFont(this, context, attrs,
                R.styleable.com_creatix_cratefree_views_FontableRadioButton,
                R.styleable.com_creatix_cratefree_views_FontableRadioButton_font);
    }

    public FontableRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        UiUtil.setCustomFont(this, context, attrs,
                R.styleable.com_creatix_cratefree_views_FontableRadioButton,
                R.styleable.com_creatix_cratefree_views_FontableRadioButton_font);
    }
}