package com.example.randy.scrollselecotr.ui.droprefresh;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.randy.scrollselecotr.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by randy on 15-6-2.
 * 下拉刷新的逻辑布局啊，可以使用在任何布局中，无论是ListView,还是LinearLayout啊。
 */
public class DropRefreshModel {

    protected int wholeHeight; //整个所有的组件的长度
    protected int headerHeight; //滑动显示的组件的长度


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


    public interface DropRefreshView{
        public void initHeader();//设置滑动显示的header

    }


}
