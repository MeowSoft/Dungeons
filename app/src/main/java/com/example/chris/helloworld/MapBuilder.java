package com.example.chris.helloworld;

import android.os.Handler;

import java.util.LinkedList;
import java.util.Random;
import java.util.Iterator;
/**
 * Created by Chris on 7/31/2016.
 */




public class MapBuilder {
	
	//{ Private members: =======================================================

	
		
		private static final int MAP_SIZE = 31;
		private static final int MAP_X_SIZE = 31;
		private static final int MAP_Y_SIZE = 31;
		
		private static final int MAP_X_DRAW_SIZE = 10;
		private static final int MAP_Y_DRAW_SIZE = 10;

		private static final int ROOM_DENSITY = 25;
		
		private Random rollDice;

		private int heroX;
		private int heroY;
		
		private int enemyChance = 90;
		
		private LinkedList<MapEnemy> myEnemies;
		
		private int exitX;
		private int exitY;
		
		//An array that will contain the map room objects.
		private MapRoom[][] Map;
		//private DrawingView MyView;
	
	//} ------------------------------------------------------------------------
	
	//{ Constructors: ==========================================================

	//{ ------------------------------------------------------------------------
	//|
	//| This constructor creates a new array of room objects that will
	//|	represent the dungeon. The 
	//|
	//| ------------------------------------------------------------------------
    public MapBuilder() {

		Map = new MapRoom[MAP_SIZE][MAP_SIZE];
        //MyView = NewView;

		rollDice = new Random();
		
        heroX = MAP_SIZE / 2;
        heroY = MAP_SIZE / 2;
		
		myEnemies = new LinkedList<MapEnemy>();

    }

	//{ BuildMap ---------------------------------------------------------------
	//|
    public void BuildMap(){

		//Map room coordinates.
        int mapX;
        int mapY;

		//Loop counters.
        int x;
        int y;
		
		//Var to increment / decrement map coordinates.
		int DirFlip;

		//Calc vars.
		int temp;
		boolean setRoom;
		
		//Vars to make sure we generate a map with a reasonable number of
		//rooms.
		int roomCount;
		int minRoomCount;
		
		//Vars to get random map coordinates.
		int rollX;
		int rollY;
		
		//Calculate minimum number of rooms we need.
		minRoomCount = (((MAP_SIZE * MAP_SIZE) * ROOM_DENSITY) / 100);
		
		do {
		
			//Erase map.
			for(x = 0; x < MAP_SIZE; x++) {
			for(y = 0; y < MAP_SIZE; y++) {
				Map[x][y] = null;
			}}
		
			//Start in the center of the map.
			mapX = MAP_SIZE / 2;
			mapY = mapX;

			//Center is always a room.
			Map[mapX][mapY] = new MapRoom(true);
			
			//Init room count.
			roomCount = 1;
			
			//Start moving in +x +y dir.
			DirFlip = 1;

			//For all coordinates on map edge...
			for(x = 0; x < MAP_SIZE; x++) {

				//Create horizontal rooms.
				for (y = 0; y < x; y++){
					mapX += DirFlip;
					Map[mapX][mapY] = _GenerateRoom(mapX, mapY);
				
					
					//Add to room count.
					if(Map[mapX][mapY].CheckIsRoom())
						roomCount++;
				}

				//This is so we don't create an extra room on the last leg of
				//the spiral.
				temp = (x == (MAP_SIZE - 1)) ? (x - 1) : x;
				
				//Create vertical rooms.
				for(y = 0; y <= temp; y++){
					mapY += DirFlip;
					Map[mapX][mapY] = _GenerateRoom(mapX, mapY);
			
					
					//Add to room count.
					if(Map[mapX][mapY].CheckIsRoom())
						roomCount++;
				}

				//Reverse direction.
				DirFlip *= -1;
			}
		
		//Go until we have a reasonable map.
		} while(roomCount < minRoomCount);
		
		do {
			
			//Roll for entrance coordinates.
			rollX = rollDice.nextInt(MAP_SIZE);
			rollY = rollDice.nextInt(MAP_SIZE);
			
		//Until entrance is a valid room.
		} while(!Map[rollX][rollY].CheckIsRoom());
		
		//Place hero at dungeon entrance.
		heroX = rollX;
		heroY = rollY;
		
		do {
			
			//Roll for exit coordinates.
			rollX = rollDice.nextInt(MAP_SIZE);
			rollY = rollDice.nextInt(MAP_SIZE);
			
		//Until exit is a valid room.
		} while(!Map[rollX][rollY].CheckIsRoom());
		
		//Set exit coordinates.
		exitX = rollX;
		exitY = rollY;
		
    } //} ----------------------------------------------------------------------


