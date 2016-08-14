//| ============================================================================
//|
//|	File:			MapEnemy.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		MapEnemy is a class to represent a dungeon enemy.
//|
//| ----------------------------------------------------------------------------

package com.example.chris.dungeons;

import java.util.Random;

class MapEnemy {
	
	//{ Private members: =======================================================

		//% Chance an enemy will not move when calling 'moveEnemy'.
		private static int NO_MOVE_PCT = 10;
		
		//Coordinates of the map room this enemy is in.
		private int myMapX;
		private int myMapY;
		
		//Flag to indicate this enemy is fighting the hero.
		private boolean fighting;
		
		//Flag to indicate this enemy is alive.
		private boolean alive;

	//} ------------------------------------------------------------------------
	
	//{ Constructors: ==========================================================

	//{ ------------------------------------------------------------------------
	//|
	//| This constructor sets the flag to set this as a room or a not-room.
	//|
	//|	Function parameters:
	//|
	//|		mapX,
	//|		mapY
	//|		Initial map room coordinates for the enemy.
	//|
	//|	Results:
	//|
	//|		The enemy's coordnates are set. The enemy starts out alive and not
	//|		fighting.
	//|
	//| ------------------------------------------------------------------------
	MapEnemy(
		int 	mapX,
		int 	mapY
	){
		
		//Set map coordinates.
		myMapX = mapX;
		myMapY = mapY;
		
		//Initialize fighting and alive flags.
		fighting = false;
		alive = true;
		
	} //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
	//{ Public methods: ========================================================
	
	//{ killEnemy --------------------------------------------------------------
	//|
	//|	Kill the enemy.
	//|
	//|	Results:
	//|
	//|		The enemy's alive flag is cleared.
	//|
	//| ------------------------------------------------------------------------
	public void killEnemy(){
		
		//Clear alive flag.
		alive = false;
		
	} //} ----------------------------------------------------------------------
	
	//{ isAlive ----------------------------------------------------------------
	//|
	//|	Check enemy alive status.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if the enemy is still alive.
	//|
	//|	Results:
	//|
	//|		The enemy's alive flag is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean isAlive() {
		
		//Return alive flag.
		return(alive);
		
	} //} ----------------------------------------------------------------------
	
	//{ checkForFight ----------------------------------------------------------
	//|
	//|	Check if the hero is around and start fighting if they are.
	//|
	//|	Function parameters:
	//|
	//|		heroX,
	//|		heroY
	//|		Map coordinates where the hero is.
	//|
	//|	Results:
	//|
	//|		If the hero is in an adjacent map room, then the enemy's fighting
	//|		flag is set.
	//|
	//| ------------------------------------------------------------------------
	public void checkForFight(
		int 	heroX,
		int 	heroY
	){
		//Set fighting flag if the hero is near.
		fighting = (	((heroX == myMapX) && ((heroY == (myMapY + 1)) || (heroY == (myMapY - 1)))) ||
						((heroY == myMapY) && ((heroX == (myMapX + 1)) || (heroX == (myMapX - 1))))	);
		
	} //} ----------------------------------------------------------------------

	//{ isFighting -------------------------------------------------------------
	//|
	//|	Check enemy fighting status.
	//|
	//|	Returns:
	//|
	//|		boolean
	//|		TRUE if the enemy is fighting the hero.
	//|
	//|	Results:
	//|
	//|		If the enemy is alive and fighting, then TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean isFighting(){
		
		//Reutrn TRUE if the enemy is alive and fighting.
		return(alive && fighting);
		
	} //} ----------------------------------------------------------------------

	//{ moveEnemy --------------------------------------------------------------
	//|
	//|	Move enemy through the dungeon.
	//|
	//|	Function parameters:
	//|
	//|		rollForMove
	//|		Reference to a Random to generate chences for movement.
	//|
	//|		myMapBuilder
	//|		Reference to the map builder.
	//|
	//|	Results:
	//|
	//|		We roll for a chance to move in one of four directions. If we
	//|		want to move, and there is a room to move to, and the room is
	//|		not occupied, then the enemy is moved.
	//|
	//| ------------------------------------------------------------------------
	public void moveEnemy(
		Random 		rollForMove,
		MapBuilder 	myMapBulder
	){
		
		int moveChance;
		int calc1;
		int chanceInc;
		
		//If the enemy is not fighting and is alive...
		if(!fighting && alive) {
		
			//Roll for a chance to move.
			moveChance = rollForMove.nextInt(100);
		
			//Get chance for movement and divide by 4.
			chanceInc = ((100 - NO_MOVE_PCT) / 4);
		
			//If we want to move...
			calc1 = NO_MOVE_PCT;
			if(moveChance > calc1) {
				
				//Set room's enemy reference to null while we move.
				myMapBulder.setMapRoomEnemy(myMapX, myMapY, null);
				
				//If we want to move right...
				calc1 += chanceInc;
				if(moveChance < calc1) {
					
					//And we are able to move...
					if(myMapBulder.checkRoomForEnemyMove((myMapX + 1), myMapY)) {
						
						//Increment x coordinate.
						myMapX++;
					}
				}
				else {

				//If we want to move down...
				calc1 += chanceInc;
				if(moveChance < calc1) {

					//And we are able to move...
					if(myMapBulder.checkRoomForEnemyMove(myMapX, (myMapY + 1))) {
						
						//Increment y coordinate.
						myMapY++;
					}
				}
				else {

				//If we want to move left...
				calc1 += chanceInc;
				if(moveChance < calc1) {

					//And we are able to move...
					if( myMapBulder.checkRoomForEnemyMove((myMapX - 1), myMapY)) {
						
						//Decrement x coordinate.
						myMapX--;
					}
				}
				
				//If we want to move up...
				else 
				
					//And we are able to move...
					if(myMapBulder.checkRoomForEnemyMove(myMapX, (myMapY - 1))) {
						
						//Decrement y coordinates.
						myMapY--;
					}
				}}
			
				//Get the room that the enemy has moved to and set it's
				//enemy reference.
				myMapBulder.setMapRoomEnemy(myMapX, myMapY, this);
			}
		}
		
	} //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
} //----------------------------------------------------------------------------


