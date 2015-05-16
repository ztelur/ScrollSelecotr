package com.example.randy.scrollselecotr;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import android.widget.Scroller;

/**
 * Created by randy on 15-5-14.
 * 制作类似于ios的滑动时间选择器
 *
 * 1
 */
public class ScrollView extends View {
    //内部监听的
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener=null;
    //外部监听手势使用
    private GestureDetector.SimpleOnGestureListener outsideGestureListener=null;
    //外界监听选择项的监听
    private SelectedListener selectedListener=null;
    private GestureDetector mDetector=null;

    /**
     * 滑动数据
     *
     */
    private OverScroller mScroller=null;

    /**
     * 滑动的监听类
     */

    private boolean isPerformingScroll=false;
    private final int MESSAGE_SCROLL=0;
    private final int MESSAGE_JUSTIFY=1;

    private EdgeEffectCompat

    private Handler animationHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    /**
     * paint相关的
     */


    /**
     *
     * @param context
     * @param attrs
     * @param defStyle
     */

    public ScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();
    }
    private void initData() {
        initGestureListener();
        mDetector=new GestureDetector(getContext(),simpleOnGestureListener);
        mScroller=new OverScroller(getContext());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean res=false;
        if (mDetector!=null) {
             res = mDetector.onTouchEvent(event);
        } else {
            res=super.onTouchEvent(event);
        }
        return res;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    //////////////////////////////////////////////////////////////////////////
    ////////////
    ///////////   Listener
    ////////////////////////////////////////////////////////////////////////////
    private void initGestureListener() {
        simpleOnGestureListener=new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //要进行滚动的啊
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                //要停止滑动的
                if (isPerformingScroll) {
                    mScroller.forceFinished(true);
                    clearMessage();
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * 清除handler还未处理的message
     */
    private void clearMessage() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }
    public void setSelectedListener(SelectedListener selectedListener) {
        this.selectedListener=selectedListener;
    }
    public void removeSelectedListener() {
        this.selectedListener=null;
    }


    interface SelectedListener {
        public void selectedOneItem();
    }

}