    private MapRoom _GenerateRoom(
		int 	mapX,
		int 	mapY
	){

  
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
		
		MapRoom result;
		
		int Mult;
		
		boolean check;
		
		MapRoom MyMaproom;
		MapEnemy myRoomEnemy;

		//Check if map coordinates above current location is a room.
		MyMaproom = (mapY > 0)? Map[mapX][(mapY - 1)] : null;
        Top = (MyMaproom != null)? MyMaproom.CheckIsRoom() : false;
		
		//Check if map coordinates to the left of current location is a room.
		MyMaproom = (mapX > 0)? Map[(mapX - 1)][mapY] : null;
        Left = (MyMaproom != null)? MyMaproom.CheckIsRoom() : false;
		
		//Check if map coordinates below current location is a room.
		MyMaproom = (mapY < (MAP_SIZE - 1))? Map[mapX][(mapY + 1)] : null;
        Bottom = (MyMaproom != null)? MyMaproom.CheckIsRoom() : false;
		
		//Check if map coordinates to the right of current location is a room.
		MyMaproom = (mapX < (MAP_SIZE - 1))? Map[(mapX + 1)][mapY] : null;
        Right = (MyMaproom != null)? MyMaproom.CheckIsRoom() : false;

		//Check if there are rooms to the left and right.
		Horiz = (Left && Right);
		
		//Check if there are rooms above and below.
		Vert = (Top && Bottom);
		
		//Check if there are rooms all around.
		All = (Horiz && Vert);
		
		//Get probability for a room based on surrounding rooms.
		Mult =	All ? 					95 :
				Horiz ? 				80 :
				Vert ? 					80 :
				(Top && Left) ? 		70 :
				(Top && Right) ? 		70 :
				(Bottom && Left) ? 		70 :
				(Bottom && Right) ? 	70 :
				Top ? 					65 :
				Left ? 					65 :
				Bottom ? 				65 : 
				Right ? 				65 : 
										0;
		
		//Init return.
		check = false;
		
		//Roll for a room.
		check = (Mult > rollDice.nextInt(100));
		
		
		result = new MapRoom(check);
		
		result.SetEnemy(false);
		if(check) {
			
			if(rollDice.nextInt(100) > enemyChance) {
				myRoomEnemy = new MapEnemy(mapX, mapY, rollDice);
				myEnemies.add(myRoomEnemy);
				result.SetEnemy(true);
			}
		}
		
		
		
		//Return the result.
		return(result);
	
	}
			
		
		public boolean moveHero(int MoveX, int MoveY) {
			
			boolean result = false;
			
			if((MoveX > 0) && (heroX < (MAP_SIZE - 1))) {

				if(_myCheckRoom((heroX + 1), heroY)) {
					heroX++;
					result = true;
				}
			}
			
			else if((MoveX < 0) && (heroX > 0)) {

				if(_myCheckRoom((heroX - 1), heroY)) {
					heroX--;
					result = true;
				}
			}
			
			else if((MoveY > 0) && (heroY < (MAP_SIZE - 1))) {

				if(_myCheckRoom(heroX, (heroY + 1))) {
					heroY++;
					result = true;
				}
			}
			
			else if((MoveY < 0) && (heroY > 0)) {

				if(_myCheckRoom(heroX, (heroY - 1))) {
					heroY--;
					result = true;
				}
			}
			
			moveEnemies();
			
			return(result);
		}
		
		public void moveEnemies() {
			
			Iterator listWalker = myEnemies.iterator();
			
			while(listWalker.hasNext()) {

				((MapEnemy) listWalker.next()).move(rollDice, this);

			}
			
		}
		
		public boolean _myCheckRoom(int myx, int myy) {
			
			boolean result;
			result = false;
			if((myx >= 0) && (myx < MAP_SIZE) && (myy >= 0) && (myy < MAP_SIZE))
					result = Map[myx][myy].CheckIsRoom();
			return(result);
		}
		
		public MapRoom GetRoom(int mapX, int mapY) {
			
		return(Map[mapX][mapY]);
		}
	

	
	public boolean _CheckRoom(int RoomX, int RoomY) {
		
		boolean result;
		
		result = false;
		if(((RoomX + heroX) < MAP_SIZE) && ((RoomY + heroY) < MAP_SIZE) && ((RoomX + heroX) >= 0) && ((RoomY + heroY) >= 0)) result = Map[(RoomX + heroX)][(RoomY + heroY)].CheckIsRoom();
		return(result);
	}
		
	public boolean _RoomHasEnemy(int roomX, int roomY) {
		boolean result;
		result = false;
		if(((roomX + heroX) < MAP_SIZE) && ((roomY + heroY) < MAP_SIZE) && ((roomX + heroX) >= 0) && ((roomY + heroY) >= 0)) {
		result = Map[roomX + heroX][roomY + heroY].CheckHasEnemy();
		}
		return(result);
	}

	
}


class MapRoom {
	
	boolean IsRoom;
	boolean HasEnemy;

	MapRoom(boolean SetIsRoom) {
		
		IsRoom = SetIsRoom;
	}
	
	public boolean CheckIsRoom() {
		return(IsRoom);
	}
	
	public void
	SetEnemy(boolean set) {
		HasEnemy = set;
	}
	
	public boolean CheckHasEnemy(){
		return(HasEnemy);
	}
}

class MapEnemy {
	
	private static int MAX_ENEMY_HEALTH = 5;
	
	int myMapX;
	int myMapY;
	
	int myHealth;
	
	MapEnemy(int mapX, int mapY, Random rollForHealth) {
		
		myMapX = mapX;
		myMapY = mapY;
		
		myHealth = rollForHealth.nextInt(MAX_ENEMY_HEALTH);
		
	}
	
	public void move(Random rollForMove, MapBuilder myMapBulder) {
		
		int temp;
		
		temp = rollForMove.nextInt(10);
		
		
		
		if(temp > 5) {
			
			myMapBulder.GetRoom(myMapX, myMapY).SetEnemy(false);
			if((temp == 6) && myMapBulder._myCheckRoom((myMapX + 1), myMapY))
				myMapX++;
			else if((temp == 7) && myMapBulder._myCheckRoom(myMapX, (myMapY + 1)))
				myMapY++;
			else if((temp == 8) && myMapBulder._myCheckRoom((myMapX - 1), myMapY))
				myMapX--;
			else if((temp == 9) && myMapBulder._myCheckRoom(myMapX, (myMapY - 1)))
				myMapY--;
			
		myMapBulder.GetRoom(myMapX, myMapY).SetEnemy(true);
		}
		
		
	}
	
}