package com.projects.babyfeeding2;

import java.util.Calendar;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import android.content.Context;

public class DFTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
	private TimePickedListener mListener;
	
	public static DFTimePicker newInstance() {
        return new DFTimePicker();
    }

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState)
	 {
		 
		 View v = inflater.inflate(R.layout.time_picker, container, false);
		 //capture the size of the devices screen
		 WindowManager wm = (WindowManager) v.getContext().getSystemService(Context.WINDOW_SERVICE); 
		 Display screen = wm.getDefaultDisplay();  
		 @SuppressWarnings("deprecation")
		double width = screen.getWidth();
		 
		android.view.WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
		       
		    double doubleSize = (width/5)*2;
		    int editTextSize = (int) doubleSize;

		    mWindowParams.gravity = Gravity.TOP ;
		    mWindowParams.x = 0;
		     mWindowParams.y = 0;
		     mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		     mWindowParams.width = (int) width;
		    
		     
		    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		 	
	    	getDialog().setCanceledOnTouchOutside(true);
	    	Button btnSet=(Button)v.findViewById(R.id.btnSet);
	    	btnSet.setOnClickListener(new View.OnClickListener(){
	    		
	    		@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	    			 // when the time is selected, send it to the activity via its callback interface method
	    			//TimePicker tp=(TimePicker)v.getParent().findViewById(R.id.tpTimePicker);
	    			
	    			TimePicker tp=(TimePicker)v.getRootView().findViewById(R.id.tpTimePicker);
	    			int hourOfDay=tp.getCurrentHour();
	    			int minute=tp.getCurrentMinute();
	    			//Log.i("TimePicker set Button ", Integer.toString(hourOfDay));
	    			Calendar c = Calendar.getInstance();
	    			 c.set(Calendar.HOUR_OF_DAY, hourOfDay);
	    			 c.set(Calendar.MINUTE, minute);
	    			  
	    			 mListener.onTimePicked(c);
	    			 getDialog().dismiss();
	    			
				}
	    		
	    	});
	    	
	    	Button btnCancel=(Button)v.findViewById(R.id.btnCancel);
	    	TimePicker tp=(TimePicker)v.findViewById(R.id.tpTimePicker);
	    	btnCancel.setOnClickListener(new View.OnClickListener(){
	    		
	    		@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	    			
	    			 getDialog().dismiss();
	    			
					
				}
	    		
	    	});
	    	
	    	
	    	return v;
	 }
	
	 public void onAttach(Activity activity)
	 {
		 super.onAttach(activity);
		 try
		 {
			 mListener = (TimePickedListener) activity;
		 }
		 catch(ClassCastException e)
		 {
			 throw new ClassCastException(activity.toString() + " must implement " + TimePickedListener.class.getName());
		 }
	 }
	 
	 @Override
	 public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	 {
	 // when the time is selected, send it to the activity via its callback interface method
	 Calendar c = Calendar.getInstance();
	 c.set(Calendar.HOUR_OF_DAY, hourOfDay);
	 c.set(Calendar.MINUTE, minute);
	  
	 mListener.onTimePicked(c);
	 }
	 
	 
	 public static interface TimePickedListener
	 {
		 public void onTimePicked(Calendar time);
	 }
	 
	public void onBackPressed()
	{
		    //Log.d("TAG", "in onBackPressed");
		    getDialog().dismiss();
		   
	}
		
	 
	 
	 
	 
}
