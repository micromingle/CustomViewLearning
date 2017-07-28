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
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.text.Format;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by HP on 2017/7/26.
 */
//view for 手持身份证
public class VideoOverlayView extends View {
    private String TAG = "OverlayView";
    int mTopTextColor, mBottomTextColor;
    Paint mTopTextPaint, mRectPaint, mBottomTextPaint;
    String mTopText;
    String mBottomText;
    int mRadius;
    Rect mTransParentRect;//透明的区域矩形
    private boolean isForeGroundDrew = false;
    Canvas mCanvas;


    public VideoOverlayView(Context context) {
        super(context);
    }

    public VideoOverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.VideoOverlayView);
        mTopTextColor = ta.getColor(R.styleable.VideoOverlayView_top_text_color, Color.WHITE);
        mTopText = ta.getString(R.styleable.VideoOverlayView_top_text);
        mRadius = (int) ta.getDimension(R.styleable.VideoOverlayView_outer_radis, TypedValue.applyDimension(COMPLEX_UNIT_DIP, 16,
                getResources().getDisplayMetrics()));
        mBottomText = ta.getString(R.styleable.VideoOverlayView_bottom_text);
        mBottomTextColor = ta.getColor(R.styleable.VideoOverlayView_bottom_text_color, Color.WHITE);
        Log.d(TAG, String.format("read text %s", mTopText));
        ta.recycle();
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void initPaint() {
        mTopTextPaint = new Paint();
        mTopTextPaint.setColor(mTopTextColor);
        mTopTextPaint.setTextSize(TypedValue.applyDimension(COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics()));

        mBottomTextPaint = new Paint();
        mBottomTextPaint.setColor(Color.WHITE);
        mBottomTextPaint.setTextSize(TypedValue.applyDimension(COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));


        mRectPaint = new Paint();
        mRectPaint.setColor(Color.WHITE);
        mRectPaint.setStrokeWidth(2);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setAntiAlias(true);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas=canvas;
        Rect rect = new Rect();
        getDrawingRect(rect);

        int top = rect.top + getPaddingTop();
        int left = rect.left + getPaddingLeft();
        int right = rect.right - getPaddingRight();
        int bottom = rect.bottom - getPaddingBottom();
        mTransParentRect = new Rect(left, top, right, bottom);
        Log.d(TAG, String.format("top=%s left=%s right=%s bottom=%s", top, left, right, bottom));

        //画圆角矩形描边
        RectF rectF = new RectF(mTransParentRect);
        canvas.drawRoundRect(rectF, mRadius, mRadius, mRectPaint);

        //画顶部的字
        String topText = mTopText == null ? "" : mTopText;
        Log.d(TAG, String.format("text to draw %s", mTopText));
        float textWidth = mTopTextPaint.measureText(topText);
        Paint.FontMetrics fm = mTopTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        Log.d(TAG, String.format("textWidth %s textHeight %s padding bottom  %s", textWidth, textHeight, getPaddingBottom()));
        int rectWidth = mTransParentRect.right - mTransParentRect.left;
        float horidelta = (rectWidth - textWidth) / 2;//宽度剩余空间
        float veridelta = (getPaddingTop()) / 2 + textHeight / 4;//高度剩余空间
        canvas.drawText(topText, mTransParentRect.left + horidelta, veridelta, mTopTextPaint);

        //画底部的字
         setBottomText(mBottomText);
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
        RectF rectF = new RectF(mTransParentRect);
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint);
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
    Runnable mTimerRunnable=new Runnable() {
        @Override
        public void run() {
           setBottomText(getDurationText());
           postDelayed(this,1000);
        }
    };
   // private int mDuration=0;
    private long mStartTime;

    public void startTimer(){
        resetTimer();
        mStartTime=System.currentTimeMillis();
        postDelayed(mTimerRunnable,1000);
    }

    public void stopTimer(){
        removeCallbacks(mTimerRunnable);
        resetTimer();
    }
    private void resetTimer() {
        setBottomText("00:00");
    }
    private String getDurationText(){
        String mText,sText;
        long millis = System.currentTimeMillis() - mStartTime;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds     = seconds % 60;
        if(minutes<10){
            mText="0"+minutes;
        }else{
            mText=String.valueOf(minutes);
        }
        if(seconds<10){
            sText="0"+seconds;
        }else{
            sText=String.valueOf(seconds);
        }
        return String.format("%s:%s",mText,sText);
    }

    private void setBottomText(String text){
        String bottomText = text == null ? "" : text;
        Log.d(TAG, String.format("text to draw %s", bottomText));
        mTopTextPaint.setTextSize(TypedValue.applyDimension(COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics()));
        float textWidth = mBottomTextPaint.measureText(bottomText);
        Paint.FontMetrics fm = mBottomTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        Log.d(TAG, String.format("textWidth %s textHeight %s padding bottom  %s", textWidth, textHeight, getPaddingBottom()));
        float rectWidth = mTransParentRect.right - mTransParentRect.left;
        float horidelta = (rectWidth - textWidth) / 2;//宽度剩余空间
        float veridelta = mTransParentRect.bottom - 15;//高度
        mCanvas.drawText(bottomText, mTransParentRect.left + horidelta, veridelta, mBottomTextPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mTimerRunnable);
    }
}
