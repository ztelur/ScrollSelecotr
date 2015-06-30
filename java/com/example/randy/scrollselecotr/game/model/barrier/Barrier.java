package com.example.randy.scrollselecotr.game.model.barrier;

import android.graphics.PointF;

import com.example.randy.scrollselecotr.game.model.Trace;

/**
 * Created by randy on 15-6-6.
 */
public interface Barrier {
    public void handleMove(Trace trace);

    /**
     * 占据的空间
     */
    public void measure();
    /**
     * 是否包含上边的点
     * @param pointF
     */
    public void contains(PointF pointF);
}
