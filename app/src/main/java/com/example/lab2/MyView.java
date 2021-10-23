package com.example.lab2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MyView extends View {
    private double[] orientation = null;
    private float[] deviceVector = null;
    private List<SpecificLocation> locations;
    private SpecificLocation myLocation;

    public void setOrientation(double[] orientation) {
        this.orientation = orientation;
    }

    public void setDeviceVector(float[] deviceVector) {
        this.deviceVector = deviceVector;
    }

    public void setLocations(List<SpecificLocation> locations) {
        this.locations = locations;
    }

    public void setMyLocation(SpecificLocation myLocation) {
        this.myLocation = myLocation;
    }


    public MyView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void invalidateLocations() {
        locations.forEach(location -> location.invalidate(myLocation.getLocation(), deviceVector));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setTextSize(20f);
        p.setARGB(255, 255, 0, 0);

        int x = 100;
        AtomicInteger y = new AtomicInteger(1);

        canvas.drawText("Device orientation:" + Arrays.toString(orientation), x, (y.getAndIncrement()) * 100, p);
        canvas.drawText("Device vector:" + Arrays.toString(deviceVector), x, (y.getAndIncrement()) * 100, p);
        canvas.drawText(myLocation.toString(), x, (y.getAndIncrement()) * 100, p);
        locations.forEach(location -> canvas.drawText(location.toString(), x, (y.getAndIncrement()) * 100, p));
    }
}
