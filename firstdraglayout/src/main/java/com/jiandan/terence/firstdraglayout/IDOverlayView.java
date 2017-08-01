package com.jiandan.terence.firstdraglayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by HP on 2017/7/26.
 */
//由于view最终要旋转90度 所以view的高用屏幕的宽来算
// view for 身份证正反面
public class IDOverlayView extends View {
    private String TAG = "OverlayView";
    int mStrikeLen = 30, mStrikeWidth = 10;
    int mColor, mLineColor, mLineWidth = 1;
    Paint mAnglePaint, mLinePaint, mTextPaint;
    Rect mTransParentRect;
    private boolean isForeGroundDrew = false;
    float mRatio = 1.73f;//宽度和高度的比
    int mTransParentHeight = 300, mTransParentWidth = (int) (mTransParentHeight * mRatio);
    int mInnerHoriPad = 30, mInnervertPad = (int) (mInnerHoriPad * mRatio);
    int mHeight, mWidth;
    private final int TOP = 150;//dip
    //private int mOuterPadding=TOP;
    final int DEGREES = -90;
    String mText = "";
    private Drawable mOverLayoutDrawable;

    public IDOverlayView(Context context) {
        this(context, null);
    }

    public IDOverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.IDOverlayView);
        mStrikeLen = (int) ta.getDimension(R.styleable.IDOverlayView_strike_lenth, mStrikeLen);
        mStrikeWidth = (int) ta.getDimension(R.styleable.IDOverlayView_strike_width, mStrikeWidth);
        mColor = ta.getColor(R.styleable.IDOverlayView_strike_color, Color.GREEN);
        mLineColor = ta.getColor(R.styleable.IDOverlayView_line_color, Color.GRAY);
        mLineWidth = (int) ta.getDimension(R.styleable.IDOverlayView_line_width, mLineWidth);
        mText = ta.getString(R.styleable.IDOverlayView_bottom_hint_text);
        mTransParentHeight= (int)ta.getDimension(R.styleable.IDOverlayView_trans_height,353);
        mTransParentWidth= (int)ta.getDimension(R.styleable.IDOverlayView_trans_width,200);
        if (mText == null) {
            mText = "";
        }
        mOverLayoutDrawable=ta.getDrawable(R.styleable.IDOverlayView_overlay_drawable);

        ta.recycle();
        initPaint();
    }

     public void setOverLayDrawable(Drawable drawable){
         mOverLayoutDrawable=drawable;
         invalidate();
     }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
//        mTransParentWidth = w - getPaddingLeft() - getPaddingRight();
//        mTransParentHeight = (int) (mRatio * mTransParentWidth);


    }

    private void initPaint() {
        mAnglePaint = new Paint();
        mAnglePaint.setColor(mColor);
        mAnglePaint.setStrokeWidth(mStrikeWidth);
        mAnglePaint.setStyle(Paint.Style.FILL);
        mAnglePaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.parseColor("#e3e3e3"));
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect viewRect = new Rect(0,0,mWidth,mHeight);

        //根据剩余的宽高来计算不透明的边距
        int horizontalPadding = (mWidth - mTransParentWidth) / 2;
        int verticalPadding = (mHeight - mTransParentHeight) / 2;

        int top = viewRect.top + verticalPadding;
        int left = viewRect.left + horizontalPadding;
        int right = viewRect.right - horizontalPadding;
        int bottom = viewRect.bottom - verticalPadding;

        mTransParentRect = new Rect(left, top, right, bottom);
        Log.d(TAG, String.format("top=%s left=%s right=%s bottom=%s", top, left, right, bottom));

        //画矩形
        canvas.drawRect(left + mLineWidth, top + mLineWidth, right - mLineWidth,
                bottom - mLineWidth, mLinePaint);
        // 画各个对角，线
        //left up corner
        canvas.drawLine(left + mStrikeWidth / 2, top, left + mStrikeWidth / 2, top + mStrikeLen, mAnglePaint);
        canvas.drawLine(left, top + mStrikeWidth / 2, left + mStrikeLen, top + mStrikeWidth / 2, mAnglePaint);
        //right up corner
        canvas.drawLine(right - mStrikeLen, top + mStrikeWidth / 2, right, top + mStrikeWidth / 2, mAnglePaint);
        canvas.drawLine(right - mStrikeWidth / 2, top, right - mStrikeWidth / 2, top + mStrikeLen, mAnglePaint);
        //left bottom corner
        canvas.drawLine(left + mStrikeWidth / 2, bottom - mStrikeLen, left + mStrikeWidth / 2, bottom, mAnglePaint);
        canvas.drawLine(left, bottom - mStrikeWidth / 2, left + mStrikeLen, bottom - mStrikeWidth / 2, mAnglePaint);
        //right bottom corner
        canvas.drawLine(right - mStrikeLen, bottom - mStrikeWidth / 2, right, bottom - mStrikeWidth / 2, mAnglePaint);
        canvas.drawLine(right - mStrikeWidth / 2, bottom - mStrikeLen, right - mStrikeWidth / 2, bottom, mAnglePaint);


        // 画图
        Bitmap bitmap = drawableToBitmap(mOverLayoutDrawable);
        if(bitmap!=null) {
            Rect dst = new Rect(left + mInnerHoriPad, top + mInnervertPad, right - mInnerHoriPad, bottom - mInnervertPad);
            canvas.drawBitmap(rotateBitmap(bitmap, DEGREES), null, dst, null);
        }

        //画字,旋转-90度
        String text = mText;
        float textWidth = mTextPaint.measureText(text);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        Log.d(TAG, String.format("textWidth %s textHeight %s padding bottom  %s", textWidth, textHeight, getPaddingBottom()));
        float startX = mTransParentRect.right + horizontalPadding / 2+textHeight/4;
        float startY = mTransParentRect.bottom-(mTransParentHeight/2-textWidth/2);
        canvas.save();
        canvas.rotate(DEGREES, startX, startY);
        canvas.drawText(mText, startX,
                startY, mTextPaint);
        canvas.restore();

        //画背景
        if (!isForeGroundDrew) {
            drawForeground();
            isForeGroundDrew = true;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("mStrikeLen", mStrikeLen); // ... save stuff
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
            mStrikeLen = bundle.getParcelable("mStrikeLen");
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

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
