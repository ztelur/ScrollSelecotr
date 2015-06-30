package com.example.randy.scrollselecotr.game.model;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.randy.scrollselecotr.R;
import com.example.randy.scrollselecotr.game.model.animator.PointFAnimatorFactory;
import com.example.randy.scrollselecotr.game.model.observer.Observer;

public class GameActivity extends Activity implements Observer{

    ActionControl mController;

    ///////////////////////////////////////////////////////////////////////////////////////
    //////   界面组件
    //////////////////////////////////////////////////////////////////////////////////////
    private ImageView mCircle;



    ///////////////////////////////////////////////////////////////////////////////////////
    //////   监听
    //////////////////////////////////////////////////////////////////////////////////////

    GestureDetector.SimpleOnGestureListener mGestureListener;
    GestureDetector mDetector;


    ///////////////////////////////////////////////////////////////////////////////////////
    //////   运动相关
    //////////////////////////////////////////////////////////////////////////////////////
    protected boolean isMark;//down动作是否在运动物体上


    //////////////////////////////////////////////////////////////////////////////////////
    ////////////  view当前的位置
    ////////////////////////////////////////////////////////////////////////////////////
    private PointF mCurrentPoint;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.e("GameActivity","onCreate");
        init();
    }
    private void init() {
        Log.e("GameActivity","init");
        initView();
        mController=new ActionControl(this);
        mGestureListener=new GestureListener();
        mDetector=new GestureDetector(GameActivity.this,mGestureListener);
    }
    private void initView() {
        ((ViewStub)findViewById(R.id.lastupdate_time)).inflate();
        mCircle=(ImageView)findViewById(R.id.circle);
        mCircle.animate().rotationX(0).rotation(1f).setDuration(100);
//        LayoutInflater
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
//        LayoutInflater
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.mDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
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
            if (isMark) {

                //转换坐标系啊.
                float downX=e1.getX();
                float downY=e1.getY();

                float startX=e2.getX();
                float startY=e2.getY();

                // 获取屏幕长和高
                int width = GameActivity.this.getWindowManager().getDefaultDisplay().getWidth();
                int height = GameActivity.this.getWindowManager().getDefaultDisplay().getHeight();
                PointF down=new PointF(width-downX,height-downY);
                PointF start=new PointF(width-startX,height-startX);
                mController.setWidthAndHeight(width,height);

                mController.doMove(down,start, velocityX, velocityY);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.e("Gesture","onDown");
            //记录是否在ImageView中啊.
            checkDown(e);
            return true;
        }
    }

    private void checkDown(MotionEvent e) {
        Log.e("GameActivity","checkDown");
        float x=e.getX();
        float y=e.getY();
        Rect frame=new Rect();
        frame=getHitRect(mCircle);
        Log.e("hitRect ","left:"+frame.left+" top"+frame.top+" right: "+frame.right+" botoom"+ frame.bottom+
                                                            "the x "+x+" the y"+y);
        if (frame.contains((int)x,(int)y)) {
            Log.e("GameActivity","checkDown true");
            isMark=true;
        }
    }
    public Rect getHitRect(View child){
        Rect frame = new Rect();
        frame.left = child.getLeft();
        frame.right = child.getRight();
        frame.top = child.getTop();
        frame.bottom = child.getBottom();
        return frame;
    }

    @Override
    public void update(PointF pointF) {
        this.mCurrentPoint=pointF;
        Log.e("GameActivity","update");
        moveView();
    }
    private void moveView() {
        if (mCurrentPoint==null) {
            throw new RuntimeException("GameActivty moveView: mCurrentPoint is null");
        }
        mCircle.setTranslationY(mCurrentPoint.y);
        mCircle.setTranslationX(mCurrentPoint.x);
    }
}
