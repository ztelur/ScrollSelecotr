package com.example.randy.scrollselecotr.Path.PathModel.evaluator;

import android.graphics.PointF;

/**
 * Created by randy on 15-6-18.
 */
public interface PathEvaluator {
    /**
     * 根据起点，终点，和距离，计算出来路径上的点啊
     * @param distance
     * @param startValue
     * @param endValue
     * @return
     */
    public Object evaluate(float distance, Object startValue, Object endValue);

}
