package com.example.chris.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

//Main activity:
public class MyActivity extends AppCompatActivity {


    private DrawingView drawView;
    private ImageButton currPaint;

    private MapBuilder MyMapBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        drawView = (DrawingView)findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);

        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        MyMapBuilder = new MapBuilder(drawView);
		drawView.setOnTouchListener(new OnSwipeTouchListener(MyActivity.this) {
            public void onSwipeTop() {
                MyMapBuilder.ShiftMap(0, 1);
                        MyMapBuilder.DrawMapWithOutDelay(10);
            }
        public void onSwipeRight() {
            MyMapBuilder.ShiftMap(-1, 0);
            MyMapBuilder.DrawMapWithOutDelay(10);
        }
        public void onSwipeLeft() {
            MyMapBuilder.ShiftMap(1, 0);
            MyMapBuilder.DrawMapWithOutDelay(10);
        }
        public void onSwipeBottom() {
            MyMapBuilder.ShiftMap(0, -1);
            MyMapBuilder.DrawMapWithOutDelay(10);
			
        }

        });
        }
		


    public void paintClicked(View view){
        //use chosen color

            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;

            MyMapBuilder.BuildMap();
        MyMapBuilder.DrawMapWithDelay(10);

    }

	//Map clicked:
	public void MapClicked(View view) {
	

	
	}
	

}
