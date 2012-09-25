package com.epam.search.searchable;
import android.database.Cursor;

import com.epam.search.data.Suggestion;
import com.epam.search.data.SuggestionProvider;
import com.epam.search.data.Suggestions;

class SearchableSuggestions implements Suggestions{
	
	public SearchableSuggestions(SearchableProvider provider, Cursor cursor) {
		super();
		mCursor = cursor;
		mProvider = provider;
	}
	
	public Cursor getCursor()
	{
		return mCursor;
	}
	
	public int getCount() {
		if(mCursor == null)
			return 0;
		return mCursor.getCount();
	}

	public Suggestion get(int i) {
		
		return new SearchableSuggestion(i, this);
	}
	
	private Cursor mCursor = null;
	private SearchableProvider mProvider = null;

	public SuggestionProvider getProvider() {
		return mProvider;
	}

}
