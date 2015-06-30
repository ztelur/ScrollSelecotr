package com.example.randy.scrollselecotr.Path.PathModel.util;

import android.graphics.PointF;

/**
 * Created by randy on 15-6-19.
 */
public class MathUtils {
    public static float distance(PointF one,PointF two) {
        float y=two.y-one.y;
        float x=two.x-one.x;
        float sum=y*y+x*x;
        return (float)Math.sqrt(sum);
    }
}
