package com.epam.search.data;

import com.epam.search.util.IconObtainer;
import com.epam.search.util.NotifiedLoadable;

public interface SuggestionProvider {
	
	String getKey();
	
	IconObtainer getIcon();
	String getName();
	String getHint();
	
	String getDefaultIntentAction();
	String getDefaultIntentData();
	
	NotifiedLoadable<Suggestions> getSuggestionsLoader(String query, int maxResults);
}
