package com.epam.search.data;


public interface Suggestions {

int getCount();
Suggestion get(int i);
SuggestionProvider getProvider();
}
