package com.projects.babyfeeding;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.projects.babyfeeding.DFTimePicker.TimePickedListener;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BreastPumpActivity extends Activity implements TimePickedListener
{
	Date newDate, preSetTime;
	private EditText mPickedTimeText;
	public  String tempTS;
	private EditText ldEdit, rdEdit, lqEdit,rqEdit;
	private EditText tsEdit;
	private int pagePosition,  day, month, year;;
	private Timestamp savedTS;
	private boolean timeChanged=false;
	private boolean lqChanged=false, rqChanged=false;
	private double lqSaved, rqSaved;
	
	private static final String[] PROJECTION =
	new String[] {
            FeedingEventsContract.BreastPumpEvents.column_timeStamp,
            FeedingEventsContract.BreastPumpEvents.column_leftDuration,
            FeedingEventsContract.BreastPumpEvents.column_leftVolumn,
            FeedingEventsContract.BreastPumpEvents.column_rightDuration,
            FeedingEventsContract.BreastPumpEvents.column_rightVolumn,
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
    private String ttp, vup;
    
    private long id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
       	WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		   
        vup= sharedPreferences.getString("Volumn_Unit", getResources().getString(R.string.volumn_unit_preferencesValue_default));
        ttp= sharedPreferences.getString("Time_Type", getResources().getString(R.string.time_type__preferencesValue_default));
		final Intent intent = getIntent();
		final String action = intent.getAction();
		setContentView(R.layout.activity_breast_pump);
		
		TextView titleEdit=(TextView)findViewById(R.id.txtViewBPTitle);
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
		DateFormat df12 = new SimpleDateFormat("hh:mm a dd/MM/yyyy");
 		
   	 	Calendar calendar=Calendar.getInstance();
   	 	calendar.set(Calendar.MONTH, month);
   	 	calendar.set(Calendar.DAY_OF_MONTH, day);
   	 	calendar.set(Calendar.YEAR, year);
	
   	 	preSetTime = (Date)calendar.getTime();
		
		if (Intent.ACTION_EDIT.equals(action)) 
		 {
	            mState = STATE_EDIT;
	            mUri = intent.getData();
	            String s=getResources().getString(R.string.UpdatePump);
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
	            OriginalContentLD = savedInstanceState.getString(ORIGINAL_CONTENTLD);
	            OriginalContentRD = savedInstanceState.getString(ORIGINAL_CONTENTRD);
	            OriginalContentLQ = savedInstanceState.getString(ORIGINAL_CONTENTLQ);
	            OriginalContentRQ = savedInstanceState.getString(ORIGINAL_CONTENTRQ);
	            OriginalContentNote=savedInstanceState.getString(ORIGINAL_CONTENTNOTE);
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

	    	 ldEdit=(EditText)findViewById(R.id.edLBreastDuration);
	    	 rdEdit=(EditText)findViewById(R.id.edRBreastDuration);
	    	 tsEdit=(EditText)findViewById(R.id.edStartTime);
	    	 lqEdit=(EditText)findViewById(R.id.ediLBreastQuantity);
	    	 rqEdit=(EditText)findViewById(R.id.edRBreastQuantity);  
	    	 
	    	 if(vup.equalsIgnoreCase("1"))
				{	
		    	 
		    			lqEdit.setHint(R.string.volumn_unit_milliliter);
		    			rqEdit.setHint(R.string.volumn_unit_milliliter);
				}
		    	if(vup.equalsIgnoreCase("2"))
				{	
		    	 
		    			lqEdit.setHint(R.string.volumn_unit_ounce);
		    			rqEdit.setHint(R.string.volumn_unit_ounce);
				}
		    	
		    	lqEdit.addTextChangedListener(new TextWatcher(){

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						lqChanged=true;
					}
		    		
		    	});
		    	
		    	rqEdit.addTextChangedListener(new TextWatcher(){

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						rqChanged=true;
					}
		    		
		    	});
	    	 
		    	if(mState==STATE_INSERT)
		    	{	
	    		 	preSetTime = (Date)calendar.getTime();
			    	if(ttp.equals("1"))
			    		mPickedTimeText.setText(df12.format(preSetTime));	
			    	else
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
		    				int ldColIndex = mCursor.getColumnIndexOrThrow("leftDuration");
		    				int lduration=mCursor.getInt(ldColIndex);
		    			
		    				int lqColIndex = mCursor.getColumnIndexOrThrow("leftVolumn");
		    				Double lquantity=mCursor.getDouble(lqColIndex);
		    				
		    				int rdColIndex = mCursor.getColumnIndexOrThrow("rightDuration");
		    				int rduration=mCursor.getInt(rdColIndex);
		    				
		    				int rqColIndex = mCursor.getColumnIndexOrThrow("rightVolumn");
		    				Double rquantity=mCursor.getDouble(rqColIndex);
		    				
		    				//tsEdit.setText(dff.format(dt));
		    				
		    				if(ttp.equalsIgnoreCase("1"))
		    					mPickedTimeText.setText(df12.format(dt));
		    				else
		    					mPickedTimeText.setText(df.format(dt));
		    				
		    				
		    				ldEdit.setText(Integer.toString(lduration));
		    				rdEdit.setText(Integer.toString(rduration));
		    				
		    		       
		    				if(vup.equalsIgnoreCase("1"))
		    				{	
		    					
		    					lqEdit.setText(Formatting.getDeci0().format(lquantity));
			    				rqEdit.setText(Formatting.getDeci0().format(rquantity));
			    				
		    				}
		    				if(vup.equalsIgnoreCase("2"))
		    				{	
		    					Double templq=lquantity/30.00;
		    					Double temprq=rquantity/30.00;
		    					lqEdit.setText(Formatting.getDeci2().format(templq));
			    				rqEdit.setText(Formatting.getDeci2().format(temprq));
		    				}
		    				
		    				lqSaved=lquantity;
		    				rqSaved=rquantity;
		    				lqChanged=false;
		    				rqChanged=false;
		    		
		    			}
		    	}
		    	
		    	
		    	Button btnSave=(Button)findViewById(R.id.buttonEnter);
		        btnSave.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Boolean isBlank=false;
					
		            	int ldi=0;
		            	int rdi=0;
		            	double lqd=0;
		            	double rqd=0;
		            	String note="";
		            	Timestamp timeStamp=null;
		            	
							
						if(tsEdit!=null)
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
						
						if(ldEdit!=null)
		            	{	String lds=ldEdit.getText().toString();
		            		ldi= InputsValidation.intValidation(lds);
		            		//Log.i("onClicked-rb:","rb1");
		            	}
		            
		            	
		            	if(rdEdit!=null)
		            	{	String rds=rdEdit.getText().toString();
		            		rdi= InputsValidation.intValidation(rds);
		            		//Log.i("onClicked-rb:","rb2");
		            	}
		            
		      
		            	if(lqEdit!=null)
		            	{
		            		
		            		if(lqChanged)
		            		{
			            		String lqs=lqEdit.getText().toString();
			            		
			            		lqd= (double) InputsValidation.doubleValidation(lqs);
			            		if(vup.equalsIgnoreCase("2"))
			        			{
			            			lqd=lqd*30.00;
			        			}
		            		}
		            		else
		            		{
		            			lqd=lqSaved;
		            		}
		            		
		            		
		            	}
		            	if(rqEdit!=null)
		            	{
		            		if(rqChanged)
		            		{
			            		String rqs=rqEdit.getText().toString();
			            		
			            		rqd= (double) InputsValidation.doubleValidation(rqs);
			            		if(vup.equalsIgnoreCase("2"))
			        			{
			            			rqd=rqd*30.00;
			        			}
		            		}
		            		else
		            		{
		            			rqd=rqSaved;
		            		}
		            	}
		            	
		            	
		            	Pump pump =new Pump(timeStamp, ldi, rdi, lqd, rqd, note);
		            	
		            	updateEvent(pump);
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
	
	 @Override
	 protected void onResume() {
	        super.onResume();
	        lqChanged=false;
			rqChanged=false;
	 }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.breast_pump, menu);
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
		DateFormat df12 = new SimpleDateFormat("hh:mm a dd/MM/yyyy");
		newDate = (Date)time.getTime();
		if(ttp.equalsIgnoreCase("1"))
		{
			mPickedTimeText.setText(df12.format(newDate));
			timeChanged=true;
		}
		else
		{
			mPickedTimeText.setText(df.format(newDate));
			timeChanged=true;
		}
	
	 }
	
	 private final void deleteEvent() {
	        if (mCursor != null) {
	            mCursor.close();
	            mCursor = null;
	            getContentResolver().delete(mUri, null, null);
	         
	        }
	    }
	 
	 
	 
	 private final void updateEvent(Pump pump) {

	        // Sets up a map to contain values to be updated in the provider.
	        ContentValues values = new ContentValues();
	      // values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, System.currentTimeMillis());

	        // This puts the desired notes text into the map.
	        values.put(FeedingEventsContract.BreastPumpEvents.column_timeStamp, pump.ts.toString());
	        values.put(FeedingEventsContract.BreastPumpEvents.column_leftDuration, Integer.toString(pump.lDuration) );
	        values.put(FeedingEventsContract.BreastPumpEvents.column_rightDuration, Integer.toString(pump.rDuration));
	        values.put(FeedingEventsContract.BreastPumpEvents.column_leftVolumn, Double.toString(pump.LQuantity));
	        values.put(FeedingEventsContract.BreastPumpEvents.column_rightVolumn, Double.toString(pump.RQuantity));
	        values.put(FeedingEventsContract.BreastPumpEvents.column_note, pump.note);
	      
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
