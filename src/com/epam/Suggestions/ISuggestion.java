package com.epam.Suggestions;

import android.app.SearchableInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

/**
 * @author Maxim_Kot
 *
 */
public interface ISuggestion {

/**
 * @return reference to object which manages a search
 */
Suggestions getParent();

/**
 * @return count of founded suggestions
 */
int getCount();

/** Obtains icon for suggestion
 * @param pos position in dataset
 * @return icon or null
 */
Drawable getIcon(int pos);

/**Obtains result of text search
 * @param pos position in dataset
 * @return suggestion or null
 */
String   getSuggestion(int pos);

/**Obtains cursor for manual data reading
 * @return cursor or null
 */
Cursor   getCursor();

/**
 * @return info about searchable or null
 */
SearchableInfo getSearchable();

/**
 * Perform search
 */
void select();

}
