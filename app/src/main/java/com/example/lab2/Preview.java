package com.example.lab2;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

public class Preview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera camera;

    Preview(Context context) {
        super(context);
        mHolder = getHolder();

        mHolder.addCallback(this);

        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {

        camera = Camera.open();
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        if (mHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Camera.Parameters parameters = camera.getParameters();

        List<Camera.Size> supportedPeviews = parameters.getSupportedPreviewSizes();

        supportedPeviews
                .stream()
                .filter(preview -> Math.abs(preview.width - w) <= 200
                        && Math.abs(preview.height - h) <= 200)
                .findFirst()
                .ifPresent(size -> {
                    parameters.setPreviewSize(size.width, size.height);
                    camera.setParameters(parameters);
                });

        try {
            camera.setPreviewDisplay(mHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}