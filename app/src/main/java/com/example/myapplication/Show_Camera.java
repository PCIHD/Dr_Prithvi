package com.example.myapplication;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public  class Show_Camera extends SurfaceView implements SurfaceHolder.Callback {

    android.hardware.Camera camera;
    public  Show_Camera(Context context , Camera camera){
        super(context);
        this.camera = camera;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder , int format , int width , int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        android.hardware.Camera.Parameters params = camera.getParameters();

        if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
            params.set("orientation","portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);

        }
        else
        {
            params.set("orientation","landscape");
            camera.setDisplayOrientation(0);
            params.setRotation(0);

        }
        camera.setParameters(params);
        try {
            camera.setPreviewDisplay(holder);
        }catch(IOException e1){
            e1.printStackTrace();
        }


    }

}
