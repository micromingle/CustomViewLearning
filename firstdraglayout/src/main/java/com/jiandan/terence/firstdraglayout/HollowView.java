package com.jiandan.terence.firstdraglayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by HP on 2017/7/27.
 */
// a view with a transparent center
public class HollowView extends View {
    private String TAG = "1HollowView";
    private int mHeight,mWidth;
    private int mHoriPadding=50,mVerPadding=40;

    public HollowView(Context context) {
        super(context);
    }

    public HollowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        post(new Runnable() {
            @Override
            public void run() {
                drawForeground();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth=getMeasuredWidth();
        mHeight=getMeasuredHeight();
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
