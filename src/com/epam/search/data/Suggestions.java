package com.epam.search.data;


/**
 * @author Maxim_Kot
 *Set of search results
 */
public interface Suggestions {

/**
 * @return count
 */
int getCount();

/**
 * @param i
 * @return Suggestion at position i
 */
Suggestion get(int i);

/**
 * @return provider which search result stored
 */
SuggestionProvider getProvider();
}
