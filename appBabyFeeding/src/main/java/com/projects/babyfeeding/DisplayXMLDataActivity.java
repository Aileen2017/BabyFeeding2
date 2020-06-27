package com.projects.babyfeeding;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class DisplayXMLDataActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_xmldata);
		TextView tv=(TextView)findViewById(R.id.txt1);
		Bundle bd= getIntent().getExtras();
		String xml=getIntent().getStringExtra("xmlText");
		tv.setText(xml);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.display_xmldata, menu);
		return true;
	}

}
