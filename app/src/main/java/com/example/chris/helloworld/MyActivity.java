//| ============================================================================
//|
//|	File:			MyActivity.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		
//|
//| ----------------------------------------------------------------------------

package com.example.chris.helloworld;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MyActivity extends AppCompatActivity {

	
    private DrawingView drawView;


	//Map builder object.
    private MapBuilder MyMapBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

		//Get main drawing view.
        drawView = (DrawingView)findViewById(R.id.drawing);

       // LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);

       // currPaint = (ImageButton)paintLayout.getChildAt(0);
       // currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

		
		//Create new MapBuilder.
        MyMapBuilder = new MapBuilder(drawView);
		
		
		drawView.setOnTouchListener(new OnSwipeTouchListener(MyActivity.this) {
            public void swipeUpFunc() {
                if(MyMapBuilder.ShiftMap(0, 1)) {
                        MyMapBuilder.DrawMapWithOutDelay();
				}
            }
        public void swipeRightFunc() {
            if(MyMapBuilder.ShiftMap(-1, 0)) {
            MyMapBuilder.DrawMapWithOutDelay();
			}
        }
        public void swipeLeftFunc() {
           if( MyMapBuilder.ShiftMap(1, 0)) {
            MyMapBuilder.DrawMapWithOutDelay();
		   }
        }
        public void swipeDownFunc() {
            if(MyMapBuilder.ShiftMap(0, -1)) {
				
            MyMapBuilder.DrawMapWithOutDelay();
			}
        }

        });
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
        MyMapBuilder.DrawMapWithDelay();

    }

	//Map clicked:
	public void MapClicked(View view) {
	

	
	}
	

}
