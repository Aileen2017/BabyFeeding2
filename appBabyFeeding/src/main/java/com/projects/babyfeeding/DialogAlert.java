package com.projects.babyfeeding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

public class DialogAlert {
	
	
	public void newInstance(String title, String content, Context context)
	{

		AlertDialog.Builder adb=new AlertDialog.Builder(context);
        adb.setTitle(title) ;
        adb.setIcon(R.drawable.pin);
        adb.setMessage(content);
       
        //adb.setNegativeButton("No", new Listener2());
      //  adb.setPositiveButton("Yes", new Listener1());
        Dialog d= adb.create();
        d.show();
	}
	

}
