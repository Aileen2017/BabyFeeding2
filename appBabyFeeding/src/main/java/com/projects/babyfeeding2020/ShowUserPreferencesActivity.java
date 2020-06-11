/*package com.projects.babyfeeding2020;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ShowUserPreferencesActivity extends Activity implements RadioGroup.OnCheckedChangeListener
{
	private RadioButton rdiMilli, rdiOunce;
	private boolean milli, ounce=false;
	 // A label for the saved state of the activity
    private static final String ORIGINAL_CONTENTTS = "origContentTS";
    private static final String ORIGINAL_CONTENTLD = "origContentLD";
   
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
   
    private long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		final String action = intent.getAction();
		setContentView(R.layout.activity_show_user_preferences);
		getWindow().setSoftInputMode(
		       	WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		
		rdiMilli=(RadioButton)findViewById(R.id.radio_milli);
    	rdiOunce=(RadioButton)findViewById(R.id.radio_ounce);
    	
    	
   
    	RadioGroup rg=(RadioGroup)findViewById(R.id.rg_nappy);
    	rg.setOnCheckedChangeListener(this) ;
    	

		if (Intent.ACTION_EDIT.equals(action)) 
		 {
	            mState = STATE_EDIT;
	            mUri = intent.getData();
	           
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
	            OriginalContentMILLI = savedInstanceState.getString(ORIGINAL_CONTENTMILLI);
	            OriginalContentOUNCE = savedInstanceState.getString(ORIGINAL_CONTENTOUNCE);
	           
	        }
	        
	        rg.setOnCheckedChangeListener(this) ;
	    	
	    	
    	 	
	    	if(mState==STATE_EDIT)
	    	{
	    		if(mCursor!=null)
	    		{
	    				mCursor.moveToFirst();
	    			
	    				long id;
	    				
	    				id=mCursor.getLong(mCursor.getColumnIndexOrThrow("_id"));
	    				
	    			
	    				int lqColIndex = mCursor.getColumnIndexOrThrow("wet");
	    				int millii=mCursor.getInt(lqColIndex);
	    				
	    				int rdColIndex = mCursor.getColumnIndexOrThrow("dirty");
	    				int ouncei=mCursor.getInt(rdColIndex);
	    			
	    				
	    				if(millii==1)
	    					rdiMilli.setChecked(true);
	    				if(ouncei==1)
	    					rdiOunce.setChecked(true);
	    		}
	    	}
	    	
	    	Button btnSave=(Button)findViewById(R.id.buttonEnter);
	        btnSave.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
	            	String note="";
	            	
					
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
	
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
		milli=false; ounce=false;
		boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_milli:
	            if (checked)
	                milli=true;
	            break;
	        case R.id.radio_ounce:
	            if (checked)
	                ounce=true;
	            break;
	    }
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
*/