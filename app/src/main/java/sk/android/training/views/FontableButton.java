package sk.android.training.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import sk.android.training.R;
import sk.android.training.utils.UiUtil;

public class FontableButton extends Button {

    public FontableButton(Context context) {
        super(context);
    }

    public FontableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        UiUtil.setCustomFont(this, context, attrs,
                R.styleable.com_creatix_cratefree_views_FontableButton,
                R.styleable.com_creatix_cratefree_views_FontableButton_font);
    }

    public FontableButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        UiUtil.setCustomFont(this, context, attrs,
                R.styleable.com_creatix_cratefree_views_FontableButton,
                R.styleable.com_creatix_cratefree_views_FontableButton_font);
    }
}