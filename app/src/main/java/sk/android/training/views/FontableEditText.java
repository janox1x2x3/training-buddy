package sk.android.training.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import sk.android.training.R;
import sk.android.training.utils.UiUtil;

public class FontableEditText extends EditText {

    AttributeSet mAttrs;

    public FontableEditText(Context context) {
        super(context);
    }

    public FontableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttrs(attrs);
        UiUtil.setCustomFont(this, context, attrs,
                R.styleable.com_creatix_cratefree_views_FontableEditText,
                R.styleable.com_creatix_cratefree_views_FontableEditText_font);
    }

    public FontableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttrs(attrs);
        UiUtil.setCustomFont(this, context, attrs,
                R.styleable.com_creatix_cratefree_views_FontableEditText,
                R.styleable.com_creatix_cratefree_views_FontableEditText_font);
    }

    public AttributeSet getAttrs() {
        return mAttrs;
    }

    public void setAttrs(AttributeSet mAttrs) {
        this.mAttrs = mAttrs;
    }
}