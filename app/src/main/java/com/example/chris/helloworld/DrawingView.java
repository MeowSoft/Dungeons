package com.example.chris.helloworld;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

/**
 * Created by Chris on 7/23/2016.
 */
public class DrawingView extends View {

    //drawing path
    private Path drawPath;

    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;

    //initial color
    private int paintColor = 0xFF660000;

    //canvas
    private Canvas drawCanvas;

    //canvas bitmap
    private Bitmap canvasBitmap;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }


    private void setupDrawing(){

        //get drawing area setup for interaction
        drawPath = new Path();
        drawPaint = new Paint();


        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(2);
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPaint.setStrokeJoin(Paint.Join.MITER);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        //view given size

        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw view

        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch
/*
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
*/
        invalidate();
        return true;
    }

    public void setColor(String newColor){

        //set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void DrawRoom(int RoomX, int RoomY, boolean room) {

        int PixX;
        int PixY;

        PixX = RoomX * 30;
        PixY = RoomY * 30;

		if(room) {
			drawPaint.setColor(Color.parseColor("Red"));
		}
		
		else {
			drawPaint.setColor(Color.parseColor("Black"));
		}
			
			
		
        drawCanvas.drawRect(PixX, PixY, (PixX + 25), (PixY + 25), drawPaint );
        invalidate();
    }

    public void EraseMap() {

	drawPaint.setColor(Color.parseColor("Black"));
        drawCanvas.drawColor(0xFFFFFFFF);
        invalidate();
    }

    public int GetCenterX() {

        return (drawCanvas.getWidth() / 2);
    }

    public int GetCenterY() {

        return (drawCanvas.getHeight() / 2);
    }

}
