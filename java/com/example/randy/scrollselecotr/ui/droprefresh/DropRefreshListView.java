package com.example.randy.scrollselecotr.ui.droprefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.randy.scrollselecotr.R;

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
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:

        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断移动操作的函数
     * @param ev
     */
    protected void onMove(MotionEvent ev) {

    }

    /**
     * 提供下拉动画的函数
     */
    protected void refreshViewByState() {

    }

    /**
     * 用于刷新的监听
     */
    public interface RefreshListener{
        public void onRefresh();
        public void loadMore();
    }
}
