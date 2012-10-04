package com.epam.search.data;

import java.util.List;

/**
 * @author Maxim_Kot
 *Set of providers, used by Search.
 *If module implements some custom providers it must provide only ProvidersPack
 */
public interface ProvidersPack {
/**
 * @return name for indentify pack
 */
String getName();	

/**
 * @return list of available providers
 */
List<SuggestionProvider> providers();
}
