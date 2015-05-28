package com.example.randy.scrollselecotr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;


/**
 * Created by randy on 15-5-26.
 * 这个是添加图片时的九宫格啊，也就是说
 * 显示图片的时候的九宫格和添加图片时候的九宫格
 *
 */
public class EditNineGridView extends NineGridView implements EditImageView.ImageDeleteClickListener{
    private static final String TAG="EditNineGridView";
    ///////////////////////////////////////////////////////////////////////////////
    //////////  画正方形的数据
    //////////////////////////////////////////////////////////////////////////////
    private RectF rectangeSize=null;
    private int lineWidthl=10; //正方形的line的宽度
    public EditNineGridView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public EditNineGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.e(TAG,"the EditNineGridView calls");
        setWillNotDraw(false);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG,"onLayout");
        super.onLayout(changed, l, t, r, b);
    }



    @Override
    protected ImageView generateImageView(int id) {
        Log.e(TAG,"generateImageView");
        EditImageView img=
        new EditImageView(getContext());
        img.setNumId(id);
        img.setImageDeleteListener(this);
        return img;
    }
    //////////////////////////////////////////////////////////////////////////////////////
    //////////  去除一个ImageView
    /////////////////////////////////////////////////////////////////////////////////////
    private  void deleteData(int id) {
        dataList.remove(id);
        initView();
    }

    @Override
    public void onDeleteClick(int id) {
        deleteData(id);
    }
    /////////////////////////////////////////////////////////////////////////////////////
    ///////////   绘画点击添加图片的正方形
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * 计算正方形的区域啊，其实和计算图片的位置相同的啊。
     * @return
     */
    private void figureRect() {
        int size=dataList.size();
        //this situation dont draw the rectangle
        if (size==0||size==9) {
            rectangeSize= null;
            return ;
        }
        int[] pos=findPositions(size);
        int singleWidth=(total_width-gaps*(rows-1)-2*margin)/3;
        int singleHeight=singleWidth;
        int left=margin+(singleWidth+gaps)*pos[0];
        int top=margin+(singleHeight+gaps)*pos[1];
        rectangeSize=new RectF(left,top,left+singleWidth,top+singleHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawRectangle(canvas);
        super.onDraw(canvas);
    }
    private void drawRectangle(Canvas canvas) {
        int id=canvas.save();
        figureRect();
        Log.e(TAG,"onDraw the recf is "+rectangeSize.left+" "+rectangeSize.top+" "+rectangeSize.right);
        if (rectangeSize!=null) {
            Paint paint=new Paint();
            paint.setColor(Color.BLACK);
            paint.setPathEffect(buildPathEffect());
            //画中心的十字
            float centerPointX=rectangeSize.left+rectangeSize.width()/2;
            float centerPointY=rectangeSize.top+rectangeSize.height()/2;
            float halfLength=rectangeSize.width()/4;
            canvas.drawLine(centerPointX,centerPointY-halfLength,centerPointX,centerPointY+halfLength,
                                        paint);
            canvas.drawLine(centerPointX-halfLength,centerPointY,centerPointX+halfLength,centerPointY,
                                        paint);

            //画正方形


            canvas.drawLine(rectangeSize.left,rectangeSize.top,rectangeSize.left,rectangeSize.bottom,paint);
            canvas.drawLine(rectangeSize.left,rectangeSize.top,rectangeSize.right,rectangeSize.top,paint);
            canvas.drawLine(rectangeSize.right,rectangeSize.top,rectangeSize.right,rectangeSize.bottom,paint);
            canvas.drawLine(rectangeSize.left,rectangeSize.bottom,rectangeSize.right,rectangeSize.bottom,paint);

        } else {
            Log.e(TAG,"rectangeSize == null");
        }
    }
    protected PathEffect buildPathEffect() {
        PathEffect effect=new DashPathEffect(new float[] {3,3},1);
        return effect;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG,"onTouchEvent"+event.getAction());
        if (event.getAction()==MotionEvent.ACTION_UP) {
            float x=event.getX();
            float y=event.getY();
            Log.e(TAG,"into check the x is "+x+" the y is "+y+" the left"+rectangeSize.left+" "+rectangeSize.top);

            if (x>rectangeSize.left&&x<rectangeSize.right&&y>rectangeSize.top&&y<rectangeSize.bottom) {
                /////说明点击了啊。可以进行添加图片啦
                //TODO:
                Log.e(TAG,"add image");
                return false;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG,"dispatchTouchEvent"+ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void generateChildLayout(int size) {
        rows=3;
        cols=3;
    }
}
