package com.jiandan.terence.firstdraglayout.flappyBird;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.jiandan.terence.firstdraglayout.R;
import com.jiandan.terence.firstdraglayout.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micromingle on 29/07/2017.
 */

public class FlappyBirdView extends SurfaceView implements Runnable,SurfaceHolder.Callback{

    SurfaceHolder mSurfaceHolder;
    Thread mRenderThread;
    private volatile boolean isRunning=false;
    Canvas mCanvas;
    private int mWidth,mHeight;
    String TAG="FlappyBirdView";
    private Background mBackground;
    private Bird mBird;
    final int DOWN_SPEED=1,UP_SIE=-8;
    private int mAutoDownSpeed,mTouchUpSize;
    private int mDownHeight=0;
    private Paint mFloorPaint=new Paint();
    private Floor mFloor;
    private int mHorizontalSpeed=10;
    private Pipe mPipe;
    private Score mScore;
    private int mMoveDistance;
    private List<Pipe> mPipeList= new ArrayList<Pipe>();
    private List<Pipe> mPipeRemoved=new ArrayList<>();
    private  final int PIPE_DISTANCE=200;
    private  int mPipeDistance;
    private enum GameStatus {
        WAITTING, RUNNING, STOP;
    };

    /**
     * 记录游戏的状态
     */
    private GameStatus mStatus = GameStatus.WAITTING;

    public FlappyBirdView(Context context) {
        this(context,null);
    }


    public FlappyBirdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder=getHolder();
        mSurfaceHolder.addCallback(this);
        setZOrderOnTop(true);// 设置画布 背景透明
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

        // 设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 设置常亮
        setKeepScreenOn(true);
        mAutoDownSpeed=Util.dp2px(getContext(),DOWN_SPEED);
        mTouchUpSize=Util.dp2px(getContext(),UP_SIE);
        mPipeDistance=Util.dp2px(getContext(),PIPE_DISTANCE);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;

        mBackground=new Background(getContext(),mWidth,mHeight, R.drawable.background);
        mBird=new Bird(getContext(),mWidth,mHeight,R.drawable.bird);
        mFloor=new Floor(getContext(),mWidth,mHeight,R.drawable.floor_bg);
        mPipe=new Pipe(getContext(),mWidth,mHeight);
        mScore=new Score(mWidth,mHeight,getContext());
        Pipe pipe=new Pipe(getContext(),mWidth,mHeight);
        mPipeList.add(pipe);
    }

    @Override
    public void run() {
        while(isRunning){
            logic();
            draw();
        }

    }

    private void draw(){
        try {
            if(mSurfaceHolder!=null){

                mCanvas=mSurfaceHolder.lockCanvas();

                if(mCanvas!=null){

                        drawBackground();
                        drawBird();
                        drawPipe();
                        drawFloor();
                        drawScore();



                }
                Thread.sleep(50);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCanvas!=null){
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG,"surfaceCreated");
        isRunning=true;
        mRenderThread=new Thread(this);
        mRenderThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG,"surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning=false;

    }

    /**
     * 处理一些逻辑上的计算
     */
    private void logic() {
        switch (mStatus) {
            case RUNNING:

                // 更新我们地板绘制的x坐标，地板移动
                mFloor.setX(mFloor.getX()-mHorizontalSpeed);

                logicPipe();

                // 默认下落，点击时瞬间上升
                mDownHeight+=mAutoDownSpeed;

                mBird.setY(mBird.getY() + mDownHeight);

                checkGameOver();

                break;

            case STOP: // 鸟落下
                // 如果鸟还在空中，先让它掉下来
                if (mBird.getY() < mFloor.getY() - mBird.getWidth()) {
                    mDownHeight += mAutoDownSpeed;
                    mBird.setY(mBird.getY() + mDownHeight);
                } else {
                    mStatus = GameStatus.WAITTING;
                    restart();
                }
                break;
            default:
                break;
        }

    }

    private void drawBackground(){

        mBackground.draw(mCanvas);

    }

    private void drawFloor(){
        Log.d(TAG," floor x="+mFloor.getX());
        mFloor.draw(mCanvas,mFloorPaint);
    }

    private void drawBird(){
        if(mDownHeight>mHeight){
            mDownHeight=mHeight;
        }
        Log.d(TAG," down height ="+mDownHeight);
        mBird.setY(mBird.getY()+mDownHeight);
        mBird.draw(mCanvas);
    }

    private void logicPipe(){
        for(Pipe pipe:mPipeList){
            if(pipe.isPipeDisappear()){
                mPipeRemoved.add(pipe);
                continue;
            }
            pipe.setX(pipe.getX()-mHorizontalSpeed);
            //算分数
            if(mBird.getStartX()>(pipe.getPipeWidth()+pipe.getX())){
                if(!pipe.isScored()) {
                    mScore.increase();
                    pipe.setScored(true);
                }
            }
        }

        Log.d(TAG,"mNeed remove size="+mPipeRemoved.size());
        Log.d(TAG,"mPipeList "+mPipeList.size());
        if(mPipeRemoved.size()>0){
            mPipeList.removeAll(mPipeRemoved);
            mPipeRemoved.clear();
        }

        mMoveDistance+=mHorizontalSpeed;
        if(mMoveDistance>=mPipeDistance){
            Pipe pipe=new Pipe(getContext(),mWidth,mHeight);
            mPipeList.add(pipe);
            mMoveDistance=0;
        }

    }

    private void drawPipe(){



        for(Pipe pipe:mPipeList){
            pipe.draw(mCanvas);
        }

      //  mPipe.draw(mCanvas);
    }

    private void checkGameOver(){
        //if touch floor
        if(mBird.touchFloor(mFloor)){
            mStatus = GameStatus.STOP;
            return;
        }
        for(Pipe pipe:mPipeList){
            // 已经穿过的
            if (pipe.getX() + pipe.getPipeWidth() < mBird.getStartX()) {
                continue;
            }
            if(mBird.touchPipe(pipe)){
                mStatus = GameStatus.STOP;
                return;
            }
        }

    };

    private void showGameOver(){
        isRunning=false;
        post(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getContext(),"game over",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("GAME IS OVER");
                builder.setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restart();
                    }
                });
                builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(getContext() instanceof Activity){
                            ((Activity) getContext()).finish();
                        }
                    }
                });
                builder.create().show();

            }
        });
    }



    private void restart(){
        mMoveDistance=0;
        mPipeRemoved.clear();
        mPipeList.clear();
        mScore.reset();
        mBird.reset();
        mStatus = GameStatus.WAITTING;
        mDownHeight=0;
       // isRunning=true;
        Pipe pipe=new Pipe(getContext(),mWidth,mHeight);
        mPipeList.add(pipe);
    }

    private void drawScore(){
        mScore.draw(mCanvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            switch (mStatus) {
                case WAITTING:
                    mStatus = GameStatus.RUNNING;
                    break;
                case RUNNING:
                    mDownHeight=mTouchUpSize;
                    break;
            }

        }

        return super.onTouchEvent(event);
    }
}
