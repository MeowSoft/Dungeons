//| ============================================================================
//|
//|	File:			MapBuilder.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		This class is responsible for buildng the dungeon and controlling
//|		movement of the hero and enemies.
//|
//| ----------------------------------------------------------------------------

package com.example.chris.dungeons;

import java.util.LinkedList;
import java.util.Random;
import java.util.Iterator;

public class MapBuilder {
	
	//{ Private members: =======================================================

		//Target room vs not-room % for dungeon.
		private static final int ROOM_DENSITY = 25;
		
		//Random number generator.
		private Random rollDice;

		//Hero coordinates.
		private int heroX;
		private int heroY;
		
		//Exit coordinates.
		private int exitX;
		private int exitY;
		
		//Chance for an enemy when creating a map room.
		private static final int ENEMY_CHANCE = 8;
		
		//Linked list to hold all enemies.
		private LinkedList<MapEnemy> myEnemies;
		
		//Flag to check if the hero is fighting an enemy.
		private boolean fightingFlag;
		
		//An array that will contain the map room objects.
		private MapRoom[][] Map;

		//Var to hold the map size.
		private int myMapSize;
	
	//} ------------------------------------------------------------------------
	
	//{ Constructors: ==========================================================

	//{ ------------------------------------------------------------------------
	//|
	//| This constructor creates a new array of room objects that will
	//|	represent the dungeon. A random number generator is created and a
	//|	linked list to hold enemies is created as well.
	//|
	//| ------------------------------------------------------------------------
    public MapBuilder(
		int mapSize
	){
		//Get map size.
		myMapSize = mapSize;
	
		//Create map room array.
		Map = new MapRoom[myMapSize][myMapSize];

		//Create RNG.
		rollDice = new Random();
		
		//Create enemy list.
		myEnemies = new LinkedList<>();
		
    } //} ----------------------------------------------------------------------

	//} ------------------------------------------------------------------------
	
	//{ Private methods: =======================================================
	
	//{ _generateRoom ----------------------------------------------------------
	//|
	//|	Generate a dungeon room.
	//|
	//|	Function parameters:
	//|
	//|		mapX,
	//|		mapY
	//|		Map coordinates to generate room for.
	//|
	//|	Returns:
	//|		
	//|		MapRoom
	//|		The generated map room.
	//|
	//|	Results:
	//|
	//|		The chance to generate a room is determined by surrounding rooms.
	//|		If there are no surrounding rooms, then no room will be generated.
	//|		this keeps isolated rooms from being generated. If a room is
	//|		generated, then a chance for an enemy in the room is rolled.
	//|
	//| ------------------------------------------------------------------------
    private MapRoom _generateRoom(
		final int 	mapX,
		final int 	mapY
	){
		//Flags to check surrounding rooms.
		boolean Top;
		boolean Left;
		boolean Right;
		boolean Bottom;
		boolean Vert;
		boolean Horiz;
		boolean All;
		
		//The result of this function.
		MapRoom result;
		
		//Chance for a room.
		int roomChance;
		
		//Flag to see if we get a room or a not-room.
		boolean check;
		
		//Var to reference surrounding rooms.
		MapRoom MyMaproom;
		
		//Var to generate an enemy.
		MapEnemy myRoomEnemy;

		//Check if map coordinates above current location is a room.
		MyMaproom = (mapY > 0)? Map[mapX][(mapY - 1)] : null;
        Top = ((MyMaproom != null) && MyMaproom.CheckIsRoom());
		
		//Check if map coordinates to the left of current location is a room.
		MyMaproom = (mapX > 0)? Map[(mapX - 1)][mapY] : null;
        Left = ((MyMaproom != null) && MyMaproom.CheckIsRoom());
		
		//Check if map coordinates below current location is a room.
		MyMaproom = (mapY < (myMapSize - 1))? Map[mapX][(mapY + 1)] : null;
        Bottom = ((MyMaproom != null) && MyMaproom.CheckIsRoom());
		
		//Check if map coordinates to the right of current location is a room.
		MyMaproom = (mapX < (myMapSize - 1))? Map[(mapX + 1)][mapY] : null;
        Right = ((MyMaproom != null) && MyMaproom.CheckIsRoom());

		//Check if there are rooms to the left and right.
		Horiz = (Left && Right);
		
		//Check if there are rooms above and below.
		Vert = (Top && Bottom);
		
		//Check if there are rooms all around.
		All = (Horiz && Vert);
		
		//Get probability for a room based on surrounding rooms.
		roomChance =	All ? 					95 :
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
		
		//Roll for a room.
		check = (roomChance > rollDice.nextInt(100));
		
		//Create map room object.
		result = new MapRoom(check);
		
		//Clear room's enemy reference.
		result.SetEnemy(null);
		
		//If we have a room...
		if(check) {
			
			//Roll for an enemy.
			if(rollDice.nextInt(100) < ENEMY_CHANCE) {
				myRoomEnemy = new MapEnemy(mapX, mapY);
				myEnemies.add(myRoomEnemy);
				result.SetEnemy(myRoomEnemy);
			}
		}
		
		//Return the result.
		return(result);
	
	} //} ----------------------------------------------------------------------

