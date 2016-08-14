//| ============================================================================
//|
//|	File:			IntroDialog.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		This is a dialog class to display an intro message when the app starts.
//|
//| ----------------------------------------------------------------------------

package com.example.chris.dungeons;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class IntroDialog
	extends
		DialogFragment
{

	//{ DialogFragment class overides: =========================================
	
	//{ onCreateDialog ---------------------------------------------------------
	//|
	//|	Override for DialogFragment::onCreateDialog method.
	//|
	//| Results:
	//|
	//|		The dialog is created and shown.
	//|
	//| ------------------------------------------------------------------------
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		//The dialog.
		final Dialog myDialog;
		
		//Interface listener.
		DialogInterface.OnClickListener myListener;
		
		//Create listener for Ok click
		myListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {}
		};
		
        //Build the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.intro_copy);
        builder.setPositiveButton(R.string.button_ok, myListener);

		//Create the dialog.
		myDialog = builder.create();
	
        //Return new dialog.
        return(myDialog);
		
    } //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
} //----------------------------------------------------------------------------

