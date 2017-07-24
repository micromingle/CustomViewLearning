package com.jiandan.terence.firstdraglayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

/**
 * Created by micromingle on 22/07/2017.
 */

public class SwipeDeleteLayout extends ViewGroup {


    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private float mLastX,mDownX;

    private View mLeftView,mRightView;
    private String TAG="SwipeDeleteLayout";
    private int mLeftBorder, mRightBorder;
    private boolean isRightViewShow;

    public SwipeDeleteLayout(Context context) {
        super(context);
    }


    public SwipeDeleteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();
        Log.d(TAG,String.format("touch slop %s", mTouchSlop));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count=getChildCount();
        for(int i=0;i<count;i++){
            View view=getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
        }
    }
    //横向排列
    //为特殊需求写的自定义view,不具有普适性，请慎用
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
             if(changed){
                 int count=getChildCount();
                 int left=getPaddingLeft();
                 for(int i=0;i<count;i++){
                     View view=getChildAt(i);
                     SwipeLayoutParam layoutParam=(SwipeLayoutParam)view.getLayoutParams();
                      left=left+layoutParam.leftMargin;
                     int right=left+view.getMeasuredWidth()+layoutParam.rightMargin;
                     int top=layoutParam.topMargin;
                     int bottom=top+view.getMeasuredHeight()+layoutParam.bottomMargin;
                     Log.d(TAG, String.format("onlayout left  %s right  %s top %s bottom %s", left, right,top,bottom));
                     view.layout(left,top,right,bottom);
                     left=right;

                 }
             }
         mLeftBorder = getChildAt(0).getLeft();
        mRightBorder = getChildAt(getChildCount() - 1).getRight();

        Log.d(TAG,String.format("left border %s right border %s", mLeftBorder, mRightBorder));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLeftView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

    }



    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mLeftView = findViewById(R.id.id_item_left);
        mRightView = findViewById(R.id.id_item_right);
        if(mLeftView==null){
            throw new IllegalArgumentException("id item left not found");
        }

        if(mRightView==null){
            throw new IllegalArgumentException("id item right not found");
        }
    }



    /*核心要点：action_down 的时候一定要记录位置*/
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        Log.d(TAG, "onInterceptTouchEvent");
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, " ACTION_DOWN" );
                mDownX = ev.getX();
                mLastX = mDownX;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE");
                float curX = ev.getX();
                float dif = mDownX - curX;
                mLastX = curX;
                if (Math.abs(dif) > mTouchSlop) {
                    Log.d(TAG, "ACTION_MOVE intercepted");
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
                Log.d(TAG, "ACTION_MOVE");
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();

                mVelocityTracker.clear();
                mVelocityTracker.addMovement(ev);
                float curX = ev.getX();
                float dif = mLastX - curX;
                mLastX = curX;
                if (getScrollX() + dif < mLeftBorder) {
                    scrollTo(mLeftBorder, 0);
                    return true;
                } else if (getScrollX() + dif + getWidth() > mRightBorder) {
                    scrollTo(mRightBorder - getWidth(), 0);
                    return true;
                }
                scrollBy((int)dif,0);
                Log.d(TAG, "ACTION_MOVE dif="+dif);

                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityX = (int) mVelocityTracker.getXVelocity();
                Log.d(TAG, "ACTION_UP velocityX="+velocityX);
                Log.d(TAG, "ACTION_UP scroll x="+getScrollX());

                //需要判断方向
                if (Math.abs(velocityX) > mMinimumVelocity) {
                    int left=mRightView.getWidth()-getScrollX();
                    Log.d(TAG, "ACTION_UP fling showing ="+isRightViewShow);
                    if (velocityX >= 0) {
                        //向右滑
                        if(isRightViewShow) {
                            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
                            invalidate();
                            isRightViewShow=false;
                        }
                    } else {
                        //向左滑
                        if(!isRightViewShow) {
                            mScroller.startScroll(getScrollX(), 0,left, 0);
                            invalidate();
                            isRightViewShow=true;
                        }
                    }

                }else{
                    Log.d(TAG, "ACTION_UP scroll");
                    // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                    int left=mRightView.getWidth()-getScrollX();
                    if(left<mRightView.getWidth()/2){
                        mScroller.startScroll(getScrollX(), 0, left, 0);
                        invalidate();
                        isRightViewShow=true;
                    }else{
                        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
                        invalidate();
                        isRightViewShow=false;

                    }
                }
                mVelocityTracker.clear();
                break;
            default:

        }
        return super.onTouchEvent(ev);
    }

    public static class SwipeLayoutParam extends MarginLayoutParams{

        public SwipeLayoutParam(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public SwipeLayoutParam(int width, int height) {
            super(width, height);
        }

        public SwipeLayoutParam(LayoutParams source) {
            super(source);
        }

        public SwipeLayoutParam(MarginLayoutParams source) {
            super(source);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new SwipeLayoutParam(getContext(),attrs);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return super.checkLayoutParams(p)&&(p instanceof SwipeLayoutParam);
    }

    @Override
    public void computeScroll() {
      if(mScroller.computeScrollOffset()){
          scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
          invalidate();
      }
    }
}
