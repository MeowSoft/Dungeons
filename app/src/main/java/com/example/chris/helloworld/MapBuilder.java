package com.example.chris.helloworld;

import android.os.Handler;

import java.util.Random;

/**
 * Created by Chris on 7/31/2016.
 */




public class MapBuilder {
	

	
	
    private static final int MAP_X_SIZE = 31;
    private static final int MAP_Y_SIZE = 31;


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
			
		
		public void ShiftMap(int MoveX, int MoveY) {
			
			if((MoveX > 0) && (BuildX < (MAP_X_SIZE - 1))) BuildX++;
			else if((MoveX < 0) && (BuildX > 0)) BuildX--;
			else if((MoveY > 0) && (BuildY < (MAP_Y_SIZE - 1))) BuildY++;
			else if((MoveY < 0) && (BuildY > 0)) BuildY--;
		}
		
	private void _DrawMap(int EdgeDim, DrawRoomFunc DF) {
	
		int x;
		int y;
		
		int MyX;
		int MyY;
		int DirFlip;
		int DelCount;
		

		
		int myEdgeDim;
		DirFlip = 1;
		myEdgeDim = EdgeDim;
		if((myEdgeDim % 2) > 0) myEdgeDim++;
		
	DelCount = 20;
	MyX = BuildX;
	MyY = BuildY;
	
	int AbsX;
	int AbsY;
	boolean roomcheck;
	AbsX = myEdgeDim / 2;
	AbsY = myEdgeDim / 2;

	MyView.EraseMap();
DelCount = DF.func(AbsX, AbsY, Map[MyX][MyY].CheckIsRoom(), DelCount);
	
        for(x = 0; x < myEdgeDim; x++) {

            for (y = 0; y <=  x; y++){
			
				
                MyX += DirFlip;
				AbsX += DirFlip;
				
				if((MyX < MAP_X_SIZE) && (MyY < MAP_Y_SIZE) && (MyX >= 0) && (MyY >= 0)) roomcheck = Map[MyX][MyY].CheckIsRoom();
				else roomcheck = false;
				
                DelCount = DF.func(AbsX, AbsY, roomcheck, DelCount);
            }

	
            for(y = 0; y <= x; y++){
				
				
                MyY += DirFlip;
				AbsY += DirFlip;
				
				if((MyX < MAP_X_SIZE) && (MyY < MAP_Y_SIZE) && (MyX >= 0) && (MyY >= 0)) roomcheck = Map[MyX][MyY].CheckIsRoom();
				else roomcheck = false;
				
                DelCount = DF.func(AbsX, AbsY, roomcheck, DelCount);
            }

            DirFlip *= -1;
			
        }
		
		  for (y = 0; y < x; y++){
			
				
                MyX += DirFlip;
				AbsX += DirFlip;
				
				if((MyX < MAP_X_SIZE) && (MyY < MAP_Y_SIZE) && (MyX >= 0) && (MyY >= 0)) roomcheck = Map[MyX][MyY].CheckIsRoom();
				else roomcheck = false;
				
                DelCount = DF.func(AbsX, AbsY, roomcheck, DelCount);
            }

	
	}
	
	public void DrawMapWithDelay(int EdgeDim) {
		_DrawMap(EdgeDim, new DrawRoomFunc() {
		
    public int func(final int RoomX, final int RoomY, final boolean room, int Delay) {

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

		} } );
	}

	public void DrawMapWithOutDelay(int EdgeDim) {
		_DrawMap(EdgeDim, new DrawRoomFunc() {
		
    public int func(final int RoomX, final int RoomY, final boolean room, int Delay) {

                // Do something after 5s = 5000ms
                MyView.DrawRoom(RoomX, RoomY, room);
        

        return(0);

		} } );
	}
	
	interface DrawRoomFunc {
		int func(final int RoomX, final int RoomY, final boolean room, int Delay);
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

	
	