package com.epam.search.data;

import com.epam.search.util.IconObtainer;
import com.epam.search.util.NotifiedLoadable;

/**
 * @author Maxim_Kot
 *Something which can provide search suggestions
 */
public interface SuggestionProvider {
	
	/**
	 * @return key which used for identification of provider
	 */
	String getKey();
	
	/**
	 * @return obtainer for icon of this provider
	 */
	IconObtainer getIcon();
	/**
	 * @return name of provider
	 */
	String getName();
	/**
	 * @return additional info about provider
	 */
	String getHint();
	
	/**
	 * @return default action for run suggestions from this provider
	 */
	String getDefaultIntentAction();
	
	/**
	 * @return default data for run suggestions from this provider
	 */
	String getDefaultIntentData();
	
	/**
	 * @param query search query
	 * @param maxResults maximum suggestions which you want that loader load
	 * @return loader which loads in provider search result
	 */
	NotifiedLoadable<Suggestions> getSuggestionsLoader(String query, int maxResults);
}
