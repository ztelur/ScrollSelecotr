package com.example.randy.scrollselecotr.Path.PathModel.Builder;

import android.graphics.PointF;

import com.example.randy.scrollselecotr.Path.PathModel.Path;
import com.example.randy.scrollselecotr.Path.PathModel.PathNode;
import com.example.randy.scrollselecotr.Path.PathModel.evaluator.PathEvaluator;

/**
 * Created by randy on 15-6-18.
 */
public  abstract class PathBuilder {
    protected PathNode head=null;
    protected PathNode cur=null;
    public PathBuilder() {

    }
    public Path generatePath() {
        return head==null?null:head;
    }
    public abstract void addPath(Object start, Object end, PathEvaluator evaluator);

}
