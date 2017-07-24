package com.jiandan.terence.firstdraglayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by HP on 2017/7/20.
 */

public class ScrollerLayout extends ViewGroup {

    private Scroller mScroller;
    private float mLastX;
    private int leftBorder, rightBorder;
    private float mDownX;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;
    private String TAG = "ScrollerLayout";

    public ScrollerLayout(Context context) {
        super(context);
    }

    public ScrollerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
        mVelocityTracker = VelocityTracker.obtain();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
        Log.d(TAG, "getMeasuredWidth=" + getMeasuredWidth());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int left = 0;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                int right = left + view.getMeasuredWidth();
                Log.d(TAG, String.format("onlayout left  %s right  %s", left, right));
                view.layout(left, 0, right, view.getMeasuredHeight());
                left = right;
            }
        }
        leftBorder = getChildAt(0).getLeft();
        rightBorder = getChildAt(getChildCount() - 1).getRight();
        Log.d(TAG, String.format("left border %s right border %s", leftBorder, rightBorder));
    }

    /*核心要点：action_down 的时候一定要记录位置*/
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        Log.d(TAG, "onInterceptTouchEvent=" + getWidth());
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, " ACTION_DOWN" + getWidth());
                mDownX = ev.getX();
                mLastX = mDownX;
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = ev.getX();
                float dif = mDownX - curX;
                mLastX = curX;
                if (Math.abs(dif) > mTouchSlop) {
                    Log.d(TAG, " ACTION_MOVE intercepted");
                    return true;
                }
                break;

            default:

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mVelocityTracker.addMovement(ev);
        int action = ev.getAction();
        Log.d(TAG, "onTouchEvent");

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(ev);
                float curX = ev.getX();
                float dif = mLastX - curX;
                mLastX = curX;
                if (getScrollX() + dif < leftBorder) {
                    scrollTo(leftBorder, 0);
                    return true;
                } else if (getScrollX() + dif + getWidth() > rightBorder) {
                    scrollTo(rightBorder - getWidth(), 0);
                    return true;
                }
                scrollBy((int) dif, 0);
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityX = (int) mVelocityTracker.getXVelocity();
                //需要判断方向
                if (Math.abs(velocityX) > mMinimumVelocity) {
                    float current = (float) getScrollX() / (float) getWidth();
                    int targetIndex2;
                    if (velocityX >= 0) {
                        //向右滑
                        targetIndex2 = (int) Math.floor(current);
                    } else {
                        //向左滑
                        targetIndex2 = (int) Math.ceil(current);
                    }
                    int dx1 = targetIndex2 * getWidth() - getScrollX();
                    mScroller.startScroll(getScrollX(), 0, dx1, 0);
                    Log.d(TAG, "flinging");
                    invalidate();
                } else {
                    // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                    int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                    int dx1 = targetIndex * getWidth() - getScrollX();
                    // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                    mScroller.startScroll(getScrollX(), 0, dx1, 0);
                    invalidate();
                }
                mVelocityTracker.clear();
                break;
            default:

        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }


}
