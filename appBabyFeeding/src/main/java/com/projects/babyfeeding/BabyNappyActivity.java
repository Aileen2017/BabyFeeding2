package com.projects.babyfeeding;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.projects.babyfeeding.DFTimePicker.TimePickedListener;


public class BabyNappyActivity extends Activity implements RadioGroup.OnCheckedChangeListener, TimePickedListener
{

	Date newDate, preSetTime;
	private EditText mPickedTimeText;
	private  String tempTS;
	private RadioButton rdiWet, rdiDirty;
	
	private Timestamp timeStamp=null, savedTS=null;
	private boolean wet, dirty, timeChanged=false;
	private int pagePosition, day, month, year;
	
	private static final String[] PROJECTION =
	new String[] {
            FeedingEventsContract.NapEvents.column_timeStamp,
            FeedingEventsContract.NapEvents.column_dirty,
            FeedingEventsContract.NapEvents.column_wet,
            FeedingEventsContract.BreastPumpEvents._ID,
            FeedingEventsContract.BreastPumpEvents.column_note
    };

    // A label for the saved state of the activity
    private static final String ORIGINAL_CONTENTTS = "origContentTS";
    private static final String ORIGINAL_CONTENTLD = "origContentLD";
    private static final String ORIGINAL_CONTENTRD = "origContentRD";
    private static final String ORIGINAL_CONTENTLQ = "origContentLQ";
    private static final String ORIGINAL_CONTENTRQ = "origContentRQ";
    private static final String ORIGINAL_CONTENTNOTE="origContentNote";
    // This Activity can be started by more than one action. Each action is represented
    // as a "state" constant
    private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;

    // Global mutable variables
    private int mState;
    private Uri mUri;
    private Cursor mCursor;
    private EditText mText;
    private String OriginalContentTS;
    private String OriginalContentLD;
    private String OriginalContentRD;
    private String OriginalContentLQ;
    private String OriginalContentRQ;
    private String OriginalContentNote;
    
    private long id;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
       	WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		
		 final Intent intent = getIntent();
	     Bundle extras = getIntent().getExtras();
	     try{
	        	pagePosition=extras.getInt("position");
	        	
	        	day=extras.getInt("day");
	        	month=extras.getInt("month");
	        	year=extras.getInt("year");
	        }
	     catch(Exception e)
	        {
	        	
	        }
	     DateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
	 		
	   	 	Calendar calendar=Calendar.getInstance();
	   	 	calendar.set(Calendar.MONTH, month);
	   	 	calendar.set(Calendar.DAY_OF_MONTH, day);
	   	 	calendar.set(Calendar.YEAR, year);
		
	   	 	preSetTime = (Date)calendar.getTime();
		
		final String action = intent.getAction();
		setContentView(R.layout.activity_baby_nappy);
		TextView titleEdit=(TextView)findViewById(R.id.txtViewBNTitle);
		
		if (Intent.ACTION_EDIT.equals(action)) 
		 {
	            mState = STATE_EDIT;
	            mUri = intent.getData();
	            String s=getResources().getString(R.string.UpdateNappy);
	            titleEdit.setText(s);
	      } 
		 else if (Intent.ACTION_INSERT.equals(action)
	                || Intent.ACTION_PASTE.equals(action)) 
		 {
	            mState = STATE_INSERT;
	            mUri = getContentResolver().insert(intent.getData(), null);

	            if (mUri == null) 
	            {

	                //Log.e("BreastFeedingActivity", "Failed to insert new note into " + getIntent().getData());
	                finish();
	                return;
	            }
	            setResult(RESULT_OK, (new Intent()).setAction(mUri.toString()));

		        // If the action was other than EDIT or INSERT:
		   } 
		 else 
		 {
		        // Log.e("BreastFeedingActivity", "Unknown action, exiting");
		         finish();
		         return;
		  }
		mCursor = managedQuery(
	            mUri,         // The URI that gets multiple notes from the provider.
	            PROJECTION,   // A projection that returns the note ID and note content for each note.
	            null,         // No "where" clause selection criteria.
	            null,         // No "where" clause selection values.
	            null          // Use the default sort order (modification date, descending)
	        );


	        if (savedInstanceState != null) {
	            OriginalContentTS = savedInstanceState.getString(ORIGINAL_CONTENTTS);
	            OriginalContentLD = savedInstanceState.getString(ORIGINAL_CONTENTLD);
	            OriginalContentRD = savedInstanceState.getString(ORIGINAL_CONTENTRD);
	            OriginalContentLQ = savedInstanceState.getString(ORIGINAL_CONTENTLQ);
	            OriginalContentRQ = savedInstanceState.getString(ORIGINAL_CONTENTRQ);
	            OriginalContentNote=savedInstanceState.getString(ORIGINAL_CONTENTNOTE);
	        }
	        
	        
	    	mPickedTimeText=(EditText)findViewById(R.id.edStartTime);
	    	rdiWet=(RadioButton)findViewById(R.id.radio_wet);
	    	rdiDirty=(RadioButton)findViewById(R.id.radio_dirty);
	    	
	    	
	    	mPickedTimeText.setOnClickListener(new View.OnClickListener(){
	    		
	    		@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	    			 getWindow().setSoftInputMode(
	   	        	      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    			showDialog();
				}
	    		
	    		
	    	});
	   
