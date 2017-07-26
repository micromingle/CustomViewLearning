package com.jiandan.terence.firstdraglayout;

import android.content.Context;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HP on 2017/6/30.
 */

public class TagLayout extends ViewGroup {

    private String TAG = "TagLayout";


    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new TagLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new TagLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new TagLayoutParams(100, 100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0, height = 0;
        int lineHeight = 0, lineWidth = 0;
        int count = getChildCount();
        //计算宽度
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if ((view.getVisibility() != GONE)) {
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                TagLayoutParams lp = (TagLayoutParams) view.getLayoutParams();
                Log.d(TAG, String.format("margin left = %s  margin right = %s margin top = %s margin bottom = %s", lp.leftMargin, lp.rightMargin,
                        lp.topMargin, lp.bottomMargin));
                int childHeight=lp.topMargin  + view.getMeasuredHeight() + lp.bottomMargin;
                int childWidth =lp.leftMargin + view.getMeasuredWidth()  + lp.rightMargin;
                if(lineWidth+childWidth>getMeasuredWidth()){
                    width=Math.max(lineWidth,width);
                    height+=lineHeight;
                    lineWidth=childWidth;
                    lineHeight=childHeight;
                }else{
                    lineWidth+=childWidth;
                    lineHeight=Math.max(lineHeight,childHeight);
                }
            }
            if (i == count -1){
                height += lineHeight;
                width = Math.max(width,lineWidth);
            }
        }
        width = width > getSuggestedMinimumWidth() ? width : getSuggestedMinimumWidth();
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
     if(changed){
        Log.d(TAG, " layout changed  ");
        int count = getChildCount();
        int xPos = 0, yPos = 0;
        int lineHeight=0;
        Log.d(TAG, " measured width onlayout = " + getMeasuredWidth());
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            TagLayoutParams lp = (TagLayoutParams) view.getLayoutParams();
            int childWidth = lp.leftMargin + view.getMeasuredWidth() + lp.rightMargin;
            int childHeight  = lp.topMargin  + view.getMeasuredHeight()  + lp.bottomMargin;
            int rTem = xPos + childWidth;
            int bTem = yPos + childHeight;
            Log.d(TAG, " child width =  " + childWidth + " child height " + childHeight);
            Log.d(TAG, String.format(" rTem = %s bTem = %s", rTem, bTem));
            if (rTem > getMeasuredWidth()) {
                Log.d(TAG, " step in ");
                xPos = 0;
                yPos += lineHeight; // 加的高度应该以当行最高的那一个高度为准，待优化
                rTem = xPos + childWidth;
                bTem = yPos + childHeight;
                lineHeight = childHeight;
            }else{
                lineHeight = Math.max(lineHeight,childHeight);
            }
            view.layout(xPos + lp.leftMargin, yPos + lp.topMargin, rTem, bTem);
            xPos = rTem;
        }
          }else{
          Log.d(TAG, " layout not changed");
           }
    }

    public static class TagLayoutParams extends MarginLayoutParams {

        public TagLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public TagLayoutParams(LayoutParams source) {
            super(source);
        }

        public TagLayoutParams(@Px int width, @Px int height) {
            super(width, height);
        }
    }




}
