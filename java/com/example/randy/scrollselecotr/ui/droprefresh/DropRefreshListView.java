package com.example.randy.scrollselecotr.ui.droprefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.randy.scrollselecotr.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by randy on 15-6-2.
 */
public class DropRefreshListView extends ListView implements AbsListView.OnScrollListener{
    protected View header;
    protected View footer;


    private int headerHeight;

    //TODO:用来标记神马时候进行下拉和上拉刷新，但是其实可以使用更好的方式啊。
    //比如android SDK sample中的。
    private int firstVisibleItem;//第一个可见的item
    private int lastVisibleItem;//最后一个可见的item
    private int totalItemCount;
    private boolean isLoading;

    //listview滚动的状态
    private int scrollState;
    private boolean isRemark;//是否在顶端按下
    protected int startY;//滑动开始的y值

    int state;
    private static final int NONE=0;
    private static final int PULL=1;
    private static final int RELEASE=2;
    private static final int REFRESHING=3;

    protected boolean userhome=false;

    private RefreshListener refreshListener;
    public DropRefreshListView(Context context) {
        super(context);
    }

    public DropRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DropRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        initHeader();
        initFooter();
        setOnScrollListener(this);
    }
    protected void initHeader() {
        header=inflate(getContext(), R.layout.header_layout,null);
        measureView(header);
        headerHeight=header.getMeasuredHeight();
        topPadding(-headerHeight);
        this.addHeaderView(header);

    }
    protected void initFooter() {
        footer=inflate(getContext(),R.layout.footer_layout,null);
        footer.setVisibility(GONE);
        this.addFooterView(footer);
    }


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
            heigth=MeasureSpec.makeMeasureSpec(tempHeight,MeasureSpec.EXACTLY);//
        } else {
            heigth=MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// onScrollListener
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState=scrollState;
        if (totalItemCount==lastVisibleItem&&scrollState==SCROLL_STATE_IDLE) {
            if (!isLoading) {  //放在多次上拉到底进行刷新啊
                isLoading=true;
                footer.setVisibility(VISIBLE);
                refreshListener.loadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem=firstVisibleItem;
        this.lastVisibleItem=firstVisibleItem+visibleItemCount;
        this.totalItemCount=totalItemCount;
    }
    public void setRefreshListener(RefreshListener listener) {
        this.refreshListener=listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem==0) {   //当前的显示的第一个item是0
                    isRemark=true;
                    startY=(int)ev.getY();//记录初始的y值啊
                    Log.i("DropRefreshView",startY+"");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state==RELEASE) {  //释放刷新的
                    state=REFRESHING;
                    refreshViewByState();
                    refreshListener.onRefresh();
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

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return super.onTouchEvent(ev);
    }

    /**
     * 判断移动操作的函数,这是对这个状态下的ev进行操作
     * @param ev
     */
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
                if (space>headerHeight+30&&
                                scrollState==SCROLL_STATE_TOUCH_SCROLL) {
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
                arrow.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                tip.setText("下拉可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(anim2);
                break;
            case RELEASE:
                arrow.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                tip.setText("松开可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(anim);
                break;
            case REFRESHING:
                topPadding(50);
                arrow.setVisibility(GONE);
                progressBar.setVisibility(VISIBLE);
                tip.setText("正在刷新..");
                arrow.clearAnimation();
                break;
        }
    }
    public void refreshComplete() {
        if (isLoading) {
            isLoading=false;
            footer.setVisibility(GONE);
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

    /**
     * 用于刷新的监听
     */
    public interface RefreshListener{
        public void onRefresh();
        public void loadMore();
    }
}
