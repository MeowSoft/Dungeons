//| ============================================================================
//|
//|	File:			EnemyAlertDialog.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		This is a dialog class to display an alert when an enemy is nearby.
//|		The dialog closes automatically after a brief delay.
//|
//| ----------------------------------------------------------------------------

package com.example.chris.helloworld;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;

public class EnemyAlertDialog 
	extends 
		DialogFragment 
{

	//{ Private members: =======================================================
	
		//Time to show alert fialog for before closing.
		private static final int DIALOG_DELAY = 1000;

	//} ------------------------------------------------------------------------
	
	//{ DialogFragment class overides: =========================================
	
	//{ onCreateDialog ---------------------------------------------------------
	//|
	//|	Override for DialogFragment::onCreateDialog method.
	//|
	//| Results:
	//|
	//|		The dialog is set up to display the message and then close after
	//|		a brief delay.
	//|
	//| ------------------------------------------------------------------------
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		//The dialog.
		final Dialog myDialog;
		
        //Build dialog with the enemy alert message.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.enemy_alert);
        
		//Create dialog.
		myDialog = builder.create();

		//Delay handler.
		final Handler handler = new Handler();

		//Create a runnable to close the dialog.
		Runnable delayClose = new Runnable() {
			@Override
			public void run() {
				myDialog.dismiss();
			}
		};
		
		//Send close function to handler.
		handler.postDelayed(delayClose, DIALOG_DELAY);

		//Return dialog reference.
        return (myDialog);
		
    } //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
} // ---------------------------------------------------------------------------

