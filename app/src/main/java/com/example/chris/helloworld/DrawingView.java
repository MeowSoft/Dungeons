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



    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;

    //initial color
    private int paintColor = 0xFF660000;

    //canvas
    private Canvas drawCanvas;

    //canvas bitmap
    private Bitmap canvasBitmap;

    private static final int MAP_DRAW_SIZE = 9;

	private int RoomSize;
	
	
	//{ DrawingView constructor ================================================
	//|
	//| This constructor calls the superclass constructor and then sets up
	//| a canvas to draw our dungeon map on.
	//|
	//| ------------------------------------------------------------------------
    public DrawingView(
		Context 		context,
		AttributeSet 	attributes
	){
	//--------------------------------------------------------------------------
		
		//Call superclass constructor.
        super(context, attributes);
		
		//Set up canvas.
        setupDrawing();
    }
	//} ------------------------------------------------------------------------

	//{ onSizeChanged method ===================================================
	//|
	//|	Override for View::onSizeChanged method.
	//|
	//| Results:
	//|
	//|		Creates a new bitmap and canvas and sets the width of the dungeon
	//|		rooms based on the width of the canvas.
	//|
	//| ------------------------------------------------------------------------
    @Override
    protected void onSizeChanged(
		int 	viewWidth,
		int 	viewHeight,
		int 	oldWidth,
		int 	oldHeight
	){
	//--------------------------------------------------------------------------
	
        //Call superclass onSizeChanged.
        super.onSizeChanged(viewWidth, viewHeight, oldWidth, oldHeight);

		//Create a bitmap with the new view width and height.
        canvasBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
		
		//Create a new canvas with the bitmap.
        drawCanvas = new Canvas(canvasBitmap);
		
		//Set roomsize based on new canvas width.
		RoomSize = (drawCanvas.getWidth() / MAP_DRAW_SIZE);
    }
	//} ------------------------------------------------------------------------
	
	//{ onDraw method ==========================================================
	//|
	//|	Override for View::onDraw method.
	//|
	//| Results:
	//|
	//|		Draws the canvas bitmap to the screen.
	//|
	//| ------------------------------------------------------------------------
    @Override
    protected void onDraw(
		Canvas 	canvas
	){
	//--------------------------------------------------------------------------

		//Redraw the canvas.
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
    }
	//} ------------------------------------------------------------------------

	//{ onTouchEvent method ====================================================
	//|
	//|	Override for View::onTouchEvent method.
	//|
	//| Results:
	//|
	//|		Detects touch events on the view.
	//|
	//| ------------------------------------------------------------------------
    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch

        invalidate();
        return true;
    }
*/
	public int getMapDim() {
		return(MAP_DRAW_SIZE);
	}
	
		
    private void setupDrawing(){

        //get drawing area setup for interaction

        drawPaint = new Paint();


        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(2);
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPaint.setStrokeJoin(Paint.Join.MITER);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setColor(String newColor){

        //set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void DrawRoom(int RoomX, int RoomY, int MaxX, int MaxY, boolean room, boolean ctr) {

        int PixX;
        int PixY;

        PixX = ((RoomX) * RoomSize);
        PixY = ((RoomY) * RoomSize);
drawPaint.setColor(Color.parseColor("Black"));
		if(room) {
			
			drawPaint.setColor(Color.parseColor("Red"));
		}
		
	
			
		
        drawCanvas.drawRect(PixX, PixY, (PixX + RoomSize - 5), (PixY + RoomSize - 5), drawPaint );
		if(ctr) {
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setColor(Color.parseColor("Green"));
			drawCanvas.drawRect(PixX, PixY, (PixX + RoomSize - 5), (PixY + RoomSize - 5), drawPaint );
			drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		}
        invalidate();
    }

    public void EraseMap() {

	drawPaint.setColor(Color.parseColor("Black"));
        drawCanvas.drawColor(0xFF888888);
        invalidate();
    }


}
