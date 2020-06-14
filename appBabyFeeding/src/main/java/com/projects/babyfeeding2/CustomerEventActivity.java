package com.projects.babyfeeding2;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.projects.babyfeeding2.DFTimePicker.TimePickedListener;

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
import android.widget.TextView;

public class CustomerEventActivity extends Activity implements TimePickedListener
{

	Date newDate, preSetTime;
	private EditText mPickedTimeText;
	public  String tempTS;
	private EditText edTitle, edDesc;
	
	private int pagePosition,  day, month, year;
	private Timestamp savedTS;
	private boolean timeChanged=false;
	
	private static final String[] PROJECTION =
	new String[] {
            FeedingEventsContract.CustomerEvents.column_timeStamp,
            FeedingEventsContract.CustomerEvents.column_title,
            FeedingEventsContract.CustomerEvents.column_description,
   
            FeedingEventsContract.CustomerEvents.column_id,
          
    };

    // A label for the saved state of the activity
    private static final String ORIGINAL_CONTENTTS = "origContentTS";
    private static final String ORIGINAL_CONTENTTI = "origContentTI";
    private static final String ORIGINAL_CONTENTDES = "origContentDES";

    // This Activity can be started by more than one action. Each action is represented
    // as a "state" constant
    private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;

    // Global mutable variables
    private int mState;
    private Uri mUri;
    private Cursor mCursor;
    private EditText mText;
    private String OriginalContentTI;
    private String OriginalContentDES;
    private String OriginalContentTS;

    
    private long id;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
       	WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		final Intent intent = getIntent();
		final String action = intent.getAction();
		setContentView(R.layout.activity_customer_event);
		
		TextView titleEdit=(TextView)findViewById(R.id.txtViewCETitle);
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
		
