
package com.example.randy.scrollselecotr;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.randy.scrollselecotr.R;
import com.example.randy.scrollselecotr.utils.VolleyInstance;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by randy on 15-5-18.
 */
public class NineGridView extends ViewGroup implements View.OnClickListener{
    private static final String TAG="NineGridView";
    private float default_width_gap=5;
    private float default_height_gap=5;

    protected int rows;
    protected int cols;

    protected int gaps=10;

    protected int margin=10;

    //    private
    protected ItemClickListener mListener;

    protected List<String> dataList=new ArrayList<String>();
    protected int total_width;

    protected int singleWidth;
    protected  int singleHeght;
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

    protected void layoutChildrenView() {
        Log.e(TAG,"layoutChildView call");
        int childNum=dataList.size();
        singleWidth=(total_width-margin*2-gaps*(rows-1))/3;
        singleHeght=singleWidth;

        //根据view的数量确定size
        ViewGroup.LayoutParams params=getLayoutParams();
        params.height=singleHeght*rows+gaps*(rows-1)+margin*2;
        params.width=singleWidth*cols+gaps*(cols-1)+margin*2;

        setLayoutParams(params);

        for(int i=0;i<childNum;i++) {
            ImageView childview=(ImageView)getChildAt(i);
            startImgRequest(dataList.get(i),childview);
            int [] positions=findPositions(i);
            int left=margin+(singleWidth+gaps)*positions[0];
            int top=margin+(singleHeght+gaps)*positions[1];
            Log.e(TAG, "the i :"+i+"the round is "+left+" "+top+" "+singleWidth+" "+top+singleHeght);
            childview.layout(left,top,left+singleWidth,top+singleHeght);
        }
    }
    protected int[] findPositions(int childNum) {
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
        if (list == null || list.isEmpty()) {
            return;
        }
        Log.e(this.getClass().getName(),"setImageData the list is"+list.size());
        dataList = list;
        initView();
    }

    protected void initView() {
        Log.e(TAG,"initView");
        generateChildLayout(dataList.size());
        removeAllViews();
        for(int i=0;i<dataList.size();i++) {
            ImageView iv=generateImageView(i);
            iv.setPadding(0, 0, 0, 0);
            iv.setBackgroundColor(Color.BLUE);
            addView(iv, generateDefaultLayoutParams());
            iv.setOnClickListener(this);
            iv.setTag(i);
        }
        layoutChildrenView();
    }
    protected ImageView generateImageView(int id) {
        ImageView iv=new ImageView(getContext());
        return iv;
    }


    //TODO:其实可以使用数学方法计算
    protected void generateChildLayout(int size) {
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
                    Bitmap res=paramAnonymousImageContainer.getBitmap();

                    paramImageView.setImageBitmap(zoomBitmap(res,singleWidth,
                            singleHeght));
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

    /**
     * 按照ImageView需要的大小缩放bitmap
     * @param bitmap
     * @param width  缩放后的大小
     * @param height 缩放后的高
     * @return
     */
    protected Bitmap zoomBitmap(Bitmap bitmap,int width,int height) {
        int oldw=bitmap.getWidth();
        int oldh=bitmap.getHeight();
        Matrix matrix=new Matrix();
        float scaleWidth=((float)width/oldw);
        float scaleHeight=((float)height/oldh);
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap res=Bitmap.createBitmap(bitmap,0,0,oldw,oldh,matrix,true);
        return res;
    }
}
