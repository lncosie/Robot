package com.lncosie.robot.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.lncosie.robot.R;


/**
 * Created by qishui on 16/4/21.
 */
public class OverlayView extends RelativeLayout implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    String version=null;
    public OverlayView(Context context) {
        super(context);
        initView();
    }

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_overlay, this, true);
        surfaceView = (SurfaceView) findViewById(R.id.log_view);
        surfaceView.getHolder().addCallback(this);
        version=this.getContext().getString(R.string.version);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.holder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.holder = null;
    }

    public void addLog(String log) {
        drawText(log);
    }

    public void drawText(String text) {
        if (this.holder == null) {
            return;
        }

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.YELLOW);
        p.setTextSize(20);

        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.BLUE);
        canvas.drawText(text, 10, 30, p);
        if(version!=null)
            canvas.drawText(version, 10,60,p);
        holder.unlockCanvasAndPost(canvas);
    }
}
