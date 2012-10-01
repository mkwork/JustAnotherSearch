package com.epam.search.searchable;


import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;

import com.epam.search.data.ProvidersPack;
import com.epam.search.data.SuggestionProvider;

public class SearchableProvidersPack implements ProvidersPack{

	public static final String NAME = "providers.pack.searchable";
	public SearchableProvidersPack(Context context) {
		mContext = context;
	}
	public String getName() {
		return NAME;
	}

	public List<SuggestionProvider> providers() {
		
		if(mProviders == null)
		{
			mProviders = new ArrayList<SuggestionProvider>();
		SearchManager sm = (SearchManager)mContext.getSystemService(Context.SEARCH_SERVICE);
		if(sm != null)
		{
			List<SearchableInfo> infos = sm.getSearchablesInGlobalSearch();
			for (SearchableInfo searchableInfo : infos) {
				mProviders.add(new SearchableProvider(mContext, searchableInfo));
			}
		}
		}
		return mProviders;
	}

	Context mContext = null;
	List<SuggestionProvider> mProviders = null;
}
