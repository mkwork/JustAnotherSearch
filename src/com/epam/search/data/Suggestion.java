package com.epam.search.data;

import com.epam.search.util.IconObtainer;

public interface Suggestion {
	
	Suggestions getSuggestions();
	
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
