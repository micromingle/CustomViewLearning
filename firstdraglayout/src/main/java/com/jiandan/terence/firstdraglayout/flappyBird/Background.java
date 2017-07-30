package com.jiandan.terence.firstdraglayout.flappyBird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by micromingle on 29/07/2017.
 */

public class Background implements Recyclable {

    Rect mRect=new Rect();
    private int mWidth,mHeight;
    private Context mContext;
    private static Bitmap mBackgroundBitmap;

    public void draw(Canvas canvas){
        if(canvas==null){
            return;
        }
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.drawBitmap(mBackgroundBitmap,null,mRect,null);
        canvas.restore();
    }

    public Background(Context context,int width, int height,int resId){
        mWidth=width;
        mHeight=height;
        mContext=context;
        mRect.set(0,0,mWidth,mHeight);
        if(mBackgroundBitmap==null) {
            mBackgroundBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
        }

    }

    @Override
    public void Recycle() {
        mContext=null;
        mBackgroundBitmap=null;
    }
}
