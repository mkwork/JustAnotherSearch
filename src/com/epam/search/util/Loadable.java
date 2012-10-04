package com.epam.search.util;


/**
 * @author Maxim_Kot
 * Named task for load something
 * @param <T> type of loading object
 */
public interface Loadable<T> {
	
	/**
	 * @return loaded object
	 */
	T getLoaded();
	
	/**
	 * loads object
	 */
	void load();
	
	/**
	 * @return key for identification loading task
	 */
	String getSign();
		
}
