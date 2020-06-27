package com.projects.babyfeeding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DialogFragmentImportPrompt extends DialogFragment
{
	
	public DialogFragmentImportPrompt()
	{
		
	}
	
	
	static DialogFragment newInstance() 
	{
        DialogFragment f = new DialogFragmentImportPrompt();
 
        return f;
    }
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        int style = DialogFragment.STYLE_NORMAL, theme = 0;
	        //setStyle(style, theme);
	       setStyle(R.style.BreastFeedingDialog, R.style.BreastFeedingDialog);
	 }
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.dialog_importdatawarning, container, false);
	        View tv = v.findViewById(R.id.txt1);
	       
	       
	        Button btn1=(Button)v.findViewById(R.id.btnYes);
	        btn1.setOnClickListener(new View.OnClickListener(){
	        	//continue exporting data
	        	
	        	public void onClick(View v)
	        	{
		        	
	        		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		        	i.setType("*/*");
		        	i.addCategory(Intent.CATEGORY_OPENABLE);
		        	String titleImportDatas= getResources().getString(R.string.title_ImportData);
		        	startActivityForResult(Intent.createChooser(i,titleImportDatas), 1);
		        	dismiss();
	        	}
	        	
	        	
	        });
	        
	       
	        
	        

	        return v;
	    }


	 
	 
	 
	 
}
