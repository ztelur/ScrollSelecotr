package com.example.randy.scrollselecotr.Path.PathModel.Builder;

import android.graphics.PointF;

import com.example.randy.scrollselecotr.Path.PathModel.PathNode;
import com.example.randy.scrollselecotr.Path.PathModel.evaluator.LineEvaluator;
import com.example.randy.scrollselecotr.Path.PathModel.evaluator.PathEvaluator;

/**
 * Created by randy on 15-6-19.
 *
 * 添加路径的时候，只需要添加起点和终点，
 */
public class NodeBuilder extends PathBuilder{
    public NodeBuilder() {
        super();
    }
    @Override
    public void addPath(Object start, Object end, PathEvaluator evaluator) {
        PathNode node=new PathNode(start,end);
        //这里需要使用直线来计算啦。
        node.setEvaluator(new LineEvaluator());
        //
        if (cur!=null) {
            cur.setNext(node);
            cur=node;
        } else {   //说明是第一次，说明都没有啊
            head=node;
            cur=node;
        }
    }
}
