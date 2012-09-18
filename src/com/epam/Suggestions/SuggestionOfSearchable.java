/**
 * @author Maxim_Kot
 * 
 */
package com.epam.Suggestions;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;


public class SuggestionOfSearchable implements ISuggestion {

	/**
	 * @param parent parent container which manages search
	 * @param info info about suggestion source
	 */
	public SuggestionOfSearchable(Suggestions parent, SearchableInfo info)
	{
		super();
		mParent = parent;
		msInfo = info;
	}
	
	/* (non-Javadoc)
	 * @see com.epam.Suggestions.ISuggestion#getIcon()
	 */
	public Drawable getIcon() {
		// 
		try {
			return getParent().
			getContext().getPackageManager().
			getActivityIcon(msInfo.getSearchActivity());
			
		} catch (NameNotFoundException e) {
			
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.epam.Suggestions.ISuggestion#getText()
	 */
	public String getText() {
		
		try {
			PackageManager pm = getParent().getContext().getPackageManager();
				return pm.getActivityInfo(msInfo.getSearchActivity(), 0).loadLabel(pm).toString();
				
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.epam.Suggestions.ISuggestion#getHint()
	 */
	public String getHint() {
		int id = msInfo.getSettingsDescriptionId();
		if (id != 0)
		{
			try {
				return getParent().
				getContext().
				getPackageManager().
				getResourcesForApplication(msInfo.getSuggestPackage()).getString(id);
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.epam.Suggestions.ISuggestion#getParent()
	 */
	public Suggestions getParent() {
		
		return mParent;
	}

	/* (non-Javadoc)
	 * @see com.epam.Suggestions.ISuggestion#getCount()
	 */
	public int getCount() {

		int count = 0;
		if (mCursor != null)
		{
			count = mCursor.getCount();
		}
		return count;
	}

	
	/* (non-Javadoc)
	 * @see com.epam.Suggestions.ISuggestion#getIcon(int)
	 */
	public Drawable getIcon(int pos) {
		Cursor cursor = getCursor();
		
		try
		{
			cursor.moveToPosition(pos);
			String source = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_1));
			Drawable icon = obtainIcon(source);
			return icon;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public String getText(int pos) {
		mCursor.moveToPosition(pos);
		
		return mCursor.getString(mCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
	}

	/* (non-Javadoc)
	 * @see com.epam.Suggestions.ISuggestion#getCursor()
	 */
	public Cursor getCursor() {
		
		return mCursor;
	}

	/* (non-Javadoc)
	 * @see com.epam.Suggestions.ISuggestion#getSearchable()
	 */
	public SearchableInfo getSearchable() {
		
		return msInfo;
	}

	/* (non-Javadoc)
	 * @see com.epam.Suggestions.ISuggestion#select()
	 */
	public void select() {
		
				
		String selection = msInfo.getSuggestSelection();
		String[] selectionArgs = 
				{
					getParent().getQuery().toString()
				};
		String sortOrder = SearchManager.SUGGEST_COLUMN_TEXT_1;
		Uri uri = getSuggestUriBase(msInfo);
		
		if (selection == null)
		{
			
			uri = uri.buildUpon().appendPath( getParent().getQuery().toString()).build();
			selectionArgs = null;
			sortOrder = null;
		}
		Integer limit = getParent().getQueryLimmit();
		if (limit != null)
		{
			uri = uri.buildUpon().
					appendQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT,
							limit.toString()).build();
		}
			mCursor = getParent().getContext().getContentResolver().query(uri, 
					null, 
					selection, 
					selectionArgs, 
					sortOrder);
		

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
           
       
        return uriBuilder.build();
    }
	
	/**Obtains icon from supported sources (resourceID or Uri)
	 * @param source string representation of one of supported sources
	 * @return icon
	 */
	Drawable obtainIcon(String source)
	{
		// if there is resource ID
		try{
		Integer resourceID  = Integer.parseInt(source);
		return obtainIconFromResource(resourceID);
		}catch (Exception e) {}
		Uri uri = Uri.parse(source);
		try {
			AssetFileDescriptor file = 
					getParent().getContext().getContentResolver().openAssetFileDescriptor(uri,  "r");
			return Drawable.createFromStream(file.createInputStream(), null);
						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**Obtains icon from resources
	 * @param id resource id
	 * @return icon
	 */
	Drawable obtainIconFromResource(Integer id)
	{
		
        Resources res = null;
        
        try {
        	           
			res = getParent().getContext().getPackageManager().
					getResourcesForApplication(msInfo.getSearchActivity().getPackageName());
		
		} catch (NameNotFoundException e) {
			res = null;
			e.printStackTrace();
		}
        if(res != null)
        {
          return res.getDrawable(id);
        }
		return null;
	}
	
	//Data
	Cursor mCursor = null;
	Suggestions mParent;
	SearchableInfo msInfo;
	public boolean isLoaded() {
		// TODO Auto-generated method stub
		return false;
	}

}
