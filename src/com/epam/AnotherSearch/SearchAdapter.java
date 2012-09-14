package com.epam.AnotherSearch;

import com.epam.Suggestions.ISuggestion;
import com.epam.Suggestions.Suggestions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {

	public SearchAdapter(Context context)
	{
		super();
		mSuggestions = new Suggestions(context);
	}
	public void setQuery(String query)
	{
		mSuggestions.setQuery(query);
		mSuggestions.setQueryLimmit(1);
		mSuggestions.reload();
		notifyDataSetChanged();
	}
	public int getCount() {
		int count = 0;
		for (ISuggestion suggestion : mSuggestions.getSuggestions()) {
			count ++;
			count += suggestion.getCount();
		}
		return count;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
		{
			if(isCategory(position))
			{
				return createCategoryView(position, parent);
			}
			else
			{
				return createSuggestionView(position, parent);
			}
		}
		else
		{
			return convertView;
		}
	}
	
	//Implementation
	private Boolean isCategory(int pos)
	{
		int finded = 0;
		for (ISuggestion suggestion : mSuggestions.getSuggestions()) {
			if (finded == pos)
			{
				return true;
			}
			
			finded += suggestion.getCount();
		}
		return true;
	}
	
	private View createSuggestionView(int position, ViewGroup parent)
	{
		TextView tv = new TextView(parent.getContext());
		tv.setText("just suggestion");
		
		return tv;
	}
	
	private View createCategoryView(int position, ViewGroup parent)
	{
		TextView tv = new TextView(parent.getContext());
		tv.setText("just category");
		
		return tv;
	}
	
	//Data
	private Suggestions mSuggestions;
}
