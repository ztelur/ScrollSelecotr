package com.example.randy.scrollselecotr.Path.PathModel;

import android.graphics.PointF;

import com.example.randy.scrollselecotr.Path.PathModel.evaluator.PathEvaluator;

/**
 * Created by randy on 15-6-18.
 */
public abstract class Path {
    private Object start;
    private Object end;
    private float length;
    //表示函数
    private PathEvaluator evaluator;


    /**
     * 调用evaluator的方法啊。
     * @param distance
     * @return
     */
    public  Object dolocate(float distance) {
        if (evaluator == null) {
            throw new RuntimeException("evaluator is null when call locate");
        }
        return evaluator.evaluate(distance, start, end);

    }
    public abstract Object locate(float distance);

    public Object getStart() {
        return start;
    }

    public void setStart(Object start) {
        this.start = start;
    }

    public Object getEnd() {
        return end;
    }

    public void setEnd(Object end) {
        this.end = end;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }


    public PathEvaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(PathEvaluator evaluator) {
        this.evaluator = evaluator;
    }
}
