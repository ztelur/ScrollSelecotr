package com.example.randy.scrollselecotr;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.randy.scrollselecotr.utils.VolleyInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randy on 15-5-18.
 *
 * 图片九宫格
 *
 */
public class NineGridView extends ViewGroup implements View.OnClickListener{
    private float default_width_gap=5;
    private float default_height_gap=5;

    private int rows;
    private int cols;

    private int gaps=10;

//    private
    private ItemClickListener mListener;

    private List<String> dataList=new ArrayList<String>();
    private int total_width;
    public NineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        ScreenTools screenTools
        total_width=context.getResources().getDisplayMetrics().widthPixels;
    }
    //这是自定义viewgroup必须实现的方法啊 ,为子view设定位置的啊
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void layoutChildrenView() {
        int childNum=dataList.size();
        int singleWidth=(total_width-gaps*(rows-1))/3;
        int singleHeight=singleWidth;

        //根据view的数量确定size
        LayoutParams params=getLayoutParams();
        params.height=singleHeight*rows+gaps*(rows-1);
        params.width=singleWidth*cols+gaps*(cols-1);

        setLayoutParams(params);

        for(int i=0;i<childNum;i++) {
            ImageView childview=(ImageView)getChildAt(i);
            startImgRequest(dataList.get(i),childview);
            int [] positions=findPositions(i);
            int left=(singleWidth+gaps)*positions[0];
            int top=(singleHeight+gaps)*positions[1];
            childview.layout(left,top,left+singleWidth,top+singleHeight);
        }
    }
    private int[] findPositions(int childNum) {
        int[] position=new int[2];
        int size=dataList.size();
        //   x x x
        //   x x x
        //   x x x
        position[0]=childNum%rows;  //计算childNum是第几列,   2
        position[1]=childNum/rows;   //计算childNUM是第几行    3
        return position;
    }


    public void setImagesData(List<String> list) {
        if (list==null||list.isEmpty()) {
            return;
        }
        generateChildLayout(list.size());
        removeAllViews();
        for(int i=0;i<list.size();i++) {
            ImageView iv=generateImageView();
            iv.setPadding(0, 0, 0, 0);
            iv.setBackgroundColor(Color.BLUE);
            addView(iv, generateDefaultLayoutParams());
            iv.setOnClickListener(this);
            iv.setTag(i);
//            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        dataList=list;
        layoutChildrenView();

    }
    private ImageView generateImageView() {
        ImageView iv=new ImageView(getContext());
        return iv;
    }


    //TODO:其实可以使用数学方法计算
    private void generateChildLayout(int size) {
        if (size<3) {
            rows=1;
            cols=size;
        } else if (size<6) {
            rows=2;
            cols=3;
            if (size==4) {
                rows=2;
                cols=2;
            }
        } else {
            rows=3;
            cols=3;
        }
    }
    private void startImgRequest(String paramString, final ImageView paramImageView)
    {
        ImageLoader.ImageListener lisnter = new ImageLoader.ImageListener()
        {
            public void onErrorResponse(VolleyError paramAnonymousVolleyError)
            {
                paramImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
            }

            public void onResponse(ImageLoader.ImageContainer paramAnonymousImageContainer, boolean paramAnonymousBoolean)
            {
                if (paramAnonymousImageContainer.getBitmap() != null){
                    paramImageView.setImageBitmap(paramAnonymousImageContainer.getBitmap());
                    paramImageView.setClickable(true);
                }else{
                    paramImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                }
            }
        };
        VolleyInstance.getInstance(getContext()).getImageloader().
                get(paramString, lisnter);
    }
    //TODO:这里其实是可以使用ItemClickListen而的.
    public void setItemClickListener(ItemClickListener l) {
        this.mListener=l;
    }
    interface ItemClickListener{
        public void itemClick(int i);
    }

    @Override
    public void onClick(View v) {
        Object i=v.getTag();
        if (mListener!=null) {
            mListener.itemClick((Integer)(i));
        }
    }
}
