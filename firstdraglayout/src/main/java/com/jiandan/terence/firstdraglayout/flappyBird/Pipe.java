package com.jiandan.terence.firstdraglayout.flappyBird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.jiandan.terence.firstdraglayout.R;
import com.jiandan.terence.firstdraglayout.Util;

import java.util.Random;

/**
 * Created by micromingle on 29/07/2017.
 */

public class Pipe {
    int x,y;
    int mWidth, mHeight;
    Context mContext;
    static Bitmap mTop,mBottom;
    int mTopHeight,mMargin;//上下两个管道的间隙;
    Rect mRect=new Rect();
    int mPipeWidth;
    Random mRandom=new Random();
    float MIN_MARGIN_RATIO=1/7f,MAX_MARGIN_RATIO=1/5f;
    float MIN_HEIGHT_RATIO=1/5f,MAX_HEIGHT_RATIO=2/5f;
    private String TAG="Pipe";
    //是否已经记过分
    private boolean isScored=false;

    /**
     * 管道的宽度 60dp
     */
    private static final int PIPE_WIDTH = 60;

    public Pipe(Context context, int width, int height) {
        this.mContext = context;
        this.mWidth = width;
        this.mHeight = height;
        x=mWidth;
        if(mBottom==null) {
            mBottom = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pipe_bottom);
        }
        if(mTop==null) {
            mTop = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pipe_top);
        }
        mPipeWidth = Util.dp2px(mContext, PIPE_WIDTH);
        int randomHeight=mRandom.nextInt((int)((MAX_HEIGHT_RATIO-MIN_HEIGHT_RATIO)*mHeight));
        mTopHeight=(int)(mHeight*MIN_HEIGHT_RATIO)+randomHeight;
        int randomMargin=mRandom.nextInt((int)((MAX_MARGIN_RATIO-MIN_MARGIN_RATIO)*mHeight));
        mMargin=(int)(mHeight*MIN_MARGIN_RATIO)+randomMargin;
        mRect.set(0,0,mPipeWidth,mHeight);
    }

    public void draw(Canvas canvas){
        if(canvas==null){
            return;
        }
        Log.d(TAG,String.format("mTopHeight %s mMargin %s",mTopHeight,mMargin));
        Log.d(TAG,String.format("mRect %s ",mRect.toString()));
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(x,-(mRect.bottom-mTopHeight));
        canvas.drawBitmap(mTop,null,mRect,null);
        canvas.translate(0,(mRect.bottom-mTopHeight)+mTopHeight+mMargin);
        canvas.drawBitmap(mBottom,null,mRect,null);
        canvas.restore();

    }

    public int getX() {
        return x;
    }

    public boolean isPipeDisappear(){
        return getX()<-mPipeWidth;
    }

    public int getPipeWidth(){
        return mPipeWidth;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isScored() {
        return isScored;
    }

    public void setScored(boolean scored) {
        isScored = scored;
    }


    public Rect getRect(){
        return mRect;
    }

    public int getTopHeight(){
        return mTopHeight;
    }

    public int getMargin(){
        return mMargin;
    }
}
