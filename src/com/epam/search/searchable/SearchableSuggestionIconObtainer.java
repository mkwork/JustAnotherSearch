package com.epam.search.searchable;

import android.graphics.drawable.Drawable;

import com.epam.search.util.IconObtainer;

class SearchableSuggestionIconObtainer implements IconObtainer{

	public SearchableSuggestionIconObtainer(String resIdOrUrl)
	{
		mResIdOrUrl = resIdOrUrl;
	}
	
	public Drawable getLoaded() {
		// TODO Auto-generated method stub
		return null;
	}

	public void load() {
		// TODO Auto-generated method stub
		
	}

	public void onLoadStarted() {
		// TODO Auto-generated method stub
		
	}

	public void onLoadFinished() {
		// TODO Auto-generated method stub
		
	}

	public void setOnLoadStartListener(
			com.epam.search.util.Loadable.OnLoadStartListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void setOnLoadFinishedListener(
			com.epam.search.util.Loadable.OnLoadFinishedListener listener) {
		// TODO Auto-generated method stub
		
	}

	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	public Drawable getIcon(Drawable placeholder) {
		// TODO Auto-generated method stub
		return placeholder;
	}
	
	String mResIdOrUrl = null;
}
