package com.example.randy.scrollselecotr.slideshow;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.randy.scrollselecotr.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by randy on 15-6-30.
 */
public class SlideShowView extends FrameLayout{
    //轮播图图片的数量
    protected  int image_num;
    //是否循环滑动
    protected  boolean isCycle;
    //自动轮播的时间间隔
    protected  int time_interal;
    //自动轮播启动开关
    protected  boolean isAutoPlay=true;
    //自定义轮播图的资源ID
    private int[] imageResIds;
    //轮播图片的ImageView的list
    protected List<ImageView> imageViewList;
    //放圆点图片的view的list
    protected List<View> dotViewList;

    private LinearLayout dotLayout;
    protected ViewPager viewPager;
    //当前轮播页
    protected int currentItem=0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    //handler
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };


    public SlideShowView(Context context) {
        this(context, null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();
        initView(context);
        if (isAutoPlay) {
            startPlay();
        }
    }

    private void initData() {
        imageResIds=new int[] {
                R.drawable.ic_launcher,
                R.drawable.ic_launcher
        };
        imageViewList=new ArrayList<ImageView>();
        dotViewList=new ArrayList<View>();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_header,this,true);
        int tag=1;
        dotLayout=(LinearLayout)findViewById(R.id.dotlayout);
        for(int imageId:imageResIds) {
            ImageView view=new ImageView(context);
            view.setImageResource(imageId);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(view);

            //添加dot view
            View dotView=new View(context);
            dotView.setTag("dot"+tag);
            tag++;
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                8,8
            );
            params.leftMargin=5;
            dotLayout.addView(dotView,params);
            dotViewList.add(dotView);
        }
        //初始化dotlist

        //初始化viewpager

    }
    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(imageViewList.get(position));
            return imageViewList.get(position);
        }
    }
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        boolean isAutoPlay =false;

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            //改变dot的位置啊
            currentItem=i;
            for(int dot=0;dot<dotViewList.size();dot++) {
                if (dot==i) {
//                    ((View)dotViewList.get(dot)).setBackground(R.drawable.dot_black);
                } else {
//                    ((View)dotViewList.get(dot)).setBackground(R.drawable.dot_white);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            switch (i) {
                case 1: //手势滑动,空闲中
                    isAutoPlay=false;
                    break;
                case 2://界面切换中
                    isAutoPlay=true;
                    break;
                case 0://滑动结束,即切换完毕或者加载完毕
                        if (isCycle) {  //是否循环滑动 就是到了最左边再滑,就到了最右边
                            if (viewPager.getCurrentItem()==viewPager.getAdapter().getCount()-1&&!isAutoPlay) {
                                viewPager.setCurrentItem(0);
                            } else if (viewPager.getCurrentItem()==0&&!isAutoPlay){
                                viewPager.setCurrentItem(viewPager.getAdapter().getCount()-1);
                            }

                        }
                    break;

            }
        }
    }

    /**
     * 启动自动播放啊.
     */
    public void startPlay() {
        scheduledExecutorService= Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(),1,4, TimeUnit.SECONDS);
    }
    public void stopPlay() {
        scheduledExecutorService.shutdown();
    }
    private class SlideShowTask implements Runnable {
        @Override
        public void run() {
            currentItem=(currentItem+1)%image_num;
            handler.obtainMessage().sendToTarget();
        }
    }

}
