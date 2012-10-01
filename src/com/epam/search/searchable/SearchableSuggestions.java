package com.epam.search.searchable;
import java.util.HashMap;
import java.util.Map;

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
		if(i < 0 || i >= getCount())
		{
			return null;
		}
		if(mSuggestionsCache.containsKey(i))
		{
			return mSuggestionsCache.get(i);
		}
		mSuggestionsCache.put(i, new SearchableSuggestion(i, this));
		return mSuggestionsCache.get(i);
	}
	
	private Cursor mCursor = null;
	private SearchableProvider mProvider = null;

	public SuggestionProvider getProvider() {
		return mProvider;
	}

	private Map<Integer, Suggestion> mSuggestionsCache = new HashMap<Integer, Suggestion>();
}
