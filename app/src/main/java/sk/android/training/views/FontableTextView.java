package sk.android.training.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import sk.android.training.R;
import sk.android.training.utils.UiUtil;
import sk.android.training.utils.listeners.TouchActionListener;


public class FontableTextView extends TextView {

    private TouchActionListener touchActionListener;
    private OnTouchListener clickableTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub

            Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(),
                    v.getBottom());
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            } else if (event.getAction() == MotionEvent.ACTION_MOVE
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop()
                        + (int) event.getY())
                        || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop()
                        + (int) event.getY())) {
                    setTextColor(getResources().getColor(android.R.color.holo_blue_light));

                    touchActionListener.onTouch();
                }
            }
            return true;

        }
    };

    public FontableTextView(Context context) {
        super(context);
    }

    public FontableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        UiUtil.setCustomFont(
                this,
                context,
                attrs,
                R.styleable.com_creatix_cratefree_views_FontableTextView,
                R.styleable.com_creatix_cratefree_views_FontableTextView_font);
    }

    public FontableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        UiUtil.setCustomFont(
                this,
                context,
                attrs,
                R.styleable.com_creatix_cratefree_views_FontableTextView,
                R.styleable.com_creatix_cratefree_views_FontableTextView_font);
    }

    public void registerClickableTouchListener(
            TouchActionListener touchActionListener) {

        this.touchActionListener = touchActionListener;
        setOnTouchListener(clickableTouchListener);

    }
}