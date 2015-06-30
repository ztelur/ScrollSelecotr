package com.example.randy.scrollselecotr.Path.PathModel.interpolator;

/**
 * Created by randy on 15-6-18.
 * 加速器
 */
public interface Interpolator {
    /**
     * 输入时间，获得沿着路径，到起点的距离
     * @param input  时间
     * @return
     */
    public float getInterpolation(float input);
}
