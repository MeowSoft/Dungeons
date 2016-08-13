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
import android.os.Handler;

public class DrawingView extends View  {

	//{ Private members: =======================================================

		//Number of rooms square to draw in the map view.
		private static final int MAP_DRAW_SIZE = 9;
		
		//Width of border between rooms in pixels.
		private static final int MAP_GRID_BORDER_SIZE = 5;
		
		//Total length of time to take drawing the view in 'DrawMapWithDelay'.
		private static final int SPIRAL_DRAW_MAX_TIME = 2500;

		//Paint for drawing stuff.
		private Paint drawPaint;

		//Canvas and bitmap to draw to.
		private Canvas drawCanvas;
		private Bitmap canvasBitmap;

		//Var to get width and height of a room in pixels.
		private int roomSize;
		
		//Parent context.
		private Context myContext;
		
		//Reference to the map builder object.
		private MapBuilder myMapBuilder;
		
		//Var to delay between drawing rooms in 'DrawMapWithDelay'.
		private int drawDelay;
	
		//Interfacce to define a function to draw a room.
		private interface drawRoomFunc {
			void func(final int roomX, final int roomY);
		}
	
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
        _setupDrawing();
		
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
		roomSize = (drawCanvas.getWidth() / MAP_DRAW_SIZE);
		
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
	
