package com.epam.search.searchable;

import android.app.SearchableInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;

import com.epam.search.data.SuggestionProvider;
import com.epam.search.data.Suggestions;
import com.epam.search.util.IconObtainer;
import com.epam.search.util.Loadable;

class SearchableProvider implements SuggestionProvider {

	public SearchableProvider(Context context, SearchableInfo info)
	{
		setSearchInfo(info);
		setContext(context);
	}
	
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public IconObtainer getIcon() {
		return new SearchableIconObtainer(this);
	}

	public String getName() {
		try {
			PackageManager pm = getContext().getPackageManager();
				return pm.getActivityInfo(getSearchInfo().getSearchActivity(), 0).loadLabel(pm).toString();
				
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		
		return null;
	}

	public String getHint() {
		int id = getSearchInfo().getSettingsDescriptionId();
		if (id != 0)
		{
			try {
				return getContext().
				getPackageManager().
				getResourcesForApplication(getSearchInfo().getSuggestPackage()).getString(id);
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		
		}
		return null;
	}

	public String getDefaultIntentAction() {
		return getSearchInfo().getSuggestIntentAction();
	}

	public String getDefaultIntentData() {
		return getSearchInfo().getSuggestIntentData();
	}

	public Loadable<Suggestions> getSuggestionsLoader(String query,
			int maxResults) {
		
		return new SearhchableLoader(this, query, maxResults);
	}
	
	public Context getContext() {
		return mContext;
	}

	private void setContext(Context context) {
		mContext = context;
	}

	public SearchableInfo getSearchInfo() {
		return mSearchInfo;
	}

	private void setSearchInfo(SearchableInfo searchInfo) {
		mSearchInfo = searchInfo;
	}

	private SearchableInfo mSearchInfo = null;
	private Context mContext = null;
}
