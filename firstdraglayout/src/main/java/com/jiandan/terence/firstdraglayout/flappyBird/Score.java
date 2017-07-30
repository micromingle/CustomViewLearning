package com.jiandan.terence.firstdraglayout.flappyBird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.jiandan.terence.firstdraglayout.R;
import com.jiandan.terence.firstdraglayout.Util;

/**
 * Created by micromingle on 29/07/2017.
 */

public class Score implements Recyclable{

    Rect mRect=new Rect();
    private int mGameWidth,mGameHeight;
    private int mStartX,mStartY;
    private Bitmap mBitmap;
    private Context mContext;
    private float ratio=1/9f;
    private int DIGITAL_HEIGHT=54,DIGITAL_WIDTH=36;
    private int mSingleHeight,mSingleWidth;
    private int[] mResArray=new int[]{
        R.drawable.n0,R.drawable.n1,R.drawable.n2,R.drawable.n3,R.drawable.n4,
            R.drawable.n5,R.drawable.n6,R.drawable.n7,R.drawable.n8,R.drawable.n9};
    private Bitmap[] mNumArray=new Bitmap[10];
    private int mScore=0;
    private int y;
    private int x;

    public Score(int gameWidth, int gameHeight, Context context) {
        this.mGameWidth = gameWidth;
        this.mGameHeight = gameHeight;
        this.mContext = context;
        for(int i=0;i<10;i++){
            mNumArray[i]=Util.loadBitmapByRes(this.mContext,mResArray[i]);
        }
        mSingleHeight=Util.dp2px(mContext,DIGITAL_HEIGHT);
        mSingleWidth=Util.dp2px(mContext,DIGITAL_WIDTH);
        y=(int)(mGameHeight*ratio)-mSingleHeight/2;
        mRect.set(0,0,mSingleWidth,mSingleHeight);

    }

    public void increase(){
        ++mScore;
    }

    public void reset(){
        mScore=0;
    }

    public void draw(Canvas canvas){
        if(canvas==null){
            return;
        }

        String grade = mScore + "";
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(mGameWidth / 2 - grade.length() * mSingleWidth / 2,
                ratio * mGameHeight);
        // draw single num one by one
        for (int i = 0; i < grade.length(); i++) {
            String numStr = grade.substring(i, i + 1);
            int num = Integer.valueOf(numStr);
            canvas.drawBitmap(mNumArray[num], null, mRect, null);
            canvas.translate(mSingleWidth, 0);
        }
        canvas.restore();

    }

    @Override
    public void Recycle() {

    }
}
