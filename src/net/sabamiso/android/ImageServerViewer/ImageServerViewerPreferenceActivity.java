package net.sabamiso.android.ImageServerViewer;

import net.sabamiso.android.ImageServerViewer.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ImageServerViewerPreferenceActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		finish();
	}
}
