package com.epam.search.util;


public interface Loadable<T> {
	
	T getLoaded();
	
	void load();
	
	void onLoadStarted();
	void onLoadFinished();
	
	void setOnLoadStartListener(OnLoadStartListener listener);
	void setOnLoadFinishedListener(OnLoadFinishedListener listener);
	
	public interface OnLoadStartListener
	{
		void onLoadStart();
	}
	public interface OnLoadFinishedListener
	{
		void onLoadFinished();
	}
}
