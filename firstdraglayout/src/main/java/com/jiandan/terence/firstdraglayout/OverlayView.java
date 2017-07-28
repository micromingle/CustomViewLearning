package com.jiandan.terence.firstdraglayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by HP on 2017/7/26.
 */
//由于view最终要旋转90度 所以view的高用屏幕的宽来算
    // view for 身份证正反面
public class OverlayView extends View {
    private String TAG = "OverlayView";
    int mLen = 30, mStrikeWidth = 10;
    int mColor, mLineColor, mLineWidth = 1;
    Paint mAnglePaint, mLinePaint;
    Rect mTransParentRect;
    private boolean isForeGroundDrew = false;
    float mRatio=1.73f;//宽度和高度的比
    int mTransParentHeight=300, mTransParentWidth=(int)(mTransParentHeight*mRatio);
    int  verticalPad = 30,horiPad = (int)(verticalPad*mRatio);
    int mHeight,mWidth;
    private final int TOP=150;//dip
    //private int mOuterPadding=TOP;

    public OverlayView(Context context) {
        super(context);
    }

    public OverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.OverlayView);
        mLen = (int) ta.getDimension(R.styleable.OverlayView_strike_lenth, mLen);
        mStrikeWidth = (int) ta.getDimension(R.styleable.OverlayView_strike_width, mStrikeWidth);
        mColor = ta.getColor(R.styleable.OverlayView_strike_color, Color.GREEN);
        mLineColor = ta.getColor(R.styleable.OverlayView_line_color, Color.GRAY);
        mLineWidth = (int) ta.getDimension(R.styleable.OverlayView_line_width, mLineWidth);
        ta.recycle();
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mHeight=getResources().getDisplayMetrics().widthPixels;
        mWidth=(int)(mHeight*mRatio);
        mTransParentHeight=getResources().getDisplayMetrics().widthPixels-2*TOP;
        mTransParentWidth=(int)(mTransParentHeight*mRatio);
        setMeasuredDimension(mWidth,mHeight);
    }

    private void initPaint() {
        mAnglePaint = new Paint();
        mAnglePaint.setColor(mColor);
        mAnglePaint.setStrokeWidth(mStrikeWidth);

        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect();
        getDrawingRect(rect);

        //根据剩余的宽高来计算不透明的边距
        int horizontalPadding=(mWidth-mTransParentWidth)/2;
        int verticalPadding=TOP;

        int top = rect.top + verticalPadding;
        int left = rect.left + horizontalPadding;
        int right = rect.right - horizontalPadding;
        int bottom = rect.bottom - verticalPadding;

        mTransParentRect = new Rect(left, top, right, bottom);
        Log.d(TAG, String.format("top=%s left=%s right=%s bottom=%s", top, left, right, bottom));

        // 画各个对角，线
        //left up corner
        canvas.drawLine(left, top + mLen, left, top, mAnglePaint);
        canvas.drawLine(left, top, left + mLen, top, mAnglePaint);

        canvas.drawLine(left + mLen, top, right - mLen, top, mLinePaint);
        //right up corner
        canvas.drawLine(right - mLen, top, right, top, mAnglePaint);
        canvas.drawLine(right, top, right, top + mLen, mAnglePaint);
        canvas.drawLine(left, top + mLen, left, bottom - mLen, mLinePaint);
        //left bottom corner
        canvas.drawLine(left, bottom - mLen, left, bottom, mAnglePaint);
        canvas.drawLine(left, bottom, left + mLen, bottom, mAnglePaint);
        canvas.drawLine(left + mLen, bottom, right - mLen, bottom, mLinePaint);
        //right bottom corner
        canvas.drawLine(right - mLen, bottom, right, bottom, mAnglePaint);
        canvas.drawLine(right, bottom - mLen, right, bottom, mAnglePaint);
        canvas.drawLine(right, top + mLen, right, bottom - mLen, mLinePaint);

        // 画图
        Drawable drawable = getResources().getDrawable(R.drawable.icon_background);
        Rect dst = new Rect(left + horiPad, top + verticalPad, right - horiPad, bottom - verticalPad);
        canvas.drawBitmap(drawableToBitmap(drawable), null, dst, null);

        //画字
        String text="are you really do that";
        float textWidth=mAnglePaint.measureText(text);
        Paint.FontMetrics fm = mAnglePaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        Log.d(TAG, String.format("textWidth %s textHeight %s padding bottom  %s", textWidth,textHeight,getPaddingBottom()));
        int rectWidth=mTransParentRect.right-mTransParentRect.left;
        float horidelta=(rectWidth-textWidth)/2;//宽度剩余空间
        float veridelta=(verticalPadding+textHeight)/2;//高度剩余空间
        mAnglePaint.setTextSize(40);
        canvas.drawText("are you really do that",mTransParentRect.left+horidelta,mTransParentRect.bottom+veridelta, mAnglePaint);


        //画背景
        if (!isForeGroundDrew) {
            drawForeground();
            setRotation(-90);
            isForeGroundDrew = true;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("mLen", mLen); // ... save stuff
        bundle.putInt("mTopTextColor", mColor);
        bundle.putInt("mStrikeWidth", mStrikeWidth);
        bundle.putInt("mLineColor", mLineColor);
        bundle.putInt("mLineWidth", mLineWidth);
        return bundle;

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("superState");
            mLen = bundle.getParcelable("mLen");
            mColor = bundle.getParcelable("mTopTextColor");
            mStrikeWidth = bundle.getParcelable("mStrikeWidth");
            mLineColor = bundle.getParcelable("mLineColor");
            mLineWidth = bundle.getParcelable("mLineWidth");
        }
        super.onRestoreInstanceState(state);
    }

    //bitmap 打洞
    private Bitmap punchAHoleInABitmap(Bitmap foreground) {
        Log.d(TAG, String.format(" foreground width %s  height %s", foreground.getWidth(), foreground.getHeight()));
        Bitmap bitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(foreground, 0, 0, paint);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(mTransParentRect, paint);
        return bitmap;
    }


    private void drawForeground() {
        Drawable fdrawable = getResources().getDrawable(R.drawable.semi_transparent);
        Bitmap fBitmap = drawableToBitmap(fdrawable);
        fBitmap = punchAHoleInABitmap(fBitmap);
        Drawable d = new BitmapDrawable(getResources(), fBitmap);
        setBackgroundDrawable(d);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof
                BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        Log.d(TAG, String.format(" canvas width %s  height %s", canvas.getWidth(), canvas.getHeight()));
        drawable.draw(canvas);
        return bitmap;
    }
}
