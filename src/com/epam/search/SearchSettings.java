package com.epam.search;

import com.epam.search.data.SuggestionProvider;

public interface SearchSettings {
	boolean isProviderOn(SuggestionProvider provider);
	void  	swithcProviderOn(SuggestionProvider provider, boolean isOn);
	
}
