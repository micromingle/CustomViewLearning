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
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by HP on 2017/7/26.
 */
//view for 手持身份证
public class HandOverlayView extends View {
    private String TAG = "OverlayView";
    int mTextColor;
    Paint mTextPaint,mRectPaint;
    String mText;
    int mRadius;
    Rect mTransParentRect;//透明的区域矩形
    private boolean isForeGroundDrew = false;


    public HandOverlayView(Context context) {
        super(context);
    }

    public HandOverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.HandOverlayView);
        mTextColor = ta.getColor(R.styleable.HandOverlayView_text_color, Color.WHITE);
        mText = ta.getString(R.styleable.HandOverlayView_hint_text);
        mRadius = (int)ta.getDimension(R.styleable.HandOverlayView_rect_radis,24);
        Log.d(TAG, String.format("read text %s",mText));
        ta.recycle();
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mRectPaint=new Paint();
        mRectPaint.setColor(Color.WHITE);
        mRectPaint.setStrokeWidth(2);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        getDrawingRect(rect);

        int top = rect.top + getPaddingTop();
        int left = rect.left + getPaddingLeft();
        int right = rect.right - getPaddingRight();
        int bottom = rect.bottom - getPaddingBottom();
        mTransParentRect = new Rect(left, top, right, bottom);
        Log.d(TAG, String.format("top=%s left=%s right=%s bottom=%s", top, left, right, bottom));

        //画圆角矩形描边
        RectF rectF=new RectF(mTransParentRect);
        canvas.drawRoundRect(rectF,mRadius,mRadius, mRectPaint);

        //画字
        String text=mText==null?"":mText;
        Log.d(TAG, String.format("text to draw %s",mText));
        mTextPaint.setTextSize(TypedValue.applyDimension(COMPLEX_UNIT_SP,13,getResources().getDisplayMetrics()));
        float textWidth= mTextPaint.measureText(text);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        Log.d(TAG, String.format("textWidth %s textHeight %s padding bottom  %s", textWidth,textHeight,getPaddingBottom()));
        int rectWidth=mTransParentRect.right-mTransParentRect.left;
        float horidelta=(rectWidth-textWidth)/2;//宽度剩余空间
        float veridelta=(getPaddingBottom())/2+textHeight/4;//高度剩余空间
        canvas.drawText(text,mTransParentRect.left+horidelta,mTransParentRect.bottom+veridelta, mTextPaint);
        //画背景
        if (!isForeGroundDrew) {
            drawForeground();
            isForeGroundDrew = true;
        }
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
        RectF rectF=new RectF(mTransParentRect);
        canvas.drawRoundRect(rectF,mRadius,mRadius, paint);
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
