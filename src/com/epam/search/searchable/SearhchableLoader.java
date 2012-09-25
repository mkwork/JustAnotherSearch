package com.epam.search.searchable;


import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.epam.search.data.Suggestions;
import com.epam.search.util.Loadable;

class SearhchableLoader implements Loadable<Suggestions> {

	public SearhchableLoader(SearchableProvider provider, String query, int limit)
	{
		super();
		mSearchableProvider = provider;
		mQuery = query;
		mLimit = limit;
	}
	
	public Suggestions getLoaded() {
		return mSuggestions;
	}

	public void load() {
		
		try
		{
			mSuggestions = null;
			if(mSearchableProvider == null)
			{
				return;
			}
			
			SearchableInfo sInfo = mSearchableProvider.getSearchInfo(); 
			String selection = sInfo.getSuggestSelection();
			String[] selectionArgs = 
					{
						mQuery
					};
			String sortOrder = SearchManager.SUGGEST_COLUMN_TEXT_1;
			Uri uri = getSuggestUriBase(sInfo);
			
			if (uri == null)
			{
				return;
			}
			
			if (selection == null)
			{
				
				
				uri = uri.buildUpon().appendPath(mQuery).build();
				selectionArgs = null;
				sortOrder = null;
			}
			
			if (mLimit > 0)
			{
				uri = uri.buildUpon().
						appendQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT,
								((Integer)mLimit).toString()).build();
			}
			
			Cursor	cursor = mSearchableProvider.getContext().getContentResolver().query(uri, 
						null, 
						selection, 
						selectionArgs, 
						sortOrder);
			
			mSuggestions = new SearchableSuggestions(mSearchableProvider, cursor);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void onLoadStarted() {
		if (mStartListener != null)
		{
			mStartListener.onLoadStart();
		}
		
	}

	public void onLoadFinished() {
		if (mFinishListener != null)
		{
			mFinishListener.onLoadFinished();
		}
		
	}

	public void setOnLoadStartListener(
			com.epam.search.util.Loadable.OnLoadStartListener listener) {
		mStartListener = listener;
		
	}

	public void setOnLoadFinishedListener(
			com.epam.search.util.Loadable.OnLoadFinishedListener listener) {
		mFinishListener = listener;
		
	}
	
	//Implementation
		/**
		 * Generate uri for search request
		 * @param searchable info about search source
		 * @return base uri for search request
		 */
		private  Uri getSuggestUriBase(SearchableInfo searchable) {
	        if (searchable == null) {
	            return null;
	        }
	        
	        String authority = searchable.getSuggestAuthority();
	            if (authority == null) {
	                return null;
	            }

	            Uri.Builder uriBuilder = new Uri.Builder()
	                    .scheme(ContentResolver.SCHEME_CONTENT)
	                    .authority(authority);

	            // if content path provided, insert it now
	            final String contentPath = searchable.getSuggestPath();
	            if (contentPath != null) {
	                uriBuilder.appendEncodedPath(contentPath);
	            }

	            // append standard suggestion query path
	            uriBuilder.appendPath(SearchManager.SUGGEST_URI_PATH_QUERY);
	           
	       Uri uri = uriBuilder.build();
	        return uri;
	    }
	com.epam.search.util.Loadable.OnLoadStartListener mStartListener = null;
	com.epam.search.util.Loadable.OnLoadFinishedListener mFinishListener = null;
	Suggestions mSuggestions = null;
	SearchableProvider mSearchableProvider = null;
	String mQuery = null;
	int mLimit = -1;
 
}
