//| ============================================================================
//|
//|	File:			OnSwipeTouchListener.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		OnSwipeTouchListener is a class to detect an up, down, left, or right
//|		swipe on the screen and call a corresponding function in response.
//|
//| ----------------------------------------------------------------------------

package com.example.chris.dungeons;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.app.DialogFragment;

public class OnSwipeTouchListener 
	implements 
		OnTouchListener 
{

	//{ Interface: =============================================================
	
	//An interface to define functions to execute on a swipe.
	public interface SwipeFunctions {
        void swipeRightFunc();
		void swipeLeftFunc();
		void swipeUpFunc();
		void swipeDownFunc() ;
    }

	//} ------------------------------------------------------------------------
	
	//{ Private members: =======================================================

		//Swipe functions.
		private SwipeFunctions mySwipeFuncs;
		
		//GestureDetector object to detect swipes.
		private final GestureDetector gestureDetector;

		//Reference to parent context.
		private Context myContext;
		
	//} ------------------------------------------------------------------------
	
	//{ Constructors: ==========================================================
	
	//{ ------------------------------------------------------------------------
	//|
	//|	The gestureDetector is created using our SwipeGestureListener class.
	//|
	//| ------------------------------------------------------------------------
    public OnSwipeTouchListener(
		Context ctx
	){
		//Set our gestureDetector to a new GestureDetector created with our
		//SwipeGestureListener class.
        gestureDetector = new GestureDetector(ctx, new SwipeGestureListener());
		
		//Set swipe functions interface.
		mySwipeFuncs = (SwipeFunctions)ctx;
		
		//Set context reference.
		myContext = ctx;
		
    } //} ----------------------------------------------------------------------

	//} ------------------------------------------------------------------------
	
	//{ OnTouchListener class overrides: ======================================= 
	
	//{ onTouch ----------------------------------------------------------------
	//|
	//|	Override for OnTouchListener::onTouch method.
	//|
	//|	A touch event on the given view will be handled by the gestureDetector
	//| and the result returned.
	//|
	//| ------------------------------------------------------------------------
    @Override
    public boolean onTouch(
		View 			myView,
		MotionEvent 	event
	){
		//Let gesture detector handle touch.
        return(gestureDetector.onTouchEvent(event));
		
    } //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
	//{ SwipeGestureListener class =============================================
	//|
	//| Extension of SimpleOnGestureListener class to detect swipe events.
	//|	This class overrides the 'onDown' method to return true and the
	//| 'onFling' method to detect the swipe direction and call the appropriate
	//| method.
	//|
	//| ------------------------------------------------------------------------
    private final class SwipeGestureListener extends SimpleOnGestureListener {

		//Coordinate delta and velocity thresholds for swipe events.
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		//{ onDown -------------------------------------------------------------
		//|
		//|	Override for SimpleOnGestureListener::onDown method.
		//|
		//| Results:
		//|
		//|		This method just returns true. The class then listens for the
		//|		next touch event.
		//|
		//| --------------------------------------------------------------------
        @Override
        public boolean onDown(
			MotionEvent event
		){
            return(true);
			
        } //} ------------------------------------------------------------------
	
		//{ onLongPress --------------------------------------------------------
		//|
		//|	Override for SimpleOnGestureListener::onLongPress method.
		//|
		//| Results:
		//|
		//|		On a long press, we will display a dialog to confirm if we
		//|		want to generate a new map.
		//|
		//| --------------------------------------------------------------------
		@Override
		public void onLongPress(MotionEvent event) {
			
			//Create new dialog and show it.
			DialogFragment dialog = new NewMapDialog();
			dialog.show(((Activity)myContext).getFragmentManager(), "");
			
		} //} ------------------------------------------------------------------
	
		//{ onFling ------------------------------------------------------------ 
		//|
		//|	Override for SimpleOnGestureListener::onFling method.
		//|
		//| Results:
		//|
		//|		Calculates the x and y deltas between touch down and touch
		//|		up events. If a delta and velocity are greater than the
		//|		thresholds defined, then the appropriate method is called
		//|		to handle the swipe direction event.
		//|
		//| --------------------------------------------------------------------
        @Override
        public boolean onFling(
			MotionEvent 	event1,
			MotionEvent 	event2,
			float 			velocityX,
			float 			velocityY
		){
			//The result of this function.
            boolean result;
		
			//Init return.
			result = false;
			
            try {
				
				//Get x and y delta between down and up events.
                int dY = ((int) (event2.getY() - event1.getY()));
                int dX = ((int) (event2.getX() - event1.getX()));

				//If dx  > dy...
                if(Math.abs(dX) > Math.abs(dY)) {
					
					//If dx > threshold and vx > threshold...
                    if(Math.abs(dX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						
						//Call function for right or left swipe.
                        if (dX > 0) {
                            mySwipeFuncs.swipeRightFunc();
                        } else {
                            mySwipeFuncs.swipeLeftFunc();
                        }
                    }
					
					//Return true.
                    result = true;
                }
				
				//If dy >= dx, and dy > threshold, and vy > threshold...
                else if (Math.abs(dY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
					
					//Call function for up or down swipe.
                    if (dY > 0) {
                        mySwipeFuncs.swipeDownFunc();
                    } else {
                        mySwipeFuncs.swipeUpFunc();
                    }
                }
				
				//Return true.
                result = true;

            }
			
			catch (Exception exception) {
                exception.printStackTrace();
            }
			
			//Return the result.
            return(result);
			
        } //} ------------------------------------------------------------------
		
    } //} ----------------------------------------------------------------------

} //----------------------------------------------------------------------------

