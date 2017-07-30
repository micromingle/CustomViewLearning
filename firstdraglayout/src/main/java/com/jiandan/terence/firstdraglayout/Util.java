package com.jiandan.terence.firstdraglayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

/**
 * Created by micromingle on 29/07/2017.
 */

public class Util {


    /**
     * dp2px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                        .getDisplayMetrics()));
        return px;
    }

    public static Bitmap loadBitmapByRes(Context context, int res){
        return BitmapFactory.decodeResource(context.getResources(),res);
    }
}
