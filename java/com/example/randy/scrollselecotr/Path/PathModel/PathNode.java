package com.example.randy.scrollselecotr.Path.PathModel;

import android.graphics.PointF;

import com.example.randy.scrollselecotr.Path.PathModel.evaluator.PathEvaluator;

/**
 * Created by randy on 15-6-18.
 *
 *  代表自定义路径中一段的
 */
public class PathNode extends Path{

    private PathNode next;

    public PathNode getNext() {
        return next;
    }

    public void setNext(PathNode next) {
        this.next = next;
    }

    public PathNode(Object start,Object end) {
        this(start,end,null);
    }
    public PathNode(Object start,Object end,PathEvaluator evaluator) {
        setStart(start);
        setEnd(end);
        setEvaluator(evaluator);
    }



    @Override
    public Object locate(float distance) {
        float self_length=getLength();
        //路径包括起点不包括重点啊。所以是等于啊。
        if (distance>=self_length) {
            return next.locate(distance-self_length);
        } else {
            return this.dolocate(distance);
        }
    }

}
