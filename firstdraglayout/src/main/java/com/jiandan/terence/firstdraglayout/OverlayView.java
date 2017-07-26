package com.jiandan.terence.firstdraglayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by HP on 2017/7/26.
 */

public class OverlayView extends AppCompatImageView {
    private String TAG = "OverlayView";
    int mLen = 30, mStrikeWidth = 10;
    int mColor;
    Paint mPaint;

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
        ta.recycle();
        initPaint();
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mStrikeWidth);
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

        //right up corner
        canvas.drawLine(right - mLen, top, right, top, mPaint);
        canvas.drawLine(right, top, right, top + mLen, mPaint);

        //left bottom corner
        canvas.drawLine(left, bottom - mLen, left, bottom, mPaint);
        canvas.drawLine(left, bottom, left + mLen, bottom, mPaint);

        //right bottom corner
        canvas.drawLine(right - mLen, bottom, right, bottom, mPaint);
        canvas.drawLine(right, bottom - mLen, right, bottom, mPaint);
    }


}
