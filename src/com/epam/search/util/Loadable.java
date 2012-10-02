package com.epam.search.util;


public interface Loadable<T> {
	
	T getLoaded();
	
	void load();
	
	String getSign();
		
}
