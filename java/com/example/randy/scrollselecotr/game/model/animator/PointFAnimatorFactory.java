package com.example.randy.scrollselecotr.game.model.animator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.PointF;

import com.example.randy.scrollselecotr.game.model.AnimatorModel;

/**
 * Created by randy on 15-6-6.
 */
public class PointFAnimatorFactory {
    public static Animator getPointFAnimator(PointF start,PointF end) {
        Animator res=ValueAnimator.ofObject(new PointFEvaluator(),start,end);
        res.setInterpolator(new MyInterpolator());
        return res;
    }
}
