//| ============================================================================
//|
//|	File:			MapRoom.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		MapRoom is a class to represent a room in the dungeon map.
//|
//| ----------------------------------------------------------------------------

package com.example.chris.helloworld;

class MapRoom {
	
	//{ Private members: =======================================================

		//Flag to set this as a room or a not-room.
		private boolean isRoom;
		
		//Reference to an enemy occupying this room.
		private MapEnemy myEnemy;
		
	//} ------------------------------------------------------------------------
	
	//{ Constructors: ==========================================================

	//{ ------------------------------------------------------------------------
	//|
	//| This constructor sets the flag to set this as a room or a not-room.
	//|
	//| ------------------------------------------------------------------------
	MapRoom(
		boolean myIsRoom
	){
		//Set room flag.
		isRoom = myIsRoom;
		
	} //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
	//{ Public methods: ========================================================
	
	//{ CheckIsRoom ------------------------------------------------------------
	//|
	//|	Check if this is a room or not-room.
	//|
	//|	Returns:
	//|		
	//|		boolean
	//|		TRUE if this is a room. FALSE if it is not.
	//|
	//|	Results:
	//|
	//|		The 'isRoom' flag is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean CheckIsRoom() {
		
		//Return room flag.
		return(isRoom);
		
	} //} ----------------------------------------------------------------------
	
	//{ SetEnemy ---------------------------------------------------------------
	//|
	//|	Set this room's enemy reference.
	//|
	//|	Function parameters:
	//|		
	//|		setEnemy
	//|		Reference to an enemy occupying this room. (May be NULL)
	//|
	//|	Results:
	//|
	//|		This room's enemy reference is set to the given enemy object.
	//|
	//| ------------------------------------------------------------------------
	public void SetEnemy(
		MapEnemy setEnemy
	){
		//Set this room's enemy reference.
		myEnemy = setEnemy;
		
	} //} ----------------------------------------------------------------------
	
	//{ CheckHasEnemy ----------------------------------------------------------
	//|
	//|	Check if this room has an enemy.
	//|
	//|	Returns:
	//|		
	//|		boolean
	//|		TRUE if there is an enemy in this room.
	//|
	//|	Results:
	//|
	//|		If this room's enemy reference is not NULL, then TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean CheckHasEnemy(){
		
		//Return TRUE if the enemy reference is not NULL.
		return(myEnemy != null);
		
	} //} ----------------------------------------------------------------------
	
	//{ CheckEnemyAlive --------------------------------------------------------
	//|
	//|	Check if this room has an alive enemy.
	//|
	//|	Returns:
	//|		
	//|		boolean
	//|		TRUE if there is an alive enemy in this room.
	//|
	//|	Results:
	//|
	//|		If this room's enemy reference is not NULL, and the enemy is
	//|		alive the TRUE is returned.
	//|
	//| ------------------------------------------------------------------------
	public boolean CheckEnemyAlive(){
		
		//The result of this function.
		boolean result;
		
		//Init return to FALSE.
		result = false;
		
		//If there is an enemy in this room...
		if(myEnemy != null) {
			
			//Set result to TRUE if enemy is alive.
			result = myEnemy.isAlive();
		}
	
		//Return the result.
		return(result);
		
	} //} ----------------------------------------------------------------------

	//} ------------------------------------------------------------------------
	
} //----------------------------------------------------------------------------


