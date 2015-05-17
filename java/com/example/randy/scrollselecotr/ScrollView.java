package com.example.randy.scrollselecotr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

/**
 * Created by randy on 15-5-14.
 * 制作类似于ios的滑动时间选择器
 *
 * 1
 */
public class ScrollView extends View {
    private static final String TAG="ScrollView";
    //TODO:测试使用dataList,之后使用datper来获得显示数据啊
    private ArrayList<String> dataList=new ArrayList<String>();

    /**
     *      当前选中的item的位置
     */
    private int choosedItemPosition;
    /**
     * 最多显示的item的数量,一般都是基数个啊
     * 默认为3
     */
    private int showItemNum=3;

    //内部监听的
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener=null;
    //外部监听手势使用
    private GestureDetector.SimpleOnGestureListener outsideGestureListener=null;
    //外界监听选择项的监听
    private SelectedListener selectedListener=null;
    private GestureDetector mDetector=null;

    /**
     * 滑动数据
     *
     */
    private OverScroller mScroller=null;

    /**
     * 滑动的监听类
     */

    private boolean isPerformingScroll=false;
    private final int MESSAGE_SCROLL=0;
    private final int MESSAGE_JUSTIFY=1;



    /**
     * paint的地方
     */
    // Viewport extremes. See mCurrentViewport for a discussion of the viewport.
    private static final float AXIS_X_MIN = -1f;
    private static final float AXIS_X_MAX = 1f;
    private static final float AXIS_Y_MIN = -1f;
    private static final float AXIS_Y_MAX = 1f;

    /**由于mCurrentViewport是代表整个画布,mContentRect是代表当前显示的大小,所以,
    *要转换过去,而mCurrentViewport只是代表整个画布,而不是真正的整个画布
    **/
    private RectF mCurrentViewport=new RectF(AXIS_X_MIN,AXIS_Y_MIN,AXIS_X_MAX,AXIS_Y_MAX);
    /**
     *
     */
    private Rect mContentRect=new Rect();

    private Point mSurfaceSizeBuffer =new Point();
    /**
     * 显示的数据
     */
    private List<String> data=new ArrayList<String>();

    private boolean canScrollX=true;   //是否可以左右进行滑动
    private boolean canScrollY=true;  //是否可以上下进行滑动

    private EdgeEffectCompat mEdgeEffectTop;
    private EdgeEffectCompat mEdgeEffectBottom;
    private EdgeEffectCompat mEdgeEffectRight;
    private EdgeEffectCompat mEdgeEffectLeft;

    private boolean mEdgeEffectRightActive=false;
    private boolean mEdgeEffectLeftActive=false;
    private boolean mEdgeEffectTopActive=false;
    private boolean mEdgeEffectBottomActive=false;


    private float mCurrentWidth;
    private float mCurrentHeight;


    private Handler animationHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    /**
     * paint相关的
     */
    private Paint mAxisPaint;
    private Paint mTwoLinePaint;
    private Paint mTextPaint;
    private Path mFristLine;
    private Path mSecondLine;


    /**
     *
     * @param context
     * @param attrs
     * @param defStyle
     */

    public ScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Sets up edge effects
        Log.e(TAG,"effect init");
        mEdgeEffectLeft = new EdgeEffectCompat(context);
        mEdgeEffectTop = new EdgeEffectCompat(context);
        mEdgeEffectRight = new EdgeEffectCompat(context);
        mEdgeEffectBottom = new EdgeEffectCompat(context);

