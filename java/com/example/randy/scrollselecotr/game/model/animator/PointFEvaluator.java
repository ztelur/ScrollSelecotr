package com.example.randy.scrollselecotr.game.model.animator;

import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by randy on 15-6-6.
 */
public class PointFEvaluator implements TypeEvaluator{
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        PointF startPoint=(PointF)startValue;
        PointF endPoint=(PointF)endValue;
        float x=startPoint.x+fraction*(endPoint.x-startPoint.x);
        float y=startPoint.y+fraction*(endPoint.y-startPoint.y);
        return new PointF(x,y);
    }
}
