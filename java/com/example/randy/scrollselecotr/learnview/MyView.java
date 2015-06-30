package com.example.randy.scrollselecotr.learnview;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by randy on 15-6-10.
 */
public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("MyView","onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
