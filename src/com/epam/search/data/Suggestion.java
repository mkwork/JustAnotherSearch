package com.epam.search.data;

import com.epam.search.util.IconObtainer;

/**
 * @author Maxim_Kot
 *Search result
 */
public interface Suggestion {
	
	/**
	 * @return parent set of results
	 */
	Suggestions getSuggestions();
	
	/**
	 * @return callback which opens result
	 */
	Runnable getLauncher();
	
	//mapping for SearchableInfo
	String getFormat();
	IconObtainer getIcon1();
	IconObtainer getIcon2();
	String getIntentAction();
	String getIntentData();
	String getIntentDataID();
	String getIntentExtraData();
	String getQuery();
	String getShorcutID();
	String getSpinnerWhileRefreshing();
	String getText1();
	String getText2();
	String getText2Url();
	
}
