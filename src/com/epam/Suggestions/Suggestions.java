package com.epam.Suggestions;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;

public class Suggestions implements ISuggestionEvents {

	 
	//constructors
	public Suggestions(Context context)
	{
		super();
		mContext = context;
		init();
		
	}
	//Events
	public boolean OnReloadStart() {
		for (ISuggestionEvents eventListener : mEventsListeners) {
			eventListener.OnReloadStart();
		}
		return false;
	}
	public boolean OnReloadFinished() {
		for (ISuggestionEvents eventListener : mEventsListeners) {
			eventListener.OnReloadFinished();
		}
		return false;
	}
	public boolean OnSuggestionLoaded(ISuggestion suggestion) {
		for (ISuggestionEvents eventListener : mEventsListeners) {
			eventListener.OnSuggestionLoaded(suggestion);
		}
		return false;
	}
	
	public void addSuggestionEventsListener(ISuggestionEvents listener)
	{
		if(!mEventsListeners.contains(listener) && listener != this)
		{
			mEventsListeners.add(listener);
		}
	}
	
	public boolean removeSuggestionEventsListener(ISuggestionEvents listener)
	{
		return mEventsListeners.remove(listener);
	}
	
	//Interface
	public String getQuery() {
		return mQuery;
	}

	public void setQuery(String mQuery) {
		this.mQuery = mQuery;
	}
	
	public List<ISuggestion> getSuggestions()
	{
		return mSuggestions;
	}
	
	public void reload()
	{
		OnReloadStart();
		for (ISuggestion suggestion : mSuggestions) {
			suggestion.select();
			OnSuggestionLoaded(suggestion);
		}		
		OnReloadFinished();
	}
	
	public Context getContext()
	{
		return mContext;
	}
	
	public Integer getQueryLimmit() {
		return mQueryLimmit;
	}
	
	public void setQueryLimmit(Integer queryLimmit) {
		if (queryLimmit == null || queryLimmit <= 0)
			this.mQueryLimmit = null;
		else
			this.mQueryLimmit = queryLimmit;
			
	}
	
	public SearchManager getSearchManager()
	{
		return (SearchManager)getContext().getSystemService(Context.SEARCH_SERVICE);
	}
	
	//Implementation
	private void init()
	{
		mSearchables = getSearchManager().getSearchablesInGlobalSearch();
		for (SearchableInfo info : mSearchables) {
			mSuggestions.add(new SuggestionOfSearchable(this, info));
		}
		//TODO: add extra sources here
	}
	
		//Data
		private String mQuery = null;
		private Integer mQueryLimmit = null;
		private List<SearchableInfo> mSearchables = new ArrayList<SearchableInfo>();
		private List<ISuggestion> mSuggestions = new ArrayList<ISuggestion>();
		private List<ISuggestionEvents> mEventsListeners = new ArrayList<ISuggestionEvents>();
		private Context mContext;
		
	
}
