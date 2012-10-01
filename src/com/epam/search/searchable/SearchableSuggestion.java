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
	
	private int mPosition = -1;
	private SearchableSuggestions mSuggestions = null;
	IconObtainer mIcon1 = null;
	IconObtainer mIcon2 = null;
	
}
