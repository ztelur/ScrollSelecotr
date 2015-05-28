package com.example.randy.scrollselecotr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.randy.scrollselecotr.R;

/**
 * Created by randy on 15-5-27.
 */
class EditImageView extends ImageView {
    private static final String TAG="EditImageView";

    ////////////////////////////////////////////////////////////////////////////////
    ////////////   画图的长度和参数
    ////////////////////////////////////////////////////////////////////////////////
    protected float mHeight;
    protected float mWidth;
    protected float topRadio=0.05f;  //上边缘比例
    protected float leftRadio=0.80f;//左边缘比例
    protected float radio=0.2f;//deleteButton的小图标和图片的大小啊

    RectF deleteRecf=new RectF(); //deleteButton的区域大小啊。

    ////////////////////////////////////////////////////////////////////////////////////
    ///////////////  监听
    ///////////////////////////////////////////////////////////////////////////////////
    private ImageDeleteClickListener mListener=null;
    private int numId=0;

    /////////////////////////////////////////////////////////////////////////////////////
    //////////////// 构造函数
    ////////////////////////////////////////////////////////////////////////////////////
    public EditImageView(Context context) {
        super(context);
    }

    public EditImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //在这里，在图片的右上角进行画一个小图片
        //计算
        figureDeleteRecf();
        Bitmap deleteButton=getDrawBitmap();
        deleteButton=zoomBitmap(deleteButton,(int)deleteRecf.width(),(int)deleteRecf.height());
//        canvas.drawBitmap(deleteButton,deleteRecf.left,deleteRecf.top,new Paint());
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(deleteRecf,paint);
        Log.e(TAG,"onDraw the recf is "+deleteButton.toString());
        super.onDraw(canvas);
    }
    protected Bitmap getDrawBitmap() {
        Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        return bmp;
    }
    /**
     * 按照ImageView需要的大小缩放bitmap
     * @param bitmap
     * @param width  缩放后的大小
     * @param height 缩放后的高
     * @return
     */
    private  Bitmap zoomBitmap(Bitmap bitmap,int width,int height) {
        int oldw=bitmap.getWidth();
        int oldh=bitmap.getHeight();
        Matrix matrix=new Matrix();
        float scaleWidth=((float)width/oldw);
        float scaleHeight=((float)height/oldh);
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap res=Bitmap.createBitmap(bitmap,0,0,oldw,oldh,matrix,true);
        return res;
    }
    protected void figureDeleteRecf() {
        RectF rectF=figureDeleteButtonLocation();
        Point point=getImageRect();
        deleteRecf=new RectF(rectF.left,rectF.top,rectF.left+point.x,rectF.top+point.y);
    }
    protected RectF figureDeleteButtonLocation() {
        RectF res=new RectF();
        //计算图片上边与ImageView图片边缘距离，要计算到padding和margin啊
        Point imgSize=getImageRect();
        float distanceTop=getPaddingTop()+imgSize.y*topRadio;
        float distanceLeft=getPaddingLeft()+imgSize.x*leftRadio;
        res.top=distanceTop;
        res.left=distanceLeft;
        return res;
    }
    public Point getImageRect() {
        int dw=this.getDrawable().getBounds().width();
        int dh=this.getDrawable().getBounds().height();
        Matrix m=this.getImageMatrix();
        float[] values=new float[10];
        m.getValues(values);
        int cw=(int)(values[0]*dw);
        int ch=(int)(values[4]*dh);
        Point res=new Point(cw,ch);
        return res;
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        Log.e(TAG,"setImageBitmap");
        super.setImageBitmap(bm);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
    ////////  监听
    //////////////////////////////////////////////////////////////////////////////////////////////
   private boolean hasImageDeleteListener() {
       return mListener!=null;
   }
   public void setImageDeleteListener(ImageDeleteClickListener listener) {
       this.mListener=listener;
   }
   public void setNumId(int tag) {
       this.numId = tag;
   }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //判断点击的点是否是deleteButton上的点，如果是那么进行消除啊。
        if (event.getAction()==MotionEvent.ACTION_UP) {
            float x=event.getX();
            float y=event.getY();
            Log.e(TAG, "extra handle action up x is" + x + " y is " + y);
            if (x>deleteRecf.left&&x<deleteRecf.right&&y>deleteRecf.top&&y<deleteRecf.bottom) {
                Log.e(TAG,"click the deleteButton");
                //删除EditNineGridView中的
                //TODO:这里可以进行一个回调函数
                if (hasImageDeleteListener()) {
                    mListener.onDeleteClick(numId);
                }
                return false;
            }
        }

        return super.onTouchEvent(event);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
    }

    /**
     * 回调函数，当ImageVIew右上角的删除图片按钮被点击的之后调用。
     */
    interface ImageDeleteClickListener{
        public void onDeleteClick(int flag);
    }

}