	//{ _setupDrawing ----------------------------------------------------------
	//|
	//|	Set up the drawing area.
	//|
	//|	Results:
	//|
	//|		A new Paint object is created and its attributes are set.
	//|
	//| ------------------------------------------------------------------------
    private void _setupDrawing(){

        //Create paint object.
        drawPaint = new Paint();

		//Set drawing style params.
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(2);
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPaint.setStrokeJoin(Paint.Join.MITER);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);

    } //} ----------------------------------------------------------------------
	
	//{ _eraseMap --------------------------------------------------------------
	//|
	//|	Erase the view.
	//|
	//|	Results:
	//|
	//|		Erases the currently drawn map view.
	//|
	//| ------------------------------------------------------------------------
    private void _eraseMap(){

		//Var to select erase color.
		int eraseColor;
		
		//Get erase color.
		eraseColor = ContextCompat.getColor(myContext, R.color.mapBackgroundColor);

		//Erase the view.
        drawCanvas.drawColor(eraseColor);
		
		//Redraw.
        invalidate();
		
    } //} ----------------------------------------------------------------------

	//{ _drawRoom --------------------------------------------------------------
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
    private void _drawRoom(
		int 		RoomX,
		int 		RoomY
	){
		//Vars to calculate pixel coordinates of room to draw.
        int pixX;
        int pixY;

		//Var to select room color.
		int roomColor;
		
		//Get pixel coordinates for room.
        pixX = ((RoomX + (MAP_DRAW_SIZE / 2)) * roomSize);
        pixY = ((RoomY + (MAP_DRAW_SIZE / 2)) * roomSize);
		
		//Default to no room color.
		roomColor = ContextCompat.getColor(myContext, R.color.noRoomColor);
		drawPaint.setColor(roomColor);
		
		//If we are drawing a room...
		if(myMapBuilder._CheckRoom(RoomX, RoomY)) {
			
			//Set room color.
			roomColor = ContextCompat.getColor(myContext, R.color.roomColor);
			drawPaint.setColor(roomColor);
		}
		
		//Draw the room.
        drawCanvas.drawRect(
			pixX,
			pixY,
			(pixX + roomSize - MAP_GRID_BORDER_SIZE),
			(pixY + roomSize - MAP_GRID_BORDER_SIZE),
			drawPaint
		);
		
		//If this is the room in the center of the view...
		if((RoomX == 0) && (RoomY == 0)) {
			
			//Draw character.
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setColor(Color.parseColor("Green"));
			drawCanvas.drawRect(
				pixX,
				pixY,
				(pixX + roomSize - MAP_GRID_BORDER_SIZE),
				(pixY + roomSize - MAP_GRID_BORDER_SIZE),
				drawPaint
			);

			drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		}
		
		if(myMapBuilder._RoomHasEnemy(RoomX, RoomY)) {
			drawPaint.setColor(Color.parseColor("Blue"));
			drawCanvas.drawCircle((pixX + (roomSize / 2)), (pixY + (roomSize / 2)), ((roomSize / 2) - 10), drawPaint);
		}
		
    } //} ----------------------------------------------------------------------

	//{ _drawMap ---------------------------------------------------------------
	//|
	//|	Draw the viewable area of the map to the screen.
	//|
	//|	Function parameters:
	//|
	//|		drawFunc
	//|		A drawRoomFunc interface instance to draw a map room.
	//|
	//|	Results:
	//|
	//|		This function will draw a section of the dungeon map to the view.
	//|		The map will be drawn from the center outward in a spiral pattern.
	//|		
	//| ------------------------------------------------------------------------
	private void _drawMap(
		drawRoomFunc drawFunc
	){
		//Loop counters.
		int x;
		int y;
		
		//Vars to get map rooms.
		int mapX;
		int mapY;
		
		//Var to increment map room coordinates.
		int DirFlip;

		//Calc var.
		int temp;
		
		//Start moving in the +x +y direction.
		DirFlip = 1;

		//Init local map coordinates.
		mapX = 0;
		mapY = 0;
	
		//Erase current map view.
		_eraseMap();
	
		//Draw center room.
		drawFunc.func(mapX, mapY);
	
		//For each loop spiraling out from the view center...
        for(x = 0; x < MAP_DRAW_SIZE; x++) {

			//Draw horizontal rooms.
            for (y = 0; y < x; y++){
                mapX += DirFlip;
                drawFunc.func(mapX, mapY);
            }

			//This is so we don't draw an extra room on the last leg of
			//the spiral.
			temp = (x == (MAP_DRAW_SIZE - 1)) ? (x - 1) : x;
			
			//Draw vertical rooms.
            for(y = 0; y <= temp; y++){
                mapY += DirFlip;
                drawFunc.func(mapX, mapY);
            }
			
			//Reverse direction.
            DirFlip *= -1;
        }
		
		//Redraw view.
        invalidate();
		
	} //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
	//{ Public methods: ========================================================
	
	//{ DrawMapWithDelay -------------------------------------------------------
	//|
	//| Draw map with a delay between rooms.
	//|
	//|	Results:
	//|
	//|		The map is drawn from the center outwards in a spiral with a 
	//|		delay between each room so we can see the spiral effect.
	//|
	//| ------------------------------------------------------------------------
	public void DrawMapWithDelay(){
		
		//Set up an instance of drawRoomFunc that draws a room after a short
		//delay.
		drawRoomFunc myDrawFunc = new drawRoomFunc() {
			public void func(
				final int roomX, 
				final int roomY
			){
				//Delay handler.
				final Handler handler = new Handler();

				//Var to calculate delay time.
				int DelCalc;
				
				//Create a runnable to draw a map room.
				Runnable delayDraw = new Runnable() {
					@Override
					public void run() {
						
						//Draw room.
						_drawRoom(roomX, roomY);
						
						//Redraw view.
						invalidate();
					}
				};
				
				//Calculate remaining delay for this room.
				DelCalc = (SPIRAL_DRAW_MAX_TIME - (drawDelay * 10));
				DelCalc /= 100;

				//Send room draw to handler.
				handler.postDelayed(delayDraw, drawDelay);

				//Update delay.
				drawDelay = (drawDelay + DelCalc);
			}
		};
		
		//Init delay.
		drawDelay = 20;
		
		//Call _drawMap with the delayed function.
		_drawMap(myDrawFunc);
		
	} //} ----------------------------------------------------------------------

	//{ DrawMapWithOutDelay ----------------------------------------------------
	//|
	//|	Draw map without delay between rooms.
	//|
	//|	Results:
	//|
	//|		The viewable map area is drawn instantly.
	//|
	//| ------------------------------------------------------------------------
	public void DrawMapWithOutDelay(){
		
		//Set up an instance of drawRoomFunc that just draws a room.
		drawRoomFunc myDrawFunc = new drawRoomFunc() {
			public void func(
				final int roomX,
				final int roomY
			){
                _drawRoom(roomX, roomY);
			} 
		};
		
		//Call _drawMap with the simple function.
		_drawMap(myDrawFunc);
		
		//Redraw view.
        invalidate();
		
	} //} ----------------------------------------------------------------------
	
	
	
	
	public void setMapBuilder(
	
		MapBuilder theMapBuilder
	){
		myMapBuilder = theMapBuilder;
	}

	//} ------------------------------------------------------------------------
	
}
