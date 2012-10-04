package com.epam.AnotherSearch;

import android.content.Context;
import android.content.SharedPreferences;

import com.epam.search.SearchSettings;
import com.epam.search.data.SuggestionProvider;

public class Settings implements SearchSettings {

	public static final String SETTINGS_PREF_FILE_NAME = "AnotherSearch.settings";
	public Settings(Context context)
	{
		mContext = context;
	}
		
	public boolean isProviderOn(SuggestionProvider provider) {
		 SharedPreferences preferences = mContext.getSharedPreferences(SETTINGS_PREF_FILE_NAME,
				 Context.MODE_PRIVATE);
		 return preferences.getBoolean(provider.getKey(), true);
	}

	public void swithcProviderOn(SuggestionProvider provider, boolean isOn) {
		SharedPreferences preferences = mContext.getSharedPreferences(SETTINGS_PREF_FILE_NAME,
				 Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(provider.getKey(), isOn); 
		editor.commit();
		}

	private Context mContext = null;
}
