//| ============================================================================
//|
//|	File:			YouWinDialog.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		This is a dialog class to display an alert when the hero reaches the
//|		dungeon exit.
//|
//| ----------------------------------------------------------------------------

package com.example.chris.dungeons;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class YouWinDialog 
	extends
		DialogFragment
{

	//{ Interface: =============================================================
		
		//This is an interface for a function to execute when the 'Ok' button
		//is pressed in the dialog.
		public interface NoticeDialogListener {
			public void onDialogPositiveClick(DialogFragment dialog);
		}

	//} ------------------------------------------------------------------------
	
	//{ Private members: =======================================================
	
		//Reference to a dialog listener interface.
		private NoticeDialogListener mListener;

	//} ------------------------------------------------------------------------
	
	//{ DialogFragment class overides: =========================================
	
	//{ onAttach ---------------------------------------------------------------
	//|
	//|	Override for DialogFragment::onAttach method.
	//|
	//| Results:
	//|
	//|		The superclass method is called. Then, a reference to the dialog
	//|		listener interface is set from tha parent context.
	//|
	//| ------------------------------------------------------------------------
    @Override
    public void onAttach(
		Context ctx
	){
		//Superclass method.
        super.onAttach(ctx);
		
        //Set interface reference.
		mListener = (NoticeDialogListener)ctx;

    } //} ----------------------------------------------------------------------

	//{ onCreateDialog ---------------------------------------------------------
	//|
	//|	Override for DialogFragment::onCreateDialog method.
	//|
	//| Results:
	//|
	//|		The dialog is created and set to execute the 
	//|		'onDialogPositiveClick' method from the interface reference when
	//|		the 'Ok' button is clicked.
	//|
	//| ------------------------------------------------------------------------
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		//The dialog.
		final Dialog myDialog;
		
		//Interface listener.
		DialogInterface.OnClickListener myListener;
		
		//Create listener to call method on 'Ok' click.
		myListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogPositiveClick(YouWinDialog.this);
			}
		};
		
        //Build the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.winner_alert);
        builder.setPositiveButton(R.string.i_win_yay, myListener);

		//Create the dialog.
		myDialog = builder.create();
	
        //Return new dialog.
        return(myDialog);
		
    } //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
} //----------------------------------------------------------------------------

