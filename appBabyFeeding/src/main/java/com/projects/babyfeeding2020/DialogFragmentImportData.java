package com.projects.babyfeeding2020;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;

public class DialogFragmentImportData extends DialogFragment
{
	int mNum;
	
	
	
	static DialogFragment newInstance(String content) 
	{
        DialogFragmentImportData f = new DialogFragmentImportData();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        
        args.putString("content", content);
        f.setArguments(args);
        
        return f;
    }
	

	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    
	        // Pick a style based on the num.
	        int style = DialogFragment.STYLE_NORMAL, theme = 0;
	        switch ((mNum-1)%6) {
	            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
	            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
	            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
	            case 4: style = DialogFragment.STYLE_NORMAL; break;
	            case 5: style = DialogFragment.STYLE_NORMAL; break;
	            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
	            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
	            case 8: style = DialogFragment.STYLE_NORMAL; break;
	        }
	        switch ((mNum-1)%6) {
	            case 4: theme = android.R.style.Theme_Holo; break;
	            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
	            case 6: theme = android.R.style.Theme_Holo_Light; break;
	            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
	            case 8: theme = android.R.style.Theme_Holo_Light; break;
	        }
	        setStyle(R.style.BreastFeedingDialog, R.style.BreastFeedingDialog);
	    }
	 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.dialog_importdata, container, false);
	        View tv = v.findViewById(R.id.txt1);
	       
	        ((TextView)tv).setText(getArguments().getString("content"));

	        return v;
	    }



}
