package com.example.randy.scrollselecotr.game.model;

import android.graphics.PointF;
import android.util.Log;

/**
 * Created by randy on 15-6-5.
 */
public class LogicMdel {
    // y=kx+b;
    private double k;
    private double b;

    //整个正方形的长宽高啊.
    private double width;
    private double height;


    //下一个碰撞点的位置
    private double mDotX;
    private double mDotY;


    //路线的起始位置啊.
    private float mViewStartX;
    private float mViewStartY;
    //当前的位置啊.
    private float mViewLocationX;
    private float mViewLocationY;



    private double r; //圆的半径

    private int xDircetion; //TODO:方向,暂时使用,以后可以使用向量代表啊. -1 标示向x正轴方向
    private int yDirection;
    //@onFling
    protected PointF mDown;
    protected PointF mStart;


    private ReboundListener mListener=null;

    public ReboundListener getmListener() {
        return mListener;
    }

    public void setmListener(ReboundListener mListener) {
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

    public double getmDotX() {
        return mDotX;
    }

    public void setmDotX(double mDotX) {
        this.mDotX = mDotX;
    }

    public double getmDotY() {
        return mDotY;
    }

    public void setmDotY(double mDotY) {
        this.mDotY = mDotY;
    }

    public double getR() {
        return r;
    }

    public float getmViewStartX() {
        return mViewStartX;
    }

    public void setmViewStartX(float mViewStartX) {
        this.mViewStartX = mViewStartX;
    }

    public float getmViewStartY() {
        return mViewStartY;
    }

    public void setmViewStartY(float mViewStartY) {
        this.mViewStartY = mViewStartY;
    }

    public float getmViewLocationX() {
        return mViewLocationX;
    }

    public void setmViewLocationX(float mViewLocationX) {
        this.mViewLocationX = mViewLocationX;
    }

    public float getmViewLocationY() {
        return mViewLocationY;
    }

    public void setmViewLocationY(float mViewLocationY) {
        this.mViewLocationY = mViewLocationY;
    }

    public void setR(double r) {
        this.r = r;
    }

    public PointF getmDown() {
        return mDown;
    }

    public void setmDown(PointF mDown) {
        this.mDown = mDown;
    }

    public PointF getmStart() {
        return mStart;
    }

    public void setmStart(PointF mStart) {
        this.mStart = mStart;
    }
    public void setTwoPoint(PointF down,PointF start) {
        setmDown(down);
        setmStart(start);
        figureFunction();
        figurePathLength();

    }
    protected void figureFunction() {
        if (mStart==null||mDown==null) {
            throw new RuntimeException("LogicMdel figureFunction mStart mDown is null");
        }
        this.k=(mStart.y-mDown.y)/(mStart.x-mDown.x);
        this.b=mStart.y-this.k*mStart.x;
        this.xDircetion=(int)(mStart.x-mDown.x);
        this.yDirection=(int)(mStart.y-mDown.y);
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
                this.mDotX=dotX>=tempDotX?dotX:tempDotX;
                this.mDotY=dotY>tempDotY?tempDotY:dotY;

            } else {    //{-1,-1}
                double dotY=r;
                double tempDotX=(dotY-b)/k;

                double dotX=r;
                double tempDotY=k*dotX+b;

                //比较二者,{-1,-1} 方向,其实看
                this.mDotX=dotX>tempDotX?dotX:tempDotX;
                this.mDotY=dotY>tempDotY?dotY:tempDotY;
            }
        } else {   //x>0
            if (yDirection>0) {   // {1,1}
                double dotX=width-r;
                double tempDotY=k*dotX+b;

                double dotY=height-r;
                double tempDotX=(dotY-b)/k;

                //比较二者,其实是 x,y小的是先碰到的啊.
                this.mDotX=dotX>tempDotX?tempDotX:dotX;
                this.mDotY=dotY>tempDotY?tempDotY:dotY;

            } else {   // {1,-1}
                double dotX=width-r;
                double tempDotY=k*dotX+b;

                double dotY=r;
                double tempDotX=(dotY-b)/k;

                //比较二者,x越小,越是的,y值越大的越是的
                this.mDotX=dotX>tempDotX?tempDotX:dotX;
                this.mDotY=dotY>tempDotY?dotY:tempDotY;


            }
        }
    }

    /**
     * 根据动画器的计算出来的x来计算现在view的位置
     * @param x
     */
    public void setPathX(float x) {
        this.mViewLocationX=x;
        this.mViewLocationY=(float)(k*x+b);

        if (mViewLocationX==mDotX||mViewLocationY==mDotY) {  //这个时候要进行反射啦.
            Log.e("setPathX","startRebound");
            onRebound(mViewLocationX,mViewLocationY);
        }

    }

    /**
     * 反弹啦.
     */
    private void onRebound(float x,float y) {
        this.k=-k;
        this.b=k*mDotX+mDotY;
        //更新啊
        setmStart(new PointF(x,y));
        figurePathLength();
        mListener.onRebound(mViewLocationX,(float)this.mDotX,10L);
    }
    interface  ReboundListener {
        public void onRebound(float startX,float endX,long time);
    }
}
