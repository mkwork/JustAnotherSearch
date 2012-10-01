package com.epam.search.util;


public interface NotifiedLoadable<T> extends Loadable<T> {

	public abstract void onLoadStarted();

	public abstract void onLoadFinished();

	public abstract void setOnLoadStartListener(OnLoadStartListener listener);

	public abstract void setOnLoadFinishedListener(
			OnLoadFinishedListener listener);
	
	public interface OnLoadStartListener
	{
		void onLoadStart();
	}
	public interface OnLoadFinishedListener
	{
		void onLoadFinished();
	}

}