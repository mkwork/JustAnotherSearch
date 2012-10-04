package com.epam.search.util;


/**
 * @author Maxim_Kot
 *Loadable task which can inform about process start and finish
 * @param <T>
 */
public interface NotifiedLoadable<T> extends Loadable<T> {

	/**
	 * must be called before loading 
	 */
	public abstract void onLoadStarted();

	/**
	 * must be called after loading
	 */
	public abstract void onLoadFinished();

	/**Set ups callback which will be called when loading started 
	 * @param listener
	 */
	public abstract void setOnLoadStartListener(OnLoadStartListener listener);

	/**Set ups callback which will be called when loading finished 
	 * @param listener
	 */
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