package com.projects.babyfeeding;


import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MyMediaConnectorClient implements MediaScannerConnectionClient {
	
	String filePath;
	MediaScannerConnection mediaScannerConnection;

	public MyMediaConnectorClient(String filePath) {
		this.filePath = filePath;
	}

	public void setScanner(MediaScannerConnection msc){
		mediaScannerConnection = msc;
	}

	@Override
	public void onMediaScannerConnected() {
		mediaScannerConnection.scanFile(filePath, null);
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
	    if(path.equals(filePath))
	    	mediaScannerConnection.disconnect();
	}

	
	
	

}
