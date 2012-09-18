package com.epam.AnotherSearch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class SettingsActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		mListView = (ListView)findViewById(R.id.lvSettings);
		mListView.setAdapter(new SettingsAdapter(this));
		
	}

	ListView mListView = null;
	
}
