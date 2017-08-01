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
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by HP on 2017/7/26.
 */

public class OverlayView2 extends AppCompatImageView {
    private String TAG = "OverlayView";
    int mLen = 30, mStrikeWidth = 10;
    int mColor, mLineColor, mLineWidth = 1;
    Paint mPaint, mLinePaint;



    public OverlayView2(Context context) {
        super(context);
    }

    public OverlayView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.IDOverlayView);
        mLen = (int) ta.getDimension(R.styleable.IDOverlayView_strike_lenth, mLen);
        mStrikeWidth = (int) ta.getDimension(R.styleable.IDOverlayView_strike_width, mStrikeWidth);
        mColor = ta.getColor(R.styleable.IDOverlayView_strike_color, Color.GREEN);
        mLineColor = ta.getColor(R.styleable.IDOverlayView_line_color, Color.GRAY);
        mLineWidth = (int) ta.getDimension(R.styleable.IDOverlayView_line_width, mLineWidth);

        ta.recycle();
        initPaint();
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mStrikeWidth);

        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect();
        getDrawingRect(rect);
        int top = rect.top;
        int left = rect.left;
        int right = rect.right;
        int bottom = rect.bottom;
        Log.d(TAG, String.format("top=%s left=%s right=%s bottom=%s", top, left, right, bottom));

        //left up corner
        canvas.drawLine(left, top + mLen, left, top, mPaint);
        canvas.drawLine(left, top, left + mLen, top, mPaint);

        canvas.drawLine(left + mLen, top, right - mLen, top, mLinePaint);
        //right up corner
        canvas.drawLine(right - mLen, top, right, top, mPaint);
        canvas.drawLine(right, top, right, top + mLen, mPaint);
        canvas.drawLine(left, top + mLen, left, bottom - mLen, mLinePaint);
        //left bottom corner
        canvas.drawLine(left, bottom - mLen, left, bottom, mPaint);
        canvas.drawLine(left, bottom, left + mLen, bottom, mPaint);
        canvas.drawLine(left + mLen, bottom, right - mLen, bottom, mLinePaint);
        //right bottom corner
        canvas.drawLine(right - mLen, bottom, right, bottom, mPaint);
        canvas.drawLine(right, bottom - mLen, right, bottom, mPaint);
        canvas.drawLine(right, top + mLen, right, bottom - mLen, mLinePaint);

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("mStrikeLen", mLen); // ... save stuff
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
            mLen = bundle.getParcelable("mStrikeLen");
            mColor = bundle.getParcelable("mTopTextColor");
            mStrikeWidth = bundle.getParcelable("mStrikeWidth");
            mLineColor = bundle.getParcelable("mLineColor");
            mLineWidth = bundle.getParcelable("mLineWidth");
        }
        super.onRestoreInstanceState(state);
    }

    private Bitmap punchAHoleInABitmap(Bitmap foreground) {
        Log.d(TAG, String.format(" foreground width %s  height %s", foreground.getWidth(), foreground.getHeight()));
        Bitmap bitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(foreground, 0, 0, paint);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredHeight() / 2, paint);
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
