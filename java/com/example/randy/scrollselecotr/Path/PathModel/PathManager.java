package com.example.randy.scrollselecotr.Path.PathModel;

import com.example.randy.scrollselecotr.Path.PathModel.Builder.NodeBuilder;
import com.example.randy.scrollselecotr.Path.PathModel.Builder.PathBuilder;

/**
 * Created by randy on 15-6-18.
 */
public class PathManager {
    public final static int TYPE_NODE=1;  //表示只给出点，点与点之间使用直线连接
    public final static int TYPE_CONNECT=2; //表示必须是一个连续的路径
    public final static int TYPE_UNCONNECT=3; //表示可以是不连续的路径
    /**
     *
     * @param type
     * @return
     */
    public PathBuilder generateBuiler(int type) {
        PathBuilder res;
        switch (type) {
            case TYPE_NODE:
                res=new NodeBuilder();
                break;
            default:
                res=new NodeBuilder();
        }
        return res;
    }

}
