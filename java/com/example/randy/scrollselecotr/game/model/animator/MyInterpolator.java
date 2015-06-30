package com.example.randy.scrollselecotr.game.model.animator;

import android.view.animation.Interpolator;

/**
 * Created by randy on 15-6-8.
 */
public class MyInterpolator implements Interpolator{
    private static final int MOST=5;
    @Override
    public float getInterpolation(float input) {
        if (input<0.5) {
            return input*10;
        } else {
            return (float)((input-0.5)*10);
        }
    }
}
