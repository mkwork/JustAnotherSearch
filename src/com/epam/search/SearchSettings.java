package com.epam.search;

import com.epam.search.data.SuggestionProvider;

/**
 * @author Maxim_Kot
 *Settings for searching
 */
public interface SearchSettings {
	/**
	 * @param provider
	 * @return true if given provider is on
	 */
	boolean isProviderOn(SuggestionProvider provider);
	
	/**Switch on/off given provider
	 * @param provider provider for switch
	 * @param isOn if true provider will be switched on, otherwise swithced off
	 */
	void  	swithcProviderOn(SuggestionProvider provider, boolean isOn);
	
}
