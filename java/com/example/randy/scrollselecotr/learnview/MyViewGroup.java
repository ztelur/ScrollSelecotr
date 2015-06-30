package com.example.randy.scrollselecotr.learnview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

/**
 * Created by randy on 15-6-10.
 */
public class MyViewGroup extends LinearLayout{
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
