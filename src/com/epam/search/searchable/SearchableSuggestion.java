package com.epam.search.searchable;

import android.app.SearchManager;
import android.database.Cursor;

import com.epam.search.data.Suggestion;
import com.epam.search.data.Suggestions;
import com.epam.search.util.IconObtainer;

class SearchableSuggestion implements Suggestion {

	public SearchableSuggestion(int position, SearchableSuggestions suggestions) {
		super();
		mPosition = position;
		mSuggestions = suggestions;
	}

	public String getFormat() {
		
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_FORMAT);
	}

	public IconObtainer getIcon1() {
		return new SearchableSuggestionIconObtainer(
				obtainCursorValue(SearchManager.SUGGEST_COLUMN_ICON_1));
	}

	public IconObtainer getIcon2() {
		return new SearchableSuggestionIconObtainer(
				obtainCursorValue(SearchManager.SUGGEST_COLUMN_ICON_2));
	}

	public String getIntentAction() {
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_INTENT_ACTION);
	}

	public String getIntentData() {
	
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_INTENT_DATA);
	}

	public String getIntentDataID() {
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
	}

	public String getIntentExtraData() {
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA);
	}

	
	public String getQuery() {
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_QUERY);
	}

	public String getShorcutID() {
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
	}

	public String getSpinnerWhileRefreshing() {
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_SPINNER_WHILE_REFRESHING);
	}

	public String getText1() {
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_TEXT_1);
	}

	public String getText2() {
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_TEXT_2);
	}

	public String getText2Url() {
		return obtainCursorValue(SearchManager.SUGGEST_COLUMN_TEXT_2_URL);
	}

	public Suggestions getSuggestions() {
		
		return mSuggestions;
	}
	
	public String obtainCursorValue(String key)
	{
		if(mPosition < 0 || mSuggestions.getCursor() == null || mSuggestions.getCount() <= 0)
		{
			return null;
		}
		
		try
		{
			Cursor cursor = mSuggestions.getCursor();
			cursor.moveToPosition(mPosition);
			int columnIndex = cursor.getColumnIndex(key);
			if (columnIndex >= 0)
			{
				return cursor.getString(columnIndex);
			}
			else
			{
				return null;
			}
			
		}catch (Exception e) {
			// when use cross process cursor there can be any exceptions
			e.printStackTrace();
			return null;
		}
		
	}
	
	private int mPosition = -1;
	private SearchableSuggestions mSuggestions = null;
}
