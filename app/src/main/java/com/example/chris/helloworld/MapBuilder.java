package com.example.chris.helloworld;

import android.os.Handler;

import java.util.Random;

/**
 * Created by Chris on 7/31/2016.
 */




public class MapBuilder {
	

	
	
    private static final int MAP_X_SIZE = 31;
    private static final int MAP_Y_SIZE = 31;
	
    private static final int MAP_X_DRAW_SIZE = 10;
    private static final int MAP_Y_DRAW_SIZE = 10;


    private int BuildX;
    private int BuildY;


	//An array that will contain the map room objects.
    private MapRoom[][] Map;
    private DrawingView MyView;

    public MapBuilder(DrawingView NewView) {

       Map = new MapRoom[MAP_X_SIZE][MAP_Y_SIZE];
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

		MapRoom NewRoom;
		
		Random Rgen = new Random();
		
        int y;
        int DirFlip;
        CurrX = MAP_X_SIZE / 2;
        CurrY = MAP_Y_SIZE / 2;

DelCount = 20;
 
		Map[CurrX][CurrY] = new MapRoom(true);

        DirFlip = 1;

		int MapDim = 30;
		
		boolean setroom;
		
        for(x = 0; x < MapDim; x++) {

            for (y = 0; y <=  x; y++){
			
				
                CurrX += DirFlip;
				
				setroom = _GenerateRoom(CurrX, CurrY, Rgen);
				Map[CurrX][CurrY] = new MapRoom(setroom);
				
				
            }

	
            for(y = 0; y <= x; y++){
				
				
                CurrY += DirFlip;
				
				setroom = _GenerateRoom(CurrX, CurrY, Rgen);
				
				Map[CurrX][CurrY] = new MapRoom(setroom);
				
            }

            DirFlip *= -1;
			
        }
		
		  for (y = 0; y < x; y++){
			
				
                CurrX += DirFlip;
				
				setroom = _GenerateRoom(CurrX, CurrY, Rgen);
			
				Map[CurrX][CurrY] = new MapRoom(setroom);
				
            }
		


    }


    private boolean _GenerateRoom(int MapX, int MapY, Random RGen) {

  
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
		
		boolean RetVal;
		
		int Mult;
		
		MapRoom MyMaproom;
		
		MyMaproom = (MapY > 0)? Map[MapX][(MapY - 1)] : null;
		
        Top = (MyMaproom != null)? MyMaproom.CheckIsRoom() : false;
		
		MyMaproom = (MapX > 0)? Map[(MapX - 1)][MapY] : null;
        Left = (MyMaproom != null)? MyMaproom.CheckIsRoom() : false;
		
		MyMaproom = (MapY < (MAP_Y_SIZE - 1))? Map[MapX][(MapY + 1)] : null;
        Bottom = (MyMaproom != null)? MyMaproom.CheckIsRoom() : false;
		
		
		MyMaproom = (MapX < (MAP_X_SIZE - 1))? Map[(MapX + 1)][MapY] : null;
        Right = (MyMaproom != null)? MyMaproom.CheckIsRoom() : false;

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
		
		RetVal = false;
		
		if(Mult > RGen.nextInt(100)) {
					RetVal = true;
				}
		
	return(RetVal);
	

	}
			
		
		public boolean ShiftMap(int MoveX, int MoveY) {
			
			boolean result = false;
			
			if((MoveX > 0) && (BuildX < (MAP_X_SIZE - 1))) {

				if(_CheckRoom((BuildX + 1), BuildY)) {
					BuildX++;
					result = true;
				}
			}
			
			else if((MoveX < 0) && (BuildX > 0)) {

				if(_CheckRoom((BuildX - 1), BuildY)) {
					BuildX--;
					result = true;
				}
			}
			
			else if((MoveY > 0) && (BuildY < (MAP_Y_SIZE - 1))) {

				if(_CheckRoom(BuildX, (BuildY + 1))) {
					BuildY++;
					result = true;
				}
			}
			
			else if((MoveY < 0) && (BuildY > 0)) {

				if(_CheckRoom(BuildX, (BuildY - 1))) {
					BuildY--;
					result = true;
				}
			}
			
			
			return(result);
		}
		
	private int MyDelay;
	
	private void _DrawMap( DrawRoomFunc DF) {
	
		int x;
		int y;
		
		int MyX;
		int MyY;
		int DirFlip;
		int DelCount = 20;
		

		
		int myEdgeDim;
		DirFlip = 1;

	MyDelay = 20;
	MyX = BuildX;
	MyY = BuildY;
	
	int viewX;
	int viewY;
	boolean roomcheck;

	int myMapDim = MyView.getMapDim();
	//if((myMapDim % 2) != 0) { myMapDim++; }
	myMapDim--;
	int offset = myMapDim / 2;
		
	
	viewX = offset;
	viewY = offset;
	myEdgeDim = viewX;
	int temp;
	MyView.EraseMap();
	
		//Draw center room.
		DF.func(viewX, viewY, Map[MyX][MyY].CheckIsRoom(), DelCount, true);
	
        for(x = 0; x <= myMapDim; x++) {

            for (y = 0; y <  x; y++){
			
				
                MyX += DirFlip;
				viewX+= DirFlip;
				
                DF.func(viewX, viewY, _CheckRoom(MyX, MyY), DelCount, false);
            }

	temp = (x == myMapDim) ? (x - 1) : x;
            for(y = 0; y <= temp; y++){
				
				
                MyY += DirFlip;
				viewY += DirFlip;
				
				
                DF.func(viewX, viewY, _CheckRoom(MyX, MyY), DelCount, false);
            }
			
            DirFlip *= -1;
			
        }
		
	

	
	}
	
	private boolean _CheckRoom(int RoomX, int RoomY) {
		
		boolean result;
		
		result = false;
		if((RoomX < MAP_X_SIZE) && (RoomY < MAP_Y_SIZE) && (RoomX >= 0) && (RoomY >= 0)) result = Map[RoomX][RoomY].CheckIsRoom();
		return(result);
	}
		
	
	interface DrawRoomFunc {
		int func(final int RoomX, final int RoomY, final boolean room, int Delay, boolean ctr);
	}
	
	public void DrawMapWithDelay() {
		_DrawMap(new DrawRoomFunc() {
		
    public int func(final int RoomX, final int RoomY, final boolean room, int Delay, final boolean ctr) {

        final Handler handler = new Handler();

		int DelCalc;
		
		DelCalc = 2500 - (MyDelay * 10);
		DelCalc /= 100;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                MyView.DrawRoom(RoomX, RoomY, MAP_X_DRAW_SIZE, MAP_Y_DRAW_SIZE, room, ctr);
            }
        }, MyDelay);

		MyDelay = MyDelay + DelCalc;
		
        return(Delay + DelCalc);

		} } );
	}

	public void DrawMapWithOutDelay() {
		_DrawMap(new DrawRoomFunc() {
		
    public int func(final int RoomX, final int RoomY, final boolean room, int Delay, boolean ctr) {

                // Do something after 5s = 5000ms
                MyView.DrawRoom(RoomX, RoomY, MAP_X_DRAW_SIZE, MAP_Y_DRAW_SIZE, room, ctr);
        

        return(0);

		} } );
	}
	
}


class MapRoom {
	
	boolean IsRoom;
	

	
	MapRoom(boolean SetIsRoom) {
		
		IsRoom = SetIsRoom;
	}
	
	public boolean CheckIsRoom() {
		return(IsRoom);
	}
	
}

	
	