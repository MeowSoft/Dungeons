//| ============================================================================
//|
//|	File:			DrawingView.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		DrawingView is a class to set up a view to draw the dungeon map and
//|		other bits to the screen.
//|
//| ----------------------------------------------------------------------------

package com.example.chris.helloworld;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

public class DrawingView extends View  {

	//{ Private members: =======================================================

		//drawing and canvas paint
		private Paint drawPaint;

		//canvas
		private Canvas drawCanvas;

		//canvas bitmap
		private Bitmap canvasBitmap;

		private static final int MAP_DRAW_SIZE = 9;
		private static final int MAP_GRID_BORDER_SIZE = 5;

		private int RoomSize;
		
		private Context myContext;
		
	//} ------------------------------------------------------------------------
	
	//{ Constructors: ==========================================================

	//{ ------------------------------------------------------------------------
	//|
	//| This constructor calls the superclass constructor and then sets up
	//| a canvas to draw our dungeon map on.
	//|
	//| ------------------------------------------------------------------------
    public DrawingView(
		Context 		context,
		AttributeSet 	attributes
	){
		
		//Call superclass constructor.
        super(context, attributes);
		
		myContext = context;
		
		//Set up canvas.
        setupDrawing();
		
    } //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------

	//{ View class method overrides: ===========================================
	
	//{ onSizeChanged ----------------------------------------------------------
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
        //Call superclass onSizeChanged.
        super.onSizeChanged(viewWidth, viewHeight, oldWidth, oldHeight);

		//Create a bitmap with the new view width and height.
        canvasBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
		
		//Create a new canvas with the bitmap.
        drawCanvas = new Canvas(canvasBitmap);
		
		//Set roomsize based on new canvas width.
		RoomSize = (drawCanvas.getWidth() / MAP_DRAW_SIZE);
		
    } //} ----------------------------------------------------------------------
	
	//{ onDraw -----------------------------------------------------------------
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
		//Redraw the canvas.
        canvas.drawBitmap(canvasBitmap, 0, 0, drawPaint);
		
    } //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------

	//{ Private methods: =======================================================
	
	//{ setupDrawing -----------------------------------------------------------
	//|
	//|	Set up the drawing area.
	//|
	//|	Results:
	//|
	//|		A new Paint object is created and its attributes are set.
	//|
	//| ------------------------------------------------------------------------
    private void setupDrawing(){

        //Create paint object.
        drawPaint = new Paint();

		//Set drawing style params.
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(2);
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPaint.setStrokeJoin(Paint.Join.MITER);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);

    } //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------

	//{ Public methods: ========================================================
	
	//{ getMapDim --------------------------------------------------------------
	//|
	//|	Return the dimensions of the viewable map area in number of rooms.
	//|
	//|	Returns:
	//|
	//|		int
	//|		The size of the viewable map area in rooms.
	//|
	//|	Results:
	//|
	//|		The square viewing area number of map rooms to display is returned.
	//|
	//| ------------------------------------------------------------------------
	public int getMapDim() {
		
		//Return draw size.
		return(MAP_DRAW_SIZE);
		
	} //} ----------------------------------------------------------------------
	
	//{ DrawRoom ---------------------------------------------------------------
	//|
	//| Draw a map room to the view.
	//|
	//|	Function parameters:
	//|
	//|		RoomX
	//|		RoomY
	//|		Map coordinates of the room to draw relative to the room in the
	//|		center of the display.
	//|
	//|	Results:
	//|
	//|		The room is drawn to the view.
	//|
	//| ------------------------------------------------------------------------
    public void DrawRoom(
		int 		RoomX,
		int 		RoomY,  
		boolean 	isRoom,
		boolean 	ctr
	){
		//Vars to calculate pixel coordinates of room to draw.
        int PixX;
        int PixY;

		//Var to select room color.
		int roomColor;
		
		//Get pixel coordinates for room.
        PixX = ((RoomX) * RoomSize);
        PixY = ((RoomY) * RoomSize);
		
		//Default to no room color.
		roomColor = ContextCompat.getColor(myContext, R.color.noRoomColor);
		drawPaint.setColor(roomColor);
		
		//If we are drawing a room...
		if(isRoom) {
			
			//Set room color.
			roomColor = ContextCompat.getColor(myContext, R.color.roomColor);
			drawPaint.setColor(roomColor);
		}
		
		//Draw the room.
        drawCanvas.drawRect(
			PixX,
			PixY,
			(PixX + RoomSize - MAP_GRID_BORDER_SIZE),
			(PixY + RoomSize - MAP_GRID_BORDER_SIZE),
			drawPaint
		);
		
		//If this is the room in the center of the view...
		if(ctr) {
			
			//Draw character.
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setColor(Color.parseColor("Green"));
			drawCanvas.drawRect(PixX, PixY, (PixX + RoomSize - MAP_GRID_BORDER_SIZE), (PixY + RoomSize - MAP_GRID_BORDER_SIZE), drawPaint );
			drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		}
		
		//Redraw view.
        invalidate();
		
    } //} ----------------------------------------------------------------------

	//{ EraseMap ---------------------------------------------------------------
	//|
	//|	Erase the view.
	//|
	//|	Results:
	//|
	//|		Erases the view. Call this before redrawing after the map has
	//|		moved.
	//|
	//| ------------------------------------------------------------------------
    public void EraseMap() {

		//Var to select erase color.
		int eraseColor;
		
		//Get erase color.
		eraseColor = ContextCompat.getColor(myContext, R.color.mapBackgroundColor);

		//Erase the view.
        drawCanvas.drawColor(eraseColor);
		
		//Redraw.
        invalidate();
		
    } //} ----------------------------------------------------------------------

	//} ------------------------------------------------------------------------
	
}
