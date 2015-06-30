package com.example.randy.scrollselecotr.game.model.barrier;

import android.graphics.PointF;
import android.graphics.RectF;

import com.example.randy.scrollselecotr.game.model.Trace;

/**
 * Created by randy on 15-6-6.
 */
public class AccelerateBarrier implements Barrier{
    @Override
    public void handleMove(Trace trace) {
        trace.speed+=10;
    }

    @Override
    public void measure() {

    }

    @Override
    public void contains(PointF pointF) {

    }
}
