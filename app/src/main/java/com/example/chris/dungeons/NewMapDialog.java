//| ============================================================================
//|
//|	File:			NewMapDialog.java
//|	Environment:	Android Studio 2.1.2
//|
//|	Description:
//|
//|		This is a dialog class to ask if the user wants to generate a new map.
//|
//| ----------------------------------------------------------------------------

package com.example.chris.dungeons;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class NewMapDialog
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
		Activity act
	){
		//Superclass method.
        super.onAttach(act);
		
        //Set interface reference.
		mListener = (NoticeDialogListener)act;

    } //} ----------------------------------------------------------------------

	//{ onCreateDialog ---------------------------------------------------------
	//|
	//|	Override for DialogFragment::onCreateDialog method.
	//|
	//| Results:
	//|
	//|		The dialog is created and set to execute the 
	//|		'onDialogPositiveClick' method from the interface reference when
	//|		the 'Yes' button is clicked and do nothing when the 'No' button
	//|		is clicked.
	//|
	//| ------------------------------------------------------------------------
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		//The dialog.
		final Dialog myDialog;
		
		//Interface listeners.
		DialogInterface.OnClickListener myPositiveListener;
		DialogInterface.OnClickListener myNegativeListener;
		
		//Create listener to call method on 'Yes' click.
		myPositiveListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogPositiveClick(NewMapDialog.this);
			}
		};
		
		//Create listener to do nothing on 'No' click.
		myNegativeListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {}
		};

        //Build the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.new_map_query);
        builder.setPositiveButton(R.string.button_yes, myPositiveListener);
		builder.setNegativeButton(R.string.button_no, myNegativeListener);
		
		//Create the dialog.
		myDialog = builder.create();
	
        //Return new dialog.
        return(myDialog);
		
    } //} ----------------------------------------------------------------------
	
	//} ------------------------------------------------------------------------
	
} //----------------------------------------------------------------------------

