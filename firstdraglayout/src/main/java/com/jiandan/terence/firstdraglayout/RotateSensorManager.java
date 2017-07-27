package com.jiandan.terence.firstdraglayout;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by HP on 2017/7/27.
 */

public class RotateSensorManager implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mRotationSensor;
    private Activity mContext;
    private static final int SENSOR_DELAY = 1000 * 1000; // 500ms
    private static final int FROM_RADS_TO_DEGS = -57;
    private RotationCallBack mCallBack;
    private String TAG="RotateSensorManager";

    public RotateSensorManager(Activity context) {
        this.mContext = context;
        try {
            mSensorManager = (SensorManager) mContext.getSystemService(Activity.SENSOR_SERVICE);
            mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//TYPE_ROTATION_VECTOR

        } catch (Exception e) {
            Toast.makeText(mContext, "Hardware compatibility issue", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void setRotateCallBack(RotationCallBack rotateCallBack){
        mCallBack=rotateCallBack;
    }

    public void start(){
        Log.d(TAG,"start");
        mSensorManager.registerListener(this, mRotationSensor,SENSOR_DELAY);
    }

    public void stop(){
        Log.d(TAG,"stop ");
        mSensorManager.unregisterListener(this);
    }
    public void destroy(){
        Log.d(TAG,"stop ");
       // mSensorManager.unregisterListener(this);
        mContext=null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG,"onSensorChanged ="+event.toString());
        if (event.sensor == mRotationSensor) {
            if (event.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                update(truncatedRotationVector);
            } else {
                update(event.values);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG,"onAccuracyChanged");
    }

    private void update(float[] vectors) {
//        float[] rotationMatrix = new float[9];
//        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
//        int worldAxisX = SensorManager.AXIS_X;
//        int worldAxisZ = SensorManager.AXIS_Z;
//        float[] adjustedRotationMatrix = new float[9];
//        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
//        float[] orientation = new float[3];
//        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
//        float pitch = orientation[1] * FROM_RADS_TO_DEGS;
//        float roll = orientation[2] * FROM_RADS_TO_DEGS;
//        Log.d(TAG,String.format("raotationX %s rotationY %s",pitch,roll));
//        if(mCallBack!=null){
//            mCallBack.onRotate(pitch,roll);
//        }
        float[] values =vectors;
        float ax = values[0];
        float ay = values[1];

        double g = Math.sqrt(ax * ax + ay * ay);
        double cos = ay / g;
        if (cos > 1) {
            cos = 1;
        } else if (cos < -1) {
            cos = -1;
        }
        double rad = Math.acos(cos);
        if (ax < 0) {
            rad = 2 * Math.PI - rad;
        }

        int uiRot = mContext.getWindowManager().getDefaultDisplay().getRotation();
        Log.d(TAG,"uiRotation ="+uiRot);
        double uiRad = Math.PI / 2 * uiRot;
        rad -= uiRad;
        int degrees = (int) (180 * rad / Math.PI);
        if(mCallBack!=null){
            mCallBack.onRotate(degrees);
        }
    }

    public interface RotationCallBack{
        void onRotate(float rotation);

    }
}
