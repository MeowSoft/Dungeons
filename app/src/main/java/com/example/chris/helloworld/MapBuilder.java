package com.example.chris.helloworld;

import android.os.Handler;

import java.util.Random;

/**
 * Created by Chris on 7/31/2016.
 */




public class MapBuilder {
    private static final int MAP_X_SIZE = 30;
    private static final int MAP_Y_SIZE = 30;


    private int BuildX;
    private int BuildY;

    private boolean[][] Map;
    private DrawingView MyView;

    public MapBuilder(DrawingView NewView) {

        Map = new boolean[MAP_X_SIZE][MAP_Y_SIZE];
        MyView = NewView;

        BuildX = MAP_X_SIZE / 2;
        BuildY = MAP_Y_SIZE / 2;

    }

    public void BuildMap() {

        int CurrX;
        int CurrY;

        int DelCount;
        int x;
		int Check;

		
		Random Rgen = new Random();
		
        int y;
        int DirFlip;
        CurrX = MAP_X_SIZE / 2;
        CurrY = MAP_X_SIZE / 2;

DelCount = 20;
        Map[CurrX][CurrY] = true;
DelCount = myDrawRoom(CurrX, CurrY, true, DelCount);

        DirFlip = 1;

		int MapDim = 27;
		
        for(x = 1; x < (MapDim +1); x++) {

            for (y = 0; y < ((x >= MapDim)? (x -1) : x); y++){
			
				
                CurrX += DirFlip;
				
				_GenerateRoom(CurrX, CurrY, Rgen);
			
				
                DelCount = myDrawRoom(CurrX, CurrY, Map[CurrX][CurrY], DelCount);
            }

			if(x < MapDim) {
            for(y = 0; y < x; y++){
				
				
                CurrY += DirFlip;
				
				_GenerateRoom(CurrX, CurrY, Rgen);
			
				
                DelCount = myDrawRoom(CurrX, CurrY, Map[CurrX][CurrY], DelCount);
            }

            DirFlip *= -1;
			}
        }

    }


    private void _GenerateRoom(int MapX, int MapY, Random RGen) {

  
		int Count;
		boolean Temp;
		int BaseChance;
		int x;
		int Diff;
		
		boolean Top;
		boolean Left;
		boolean Right;
		boolean Bottom;
		boolean Vert;
		boolean Horiz;
		boolean All;
		
		int Mult;
		
        Top = (MapX > 0)? Map[MapX][(MapY - 1)] : false;
        Left = (MapX > 0)? Map[(MapX - 1)][MapY] : false;
        Bottom = (MapY < MAP_Y_SIZE)? Map[MapX][(MapY + 1)] : false;
        Right = (MapX < MAP_X_SIZE)? Map[(MapX + 1)][MapY] : false;

		Horiz = (Left && Right);
		Vert = (Top && Bottom);
		All = (Horiz && Vert);
		
		Mult =	All ? 95 : 
				Horiz ? 80 :
				Vert ? 80 :
				(Top && Left) ? 70 :
				(Top && Right) ? 70 :
				(Bottom && Left) ? 70 :
				(Bottom && Right) ? 70 :
				Top ? 65 :
				Left ? 65 :
				Bottom ? 65 : 
				Right ? 65 : 0;
		
		Map[MapX][MapY] = false;
		
		if(Mult > RGen.nextInt(100)) {
					Map[MapX][MapY] = true;
				}
		
	
	

	}
			
		
	
	
    private int myDrawRoom(final int RoomX, final int RoomY, final boolean room, int Delay) {

        final Handler handler = new Handler();

		int DelCalc;
		
		DelCalc = 2500 - (Delay * 10);
		DelCalc /= 100;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                MyView.DrawRoom(RoomX, RoomY, room);
            }
        }, Delay);

        return(Delay + DelCalc);

    }


}
