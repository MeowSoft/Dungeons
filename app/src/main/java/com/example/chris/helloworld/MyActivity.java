//| ============================================================================
//|
//|	File:			MyActivity.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		This is the main activity for the game.
//|
//| ----------------------------------------------------------------------------

package com.example.chris.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.DialogFragment;

public class MyActivity 
	extends 
		AppCompatActivity 
	
	implements 
		OnSwipeTouchListener.SwipeFunctions,
		YouWinDialog.NoticeDialogListener,
		NewMapDialog.NoticeDialogListener
{
	
	//{ Private members: =======================================================
	
		private static final int DUNGEON_SIZE = 31;
	
		//Drawing view object.
		private DrawingView drawView;

		//Map builder object.
		private MapBuilder myMapBuilder;

	//} ------------------------------------------------------------------------

	//{ AppCompatActivity class overrides: =====================================
	
	//{ onCreate ---------------------------------------------------------------
	//|
	//|	Override for AppCompatActivity::onCreate method.
	//|
	//| Results:
	//|
	//|		Creates a new bitmap and canvas and sets the width of the dungeon
	//|		rooms based on the width of the canvas.
	//|
	//| ------------------------------------------------------------------------
    @Override
    protected void onCreate(
		Bundle savedInstanceState
	){
		//Call superclass onCreate and set the view.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

		//Create new MapBuilder.
        myMapBuilder = new MapBuilder(DUNGEON_SIZE);

		//Get main drawing view.
        drawView = (DrawingView)findViewById(R.id.drawing);

		//Set map builder reference in drawing view.
		drawView.setMapBuilder(myMapBuilder);
		
		//Create a swipe touch listener to look for swipes on the map view.
		drawView.setOnTouchListener(new OnSwipeTouchListener(MyActivity.this));

		//Show intro dialog.
        DialogFragment dialog = new IntroDialog();
        dialog.show(MyActivity.this.getFragmentManager(), "");
		
	} //} ----------------------------------------------------------------------

	//} ------------------------------------------------------------------------

	//{ OnSwipeTouchListener functions: ========================================

	//{ swipeUpFunc ------------------------------------------------------------
	//|
	//|	Function to execute on swipe up.
	//|
	//| Results:
	//|
	//|		We try and move the hero one space down in the map. If the hero
	//|		moves, we redraw the map view.
	//|
	//| ------------------------------------------------------------------------
	@Override
	public void swipeUpFunc() {
		
		//Try to move hero. If they move, redraw the view.
		if(myMapBuilder.moveHero(0, 1)) {
			drawView.DrawMapWithOutDelay();
		}
		
	} //} ----------------------------------------------------------------------

	//{ swipeRightFunc ---------------------------------------------------------
	//|
	//|	Function to execute on swipe right.
	//|
	//| Results:
	//|
	//|		We try and move the hero one space left in the map. If the hero
	//|		moves, we redraw the map view.
	//|
	//| ------------------------------------------------------------------------
	@Override
	public void swipeRightFunc() {
		
		//Try to move hero. If they move, redraw the view.
		if(myMapBuilder.moveHero(-1, 0)) {
			drawView.DrawMapWithOutDelay();
		}
		
	} //} ----------------------------------------------------------------------

	//{ swipeLeftFunc ----------------------------------------------------------
	//|
	//|	Function to execute on swipe left.
	//|
	//| Results:
	//|
	//|		We try and move the hero one space right in the map. If the hero
	//|		moves, we redraw the map view.
	//|
	//| ------------------------------------------------------------------------
	@Override
	public void swipeLeftFunc() {
		
		//Try to move hero. If they move, redraw the view.
		if( myMapBuilder.moveHero(1, 0)) {
			drawView.DrawMapWithOutDelay();
		}
	   
	} //} ----------------------------------------------------------------------

	//{ swipeDownFunc ----------------------------------------------------------
	//|
	//|	Function to execute on swipe down.
	//|
	//| Results:
	//|
	//|		We try and move the hero one space up in the map. If the hero
	//|		moves, we redraw the map view.
	//|
	//| ------------------------------------------------------------------------
	@Override
	public void swipeDownFunc() {
		
		//Try to move hero. If they move, redraw the view.
		if(myMapBuilder.moveHero(0, -1)) {
			drawView.DrawMapWithOutDelay();
		}
		
	} //} ----------------------------------------------------------------------

	//} ------------------------------------------------------------------------

	//{ YouWinDialog functions: ================================================

	//{ onDialogPositiveClick --------------------------------------------------
	//|
	//|	Build a new map and redraw when the button is clicked.
	//|
	//| ------------------------------------------------------------------------
	@Override
    public void onDialogPositiveClick(
		DialogFragment dialog
	){
	   //Build a new map.
		myMapBuilder.buildMap();
		
		//Draw map with spiral delay effect.
        drawView.DrawMapWithDelay();
		
		// User touched the dialog's positive button
       dialog.dismiss();
	   
    } //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
	//{ Public methods: ========================================================
	
	//{ generateClicked --------------------------------------------------------
	//|
	//|	Function to execute when 'Generate dungeon' is clicked.
	//|
	//| Function parameters:
	//|
	//|		view
	//|		The parent view.
	//|
	//| Results:
	//|
	//|		A dialog is shown to confirm we want to generate a new map.
	//|
	//| ------------------------------------------------------------------------
    public void generateClicked(
		View view
	){
        //Show dialog to confirm new map.
        DialogFragment dialog = new NewMapDialog();
        dialog.show(MyActivity.this.getFragmentManager(), "");
		
    } //} ----------------------------------------------------------------------

	//{ fightClicked -----------------------------------------------------------
	//|
	//|	Function to execute when 'Fight!' is clicked.
	//|
	//| Function parameters:
	//|
	//|		view
	//|		The parent view.
	//|
	//| Results:
	//|
	//|		The 'fightEnemies' method is called.
	//|
	//| ------------------------------------------------------------------------
	public void fightClicked(
		View view
	){

		//Fight all nearby enemies.
		myMapBuilder.fightEnemies();
		
		//Redraw to show killed enemies.
		drawView.DrawMapWithOutDelay();

	} //} ----------------------------------------------------------------------

	//{ rightClicked -----------------------------------------------------------
	//|
	//|	Function to execute when arrow button is clicked.
	//|
	//| ------------------------------------------------------------------------
	public void rightClicked(
		View view
	){
		swipeLeftFunc();
		
	} //} ----------------------------------------------------------------------
	
	//{ leftClicked ------------------------------------------------------------
	//|
	//|	Function to execute when arrow button is clicked.
	//|
	//| ------------------------------------------------------------------------
	public void leftClicked(
		View view
	){
		swipeRightFunc();
		
	} //} ----------------------------------------------------------------------
	
	//{ downClicked ------------------------------------------------------------
	//|
	//|	Function to execute when arrow button is clicked.
	//|
	//| ------------------------------------------------------------------------
	public void downClicked(
		View view
	){
		swipeUpFunc();
		
	} //} ----------------------------------------------------------------------
	
	//{ upClicked --------------------------------------------------------------
	//|
	//|	Function to execute when arrow button is clicked.
	//|
	//| ------------------------------------------------------------------------
	public void upClicked(
		View view
	){
		swipeDownFunc();
		
	} //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------

} //----------------------------------------------------------------------------


