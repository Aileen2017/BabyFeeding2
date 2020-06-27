package com.projects.babyfeeding;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AlertDialogImportData extends DialogFragment
{

	public static AlertDialogImportData newInstance(String title) {
        AlertDialogImportData frag = new AlertDialogImportData();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
	 	
	@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) 
	 	{
			String title = getArguments().getString("title");
	        
	        AlertDialog.Builder adb=new AlertDialog.Builder(getActivity().getApplicationContext());
	        adb.setTitle(title) ;
	        adb.setPositiveButton("Yes", new Listener1());
	        adb.setNegativeButton("No", new Listener2());
	        Dialog d= adb.create();
	        d.show();
	        return d;
	    }
	
		class Listener1 implements DialogInterface.OnClickListener
		{
			public void onClick(DialogInterface dialog, int which)
			{
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	        	i.setType("*/*");
	        	i.addCategory(Intent.CATEGORY_OPENABLE);
	        	String titleImportDatas= getResources().getString(R.string.title_ImportData);
	        	startActivityForResult(Intent.createChooser(i,titleImportDatas), 1);
	        	dismiss();
			}
			
		}
	
		class Listener2 implements DialogInterface.OnClickListener
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dismiss();
			}
		}

}
