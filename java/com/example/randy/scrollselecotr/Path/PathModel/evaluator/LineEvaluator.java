package com.example.randy.scrollselecotr.Path.PathModel.evaluator;

import android.graphics.Point;
import android.graphics.PointF;

import com.example.randy.scrollselecotr.Path.PathModel.util.MathUtils;

/**
 * Created by randy on 15-6-19.
 */
public class LineEvaluator implements PathEvaluator{
    @Override
    public Object evaluate(float distance, Object startValue, Object endValue) {
        //这里对于object如何操作不太清楚啊！！！本项目使用float
        if (startValue instanceof PointF) {
            PointF start=(PointF)startValue;
            PointF end=(PointF)endValue;

            float maxLenght= MathUtils.distance(start,end);
            float maxY=end.y-start.y;
            float maxX=end.x-start.x;
            float figureY=(maxY/maxLenght)*distance;
            float figureX=(maxX/maxLenght)*distance;
            return new PointF(figureX,figureY);
        } else {
            throw new RuntimeException("not default PointF");
        }
    }
}
