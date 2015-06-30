package com.example.randy.scrollselecotr.game.model;

import android.graphics.PointF;
import android.util.Log;

import com.example.randy.scrollselecotr.game.model.observer.Observer;

/**
 * Created by randy on 15-6-5.
 */
public class LogicMdel implements Observer{
    // y=kx+b;
    private double k;
    private double b;


    //整个正方形的长宽高啊.
    private double width;
    private double height;


    //下一个路径重点位置位置
//    private double mEndX;
//    private double mENdY;

    private PointF mEndPoint=new PointF();
    //当前位置
    private PointF mCurrentPoint;

    //@onFling
    //路径的起始点
    protected PointF mStart;

    private double r; //圆的半
    private int xDircetion; //TODO:方向,暂时使用,以后可以使用向量代表啊. -1 标示向x正轴方向
    private int yDirection;



    private ChangeListener mListener=null;

    public ChangeListener getmListener() {
        return mListener;
    }

    public void setmListener(ChangeListener mListener) {
        this.mListener = mListener;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }



    public double getR() {
        return r;
    }

//    public float getmViewStartX() {
//        return mViewStartX;
//    }
//
//    public void setmViewStartX(float mViewStartX) {
//        this.mViewStartX = mViewStartX;
//    }
//
//    public float getmViewStartY() {
//        return mViewStartY;
//    }
//
//    public void setmViewStartY(float mViewStartY) {
//        this.mViewStartY = mViewStartY;
//    }



    public void setR(double r) {
        this.r = r;
    }

    public PointF getmEndPoint() {
        return mEndPoint;
    }

    public void setmEndPoint(PointF mEndPoint) {
        this.mEndPoint = mEndPoint;
    }

    public PointF getmCurrentPoint() {
        return mCurrentPoint;
    }

    public void setmCurrentPoint(PointF mCurrentPoint) {
        this.mCurrentPoint = mCurrentPoint;
    }

    public PointF getmStart() {
        return mStart;
    }

    public void setmStart(PointF mStart) {
        this.mStart = mStart;
    }


    public void setTwoPoint(PointF down,PointF start) {

        setmStart(start);
        figureFunction(down,start);
        figurePathLength();

    }
    protected void figureFunction(PointF down,PointF start) {
        if (start==null||down==null) {
            throw new RuntimeException("LogicMdel figureFunction mStart mDown is null");
        }
        this.k=(start.y-down.y)/(start.x-down.x);
        this.b=start.y-this.k*start.x;
        this.xDircetion=(int)(start.x-down.x);
        this.yDirection=(int)(start.y-down.y);
    }


    /**
     * 丑陋的if语句实现,应该可以使用数学给出更加简单的计算
     */
    protected void figurePathLength() {
        if (xDircetion<0) {    //向x负半轴移动
            if (yDirection>0) {    //先y正半轴移动
                /// 情况一  与y轴相碰
                double dotX=r;
                double tempDotY=k*dotX+b;

                //情况二 与上方的x轴碰撞啊
                double dotY=height-r;
                double tempDotX=(dotY-b)/k;

//                比较两种情况,谁先进行
                //由于是向(-1,1)方向进行,所以看x大的,或者y小的就可以啦

                //其实如果是二者相等的话,应该是直接原路返回的啊.
                this.mEndPoint.x =(float)(dotX>=tempDotX?dotX:tempDotX);
                this.mEndPoint.y =(float)(dotY>tempDotY?tempDotY:dotY);

            } else {    //{-1,-1}
                double dotY=r;
                double tempDotX=(dotY-b)/k;

                double dotX=r;
                double tempDotY=k*dotX+b;

                //比较二者,{-1,-1} 方向,其实看
                this.mEndPoint.x =(float)(dotX>tempDotX?dotX:tempDotX);
                this.mEndPoint.y =(float)(dotY>tempDotY?dotY:tempDotY);
            }
        } else {   //x>0
            if (yDirection>0) {   // {1,1}
                double dotX=width-r;
                double tempDotY=k*dotX+b;

                double dotY=height-r;
                double tempDotX=(dotY-b)/k;

                //比较二者,其实是 x,y小的是先碰到的啊.
                this.mEndPoint.x =(float)(dotX>tempDotX?tempDotX:dotX);
                this.mEndPoint.y =(float)(dotY>tempDotY?tempDotY:dotY);

            } else {   // {1,-1}
                double dotX=width-r;
                double tempDotY=k*dotX+b;

                double dotY=r;
                double tempDotX=(dotY-b)/k;

                //比较二者,x越小,越是的,y值越大的越是的
                this.mEndPoint.x =(float)(dotX>tempDotX?tempDotX:dotX);
                this.mEndPoint.y =(float)(dotY>tempDotY?dotY:tempDotY);


            }
        }
    }

    /**
     * 根据动画器的计算出来的x来计算现在view的位置
     * @param x
     * @deprecated  暂时不用了.
     */
    public void setPathX(float x) {
        this.mCurrentPoint.x=x;
        this.mCurrentPoint.y=(float)(k*x+b);

        if (mCurrentPoint.equals(mEndPoint)) {  //这个时候要进行反射啦.
            Log.e("setPathX","startRebound");
        }

    }

    /**
     * 反弹经过的点
     * @param point
     */
    private void onRebound(PointF point) {
        this.k=-k;
        this.b=k* mEndPoint.x + mEndPoint.y;
        //更新啊
        setmStart(point);
        figurePathLength();
        mListener.onRebound(mStart,mEndPoint,2000);
    }

    //更新当前运动的点啊.
    @Override
    public void update(PointF pointF) {
        this.mCurrentPoint=pointF;
        //TODO:这里要进行检查是否有其他的影响路径的物体啊.
        //对于反弹,这个要在刚开始计算路径的时候就计算好重点的位置,如果是其他的加速,减少,等效果
        //在这个地方进行区分
        doUpdate();

    }


    private void doUpdate() {
        if (mCurrentPoint.equals(mEndPoint)) {
            //要进行反弹啦.

        }


    }

    interface  ChangeListener {
        /**
         * 其实速度的改变,就是时间的改变,速度应该也是在这个类中的进行的啊.
         * @param start
         * @param end
         * @param time
         */
        public void onRebound(PointF start,PointF end,long time);

        /**
         * 其他效果的改变,暂时还没有确定呢.
         */
        public void onChange();
    }


}
