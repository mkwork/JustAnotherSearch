package com.epam.search.data;

import com.epam.search.util.IconObtainer;
import com.epam.search.util.Loadable;

public interface SuggestionProvider {
	
	String getKey();
	
	IconObtainer getIcon();
	String getName();
	String getHint();
	
	String getDefaultIntentAction();
	String getDefaultIntentData();
	
	Loadable<Suggestions> getSuggestionsLoader(String query, int maxResults);
}
