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
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.app.Dialog;
import android.app.DialogFragment;


public class MyActivity 
	extends 
		AppCompatActivity 
	
	implements 
		TitleDialog.NoticeDialogListener,
		OnSwipeTouchListener.SwipeFunctions
{


    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
       dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }

    private DrawingView drawView;


	//Map builder object.
    private MapBuilder MyMapBuilder;

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new TitleDialog();
        dialog.show(getFragmentManager(), "NoticeDialogFragment");
    }



	@Override
public void swipeUpFunc() {
                if(MyMapBuilder.moveHero(0, 1)) {
                        drawView.DrawMapWithOutDelay();
				}
            }
			
			@Override
        public void swipeRightFunc() {
            if(MyMapBuilder.moveHero(-1, 0)) {
            drawView.DrawMapWithOutDelay();
			}
        }
		@Override
        public void swipeLeftFunc() {
           if( MyMapBuilder.moveHero(1, 0)) {
            drawView.DrawMapWithOutDelay();
		   }
        }
		@Override
        public void swipeDownFunc() {
            if(MyMapBuilder.moveHero(0, -1)) {

            drawView.DrawMapWithOutDelay();
			}
        }
		
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {


		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

		//Create new MapBuilder.
        MyMapBuilder = new MapBuilder();

		//Get main drawing view.
        drawView = (DrawingView)findViewById(R.id.drawing);

		drawView.setMapBuilder(MyMapBuilder);
		
       // LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);

       // currPaint = (ImageButton)paintLayout.getChildAt(0);
       // currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));



		drawView.setOnTouchListener(new OnSwipeTouchListener(MyActivity.this));

        showNoticeDialog();

        }



    public void paintClicked(View view){
        //use chosen color

            //ImageButton imgView = (ImageButton)view;
            //String color = view.getTag().toString();
           // drawView.setColor(color);
           // imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            //currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            //currPaint=(ImageButton)view;

            MyMapBuilder.BuildMap();
        drawView.DrawMapWithDelay();

    }

	//Map clicked:
	public void MapClicked(View view) {



	}


}
