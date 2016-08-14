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

package com.example.chris.dungeons;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.app.DialogFragment;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;

public class DrawingView
	extends 
		View
{

	//{ Private members: =======================================================

		//Number of rooms square to draw in the map view.
		private static final int MAP_DRAW_SIZE = 9;
		
		//Width of border between rooms in pixels.
		private static final int MAP_GRID_BORDER_SIZE = 5;
		
		//Total length of time to take drawing the view in 'DrawMapWithDelay'.
		private static final int SPIRAL_DRAW_MAX_TIME = 2500;

		//Time between successive enemy updates. (How fast they move)
		private static final int ENEMY_UPDATE_TIME = 500;
		
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
	
		//Interface to define a function to draw a room.
		private interface drawRoomFunc {
			void func(final int roomX, final int roomY);
		}
	
		//A timer and task to update enemy activity.
		private Timer enemyUpdateTimer;
		private TimerTask updateEnemyTask;
	
		//Flag to indicate a map has been generated for enemy update task.
		private boolean mapflag;
	
		//Flag to only show enemy alert on the first encounter.
		private boolean alertFlag;
	
	//} ------------------------------------------------------------------------

	//{ Constructors: ==========================================================

	//{ ------------------------------------------------------------------------
	//|
	//| This constructor calls the superclass constructor and then sets up
	//| a canvas to draw our dungeon map on.
	//|
	//| ------------------------------------------------------------------------
    public DrawingView(
		final Context 	context,
		AttributeSet 	attributes
	){
		//Call superclass constructor.
        super(context, attributes);
		
		//Get context.
		myContext = context;

		//Set up canvas.
        _setupDrawing();
		mapflag = false;
		
		//Initialize alert flag.
		alertFlag = false;
		
		//Set up enemy activity update timer.
		enemyUpdateTimer = new Timer();
		updateEnemyTask = new TimerTask() {
			
			//Update task needs to redraw the view if enemies move so we
			//set it up to run on the UI thread.
			@Override
			public void run() {
				((Activity)context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						//If a map has been generated...
						if(mapflag) {
							
							//Update enemies.
							myMapBuilder.updateEnemyActivity();
				
							//Redraw map.
							DrawMapWithOutDelay();
				
							//Redraw view.
							invalidate();
						}
					}
				});
			}
		};

		//Schedule enemy task with timer.
		enemyUpdateTimer.schedule(updateEnemyTask, 0, ENEMY_UPDATE_TIME);
		
    } //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------

	//{ View class overrides: ==================================================
	
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
	//|		roomX
	//|		roomY
	//|		Map coordinates of the room to draw relative to the room in the
	//|		center of the display.
	//|
	//|	Results:
	//|
	//|		The room is drawn to the view.
	//|
	//| ------------------------------------------------------------------------
    private void _drawRoom(
		int 		roomX,
		int 		roomY
	){
		//Vars to calculate pixel coordinates of room to draw.
        int pixX;
        int pixY;

		//Var to select room color.
		int roomColor;
		
		//Get pixel coordinates for room.
        pixX = ((roomX + (MAP_DRAW_SIZE / 2)) * roomSize);
        pixY = ((roomY + (MAP_DRAW_SIZE / 2)) * roomSize);
		
		//Default to no room color.
		roomColor = ContextCompat.getColor(myContext, R.color.noRoomColor);
		drawPaint.setColor(roomColor);
		
		//If we are drawing a room...
		if(myMapBuilder.checkRoom(roomX, roomY)) {
			
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
		
		//Draw hero in the center of the view.
		if((roomX == 0) && (roomY == 0)) {
			_drawHero(pixX, pixY);
		}
		
		//If the room is the exit room, then draw the exit.
		else if(myMapBuilder.roomIsExit(roomX, roomY)) {
			_drawExit(pixX, pixY);
		}
		
		//If the room has an enemy, then draw one.
		else if(myMapBuilder.roomHasEnemy(roomX, roomY)) {
			_drawEnemy(pixX, pixY, myMapBuilder.roomEnemyIsAlive(roomX, roomY));
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
	
	} //} ----------------------------------------------------------------------
	
	//{ _drawHero --------------------------------------------------------------
	//|
	//|	Draw our hero on the map view.
	//|
	//|	Function parameters:
	//|
	//|		pixX,
	//|		pixY
	//|		X and Y pixel coordinates to draw our hero to.
	//|
	//|	Results:
	//|
	//|		The hero is drawn to the view at the given location.
	//|
	//| ------------------------------------------------------------------------
	private void _drawHero(
		int 	pixX,
		int 	pixY
	){
		//Hero bitmap.
		Bitmap myHero;
		
		//Get hero bitmap.
		myHero = BitmapFactory.decodeResource(getResources(), R.drawable.hero);

		//Scale.
		myHero = Bitmap.createScaledBitmap(myHero, roomSize, roomSize, true);
		
		//Draw to screen.
		drawCanvas.drawBitmap(myHero, pixX, pixY, drawPaint);
		
	} //} ----------------------------------------------------------------------
	
	//{ _drawExit --------------------------------------------------------------
	//|
	//|	Draw the exit on the map view.
	//|
	//|	Function parameters:
	//|
	//|		pixX,
	//|		pixY
	//|		X and Y pixel coordinates to draw the exit to.
	//|
	//|	Results:
	//|
	//|		The exit is drawn to the view at the given location.
	//|
	//| ------------------------------------------------------------------------
	private void _drawExit(
		int 	pixX,
		int 	pixY
	){
		//Color to draw the exit.
		int exitColor;
		
		//Get exit color.
		exitColor = ContextCompat.getColor(myContext, R.color.mapExitColor);
		
		//Draw exit.
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setColor(exitColor);
		drawPaint.setStrokeWidth(6);
		drawCanvas.drawRect(
			pixX + 6,
			pixY + 6,
			(pixX + roomSize - MAP_GRID_BORDER_SIZE - 6),
			(pixY + roomSize - MAP_GRID_BORDER_SIZE - 6),
			drawPaint
		);
		
		drawPaint.setStrokeWidth(2);
		drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		
	} //} ----------------------------------------------------------------------
	
	//{ _drawEnemy -------------------------------------------------------------
	//|
	//|	Draw an enemy on the map view.
	//|
	//|	Function parameters:
	//|
	//|		pixX,
	//|		pixY
	//|		X and Y pixel coordinates to draw the enemy to.
	//|
	//|		alive
	//|		Flag to indicate if enemy is alive or dead.
	//|
	//|	Results:
	//|
	//|		An enemy is drawn to the view at the given location.
	//|
	//| ------------------------------------------------------------------------
	private void _drawEnemy(
		int 	pixX, 
		int 	pixY,
		boolean alive
	){
		//Enemy bitmap.
		Bitmap myEnemy;
		
		//Get alive or dead enemy bitmap.
		if(alive) {
			myEnemy = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
		}
		else{
			myEnemy = BitmapFactory.decodeResource(getResources(), R.drawable.deadenemy);
		}
		
		//Scale the enemy.
		myEnemy = Bitmap.createScaledBitmap(myEnemy, roomSize, roomSize, true);
		
		//Draw to screen.
		drawCanvas.drawBitmap(myEnemy, pixX, pixY, drawPaint);
		
	} //} ----------------------------------------------------------------------
	
	//{ _showEnemyWarning ------------------------------------------------------
	//|
	//|	Show an enemy alert pop up.
	//|
	//| Results:
	//|
	//|		An enemy alert dialog is created and shown.
	//|
	//| ------------------------------------------------------------------------
	private void _showEnemyWarning() {
		
        //Create new alert dialog and show it.
        DialogFragment dialog = new EnemyAlertDialog();
        dialog.show(((Activity)myContext).getFragmentManager(), "");
		
    } //} ----------------------------------------------------------------------

	//{ _showWinnerNotice ------------------------------------------------------
	//|
	//|	Show the 'you win' pop up.
	//|
	//| Results:
	//|
	//|		A winner dialog is created and shown.
	//|
	//| ------------------------------------------------------------------------
	private void _showWinnerNotice() {
		
		//Reset flags for next time.
		mapflag = false;
		alertFlag = false;
		
        //Create new alert dialog and show it.
        DialogFragment dialog = new YouWinDialog();
        dialog.show(((Activity)myContext).getFragmentManager(), "");
		
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
		
		//Set to indicate we have a map we can start drawing enemies to.
		mapflag = true;
		
	} //} ----------------------------------------------------------------------

	//{ DrawMapWithOutDelay ----------------------------------------------------
	//|
	//|	Draw map without delay between rooms.
	//|
	//|	Results:
	//|
	//|		The viewable map area is drawn instantly. If there are any enemies
	//|		nearby, a pop up is shown briefly.
	//|
	//| ------------------------------------------------------------------------
	public void DrawMapWithOutDelay(){

		//To get reference to the 'Fight!' button.
		Button fightButton;

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
		
		//Get fight button.
		fightButton = (Button)((Activity)myContext).findViewById(R.id.fightbutton);
		
		//If an enemy approaches, and we have not shown the alert message yet,
		//then show pop up.
		if(myMapBuilder.enemyApproached() && !alertFlag) {
			alertFlag = true;
			_showEnemyWarning();
		}
		
		//If there are enemies around, the fight button should be visible.
		if(myMapBuilder.enemiesNearby()) {
			fightButton.setVisibility(Button.VISIBLE);
		}
		
		//Otherwise, it should be hidden.
		else {
			fightButton.setVisibility(Button.INVISIBLE);
		}

		//If the hero has reached the exit, then show pop up.
		if(myMapBuilder.heroIsAtExit()) {
			_showWinnerNotice();
		}
		
		//Redraw view.
        invalidate();
		
	} //} ----------------------------------------------------------------------
	
	//{ setMapBuilder ----------------------------------------------------------
	//|
	//|	Sets a reference to the map builder object.
	//|
	//|	Function parameters:
	//|
	//|		theMapBuilder
	//|		Map builder to set the reference to.
	//|
	//|	Results:
	//|
	//|		The map builder reference is set so we can call map builder
	//|		methods from this class.
	//|
	//| ------------------------------------------------------------------------
	public void setMapBuilder(
	
		MapBuilder theMapBuilder
	){
		//Set the map builder reference.
		myMapBuilder = theMapBuilder;
		
	} //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
} //----------------------------------------------------------------------------

