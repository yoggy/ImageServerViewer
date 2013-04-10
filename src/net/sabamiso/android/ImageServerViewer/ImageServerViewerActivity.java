package net.sabamiso.android.ImageServerViewer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

public class ImageServerViewerActivity extends Activity implements HTTPImageGetThreadUpdateListener{

	HTTPImageGetThread http_thread;
	ImageServerViewerView view;
	SharedPreferences pref;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		pref = PreferenceManager.getDefaultSharedPreferences(this);

		super.onCreate(savedInstanceState);
		view = new ImageServerViewerView(this);
		view.setURI(pref.getString("url", getString(R.string.url_default_value)));
		setContentView(view);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		http_thread = new HTTPImageGetThread();
		http_thread.setUpdateListener(this);
		http_thread.setUrl(pref.getString("url", getString(R.string.url_default_value)));
		http_thread.start();
	}

	@Override
	public void onPause() {
		http_thread.stopThread();
		http_thread = null;
		
		super.onPause();
		finish();
	}

	@Override
	public void update(HTTPImageGetThread thread) {
		Bitmap bmp = thread.getBitmap();
		view.setBitmap(bmp);
		view.setLastStatus(thread.getStatusCode());
	}
}
