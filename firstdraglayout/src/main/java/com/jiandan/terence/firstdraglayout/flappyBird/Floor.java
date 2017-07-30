package com.jiandan.terence.firstdraglayout.flappyBird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

/**
 * Created by micromingle on 29/07/2017.
 */

public class Floor {

    int x,y;
    private int mWidth,mHeight;
    private Context mContext;
     BitmapShader mBitmapShader;
    private int mFloorHeight;
    private  Bitmap mBitmap;
    Rect mRect=new Rect();
    float ratio=4/5f;


    public Floor(Context context,int width,int height,int resId){
        mContext=context;
        mWidth=width;
        mHeight=height;
        //if(mBitmap==null) {
            mBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
      //  }
        y=(int)(mHeight*ratio);
        mFloorHeight=mHeight-y;
    }

    public void draw(Canvas canvas,Paint paint){
        if(canvas==null){
            return;
        }
        if (-x > mWidth)
        {
            x = x % mWidth;
        }
        paint.setShader(mBitmapShader);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        //这一段并不理解
        canvas.translate(x,y);
        mRect.set(x,0,mWidth-x,mFloorHeight);
        canvas.drawRect(mRect,paint);
        canvas.restore();
        paint.setShader(null);

    }

    public int getX(){
        return x;
    };

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x=x;

    }
}