	//{ _checkForHeroMove ------------------------------------------------------
	//|
	//|	Check if the hero can be moved into the given room
	//|
	//|	Function parameters:
	//|
	//|		checkX,
	//|		checkY
	//|		Coordinates of the room to check.
	//|	
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if the hero can be moved.
	//|
	//|	Results:
	//|
	//|		If the room is unoccupied, then TRUE is returned.
	//|
	//|	------------------------------------------------------------------------
	private boolean _checkForHeroMove(
		int 	checkX,
		int 	checkY
	){
		//The result of this function.
		boolean result;
		
		//Reference to a map room.
		MapRoom roomPtr;
		
		//Init result.
		result = false;
		
		//Get indexed room.
		roomPtr = _getMapRoom(checkX, checkY);
		
		//Room is valid...
		if(roomPtr != null) {
			
			//If map room is a room, and there is no alive enemy inside,
			//then we can return TRUE.
			result = roomPtr.CheckIsRoom() && !roomPtr.CheckEnemyAlive();
		}
		
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------
	
	//{ _checkMapBounds --------------------------------------------------------
	//|
	//|	Check if coordinates are valid.
	//|
	//|	Function parameters:
	//|
	//|		checkX,
	//|		checkY
	//|		Coordinates to check.
	//|	
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if the coordinates are valid map coordinates.
	//|
	//|	Results:
	//|
	//|		If the given coordinates are valid, then TRUE is returned.
	//|
	//|	------------------------------------------------------------------------
	private boolean _checkMapBounds(
		int 	checkX,
		int 	checkY
	){
		//The result of this function.
		boolean result;
		
		//If coordinates are on the map, then return TRUE.
		result = (	(checkX >= 0) && 
					(checkX < myMapSize) && 
					(checkY >= 0) && 
					(checkY < myMapSize)
				);
		
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------
	
	//{ _checkRoomForEnemy -----------------------------------------------------
	//|
	//|	Check if there is an alive enemy in a room.
	//|
	//|	Function parameters:
	//|
	//|		checkX,
	//|		checkY
	//|		Coordinates of the room to check.
	//|	
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if there is an alive enemy in the room.
	//|
	//|	Results:
	//|
	//|		If the room contains an alive enemy, then TRUE is returned.
	//|
	//|	------------------------------------------------------------------------
	private boolean _checkRoomForEnemy(
		int 	checkX,
		int 	checkY
	){
		//The result of this function.
		boolean result;
		
		//Init result.
		result = false;
		
		//If coordinates are valid, check for an alive enemy in the room.
		if(_checkMapBounds(checkX, checkY)) {
			result = Map[checkX][checkY].CheckEnemyAlive();
		}
		
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------
	
	//{ _checkRoomForHero ------------------------------------------------------
	//|
	//|	Check if the hero is in a room.
	//|
	//|	Function parameters:
	//|
	//|		checkX,
	//|		checkY
	//|		Coordinates of the room to check.
	//|	
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if the hero is in the room.
	//|
	//|	Results:
	//|
	//|		If the hero is in the room, then TRUE is returned.
	//|
	//|	------------------------------------------------------------------------
	private boolean _checkRoomForHero(
		int 	checkX, 
		int 	checkY
	){
		//If the given coordinates match the hero's, return TRUE.
		return((checkX == heroX) && (checkY == heroY));
		
	} //} ----------------------------------------------------------------------
	
	//{ _getMapRoom ------------------------------------------------------------
	//|
	//|	Get a map room.
	//|
	//|	Function parameters:
	//|
	//|		roomX,
	//|		roomY
	//|		Coordinates of the room to get.
	//|	
	//|	Returns:
	//|
	//|		MapRoom
	//|		The indesed room.
	//|
	//|	Results:
	//|
	//|		If the given coordinates are valid, then the corresponding map
	//|		room is returned.
	//|
	//|	------------------------------------------------------------------------
	private MapRoom _getMapRoom(
		int 	roomX,
		int		roomY
	){
		//The result of this function.
		MapRoom result;
		
		//Init result.
		result = null;
		
		//If coordinates are valid, return the indexed map room.
		if(_checkMapBounds((roomX), (roomY))){
			result = Map[(roomX)][(roomY )];
		}
		
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------

	//} ------------------------------------------------------------------------
	
	//{ Public methods: ========================================================
	
	//{ buildMap ---------------------------------------------------------------
	//|
	//|	Build the dungeon.
	//|
	//|	Results:
	//|
	//|		A dungeon map is generated starting at the center of the map and
	//|		spiraling outward. The generate process runs until there are
	//|		enough rooms to have a reasonably large dungeon. Entrance and
	//|		exit coordinates are randomly generated until they coincide with
	//|		a dungeon room. The hero is then placed at the entrance.
	//|
	//| ------------------------------------------------------------------------
    public void buildMap(){

		//Map room coordinates.
        int mapX;
        int mapY;

		//Loop counters.
        int x;
        int y;
		
		//Var to increment / decrement map coordinates.
		int DirFlip;

		//Calc var.
		int temp;
		
		//Vars to make sure we generate a map with a reasonable number of
		//rooms.
		int roomCount;
		int minRoomCount;
		
		//Vars to get random map coordinates.
		int rollX;
		int rollY;
		
		//Calculate minimum number of rooms we need.
		minRoomCount = (((myMapSize * myMapSize) * ROOM_DENSITY) / 100);
		
		do {
			
			//Clear enemies list.
			myEnemies.clear();
		
			//Erase map.
			for(x = 0; x < myMapSize; x++) {
			for(y = 0; y < myMapSize; y++) {
				Map[x][y] = null;
			}}
		
			//Start in the center of the map.
			mapX = myMapSize / 2;
			mapY = mapX;

			//Center is always a room.
			Map[mapX][mapY] = new MapRoom(true);
			
			//Init room count.
			roomCount = 1;
			
			//Start moving in +x +y dir.
			DirFlip = 1;

			//For all coordinates on map edge...
			for(x = 0; x < myMapSize; x++) {

				//Create horizontal rooms.
				for (y = 0; y < x; y++){
					mapX += DirFlip;
					Map[mapX][mapY] = _generateRoom(mapX, mapY);
				
					
					//Add to room count.
					if(Map[mapX][mapY].CheckIsRoom())
						roomCount++;
				}

				//This is so we don't create an extra room on the last leg of
				//the spiral.
				temp = (x == (myMapSize - 1)) ? (x - 1) : x;
				
				//Create vertical rooms.
				for(y = 0; y <= temp; y++){
					mapY += DirFlip;
					Map[mapX][mapY] = _generateRoom(mapX, mapY);
			
					
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
			rollX = rollDice.nextInt(myMapSize);
			rollY = rollDice.nextInt(myMapSize);
			
		//Until entrance is a valid room.
		} while(!Map[rollX][rollY].CheckIsRoom());
		
		//Place hero at dungeon entrance.
		heroX = rollX;
		heroY = rollY;
		
		do {
			
			//Roll for exit coordinates.
			rollX = rollDice.nextInt(myMapSize);
			rollY = rollDice.nextInt(myMapSize);
			
		//Until exit is a valid room. (And not the one the hero is in)
		} while(!Map[rollX][rollY].CheckIsRoom() || ((rollX == heroX) && (rollY == heroY)));
		
		//Set exit coordinates.
		exitX = rollX;
		exitY = rollY;
		
    } //} ----------------------------------------------------------------------

	//{ moveHero ---------------------------------------------------------------
	//|
	//|	Move the hero.
	//|
	//|	Function parameters:
	//|
	//|		moveX,
	//|		moveY,
	//|		The direction to move the hero in.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if the hero was able to move. FALSE if the hero was blocked.
	//|
	//|	Results:
	//|
	//|		The sign of moveX and moveY will determine the direction to try
	//|		and move. If there is nothing in the way of the hero, then the
	//|		hero is moved and TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean moveHero(
		int 	moveX,
		int 	moveY
	){
		//The result of this function.
		boolean result = false;
		
		//Try to move right:
		if((moveX > 0) && (heroX < (myMapSize - 1))) {
			if(_checkForHeroMove((heroX + 1), heroY)) {
				heroX++;
				result = true;
			}
		}
		
		//Try to move left:
		else if((moveX < 0) && (heroX > 0)) {
			if(_checkForHeroMove((heroX - 1), heroY)) {
				heroX--;
				result = true;
			}
		}
		
		//Try to move down:
		else if((moveY > 0) && (heroY < (myMapSize - 1))) {
			if(_checkForHeroMove(heroX, (heroY + 1))) {
				heroY++;
				result = true;
			}
		}
		
		//Try to move up:
		else if((moveY < 0) && (heroY > 0)) {
			if(_checkForHeroMove(heroX, (heroY - 1))) {
				heroY--;
				result = true;
			}
		}
		
		//If the hero has moved, clear the fighting flag.
		if(result)
			fightingFlag = false;
		
		//Check for new enemies to fight.
		updateEnemyActivity();
		
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------

	//{ heroIsAtExit -----------------------------------------------------------
	//|
	//| Check if we are at the exit.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if we are at the exit.
	//|
	//|	Results:
	//|
	//|		If the hero is at the exit, then TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean heroIsAtExit() {
	
		//Return TRUE if we have reached the exit.
		return((heroX == exitX) && (heroY == exitY));
		
	} //} ----------------------------------------------------------------------

	//{ updateEnemyActivity ----------------------------------------------------
	//|
	//|	Handle enemy activity.
	//|
	//|	Results:
	//|
	//|		All enemies are given a chance to move and are checked to see if
	//|		they are fighting the hero.
	//|
	//| ------------------------------------------------------------------------
	public void updateEnemyActivity() {
		
		//Iterator to step through enemies list.
		Iterator listWalker = myEnemies.iterator();
		
		//Var to get enemy from list.
		MapEnemy myEnemy;
		
		//For all enemies...
		while(listWalker.hasNext()) {

			//Get enemy.
			myEnemy = ((MapEnemy) listWalker.next());
		
			//Give enemy a chance to move.
			myEnemy.moveEnemy(rollDice, this);
			
			//Check if enemy is in a fight after moving.
			myEnemy.checkForFight(heroX, heroY);
		}
		
	} //} ----------------------------------------------------------------------

	//{ enemiesNearby ----------------------------------------------------------
	//|
	//|	Check if there are enemies near the hero.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if there is an enemy next to the hero.
	//|
	//|	Results:
	//|
	//|		If an enemy is directly above, below, or to the side of the hero,
	//|		then TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean enemiesNearby(){
		
		//The result of this function.
		boolean result;
		
		//Check for enemies.
		result = _checkRoomForEnemy((heroX + 1), heroY);
		result |= _checkRoomForEnemy((heroX - 1), heroY);
		result |= _checkRoomForEnemy(heroX, (heroY + 1));
		result |= _checkRoomForEnemy(heroX, (heroY - 1));
		
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------

	//{ enemyApproached --------------------------------------------------------
	//|
	//|	Check if an enemy has just approached the hero.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if there were no enemies near the hero before and one has
	//|		just approached them.
	//|
	//|	Results:
	//|
	//|		If an enemy approaches the hero, then a flag is set and TRUE is
	//|		returned. If the flag is still set next time this function is
	//|		called, then FALSE is returned. If there are no more enemies
	//|		nearby, then the flag is cleared.
	//|
	//| ------------------------------------------------------------------------
	public boolean enemyApproached(){
		
		//The result of this function.
		boolean result;
		
		//Calc var.
		boolean check;
		
		//Check for nearby enemies.
		check = enemiesNearby();
		
		//If there are enemies nearby and there weren't before, then return
		//TRUE.
		result = (check && !fightingFlag);
		
		//Set flag for next time.
		fightingFlag = check;
		
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------
	
	//{ fightEnemies -----------------------------------------------------------
	//|
	//|	Fight any enemies that are near the hero.
	//|
	//|	Results:
	//|
	//|		The enemies list is scanned. Any enemies that are near the hero
	//|		are killed.
	//|
	//| ------------------------------------------------------------------------
	public void fightEnemies() {

		//Iterator to step through enemies list.
		Iterator listWalker = myEnemies.iterator();
		
		//Var to get enemy from list.
		MapEnemy myEnemy;
		
		//For all enemies...
		while(listWalker.hasNext()) {

			//Get enemy.
			myEnemy = ((MapEnemy) listWalker.next());
		
			//If enemy is fghting the hero, kill the enemy.
			if(myEnemy.isFighting())
				myEnemy.killEnemy();
		}
		
	} //} ----------------------------------------------------------------------

	//{ checkRoomForEnemyMove --------------------------------------------------
	//|
	//|	See if a room is unoccupied for an enemy to move into.
	//|
	//|	Function parameters:
	//|
	//|		checkX,
	//|		checkY,
	//|		Room coordinates to check.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if an enemy can move into the room.
	//|
	//|	Results:
	//|
	//|		If the room coordinates are valid and the room is unoccupied,
	//|		then TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean checkRoomForEnemyMove(
		int 	checkX,
		int 	checkY
	){
		//The result of this function.
		boolean result;
		
		//If coordinates are valid, and it is a room, and the hero is not 
		//in the room and another enemy is not in the room, then return TRUE.
		result = (	_checkMapBounds(checkX, checkY) &&
					Map[checkX][checkY].CheckIsRoom() &&
					!_checkRoomForEnemy(checkX, checkY) &&
					!_checkRoomForHero(checkX, checkY) &&
					(checkX != exitX) &&
					(checkY != exitY));
	
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------

	//{ setMapRoomEnemy --------------------------------------------------------
	//|
	//|	Set a map room's enemy reference.
	//|
	//|	Function parameters:
	//|
	//|		roomX,
	//|		roomY,
	//|		Map coordinates of the room to set the enemy reference for.
	//|
	//|		myEnemy
	//|		The enemy reference to set. (May be NULL)
	//|
	//|	Results:
	//|
	//|		The room's enemy reference is set.
	//|
	//| ------------------------------------------------------------------------
	public void setMapRoomEnemy(
		int			roomX,
		int 		roomY,
		MapEnemy 	myEnemy
	){
		//Set the room's enemy reference.
		Map[roomX][roomY].SetEnemy(myEnemy);
	
	} //} ----------------------------------------------------------------------
	
	//{ checkRoom --------------------------------------------------------------
	//|
	//|	Check if a room is a dungeon room or a not-room.
	//|
	//|	Function parameters:
	//|
	//|		roomX,
	//|		roomY
	//|		Coordinates relative to the hero of the room to check.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if there is an enemy in the room.
	//|
	//|	Results:
	//|
	//|		If the room contains an enemy, then TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean checkRoom(
		int 	roomX,
		int 	roomY
	){
		//The result of this function.
		boolean result;

		//Var to get a reference to the room to check.
		MapRoom myRoom;
		
		//Init return.
		result = false;
		
		//Get reference to the room we are checking.
		myRoom = _getMapRoom((roomX + heroX), (roomY + heroY));
		
		//If we have a room, check for an enemy.
		if(myRoom != null){
			result = myRoom.CheckIsRoom();
		}
		
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------

	//{ roomHasEnemy -----------------------------------------------------------
	//|
	//|	Check if a room has an enemy (alive or dead).
	//|
	//|	Function parameters:
	//|
	//|		roomX,
	//|		roomY
	//|		Coordinates relative to the hero of the room to check.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if there is an enemy in the room.
	//|
	//|	Results:
	//|
	//|		If the room contains an enemy, then TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean roomHasEnemy(
		int 	roomX,
		int 	roomY
	){
		//The result of this function.
		boolean result;

		//Var to get a reference to the room to check.
		MapRoom myRoom;
		
		//Init return.
		result = false;
		
		//Get reference to the room we are checking.
		myRoom = _getMapRoom((roomX + heroX), (roomY + heroY));
		
		//If we have a room, check for an enemy.
		if(myRoom != null){
			result = myRoom.CheckHasEnemy();
		}
		
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------

	//{ roomEnemyIsAlive -------------------------------------------------------
	//|
	//|	Check if a room has an alive enemy.
	//|
	//|	Function parameters:
	//|
	//|		roomX,
	//|		roomY
	//|		Coordinates relative to the hero of the room to check.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if there is an alive enemy in the room.
	//|
	//|	Results:
	//|
	//|		If the room contains an alive enemy, then TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean roomEnemyIsAlive(
		int 	roomX,
		int 	roomY
	){
		//The result of this function.
		boolean result;
		
		//Var to get a reference to the room to check.
		MapRoom myRoom;
		
		//Init return.
		result = false;
		
		//Get reference to the room we are checking.
		myRoom = _getMapRoom((roomX + heroX), (roomY + heroY));
		
		//If we have a room, check for an alive enemy.
		if(myRoom != null){
			result = myRoom.CheckEnemyAlive();
		}
		
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------
	
	//{ roomIsExit ------------------------------------------------------------
	//|
	//|	Check if a room is the exit room.
	//|
	//|	Function parameters:
	//|
	//|		roomX,
	//|		roomY
	//|		Coordinates relative to the hero of the room to check.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if the room is the exit room.
	//|
	//|	Results:
	//|
	//|		If the room is the exit room TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean roomIsExit(
		int 	roomX,
		int 	roomY
	){
		//Return TRUE if the room is the exit.
		return(((roomX + heroX) == exitX) && ((roomY + heroY) == exitY));
		
	} //} ----------------------------------------------------------------------

	//} ------------------------------------------------------------------------
	
} //----------------------------------------------------------------------------

