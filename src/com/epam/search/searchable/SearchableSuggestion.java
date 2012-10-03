package com.epam.search.searchable;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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
		if(mIcon1 == null)
		{
			 mIcon1 = new IconObtainer(getContext(), obtainCursorValue(SearchManager.SUGGEST_COLUMN_ICON_1)
						, getResourceOwner());
		}
		return mIcon1;
				
	}

	public IconObtainer getIcon2() {
		if(mIcon2 == null)
		{
			 mIcon2 = new IconObtainer(getContext(), obtainCursorValue(SearchManager.SUGGEST_COLUMN_ICON_2)
						, getResourceOwner());
		}
		return mIcon2;
	}

	public String getIntentAction() {
		String action = getSearchableProvider().getSearchableInfo().getSuggestIntentAction();
		 
		if(action == null)
		{
			action = obtainCursorValue(SearchManager.SUGGEST_COLUMN_INTENT_ACTION);
		}
		return action;
	}

	public String getIntentData() {
	
		String data = getSearchableProvider().getSearchableInfo().getSuggestIntentData();
		String suggestionData = obtainCursorValue(SearchManager.SUGGEST_COLUMN_INTENT_DATA);
		if(data != null)
		{
			suggestionData = obtainCursorValue(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
			data = Uri.parse(data).buildUpon().
					appendPath(suggestionData).
				build().toString();
						
		}
		else
		{
			data = suggestionData;
		}
		return data;
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

	private Context getContext()
	{
		return ((SearchableProvider)getSuggestions().getProvider()).getContext();
	}
	
	private ComponentName getResourceOwner()
	{
		return ((SearchableProvider)getSuggestions().getProvider()).
				getSearchableInfo().getSearchActivity();
	}
	
	public Runnable getLauncher() {
		
		return new Runnable() {
			/*
			 * public static boolean launch(SuggestionIndex index)
	{
		if (index == null || index.isCategory() || index.getSuggestion() == null)
		{
			return false;
		}
		String action = getIntentAction(index);
		String data = getIntentData(index);
		String extra = 	getIntentExtra(index);	
		if(action == null)
		{
			return false;
		}
		
		Intent intent = new Intent(action);
		if(data  != null)
		{
			intent.setData(Uri.parse(data));
		}
		
		if (extra != null)
		{
			intent.putExtra(SearchManager.EXTRA_DATA_KEY, extra);
		}
		
		intent.setComponent(index.getSuggestion().getSearchable().getSearchActivity());
		index.getSuggestion().getParent().getContext().startActivity(intent);
		return true;
	}
	
	private static String getIntentAction(SuggestionIndex index)
	{
		String action = index.getSuggestion().getSearchable().getSuggestIntentAction();
		if (action == null)
		{
			Cursor cursor = index.getSuggestion().getCursor();
			int column = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_ACTION);
			if (column > 0)
			{
				try
				{
					cursor.moveToPosition(index.getIndex());
					action = cursor.getString(column);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return action;
	}
	
	private static String getIntentData(SuggestionIndex index)
	{
		String data = index.getSuggestion().getSearchable().getSuggestIntentData();
		Cursor cursor = index.getSuggestion().getCursor();
		int column = -1;
		try{
			cursor.moveToPosition(index.getIndex());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (data != null)
		{
			
			column = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
			if (column > 0)
			{
				try
				{
					
					data = Uri.parse(data).buildUpon().
								appendPath(cursor.getString(column)).
							build().toString();
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else
		{
			column = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA);
			if (column > 0)
			{
				try
				{
					
					data = cursor.getString(column);
							
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return data;
		
	}
	
	static private String getIntentExtra(SuggestionIndex index)
	{
		String extra = index.getSuggestion().getSearchable().getSuggestIntentAction();
		
		Cursor cursor = index.getSuggestion().getCursor();
		int column = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA);
		if (column > 0)
		{
			try
			{
				cursor.moveToPosition(index.getIndex());
				extra = cursor.getString(column);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return extra;
	}
			 * */
			public void run() {
				String action = getIntentAction();
				String data = getIntentData();
				String extra = 	getIntentExtraData();	
				if(action == null)
				{
					return ;
				}
				
				android.content.Intent intent = new android.content.Intent(action);
				if(data  != null)
				{
					intent.setData(Uri.parse(data));
				}
				
				if (extra != null)
				{
					intent.putExtra(SearchManager.EXTRA_DATA_KEY, extra);
				}
				
				SearchableProvider provider = (SearchableProvider)getSuggestions().getProvider();
				if(provider != null)
				{
					intent.setComponent(provider.getSearchableInfo().getSearchActivity());
					provider.getContext().startActivity(intent);
				}
								
			}
		};
	}
	
	private SearchableProvider getSearchableProvider()
	{
		return (SearchableProvider)getSuggestions().getProvider();
	}
	private int mPosition = -1;
	private SearchableSuggestions mSuggestions = null;
	IconObtainer mIcon1 = null;
	IconObtainer mIcon2 = null;
	
}
