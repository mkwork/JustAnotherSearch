package com.epam.Suggestions;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.epam.Suggestions.Suggestions.SuggestionIndex;

public class SuggestionLauncher {
	public static boolean launch(SuggestionIndex index)
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
	
}
