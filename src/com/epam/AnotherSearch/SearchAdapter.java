package com.epam.AnotherSearch;

import com.epam.Suggestions.ISuggestion;
import com.epam.Suggestions.Suggestions;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
	public void setQuery(CharSequence s)
	{
		
		mSuggestions.setQuery(s);
		mSuggestions.reload();
		notifyDataSetChanged();
	}
	public int getCount() {
		if(mSuggestions.getQuery() == null || mSuggestions.getQuery().toString().isEmpty())
		{
			return 0;
		}
		int count = 0;
		for (ISuggestion suggestion : mSuggestions.getSuggestions()) {
			if(suggestion.getCount() > 0)
			{
				count ++;
				count += suggestion.getCount();
			}
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
		TextView tv = null;
		try
		{
			SuggestionIndex index = findSuggestion(position);
			SuggestionData data = getSuggestionData(index);
			tv = (TextView)convertView;
			if(tv == null)
			{
				tv = new TextView(parent.getContext());
			}
			tv.setText(data.getText());
		}catch (Exception e) {
			e.printStackTrace();
			tv = new TextView(parent.getContext());
			tv.setText("Error");
		}
		return tv;
	}
	
	//Implementation
		
	
	private SuggestionData getSuggestionData(SuggestionIndex index)
	{
		
		SuggestionData data = new SuggestionData(null, null);
		if (index.isCategory()){
			data.setIcon(null);
			data.setText(index.getSuggestion().getSearchable().getSearchActivity().getPackageName());
		}
		else
		{
			data.setIcon(index.getSuggestion().getIcon(index.getIndex()));
			data.setText(index.getSuggestion().getSuggestion(index.getIndex()));
		}
		return data;
	}
	
	private SuggestionIndex findSuggestion(int pos)
	{
		int i = 0;
	
		for (ISuggestion suggestion : mSuggestions.getSuggestions()) {
			if(suggestion.getCount() <= 0)
			{
				 continue;
				 
			}
			if (i == pos || pos <= i + suggestion.getCount() )
			{
				Integer index = null;
				if (i != pos)
				{
					index = i + suggestion.getCount() - pos;
				}
				SuggestionIndex sIndex = new SuggestionIndex(suggestion, index);
				sIndex.setIsCategory(i == pos);
								
				return sIndex;
			}
			
			i+= suggestion.getCount() + 1;
			
		}
		return null;
	}
	
	//Data
	private class SuggestionIndex
	{
		public SuggestionIndex(ISuggestion suggestion, Integer index)
		{
			super();
			setSuggestion(suggestion);
			setIndex(index);
		}
		
		public ISuggestion getSuggestion() {
			return suggestion;
		}
		public void setSuggestion(ISuggestion suggestion) {
			this.suggestion = suggestion;
		}
		public Integer getIndex() {
			return index;
		}
		public void setIndex(Integer index) {
			this.index = index;
		}
		
		private Boolean isCategory() {
			return isCategory;
		}

		private void setIsCategory(Boolean isCategory) {
			this.isCategory = isCategory;
		}

		private ISuggestion suggestion = null;
		private Integer index = null;
		private Boolean isCategory = false;
	}
	
	private class SuggestionData
	{
		public SuggestionData(String text,Drawable icon)
		{
			setIcon(icon);
			setText(text);
		}
		public Drawable getIcon() {
			return icon;
		}
		public void setIcon(Drawable icon) {
			this.icon = icon;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		private Drawable icon;
		private String text;
		
	}
	private Suggestions mSuggestions;
}
