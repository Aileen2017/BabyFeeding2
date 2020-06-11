package com.projects.babyfeeding2020;

import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.projects.babyfeeding2020.R;
import com.projects.babyfeeding2020.DFTimePicker.TimePickedListener;

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

public class BreastFeedingActivity extends Activity implements TimePickedListener
{
	private EditText mPickedTimeText;
	public  String tempTS, timeStampStr;
	Date newDate, preSetTime;
	private EditText ldEdit, rdEdit, bqEdit,eqEdit;//, tsEdit;
	private int pagePosition, day, month, year;
	private Calendar calendar;
	private Timestamp savedTS;
	private boolean bqChanged=false, eqChanged=false;
	private double bqSaved, eqSaved;
	
    private static final String[] PROJECTION =
            new String[] {
                FeedingEventsContract.Feeds.column_timeStamp,
                FeedingEventsContract.Feeds.column_leftDuration,
                FeedingEventsContract.Feeds.column_rightDuration,
                FeedingEventsContract.Feeds.column_bottleFeed,
                FeedingEventsContract.Feeds.column_expressFeed,
                FeedingEventsContract.Feeds._ID,
                FeedingEventsContract.Feeds.column_note
        };

        // A label for the saved state of the activity
        private static final String ORIGINAL_CONTENTTS = "origContentTS";
        private static final String ORIGINAL_CONTENTLD = "origContentLD";
        private static final String ORIGINAL_CONTENTRD = "origContentRD";
        private static final String ORIGINAL_CONTENTBQ = "origContentBQ";
        private static final String ORIGINAL_CONTENTEQ = "origContentEQ";
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
        private String OriginalContentBQ;
        private String OriginalContentEQ;
        private String OriginalContentNote;
        private boolean timeChanged=false;
        private long id;
        private String ttp, vup;
	
	void showDialog() {
        // Create the fragment and show it as a dialog.
		DFTimePicker dtf = DFTimePicker.newInstance();
        DialogFragment newFragment = dtf;
    	
        newFragment.show(getFragmentManager(), "dialog");
    }
	
	@Override
	public void onNewIntent(Intent newIntent)
	{
		setIntent(newIntent);
	}
	
