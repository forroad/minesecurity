package com.ycjw.minesecurity.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class StudyWebView extends WebView{
    public StudyWebView(Context context) {
        super(context);
    }

    public StudyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StudyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StudyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onTouchScreenListener != null){
            onTouchScreenListener.onTouchScreen();
        }
        return super.onTouchEvent(event);
    }

    private OnTouchScreenListener onTouchScreenListener = null;

    public interface OnTouchScreenListener {
        void onTouchScreen();
    }

    public void setOnTouchScreenListener(OnTouchScreenListener onTouchScreenListener) {
        this.onTouchScreenListener = onTouchScreenListener;
    }
}
