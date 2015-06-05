package com.example.randy.scrollselecotr.game.model;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.randy.scrollselecotr.R;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

public class GameActivity extends Activity implements LogicMdel.ReboundListener{
    ///////////////////////////////////////////////////////////////////////////////////////
    //////   动画
    //////////////////////////////////////////////////////////////////////////////////////

    ValueAnimator mAnimator;


    ///////////////////////////////////////////////////////////////////////////////////////
    //////   界面组件
    //////////////////////////////////////////////////////////////////////////////////////
    private ImageView mCircle;
    private LogicMdel mPath;


    ///////////////////////////////////////////////////////////////////////////////////////
    //////   监听
    //////////////////////////////////////////////////////////////////////////////////////

    GestureDetector.SimpleOnGestureListener mGestureListener;
    GestureDetector mDetector;


    ///////////////////////////////////////////////////////////////////////////////////////
    //////   运动相关
    //////////////////////////////////////////////////////////////////////////////////////
    protected boolean isMark;//down动作是否在运动物体上




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.e("GameActivity","onCreate");
        init();
    }
    private void init() {
        Log.e("GameActivity","init");
        initView();
        mPath=new LogicMdel();
        mPath.setmListener(this);
        mGestureListener=new GestureListener();
        mDetector=new GestureDetector(GameActivity.this,mGestureListener);
        initAnimatorListener();

    }
    private void initView() {
        mCircle=(ImageView)findViewById(R.id.circle);

    }
    private void initAnimatorListener(){


        if (mAnimator!=null) {
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentXValue=(Float)animation.getAnimatedValue();
                    Log.d("AnimatorListener the current X value",currentXValue+"");
                    mPath.setPathX(currentXValue);
                    mCircle.setTranslationX(mPath.getmViewLocationX());
                    mCircle.setTranslationY(mPath.getmViewLocationY());



                    //设置ImageView的位置啊.
                }
            });

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e("Gesture","onScroll");
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //在这里获得移动的方向,速度和角度
            Log.e("Gesture","onFing");
                checkDown(e1);
                doMove(e1, e2, velocityX, velocityY);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.e("Gesture","onDown");
            //记录是否在ImageView中啊.
            checkDown(e);
            return super.onDown(e);
        }
    }

    private void checkDown(MotionEvent e) {
        float x=e.getX();
        float y=e.getY();
        final Rect frame=new Rect();
        mCircle.getHitRect(frame);
        if (frame.contains((int)x,(int)y)) {
            isMark=true;
        }
    }
    /**
     *
     * @param e1  The first down motion event that started the fling.
     * @param e2   The move motion event that triggered the current onFling
     * @param velocityX  The velocity of this fling measured in pixels per second along the x axis.
     * @param velocityY  The velocity of this fling measured in pixels per second along the y axis
     */
    private void doMove(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY) {
        if (mPath==null) {
            mPath=new LogicMdel();
        }
        PointF down=new PointF(e1.getX(),e1.getY());
        PointF start=new PointF(e2.getX(),e2.getY());
        mPath.setTwoPoint(down,start);
        //设置路径的起始位置啊.



        mAnimator=ValueAnimator.ofFloat(start.x,(float)mPath.getmDotX());
        //TODO:其实这里要根据速率进行计算的啊.
        //暂时是按照x的距离开始的啊.
        long xTime=(long)((start.x-mPath.getmDotX())/velocityX);
        long yTime=(long)((start.y-mPath.getmDotY())/velocityY);

        //TODO:不知道如何处理,先取最大的
        long duration=xTime>yTime?xTime:yTime;
        mAnimator.setDuration(2000);
        initAnimatorListener();
        mAnimator.start();
    }

    @Override
    public void onRebound(float startX,float endX,long time) {
        mAnimator.cancel();
        mAnimator=ValueAnimator.ofFloat(startX,endX);
        mAnimator.setDuration(2000);
        initAnimatorListener();
        mAnimator.start();
    }
}
