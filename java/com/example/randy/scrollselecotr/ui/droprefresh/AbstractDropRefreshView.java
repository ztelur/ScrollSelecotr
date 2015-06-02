package com.example.randy.scrollselecotr.ui.droprefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.randy.scrollselecotr.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by randy on 15-6-2.
 */
public abstract class AbstractDropRefreshView extends ViewGroup{
    protected int wholeHeight; //整个所有的组件的长度
    protected int headerHeight; //滑动显示的组件的长度
    protected int currentLine; //当前的屏幕顶端的y的值
    protected int showHeight;//当前组件可以显示区域的长
    protected int topLine;//主界面最上方的y值，用于下拉刷新
    protected int bottomLine;//主界面最下方的y值，用于上拉刷新


    private boolean isLoading;

    //listview滚动的状态
    private int scrollState;
    private boolean isRemark;//是否在顶端按下
    protected int startY;//滑动开始的y值

    protected int  state;
    private static final int NONE=0;
    private static final int PULL=1;
    private static final int RELEASE=2;
    private static final int REFRESHING=3;


    protected View header;
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener=new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //这里可以获得scroll的y的距离，从而进行计算
            currentLine+=distanceY;
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };
    private GestureDetector mGestureDetector=null;


    public AbstractDropRefreshView(Context context) {
        this(context,null,0);
    }

    public AbstractDropRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AbstractDropRefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }
    protected final void initView() {
        //计算整个界面的长度
        initHeader();
        //获得整个界面的长度
//       TODO:
//        wholeHeight=xxxxxxx
//        currentHeight=headerHeight;
//        showHeight=XXXXXX
    }

    /**
     * 设置header的逻辑流程，使用构造方法来设置用户自己的选择
     */
    public final void initHeader() {
        View header=getDropShowView();
        measureView(header);
        headerHeight=header.getMeasuredHeight();
        topPadding(-headerHeight);
    }
    public abstract View getDropShowView();
    /**
     * 计算子view的大小，并通知父布局
     * @param view
     */
    protected void measureView(View view) {
        ViewGroup.LayoutParams p=view.getLayoutParams();
        if (p==null) {
            p=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //这是获得childMeasureSpec的啊。
        int width=ViewGroup.getChildMeasureSpec(0,0,p.width);
        int heigth;
        int tempHeight=p.height;
        if (tempHeight>0) {
            //makeMeasureSpec是通过int size和int mode来计算出一个大小值的啊。
            heigth= View.MeasureSpec.makeMeasureSpec(tempHeight, View.MeasureSpec.EXACTLY);//
        } else {
            heigth= View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(width,heigth);  //这个是自己给自己通知大小的啊。
    }
    /**
     * 设置上方的padding啊。
     * @param topPadding
     */
    protected void topPadding(int topPadding) {
        header.setPadding(header.getPaddingLeft(),topPadding,header.getPaddingRight(),
                header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (currentLine==topLine) {   //当前的显示的第一个item是0
                    isRemark=true;
                    startY=(int)ev.getY();//记录初始的y值啊
                    Log.i("DropRefreshView", startY + "");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state==RELEASE) {  //释放刷新的
                    state=REFRESHING;
                    refreshViewByState();
//                    refreshListener.onRefresh();
                } else if (state==PULL) {  //在下拉时进行释放
                    state=NONE;
                    isRemark=false;
                    refreshViewByState();
                }
                break;
            default:

        }
        return super.dispatchTouchEvent(ev);
    }

    protected void onMove(MotionEvent ev) {
        if (!isRemark) return;
        //计算当前的toppadding啊。
        int tempY=(int)ev.getY();
        int space=tempY-startY;
        int topPadding=space-headerHeight;
        switch (state) {
            case NONE:
                if (space>0) {
                    state=PULL;
                    refreshViewByState();
                }
                break;
            case PULL:
                topPadding(topPadding);  //这里就是改变长度啦。
                if (space>headerHeight+30
                        ){
//                        scrollState==SCROLL_STATE_TOUCH_SCROLL) {
                    state=RELEASE;
                    refreshViewByState();
                }
                break;
            case RELEASE:
                topPadding(topPadding);
                if (space<headerHeight+30) {   //标示又变成pull啦
                    state=PULL;
                    refreshViewByState();
                } else if (space<=0) {   //表面不在下拉啦
                    state=NONE;
                    isRemark=false;
                    refreshViewByState();
                }
                break;
            default:

        }
    }

    /**
     * 提供下拉动画的函数
     */
    protected void refreshViewByState() {
        TextView tip=(TextView)header.findViewById(R.id.tip);
        ImageView arrow=(ImageView)header.findViewById(R.id.arrow);
        ProgressBar progressBar=(ProgressBar)header.findViewById(R.id.progress);
        RotateAnimation anim=new RotateAnimation(0,180,RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(500);
        anim.setFillAfter(true);

        RotateAnimation anim2=new RotateAnimation(180,0,RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(500);
        anim.setFillAfter(true);

        switch (state) {
            case NONE:
                arrow.clearAnimation();
                topPadding(-headerHeight);
                break;
            case PULL:
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tip.setText("下拉可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(anim2);
                break;
            case RELEASE:
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tip.setText("松开可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(anim);
                break;
            case REFRESHING:
                topPadding(50);
                arrow.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tip.setText("正在刷新..");
                arrow.clearAnimation();
                break;
        }
    }
    public void refreshComplete() {
        if (isLoading) {
            isLoading=false;
//            footer.setVisibility(GONE);
            return;
        }
        state=NONE;
        isRemark=false;
        refreshViewByState();
        TextView lastupdateDate=(TextView)header.findViewById(R.id.lastupdate_time);
        SimpleDateFormat format=new SimpleDateFormat("yyy年MM月 hh:mm:ss");
        Date date=new Date(System.currentTimeMillis());
        String time=format.format(date);
        lastupdateDate.setText(time);
    }
}