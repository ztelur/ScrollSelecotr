package com.example.randy.scrollselecotr.game.model;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import com.example.randy.scrollselecotr.game.model.animator.PointFAnimatorFactory;
import com.example.randy.scrollselecotr.game.model.observer.Observer;

/**
 * Created by randy on 15-6-5.
 * 小球的运动的控制类啊
 * C
 */
public class ActionControl implements LogicMdel.ChangeListener{
    private LogicMdel mPath;
    private AnimatorModel mAnimator;
    private Observer view;

    public ActionControl(Observer view) {
        mAnimator=new AnimatorModel();
        mPath=new LogicMdel();
        this.view=view;
        setAnimatorListener();
    }

    public void setAnimatorListener() {
        mAnimator.attach(mPath);
        mAnimator.attach(view);
    }
    /**
     *
     * @param e1  The first down motion event that started the fling.
     * @param e2   The move motion event that triggered the current onFling
     * @param velocityX  The velocity of this fling measured in pixels per second along the x axis.
     * @param velocityY  The velocity of this fling measured in pixels per second along the y axis
     */
    public void doMove(PointF down,PointF start,float velocityX,float velocityY) {
        if (mPath==null) {
            mPath=new LogicMdel();
        }
        mPath.setTwoPoint(down,start);
        //设置路径的起始位置啊.
        mAnimator.initAnimator(start,mPath.getmEndPoint(),2000);
        mAnimator.start();
    }


    @Override
    public void onRebound(PointF start, PointF end, long time) {
        mAnimator.cancel();
        mAnimator.reConfigAnimator(start,end,time);
        mAnimator.start();
    }

    @Override
    public void onChange() {

    }
    public void setWidthAndHeight(int width,int height) {
        mPath.setWidth(width);
        mPath.setHeight(height);
    }
}
