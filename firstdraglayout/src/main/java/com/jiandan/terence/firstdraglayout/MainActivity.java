package com.jiandan.terence.firstdraglayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.jiandan.terence.firstdraglayout.flappyBird.FlappyBirdView;

public class MainActivity extends AppCompatActivity {
    RotateSensorManager mRotationSensor;
    IDOverlayView mOverlayView;
    private String TAG = "RotateMainActivity";
    ViewGroup.LayoutParams mLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main6);
      //  setContentView(new Panel(this));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView textView = (TextView) findViewById(R.id.tv_text);
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "clicking me", Toast.LENGTH_LONG).show();
                }
            });
        }
        mOverlayView = (IDOverlayView) findViewById(R.id.overlay);
        if(mOverlayView!=null) {
            mOverlayView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mLayoutParams = mOverlayView.getLayoutParams();
            mRotationSensor = new RotateSensorManager(this);
          //  rotateAndScaleView(-90);
            mRotationSensor.setRotateCallBack(new RotateSensorManager.RotationCallBack() {
                @Override
                public void onRotate(float rotation) {
                    Log.d(TAG, "rotation actual degree=" + String.valueOf(rotation));
                    int rot = (int) rotation;
                    if (mOverlayView != null) {
                        if (225 < rot && rot <= 315) {
                            rotateAndScaleView(-90);
                            Log.d(TAG, "rotation degree 270");
                        } else {
                            Log.d(TAG, "rotation degree 90");
                            rotateAndScaleView(0);
                        }
                    }
                }
            });
        }
        //  findViewById(R.id.overlay).setRotation(-90);
       // TimerGridView pixelGrid = (TimerGridView) findViewById(R.id.timer_view);
       // pixelGrid.startPlay();
        FlappyBirdView flappyBirdView=new FlappyBirdView(this);
      //  setContentView(flappyBirdView);
        ((VideoOverlayView)findViewById(R.id.video_overlay)).startTimer();
    }

    AnimatorSet mAnimatorSet = new AnimatorSet();
    boolean isPlaying = false;

    private void rotateAndScaleView(int degree) {
        if (isPlaying||mOverlayView==null) {
            return;
        }
        ObjectAnimator scaleAnimatorX = null, scaleAnimatorY = null;
        if (degree == 0) {
            scaleAnimatorX = ObjectAnimator.ofFloat(mOverlayView, "scaleX", mOverlayView.getScaleX(), 1.0f);
            scaleAnimatorY = ObjectAnimator.ofFloat(mOverlayView, "scaleY", mOverlayView.getScaleY(), 1.0f);
        } else if (degree == -90) {
            scaleAnimatorX = ObjectAnimator.ofFloat(mOverlayView, "scaleX", mOverlayView.getScaleX(), 2.0f);
            scaleAnimatorY = ObjectAnimator.ofFloat(mOverlayView, "scaleY", mOverlayView.getScaleY(), 2.0f);

        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mOverlayView, "rotation", mOverlayView.getRotation(), degree);
        mAnimatorSet.playTogether(scaleAnimatorX, scaleAnimatorY, objectAnimator);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator());
        mAnimatorSet.setDuration(500);
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isPlaying = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isPlaying = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isPlaying = false;
            }
        });
        mAnimatorSet.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRotationSensor != null) {
           // mRotationSensor.start();
        }
    }

    protected void onStop() {
        super.onStop();
        if (mRotationSensor != null) {
           // mRotationSensor.stop();
        }
    }
}