	    	RadioGroup rg=(RadioGroup)findViewById(R.id.rg_nappy);
	    	rg.setOnCheckedChangeListener(this) ;
	    	
	    	
	    	 	if(mState==STATE_INSERT)
		    	{	
	    	 		 
		    		 
			    	 preSetTime = (Date)calendar.getTime();
		    	
			    	mPickedTimeText.setText(df.format(preSetTime));
		    	}
		    	if(mState==STATE_EDIT)
		    	{
		    		if(mCursor!=null)
		    		{
		    				mCursor.moveToFirst();
		    			
		    				long id;
		    				
		    				int tsColIndex=mCursor.getColumnIndexOrThrow("timeStamp");
		    				String timeStampStr=mCursor.getString(tsColIndex);
		    				Timestamp ts=Timestamp.valueOf(timeStampStr);
		    				savedTS=ts;
		    				Date dt=new Date(ts.getTime());
		    				SimpleDateFormat dff=new SimpleDateFormat("HH:mm dd/MM/yyyy");
		    				
		    				id=mCursor.getLong(mCursor.getColumnIndexOrThrow("_id"));
		    				
		    			
		    				int lqColIndex = mCursor.getColumnIndexOrThrow("wet");
		    				int weti=mCursor.getInt(lqColIndex);
		    				
		    				int rdColIndex = mCursor.getColumnIndexOrThrow("dirty");
		    				int dirtyi=mCursor.getInt(rdColIndex);
		    			
		    				mPickedTimeText.setText(dff.format(dt));
		    				if(weti==1)
		    					rdiWet.setChecked(true);
		    				if(dirtyi==1)
		    					rdiDirty.setChecked(true);
		    		}
		    	}
		    	
		    	
		    	Button btnSave=(Button)findViewById(R.id.buttonEnter);
		        btnSave.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
		            	String note="";
		            	
		            	
						//Log.i("BreastFeedingActivity::onClick", "CreateHelper");
							
						if(mPickedTimeText!=null)
		            	{
		            		if(timeChanged)
		            		{	if(newDate!=null)
		            				timeStamp=new Timestamp(newDate.getTime());
		            			
		            		}
		            		else
		            		{
		            			if(mState==STATE_EDIT)
		            			{
		            				timeStamp=savedTS;
		            			}
		            			if(mState==STATE_INSERT)
		            			{	//Long now = Long.valueOf(System.currentTimeMillis());
		            				timeStamp=new Timestamp(preSetTime.getTime());
		            			}
		            		}
		            			
		            		
		            	}
						
		            	Nappy nappy =new Nappy(timeStamp, wet,dirty, note);
		            	
		            	updateEvent(nappy);
		            	setResult(RESULT_OK);	
		            	finish();
					}
				});
		        
		        Button btnDelete=(Button)findViewById(R.id.buttonDel);
		        btnDelete.setOnClickListener(new View.OnClickListener() {
		        
		        	public void onClick(View v) {
		        		
		        		deleteEvent();
		        		 setResult(RESULT_OK);
		        		finish();
		        	}
		        
		        }); 
	}
	
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		wet=false; dirty=false;
		if(checkedId == R.id.radio_wet){
             wet=true;
        }
        if(checkedId == R.id.radio_dirty){
            dirty=true;
        }
        
    }
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
		wet=false; dirty=false;
		boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_dirty:
	            if (checked)
	                dirty=true;
	            break;
	        case R.id.radio_wet:
	            if (checked)
	                wet=true;
	            break;
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		
		return true;
	}
	
	void showDialog() {
        // Create the fragment and show it as a dialog.
		DFTimePicker dtf = DFTimePicker.newInstance();
        DialogFragment newFragment = dtf;
    	
        newFragment.show(getFragmentManager(), "dialog");
    }
	
	@Override
	public void onTimePicked(Calendar time)
	 {
	 // display the selected time in the TextView
		// DateFormat df = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
		time.set(Calendar.MONTH, month);
		time.set(Calendar.DAY_OF_MONTH, day);
		time.set(Calendar.YEAR, year);
		DateFormat df = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
		newDate = (Date)time.getTime();
		
		mPickedTimeText.setText(df.format(newDate));
		timeChanged=true;
		// mPickedTimeText.setText(Integer.toString(hour)+" "+Integer.toString(minute));
	 }
	
	 private final void deleteEvent() {
	        if (mCursor != null) {
	            mCursor.close();
	            mCursor = null;
	            getContentResolver().delete(mUri, null, null);
	         
	        }
	    }

	 private final void updateEvent(Nappy nappy) {

	        // Sets up a map to contain values to be updated in the provider.
	        ContentValues values = new ContentValues();
	     
	        values.put(FeedingEventsContract.NapEvents.column_timeStamp, nappy.ts.toString());
	        values.put(FeedingEventsContract.NapEvents.column_wet, nappy.wet );
	        values.put(FeedingEventsContract.NapEvents.column_dirty,nappy.dirty);
	       
	        values.put(FeedingEventsContract.NapEvents.column_note, nappy.note);
	      
	        getContentResolver().update(
	                mUri,    // The URI for the record to update.
	                values,  // The map of column names and new values to apply to them.
	                null,    // No selection criteria are used, so no where columns are necessary.
	                null     // No where columns are used, so no where arguments are necessary.
	            );

	       
	    } 
	 
	 
	 	@Override
	    public void onBackPressed() {
	        super.onBackPressed();   
	        if (mCursor != null) {
	            if (mState == STATE_EDIT) {
	                // Put the original note text back into the database
	                mCursor.close();
	           
	            } else if (mState == STATE_INSERT) {
	                // We inserted an empty note, make sure to delete it
	                deleteEvent();
	            }
	        }
	        setResult(RESULT_CANCELED);
	        finish();
			

	    }
	 
}