	 @Override
	 public void onTimePicked(Calendar time)
	 {
			
			time.set(Calendar.MONTH, month);
			time.set(Calendar.DAY_OF_MONTH, day);
			time.set(Calendar.YEAR, year);
        
			DateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
			DateFormat dff = new SimpleDateFormat("hh:mm a dd/MM/yyyy");
		
			newDate = (Date)time.getTime();
			if(ttp.equalsIgnoreCase("1"))
			{
				mPickedTimeText.setText(dff.format(newDate));
				timeChanged=true;
			}
			else
			{
				mPickedTimeText.setText(df.format(newDate));
				timeChanged=true;
			}
			
		
	 }
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_breastfeeding2);	
	        /*
	         * Creates an Intent to use when the Activity object's result is sent back to the
	         * caller.
	         */
	        getWindow().setSoftInputMode(
	        	      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	        
	        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			   
	        vup= sharedPreferences.getString("Volumn_Unit", getResources().getString(R.string.volumn_unit_preferencesValue_default));
	        ttp= sharedPreferences.getString("Time_Type", getResources().getString(R.string.time_type__preferencesValue_default));
		
	  
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
	        	System.out.println(e.toString());
	        }
	        
	   	 	DateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
	   	 	DateFormat df12 = new SimpleDateFormat("hh:mm a dd/MM/yyyy");
 		
	   	 	Calendar calendar=Calendar.getInstance();
	   	 	calendar.set(Calendar.MONTH, month);
	   	 	calendar.set(Calendar.DAY_OF_MONTH, day);
	   	 	calendar.set(Calendar.YEAR, year);
		
	   	 	preSetTime = (Date)calendar.getTime();
	        /*
	         *  Sets up for the edit, based on the action specified for the incoming Intent.
	         */

	        // Gets the action that triggered the intent filter for this Activity
	        final String action = intent.getAction();
	        TextView titleEdit=(TextView)findViewById(R.id.txtViewBFTitle);
	        // For an edit action:
	        if (Intent.ACTION_EDIT.equals(action)) {

	            // Sets the Activity state to EDIT, and gets the URI for the data to be edited.
	            mState = STATE_EDIT;
	            mUri = intent.getData();
	            String s=getResources().getString(R.string.UpdateFeed);
	            titleEdit.setText(s);
	            // For an insert or paste action:
	        } 
	        else if (Intent.ACTION_INSERT.equals(action)
	                || Intent.ACTION_PASTE.equals(action)) 
	        {

	            // Sets the Activity state to INSERT, gets the general note URI, and inserts an
	            // empty record in the provider
	            mState = STATE_INSERT;
	            mUri = getContentResolver().insert(intent.getData(), null);

	            /*
	             * If the attempt to insert the new note fails, shuts down this Activity. The
	             * originating Activity receives back RESULT_CANCELED if it requested a result.
	             * Logs that the insert failed.
	             */
	            if (mUri == null) {

	                // Writes the log identifier, a message, and the URI that failed.
	               // Log.e("BreastFeedingActivity", "Failed to insert new note into " + getIntent().getData());

	                // Closes the activity.
	                finish();
	                return;
	            }
	          //  id= Integer.parseInt(mUri.getPathSegments().get(FeedingEventsContract.Feeds.FEEDS_ID_PATH_POSITION));
	            // Since the new entry was created, this sets the result to be returned
	            // set the result to be returned.
	            setResult(RESULT_OK, (new Intent()).setAction(mUri.toString()));

	        // If the action was other than EDIT or INSERT:
	        } 
	        else 
	        {

	            // Logs an error that the action was not understood, finishes the Activity, and
	            // returns RESULT_CANCELED to an originating Activity.
	            //Log.e("BreastFeedingActivity", "Unknown action, exiting");
	            finish();
	            return;
	        }

	        /*
	         * Using the URI passed in with the triggering Intent, gets the note or notes in
	         * the provider.
	         * Note: This is being done on the UI thread. It will block the thread until the query
	         * completes. In a sample app, going against a simple provider based on a local database,
	         * the block will be momentary, but in a real app you should use
	         * android.content.AsyncQueryHandler or android.os.AsyncTask.
	         */
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

	        /*
	         * If this Activity had stopped previously, its state was written the ORIGINAL_CONTENT
	         * location in the saved Instance state. This gets the state.
	         */
	        if (savedInstanceState != null) {
	            OriginalContentTS = savedInstanceState.getString(ORIGINAL_CONTENTTS);
	            OriginalContentLD = savedInstanceState.getString(ORIGINAL_CONTENTLD);
	            OriginalContentRD = savedInstanceState.getString(ORIGINAL_CONTENTRD);
	            OriginalContentBQ = savedInstanceState.getString(ORIGINAL_CONTENTBQ);
	            OriginalContentEQ = savedInstanceState.getString(ORIGINAL_CONTENTEQ);
	            OriginalContentNote=savedInstanceState.getString(ORIGINAL_CONTENTNOTE);
	        }
	      
			
			
	    	//final EditText tsEdit;
	    	mPickedTimeText=(EditText)findViewById(R.id.txtTimePicker);
	    	
	    	mPickedTimeText.setOnClickListener(new View.OnClickListener(){
	    		
	    		@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	    			 getWindow().setSoftInputMode(
	   	        	      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    			showDialog();
				}

	    	});
	    	 ldEdit=(EditText)findViewById(R.id.inputLDuration);
	    	 rdEdit=(EditText)findViewById(R.id.inputRDuration);
	    	 //tsEdit=(EditText)findViewById(R.id.txtTimePicker);
	    	 bqEdit=(EditText)findViewById(R.id.inputBotQuantity);
	    	 eqEdit=(EditText)findViewById(R.id.inputEFQuantity);
	    	
	    	/* EditText editTextBot=(EditText)findViewById(R.id.inputBotQuantity);
	    	 EditText editTextEF=(EditText)findViewById(R.id.inputEFQuantity);*/
	    	 
	    	 
	    	 
	    	if(vup.equalsIgnoreCase("1"))
			{	
	    	 
	    			bqEdit.setHint(R.string.volumn_unit_milliliter);
	    			eqEdit.setHint(R.string.volumn_unit_milliliter);
			}
	    	if(vup.equalsIgnoreCase("2"))
			{	
	    	 
	    			bqEdit.setHint(R.string.volumn_unit_ounce);
	    			eqEdit.setHint(R.string.volumn_unit_ounce);
			}
	    	
	    	bqEdit.addTextChangedListener(new TextWatcher(){

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
					bqChanged=true;
				}
	    		
	    	});
	    	
	    	eqEdit.addTextChangedListener(new TextWatcher(){

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
					eqChanged=true;
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
	    				String eventType;
	    				
	    				int tsColIndex=mCursor.getColumnIndexOrThrow("timeStamp");
	    				String timeStampStr=mCursor.getString(tsColIndex);
	    				Timestamp ts=Timestamp.valueOf(timeStampStr);
	    				savedTS=ts;
	    				Date dt=new Date(ts.getTime());
	    				SimpleDateFormat dff=new SimpleDateFormat("HH:mm dd/MM/yyyy");
	    				SimpleDateFormat dff12=new SimpleDateFormat("hh:mm a dd/MM/yyyy");
	    				id=mCursor.getLong(mCursor.getColumnIndexOrThrow("_id"));
	    				int ldColIndex = mCursor.getColumnIndexOrThrow("leftDuration");
	    				int lduration=mCursor.getInt(ldColIndex);
	    				
	    				int rdColIndex = mCursor.getColumnIndexOrThrow("rightDuration");
	    				int rduration=mCursor.getInt(rdColIndex);
	    			
	    				int rfColIndex=mCursor.getColumnIndexOrThrow("bottleQuantity");
	    				double bq=mCursor.getDouble(rfColIndex);
	    				
	    				int lfColIndex=mCursor.getColumnIndexOrThrow("EFQuantity");
	    				double eq=mCursor.getDouble(lfColIndex);
	    				
	    				if(ttp.equalsIgnoreCase("1"))
	    					mPickedTimeText.setText(dff12.format(dt));
	    				else
	    					mPickedTimeText.setText(dff.format(dt));
	    					
	    				ldEdit.setText(Integer.toString(lduration));
	    				rdEdit.setText(Integer.toString(rduration));
	    				
	    	
	    				if(vup.equalsIgnoreCase("1"))
	    				{	
	    				
	    					bqEdit.setText(Formatting.getDeci0().format(bq));
	    					eqEdit.setText(Formatting.getDeci0().format(eq));
	    					
	    				}
	    				if(vup.equalsIgnoreCase("2"))
	    				{	
	    					Double tempbq=bq/30.00;
	    					Double tempeq=eq/30.00;
	    					bqEdit.setText(Formatting.getDeci2().format(tempbq));
	    					eqEdit.setText(Formatting.getDeci2().format(tempeq));
	    				}
	    				bqSaved=bq;
	    				eqSaved=eq;
	    				bqChanged=false;
	    				eqChanged=false;
	    			}
	    	}

	    	
	    	
	    	
	    	
	    	/*Button btnCancel=(Button)findViewById(R.id.btnBFCancel);
	    	btnCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
			        if (mCursor != null) {
			            if (mState == STATE_EDIT) {
			                // Put the original note text back into the database
			                mCursor.close();
			                mCursor = null;
			                ContentValues values = new ContentValues();
			                values.put(FeedingEventsContract.Feeds.column_timeStamp, OriginalContentTS);
			                values.put(FeedingEventsContract.Feeds.column_leftDuration, OriginalContentLD);
			                values.put(FeedingEventsContract.Feeds.column_rightDuration, OriginalContentRD);
			                values.put(FeedingEventsContract.Feeds.column_bottleFeed, OriginalContentBQ);
			                values.put(FeedingEventsContract.Feeds.column_expressFeed, OriginalContentEQ);
			                values.put(FeedingEventsContract.Feeds.column_note, OriginalContentNote);
			                getContentResolver().update(mUri, values, null, null);
			              
			                
			            } else if (mState == STATE_INSERT) {
			                // We inserted an empty note, make sure to delete it
			                deleteEvent();
			            }
			        }
			        setResult(RESULT_CANCELED);
			        finish();
					
				}
			});*/
	    	
	    	
	    	Button btnSave=(Button)findViewById(R.id.btnSave);
	        btnSave.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Boolean isBlank=false;
				
	            	int ldi=0;
	            	int rdi=0;
	            	Double bqi=0.0;
	            	Double eqi=0.0;
	            	String note="";
	            	Timestamp timeStamp=null;
	            	
						
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
	            
	      
	            	if(bqEdit!=null)
	            	{
	            		if(bqChanged)
	            		{
		            		String bqs=bqEdit.getText().toString();
		            		
		            		bqi= (double) InputsValidation.doubleValidation(bqs);
		            		if(vup.equalsIgnoreCase("2"))
		        			{
		            			bqi=bqi*30.00;
		        			}
	            		}
	            		else
	            		{
	            			bqi=bqSaved;
	            		}
	            		
	            	}
	            	if(eqEdit!=null)
	            	{
	            		if(eqChanged)
	            		{
		            		String eqs=eqEdit.getText().toString();
		            		eqi= (double) InputsValidation.doubleValidation(eqs);
		            		if(vup.equalsIgnoreCase("2"))
		        			{
		            			eqi=eqi*30.00;
		        			}
	            		}
	            		else
	            		{
	            			eqi=eqSaved;
	            		}
	            	}
	            	
	            	
	            	Feed feed=new Feed(timeStamp, ldi, rdi, bqi, eqi, note);
	            	
	            	updateEvent(feed);
	            	setResult(RESULT_OK);	
	            	finish();
				}
			});
	        
	        Button btnDelete=(Button)findViewById(R.id.btnBFDelete);
	        btnDelete.setOnClickListener(new View.OnClickListener() {
	        
	        	public void onClick(View v) {
	        		
	        		deleteEvent();
	        		 setResult(RESULT_OK);
	        		finish();
	        	}
	        
	        });
	        
	    }
	 
	 
	 /**
	     * This method is called when the Activity is about to come to the foreground. This happens
	     * when the Activity comes to the top of the task stack, OR when it is first starting.
	     *
	     * Moves to the first note in the list, sets an appropriate title for the action chosen by
	     * the user, puts the note contents into the TextView, and saves the original text as a
	     * backup.
	     */
	    @Override
	    protected void onResume() {
	        super.onResume();
	        bqChanged=false;
			eqChanged=false;
	        /*
	         * mCursor is initialized, since onCreate() always precedes onResume for any running
	         * process. This tests that it's not null, since it should always contain data.
	         */
	       /* if (mCursor != null) {
	            // Requery in case something changed while paused (such as the title)
	            mCursor.requery();

	             Moves to the first record. Always call moveToFirst() before accessing data in
	             * a Cursor for the first time. The semantics of using a Cursor are that when it is
	             * created, its internal index is pointing to a "place" immediately before the first
	             * record.
	             
	            Log.i("onResume:", Integer.toString(mCursor.getCount()));
	            mCursor.moveToFirst();
	            Log.i("onResume:", Integer.toString(mCursor.getCount()));
	            
	            // Modifies the window title for the Activity according to the current Activity state.
	            if (mState == STATE_EDIT) {
	                // Set the title of the Activity to include the note title
	                int colTSIndex = mCursor.getColumnIndex(FeedingEventsContract.Feeds.column_timeStamp);
	                String strTS = mCursor.getString(colTSIndex);
	                
	                //Resources res = getResources();
	                //String text = String.format(res.getString(R.string.title_edit), title);
	             
	                EditText txtTP=(EditText)findViewById(R.id.txtTimePicker1);
	                txtTP.setText(strTS);
	                
	                int colLDIndex = mCursor.getColumnIndex(FeedingEventsContract.Feeds.column_leftDuration);
	                String strLD = mCursor.getString(colLDIndex);
	                EditText txtLD=(EditText)findViewById(R.id.inputLDuration);
	                txtLD.setText(strLD);
	                
	                int colRDIndex = mCursor.getColumnIndex(FeedingEventsContract.Feeds.column_rightDuration);
	                String strRD = mCursor.getString(colRDIndex);
	                EditText txtRD=(EditText)findViewById(R.id.inputRDuration);
	                txtRD.setText(strRD);
	                
	                int colBQIndex = mCursor.getColumnIndex(FeedingEventsContract.Feeds.column_bottleFeed);
	                String strBQ = mCursor.getString(colBQIndex);
	                EditText txtBQ=(EditText)findViewById(R.id.inputBotQuantity);
	                txtBQ.setText(strBQ);
	               
	                int colEQIndex = mCursor.getColumnIndex(FeedingEventsContract.Feeds.column_expressFeed);
	                String strEQ = mCursor.getString(colEQIndex);
	                EditText txtEQ=(EditText)findViewById(R.id.inputEFQuantity);
	                txtEQ.setText(strEQ);
	                
	                int colNoteIndex = mCursor.getColumnIndex(FeedingEventsContract.Feeds.column_note);
	                String strNote = mCursor.getString(colNoteIndex);
	              //  EditText txtNote=(EditText)findViewById(R.id.inputEFQuantity);
	               // txtEQ.setText(strNote);
	                
	                if (OriginalContentTS == null) {
		                OriginalContentTS = strTS;
		             }
	                if(OriginalContentLD==null){
	                	OriginalContentLD=strLD;
	                }
	                if(OriginalContentRD==null){
	                	OriginalContentRD=strRD;
	                }
	                if(OriginalContentBQ==null){
	                	OriginalContentBQ=strBQ;
	                }
	                if(OriginalContentEQ==null){
	                	OriginalContentEQ=strEQ;
	                }
	                if(OriginalContentNote==null){
	                	OriginalContentNote=strNote;
	                }
	            } 
	             else if (mState == STATE_INSERT) {
	                //setTitle(getText(R.string.title_create));
	            }*/

	        /*
	         * Something is wrong. The Cursor should always contain data. Report an error in the
	         * note.
	         */
	    /*   else {
	           // setTitle(getText(R.string.error_title));
	           // mText.setText(getText(R.string.error_message));
	        }
	    }*/
	  }
	 
	 private final void deleteEvent() {
	        if (mCursor != null) {
	            mCursor.close();
	            mCursor = null;
	            getContentResolver().delete(mUri, null, null);
	         
	        }
	    }
	 
	 
	 
	 private final void updateEvent(Feed feed) {

	        // Sets up a map to contain values to be updated in the provider.
	        ContentValues values = new ContentValues();
	      // values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, System.currentTimeMillis());

	        // This puts the desired notes text into the map.
	        values.put(FeedingEventsContract.Feeds.column_timeStamp, feed.ts.toString());
	        values.put(FeedingEventsContract.Feeds.column_leftDuration, Integer.toString(feed.lDuration) );
	        values.put(FeedingEventsContract.Feeds.column_rightDuration, Integer.toString(feed.rDuration));
	        values.put(FeedingEventsContract.Feeds.column_bottleFeed, Double.toString(feed.BFQuantity));
	        values.put(FeedingEventsContract.Feeds.column_expressFeed, Double.toString(feed.EFQuantity));
	        values.put(FeedingEventsContract.Feeds.column_note, feed.note);
	      
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.breast_feeding, menu);
		return true;
	}


	public void onBackPressed()
	{
        if (mCursor != null) {
            if (mState == STATE_EDIT) {
                // Put the original note text back into the database
                mCursor.close();
                mCursor = null;
             /*   ContentValues values = new ContentValues();
                values.put(FeedingEventsContract.Feeds.column_timeStamp, OriginalContentTS);
                values.put(FeedingEventsContract.Feeds.column_leftDuration, OriginalContentLD);
                values.put(FeedingEventsContract.Feeds.column_rightDuration, OriginalContentRD);
                values.put(FeedingEventsContract.Feeds.column_bottleFeed, OriginalContentBQ);
                values.put(FeedingEventsContract.Feeds.column_expressFeed, OriginalContentEQ);
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
