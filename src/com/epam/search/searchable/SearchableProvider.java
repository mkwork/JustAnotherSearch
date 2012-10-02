package com.epam.search.searchable;

import android.app.SearchableInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;

import com.epam.search.data.SuggestionProvider;
import com.epam.search.data.Suggestions;
import com.epam.search.util.IconObtainer;
import com.epam.search.util.Loadable;
import com.epam.search.util.NotifiedLoadable;

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
		if(mIconObtainer == null)
		{
			mIconObtainer= new IconObtainer(new Loadable<Drawable>() {
				
				public void load() {
					try {
						mIcon =  
						getContext().getPackageManager().
						getActivityIcon(getSearchableInfo().getSearchActivity());
						
					} catch (NameNotFoundException e) {
						
						e.printStackTrace();
					}
				}
				
				public Drawable getLoaded() {
					return mIcon;
				}
				
				Drawable mIcon = null;

				public String getSign() {
					
					return "ActivityIcon." + getSearchableInfo().getSearchActivity().getPackageName();
				}
			});
		}
		return mIconObtainer;
	}

	public String getName() {
		try {
			PackageManager pm = getContext().getPackageManager();
				return pm.getActivityInfo(getSearchableInfo().getSearchActivity(), 0).loadLabel(pm).toString();
				
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		
		return null;
	}

	public String getHint() {
		int id = getSearchableInfo().getSettingsDescriptionId();
		if (id != 0)
		{
			try {
				return getContext().
				getPackageManager().
				getResourcesForApplication(getSearchableInfo().getSuggestPackage()).getString(id);
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		
		}
		return null;
	}

	public String getDefaultIntentAction() {
		return getSearchableInfo().getSuggestIntentAction();
	}

	public String getDefaultIntentData() {
		return getSearchableInfo().getSuggestIntentData();
	}

	public NotifiedLoadable<Suggestions> getSuggestionsLoader(String query,
			int maxResults) {
		
		return new SearhchableLoader(this, query, maxResults);
	}
	
	public Context getContext() {
		return mContext;
	}

	private void setContext(Context context) {
		mContext = context;
	}

	public SearchableInfo getSearchableInfo() {
		return mSearchInfo;
	}

	private void setSearchInfo(SearchableInfo searchInfo) {
		mSearchInfo = searchInfo;
	}
	
	

	private SearchableInfo mSearchInfo = null;
	private Context mContext = null;
	private IconObtainer mIconObtainer = null;
}