        initData();
    }

    public ScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);  //error:super(),这样会调用super的,而不是this的
    }

    private void initData() {
        initGestureListener();
        mDetector=new GestureDetector(getContext(),simpleOnGestureListener);
        mScroller=new OverScroller(getContext());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean res=false;
        if (mDetector!=null) {
             res = mDetector.onTouchEvent(event);
        } else {
            res=super.onTouchEvent(event);
        }
        return res;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    //////////////////////////////////////////////////////////////////////////
    ////////////
    ///////////   Listener
    ////////////////////////////////////////////////////////////////////////////
    private void initGestureListener() {
        simpleOnGestureListener=new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                /**要进行滚动的啊
                计算滚动的距离,更新mContetnRect的值
                 {@link mCurrentViewport}
                **/
                float viewportOffsetX=distanceX*mCurrentViewport.width()/mContentRect.width();
                float viewportOffsetY=distanceY*mCurrentViewport.height()/mContentRect.height();

                computeScrollSurfaceSize(mSurfaceSizeBuffer);

                /**
                 * 计算滑动的x,y值
                 * {@link computeScrollSurfaceSize()}
                 * 如果不经zoom的,只进行移动的话
                 * mCurrentViewport.left=x_min
                 * 计算在当前mSurfaceSize下的滑动的距离????
                 */

                int scrolledX=(int)(mSurfaceSizeBuffer.x*(mCurrentViewport.left+viewportOffsetX-
                                        AXIS_X_MIN)/(AXIS_X_MAX-AXIS_X_MIN));
                int scrolledY=(int)(mSurfaceSizeBuffer.y*(AXIS_Y_MAX-mCurrentViewport.bottom-
                                        viewportOffsetY)/(AXIS_Y_MAX-AXIS_Y_MIN));

                boolean tcanScrollX=canScrollX&&(
                            mCurrentViewport.left>AXIS_X_MIN
                         ||mCurrentViewport.right<AXIS_X_MAX
                        );
                boolean tcanScrollY=canScrollY&&(
                        mCurrentViewport.top>AXIS_Y_MIN
                        ||mCurrentViewport.bottom<AXIS_Y_MAX
                        );
                //改变了mCurrentViewport的啊.
                setmCurrentViewportBottomAndLeft(mCurrentViewport.left+viewportOffsetX,
                                                        mCurrentViewport.bottom+viewportOffsetY);
                //对四个角的effect进行onPull
                if (canScrollX&&scrolledX<0) {
                    //??????TODO: scrolledX/(float)mContentRect.width
                    mEdgeEffectLeft.onPull(scrolledX/(float)mContentRect.width());
                    mEdgeEffectLeftActive=true;
                }
                /**
                 *  scrolledX+mContetnRect.width>mSurfaceSizeBuffer.x
                 */
                if (canScrollX&&scrolledX> mSurfaceSizeBuffer.x-mContentRect.width()) {
                    mEdgeEffectRight.onPull((scrolledX-mSurfaceSizeBuffer.x+mContentRect.width())
                                    /(float)mContentRect.width());
                    mEdgeEffectRightActive=true;
                }
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                //要停止滑动的
                if (isPerformingScroll) {
                    mScroller.forceFinished(true);
                    clearMessage();
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * Computes the current scrollable surface size, in pixels. For example, if the entire chart
     * area is visible, this is simply the current size of {@link #mContentRect}. If the chart
     * is zoomed in 200% in both directions, the returned size will be twice as large horizontally
     * and vertically.
     * @param out
     */
    private void computeScrollSurfaceSize(Point out) {
        out.set(
                (int) (mContentRect.width()*(AXIS_X_MAX-AXIS_X_MIN)/mCurrentViewport.width()),
                (int) (mContentRect.height()*(AXIS_Y_MAX-AXIS_Y_MIN)/mCurrentViewport.height())
        );
    }

    /**
     * 清除handler还未处理的message
     */
    private void clearMessage() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }
    public void setSelectedListener(SelectedListener selectedListener) {
        this.selectedListener=selectedListener;
    }
    public void removeSelectedListener() {
        this.selectedListener=null;
    }


    interface SelectedListener {
        public void selectedOneItem();
    }
    public void setAdatperData(List<String> data) {
        this.data=data;
    }

    /**
     *   自定义view时必须要overide的两个函数啊.
     */
    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minViewSize=getResources().getDimensionPixelSize(R.dimen.min_view_size);
        int measuredWidth=Math.max(minViewSize+getPaddingLeft()+getPaddingRight(),widthMeasureSpec);
        int mesauredHeight=Math.max(minViewSize+getPaddingBottom()+getPaddingTop(),heightMeasureSpec);

        setMeasuredDimension(measuredWidth, mesauredHeight);
        mCurrentHeight=getMeasuredHeight();
        mCurrentWidth=measuredWidth;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 设置bottom and left
     * @param x
     * @param y
     */
    private void setmCurrentViewportBottomAndLeft(float x,float y) {
        float curWidth=mCurrentViewport.width();
        float curHeight=mCurrentViewport.height();
        // make sure x>=x_min && x+curWidth<=x_max
        x=Math.max(AXIS_X_MIN,Math.min(x,AXIS_X_MAX-curWidth));
        // make sure y-curHeight>y_min && y < y_max
        y=Math.max(AXIS_Y_MIN+curHeight,Math.min(y,AXIS_Y_MAX));
        mCurrentViewport.set(x,y-curHeight,x+curWidth,y);
        //Cause an invalidate to happen on the next animation time step,
        // typically the next display frame
        ViewCompat.postInvalidateOnAnimation(this);//???
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////  draw
    ///////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int num=canvas.save();
        //Draws the axes 画出标志的两条线
        drawTwoLine(canvas);
        //裁剪一些区域
        int clipRestoreCount=canvas.save();
        drawDataUnclipped(canvas);
        drawEdgeEffect(canvas);

        canvas.restoreToCount(clipRestoreCount);

        canvas.drawRect(mContentRect,mAxisPaint);

        canvas.restoreToCount(num);
    }

    /**
     * 画出选择器选择的数据的啊.画出要显示的数据或者要移动的图片
     */
    private void drawDataUnclipped(Canvas canvas) {
        int num=canvas.save();
        if (mTextPaint==null) {
            setDefaultTextPaint();
        }
        canvas.drawText("3",10,10,mTextPaint);

        canvas.drawText("4",10,mCurrentHeight/3+10,mTextPaint);

        canvas.drawText("5",10,mCurrentHeight*2/3+10,mTextPaint);
        canvas.restoreToCount(num);
    }

    private void setDefaultTextPaint() {
        mTextPaint=new TextPaint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(50);
    }

    /**
     * 画出时间卷轴上的两条线啊.
     * @param canvas
     */
    private void drawTwoLine(Canvas canvas) {
        if (mTwoLinePaint==null) {
            mTwoLinePaint=new Paint();
        }
//        if (mFristLine==null) {
//            mFristLine=new Path();
//            mFristLine.
//        }
        mTwoLinePaint.setColor(Color.BLUE);
        canvas.drawLine(0,mCurrentHeight / 3, mCurrentWidth,mCurrentHeight / 3, mTwoLinePaint);
        canvas.drawLine(0,mCurrentHeight*2/3,mCurrentWidth,mCurrentHeight*2/3,mTwoLinePaint);

    }

    /**
     * 画滑动到边界的glow的界面效果
     * @param canvas
     */
    private void drawEdgeEffect(Canvas canvas) {
        boolean needsInvalidate=false;
        if (!mEdgeEffectTop.isFinished()) {  //如果没有接收
            final int restoreCount=canvas.save();

            canvas.translate(mContentRect.left,mContentRect.top); //将画布进行平移啦  //x y
            //Set the size of this edge effect in pixels
            mEdgeEffectTop.setSize(mContentRect.width(),mContentRect.height());
            if (mEdgeEffectTop.draw(canvas)) {
                needsInvalidate=true;
            }
            canvas.restoreToCount(restoreCount);
        }

        if (!mEdgeEffectBottom.isFinished()) {
            final int restoreCount=canvas.save();

            canvas.translate(2*mContentRect.left-mContentRect.right,mContentRect.bottom);
            mEdgeEffectBottom.setSize(mContentRect.width(),mContentRect.height());
            if (mEdgeEffectBottom.draw(canvas)) {
                needsInvalidate=true;
            }
            canvas.restoreToCount(restoreCount);
        }

        if (!mEdgeEffectLeft.isFinished()) {
            final int restoreCount=canvas.save();
            canvas.translate(mContentRect.left,mContentRect.bottom);
            canvas.rotate(-90,0,0);
            mEdgeEffectLeft.setSize(mContentRect.height(),mContentRect.width());
            if (mEdgeEffectLeft.draw(canvas)) {
                needsInvalidate=true;
            }
            canvas.restoreToCount(restoreCount);
        }

        if (!mEdgeEffectRight.isFinished()) {
            final int restoreCount=canvas.save();
            canvas.translate(mContentRect.right,mContentRect.top);
            canvas.rotate(90,0,0);
            mEdgeEffectRight.setSize(mContentRect.height(),mContentRect.width());
            if (mEdgeEffectRight.draw(canvas)) {
                needsInvalidate=true;
            }
            canvas.restoreToCount(restoreCount);
        }

        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    interface ScrollViewAdapter extends Adapter {
        @Override
        int getCount();

        @Override
        Object getItem(int position);

        @Override
        long getItemId(int position);

        @Override
        View getView(int position, View convertView, ViewGroup parent);
    }

}
