package com.epam.AnotherSearch;

import android.content.Context;

import com.epam.search.Search;
import com.epam.search.SearchComposer;
import com.epam.search.searchable.SearchableProvidersPack;

public class FullSearchComposer implements SearchComposer{
	public FullSearchComposer(Context context)
	{
		mSearch = new Search();
		mSearch.setSettings(new Settings(context));
		mSearch.addProvidersPack(new SearchableProvidersPack(context));
	}
	public Search getSearch() {
		return mSearch;
	}

	private Search mSearch = null;
}
