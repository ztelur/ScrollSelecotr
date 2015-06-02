package com.example.randy.scrollselecotr.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.randy.scrollselecotr.R;
import com.example.randy.scrollselecotr.ui.droprefresh.AbstractDropRefreshView;

/**
 * Created by randy on 15-6-2.
 */
public class TestLinearLayout extends AbstractDropRefreshView{
    public TestLinearLayout(Context context) {
        this(context, null, 0);
    }

    public TestLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSelfView(context);
    }
    private void initSelfView(Context context) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    @Override
    public View getDropShowView() {
        return findViewById(R.id.header);
    }
}
