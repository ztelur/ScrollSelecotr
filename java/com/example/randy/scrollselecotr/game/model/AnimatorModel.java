package com.example.randy.scrollselecotr.game.model;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.util.Log;

import com.example.randy.scrollselecotr.game.model.animator.PointFAnimatorFactory;
import com.example.randy.scrollselecotr.game.model.observer.Observer;
import com.example.randy.scrollselecotr.game.model.observer.Subject;

/**
 * Created by randy on 15-6-6.
 */
public class AnimatorModel extends Subject {
    protected ValueAnimator mAnimator;

    private PointF mCurrentPoint;

    public void initAnimator(PointF start,PointF end,long time) {
        mAnimator=(ValueAnimator) PointFAnimatorFactory.getPointFAnimator(start,end);
        initAnimatorListener();
        mAnimator.setDuration(time);
        //TODO:其实这里要根据速率进行计算的啊.
        //暂时是按照x的距离开始的啊.
        //TODO:不知道如何处理,先取最大的
    }
    public void reConfigAnimator(PointF start,PointF end,long time) {
        mAnimator=(ValueAnimator) PointFAnimatorFactory.getPointFAnimator(start,end);
        initAnimatorListener();
        mAnimator.setDuration(time);
    }
    public void start() {
        mAnimator.start();
    }
    public void cancel() {
        mAnimator.cancel();
    }

    private void initAnimatorListener(){
        if (mAnimator!=null) {
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    PointF currentXValue=(PointF)animation.getAnimatedValue();
                    mCurrentPoint=currentXValue;
                    notifyObserver();
//                    mCircle.setTranslationX(mPath.getmViewLocationX());
//                    mCircle.setTranslationY(mPath.getmViewLocationY());
                    //设置ImageView的位置啊.
                }
            });

        }
    }

    @Override
    public void notifyObserver() {
        for(Observer ob : mObservers ) {
            ob.update(mCurrentPoint);
        }
    }
}
