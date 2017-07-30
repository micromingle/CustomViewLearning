package com.jiandan.terence.firstdraglayout.flappyBird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.jiandan.terence.firstdraglayout.BuildConfig;
import com.jiandan.terence.firstdraglayout.Util;

/**
 * Created by micromingle on 29/07/2017.
 */

public class Bird implements Recyclable {

    private Rect mRect=new Rect();
    private int mGameWidth,mGameHeight;
    private int mStartX,mStartY;
    private final int BIRD_WIDTH=34,BIDR_HEIGHT=24;
    private int mBirdWidth,mBirdHeight;
    private static Bitmap mBitmap;
    private Context mContext;
    private float ratio=4/5f;
    private String TAG="Bird";

    public Bird(Context context,int width, int height,int resId){
        mContext=context;
        mGameHeight=height;
        mGameWidth=width;

        if(mBitmap==null) {
            mBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
        }
        mBirdWidth= Util.dp2px(mContext,BIRD_WIDTH);
        mBirdHeight=Util.dp2px(mContext,BIDR_HEIGHT);
        mStartX=mGameWidth/2-mBirdWidth/2;
        mStartY=mGameHeight/2-mBirdHeight/2;


    }

    public void draw(Canvas canvas){
        if(canvas==null){
            return;
        }
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        mRect.set(mStartX,mStartY,mStartX+mBirdWidth,mStartY+mBirdHeight);
        canvas.drawBitmap(mBitmap,null,mRect,null);
        canvas.restore();
    }

    public void setX(int x){
        mStartX=x;
        if(mStartX<0){
            mStartX=0;
        }
        if(mStartX>mGameWidth){
            mStartX=mGameWidth;
        }
    }

    public void setY(int y){

        mStartY=y;
        if(mStartY<0){
            mStartY=0;
        }
        if(mStartY>(int)(mGameHeight*ratio)){
            mStartY=(int)(mGameHeight*ratio)-mBirdHeight/2;
        }



    }

    public int getY(){
        return mStartY;
    }

    public int getStartX() {
        return mStartX;
    }

    public void setStartX(int mStartX) {
        this.mStartX = mStartX;
    }

    public int getStartY() {
        return mStartY;
    }

    public void setStartY(int mStartY) {
        this.mStartY = mStartY;
    }

    public void reset(){
        mStartX=mGameWidth/2-mBirdWidth/2;
        mStartY=mGameHeight/2-mBirdHeight/2;

    }
    @Override
    public void Recycle() {

    }

    public int getWidth(){
       return mBirdWidth;
    }

    public boolean touchPipe(Pipe pipe){
        boolean frontTouch=getStartX()+mBirdWidth>pipe.getX();
        boolean backTouch=getStartX()<pipe.getX()+pipe.getPipeWidth();

        boolean topTouch=getStartY()<pipe.getTopHeight();
        boolean bottomTouch=getStartY()+mBirdHeight>pipe.getTopHeight()+pipe.getMargin();

        Log.d(TAG,String.format("front touch %s back touch %s topTouch %s bottomTouch %s",frontTouch,backTouch,topTouch,bottomTouch));
        boolean isTouch=(frontTouch&&backTouch)&&(topTouch||bottomTouch);
        if(isTouch){
            Log.d(TAG,"touch pipe");
        }
        return isTouch;
    }

    public boolean touchFloor(Floor floor){
        if(getStartY()+mBirdHeight>=floor.getY()){
            Log.d(TAG,"touch floor");
            return true;
        }
        return false;
    }
}
