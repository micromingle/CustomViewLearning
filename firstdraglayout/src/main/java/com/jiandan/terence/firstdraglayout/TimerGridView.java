package com.jiandan.terence.firstdraglayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

//仅支持中文
public class TimerGridView extends View {
    private int mNumColumns, mNumRows;
    private int mCellWidth, mCellHeight;
    private Paint mBlackPaint = new Paint();
    private Paint mRedPaint = new Paint();
    private Paint mDividerPaint = new Paint();
    String mText = "";
    private String TAG = "TimerGridView";
    String[] mTextArray = mText.split("");

    public TimerGridView(Context context) {
        this(context, null);
    }

    public TimerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBlackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBlackPaint.setColor(Color.BLACK);
        mBlackPaint.setTextSize(TypedValue.applyDimension(COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        mRedPaint.setStyle(Paint.Style.STROKE);
        mRedPaint.setColor(Color.RED);
        mRedPaint.setTextSize(TypedValue.applyDimension(COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        mDividerPaint.setStyle(Paint.Style.STROKE);
        mDividerPaint.setColor(Color.GRAY);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TimerGridView);
        mText = ta.getString(R.styleable.TimerGridView_timer_text);
        if (!TextUtils.isEmpty(mText)) {
            mTextArray = mText.split("");
        }
        mNumColumns = ta.getInteger(R.styleable.TimerGridView_columnCount, 6);
        mNumRows = ta.getInteger(R.styleable.TimerGridView_rowCount, 6);
        ta.recycle();

        Log.d(TAG, "mtext lenth =" + mTextArray.length);
    }

    public void setmNumColumns(int mNumColumns) {
        this.mNumColumns = mNumColumns;
        calculateDimensions();
    }

    public int getmNumColumns() {
        return mNumColumns;
    }

    public void setmNumRows(int mNumRows) {
        this.mNumRows = mNumRows;
        calculateDimensions();
    }

    public int getmNumRows() {
        return mNumRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
        Log.d(TAG, "onSizeChanged");
    }

    private void calculateDimensions() {
        if (mNumColumns < 1 || mNumRows < 1) {
            return;
        }

        mCellWidth = getWidth() / mNumColumns;
        mCellHeight = getHeight() / mNumRows;
        invalidate();
    }

    private int mCurrentIndex = 0;//当前读到的字

    private final int DELAY = 500;

    Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            mCurrentIndex++;
            invalidate();
            postDelayed(this, DELAY);
        }
    };

    public void startPlay() {
        resetTimer();
        postDelayed(mTimerRunnable, DELAY);
    }

    public void stopPlay() {
        removeCallbacks(mTimerRunnable);
        // resetTimer();
    }

    private void resetTimer() {
        mCurrentIndex = 0;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mTimerRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        if (mNumColumns == 0 || mNumRows == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();
        Paint paint;

        for (int i = 0; i < mNumColumns; i++) {
            for (int j = 0; j < mNumRows; j++) {
                canvas.drawRect(i * mCellWidth, j * mCellHeight,
                        (i + 1) * mCellWidth, (j + 1) * mCellHeight,
                        mDividerPaint);
                int textIndex = j * mNumColumns + i;
                if (textIndex <= mCurrentIndex) {
                    //画红色
                    paint = mRedPaint;
                } else {
                    //画黑色
                    paint = mBlackPaint;
                }
                if (mTextArray != null) {
                    if (textIndex < mTextArray.length) {
                        if (textIndex != 0) {
                            String text = mTextArray[textIndex];
                            float textWidth = paint.measureText(text);
                            Paint.FontMetrics fm = paint.getFontMetrics();
                            float textHeight = fm.descent - fm.ascent;
                            int dx = (int) ((mCellWidth - textWidth) / 2);
                            int dy = (int) (mCellHeight - textHeight) / 2;
                            canvas.drawText(text, i * mCellWidth + dx, (j + 1) * mCellHeight - dy, paint);
                        } else {
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                            int bWidth = bitmap.getWidth();
                            int bHeight = bitmap.getHeight();
                            int dx = (int) ((mCellWidth - bWidth) / 2);
                            int dy = (int) (mCellHeight - bHeight) / 2;
                            canvas.drawBitmap(bitmap, i * mCellWidth + dx, j * mCellHeight + dy, paint);
                        }
                    }

                    if (mCurrentIndex > mTextArray.length) {
                        stopPlay();
                    }
                } else {
                    Log.d(TAG, "text array is null");
                }

            }
        }

        for (int i = 1; i < mNumColumns; i++) {
            canvas.drawLine(i * mCellWidth, 0, i * mCellWidth, height, mDividerPaint);
        }

        for (int i = 1; i < mNumRows; i++) {
            canvas.drawLine(0, i * mCellHeight, width, i * mCellHeight, mDividerPaint);
        }
    }


}