		if (Intent.ACTION_EDIT.equals(action)) 
		 {
	            mState = STATE_EDIT;
	            mUri = intent.getData();
	            String s=getResources().getString(R.string.UpdateCE);
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

	        // For a paste, initializes the data from clipboard.
	        // (Must be done after mCursor is initialized.)
	        if (Intent.ACTION_PASTE.equals(action)) {
	            // Does the paste
	        //    performPaste();
	            // Switches the state to EDIT so the title can be modified.
	            mState = STATE_EDIT;
	        }


	        if (savedInstanceState != null) {
	            OriginalContentTS = savedInstanceState.getString(ORIGINAL_CONTENTTS);
	            OriginalContentTI = savedInstanceState.getString(ORIGINAL_CONTENTTI);
	            OriginalContentDES = savedInstanceState.getString(ORIGINAL_CONTENTDES);
	        
	        }
	        
	        
	    	mPickedTimeText=(EditText)findViewById(R.id.edStartTime);
	    	
	    	mPickedTimeText.setOnClickListener(new View.OnClickListener(){
	    		
	    		@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	    			 getWindow().setSoftInputMode(
	   	        	      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    			showDialog();
				}
	    		
	    		
	    	});

	    	 edTitle=(EditText)findViewById(R.id.inputEventTitle);
	    	 edDesc=(EditText)findViewById(R.id.inputEventDesc);
	    	
	    	 
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
		    				int tiColIndex = mCursor.getColumnIndexOrThrow("title");
		    				String tis=mCursor.getString(tiColIndex);
		    			
		    				int descColIndex = mCursor.getColumnIndexOrThrow("description");
		    				String descs=mCursor.getString(descColIndex);
		    		
		    				 mPickedTimeText.setText(dff.format(dt));
		    				edTitle.setText(tis);
		    				edDesc.setText(descs);
		    			
		    			}
		    	}
		    	
		    	
		    	Button btnSave=(Button)findViewById(R.id.btnCESave);
		        btnSave.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Boolean isBlank=false;
						String title="", desc="";
					/*
		            	int ldi=0;
		            	int rdi=0;
		            	int bqi=0;
		            	int eqi=0;*/
		            	
		            	Timestamp timeStamp=null;
		            	
						//Log.i("BreastFeedingActivity::onClick", "CreateHelper");
							
						if(mPickedTimeText!=null)
		            	{
		            		if(timeChanged)
		            		{	if(newDate!=null)
		            				timeStamp=new Timestamp(newDate.getTime());
		            		}
		            		else
		            		{	//Long now = Long.valueOf(System.currentTimeMillis());
		            			if(mState==STATE_INSERT)
		            				timeStamp=new Timestamp(preSetTime.getTime());
		            			if(mState==STATE_EDIT)
		            				timeStamp=savedTS;
		            		
		            		}
		            		
		            	}
						
						if(edTitle!=null)
		            	{	title=edTitle.getText().toString();
		            		//ldi= InputsValidation.intValidation(lds);
		            		//Log.i("onClicked-rb:","rb1");
		            	}
		            
		            	
		            	if(edDesc!=null)
		            	{	desc=edDesc.getText().toString();
		            		//rdi= InputsValidation.intValidation(rds);
		            		//Log.i("onClicked-rb:","rb2");
		            	}
		          	
		            	
		            	CustomerEvent ce =new CustomerEvent(timeStamp, title, desc);
		            	
		            	updateEvent(ce);
		            	setResult(RESULT_OK);	
		            	finish();
					}
				});
		        
		        Button btnDelete=(Button)findViewById(R.id.btnCEDelete);
		        btnDelete.setOnClickListener(new View.OnClickListener() {
		        
		        	public void onClick(View v) {
		        		
		        		deleteEvent();
		        		 setResult(RESULT_OK);
		        		finish();
		        	}
		        
		        }); 
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer_event, menu);
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
	  time.set(Calendar.MONTH, month);
		time.set(Calendar.DAY_OF_MONTH, day);
		time.set(Calendar.YEAR, year);
     
		DateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
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
	 
	 
	 
	 private final void updateEvent(CustomerEvent ce) {

	        // Sets up a map to contain values to be updated in the provider.
	        ContentValues values = new ContentValues();
	      // values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, System.currentTimeMillis());

	        // This puts the desired notes text into the map.
	        values.put(FeedingEventsContract.CustomerEvents.column_timeStamp, ce.ts.toString());
	        values.put(FeedingEventsContract.CustomerEvents.column_title, ce.title );
	        values.put(FeedingEventsContract.CustomerEvents.column_description,ce.description);
	       
	      
	        /*
	         * Updates the provider with the new values in the map. The ListView is updated
	         * automatically. The provider sets this up by setting the notification URI for
	         * query Cursor objects to the incoming URI. The content resolver is thus
	         * automatically notified when the Cursor for the URI changes, and the UI is
	         * updated.
	         * Note: This is being done on the UI thread. It will block the thread until the
	         * update completes. In a sample app, going against a simple provider based on a
	         * local database, the block will be momentary, but in a real app you should use
	         * android.content.AsyncQueryHandler or android.os.AsyncTask.
	         */
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
	               /* mCursor = null;
	                ContentValues values = new ContentValues();
	                values.put(FeedingEventsContract.BreastPumpEvents.column_timeStamp, OriginalContentTS);
	                values.put(FeedingEventsContract.BreastPumpEvents.column_leftDuration, OriginalContentLD);
	                values.put(FeedingEventsContract.BreastPumpEvents.column_rightDuration, OriginalContentRD);
	                values.put(FeedingEventsContract.BreastPumpEvents.column_leftVolumn, OriginalContentLQ);
	                values.put(FeedingEventsContract.BreastPumpEvents.column_rightVolumn, OriginalContentRQ);
	                values.put(FeedingEventsContract.Feeds.column_note, OriginalContentNote);
	                getContentResolver().update(mUri, values, null, null);*/
	              
	                
	            } else if (mState == STATE_INSERT) {
	                // We inserted an empty note, make sure to delete it
	                deleteEvent();
	            }
	        }
	        setResult(RESULT_CANCELED);
	        finish();
			

	    }

}
