package com.epam.AnotherSearch;

import java.net.URL;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InProviderSearchAdapter extends BaseAdapter implements ISearchEntry{

	public InProviderSearchAdapter(Context context, SearchableInfo searchable) {
		super();
		m_context = context;
		m_packageManager = context.getPackageManager();
		m_searchInfo = searchable;
		m_contentResolver = context.getContentResolver();
		
		try {reload();}
		catch (Exception e) {
		m_cursor = null;
		}
	}
	
	public int getCount() {
		if(m_cursor == null)
		{
			return 0;
		}
		
		int count = 1;
		try {
			count = m_cursor.getCount();
		}
		catch (Exception e) {
			e.toString();
		}
		return count;
	}

	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	public View getView(int pos, View toConvert, ViewGroup parent) {
		
		
		TextView view;
		if(toConvert == null)
		{
			view = new TextView(m_context);
			try{
			m_cursor.moveToPosition(pos);
			String columnName = m_searchInfo.getSuggestSelection();
			int column = m_cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
			view.setText(m_cursor.getString(column));
			}catch (Exception e) {
				view.setText("S");
			}
			
		}
		else
		{
			view = (TextView)toConvert;
		}
		
		return view;
	}
	
	public String getEntry() {
		
		return m_entry;
	}

	public void setEntry(String entry) {
		m_entry = entry;
		reload();
		
	}

	public void reload() {
		
		ProviderInfo info = m_packageManager.resolveContentProvider(m_searchInfo.getSuggestAuthority(), 0);
		
		String[] projection = 
		{
			SearchManager.SUGGEST_COLUMN_TEXT_1/*,
			SearchManager.SUGGEST_COLUMN_TEXT_2,
			SearchManager.SUGGEST_COLUMN_ICON_1,
			SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
			SearchManager.SUGGEST_COLUMN_INTENT_DATA,
			SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA*/
		};
		String selection = m_searchInfo.getSuggestSelection();
		String[] selectionArgs = 
				{
					"M"
				};
		String sortOrder = SearchManager.SUGGEST_COLUMN_TEXT_1;
		Uri uri = getSuggestUriBase(m_searchInfo);
		
		if (selection == null)
		{
			uri = uri.buildUpon().appendEncodedPath("a").
			appendQueryParameter(android.app.SearchManager.SUGGEST_PARAMETER_LIMIT, "1").build();
			m_cursor = m_contentResolver.query(uri, 
					null, 
					null, 
					null, 
					null);
		}
		else
		{
			m_cursor = m_contentResolver.query(uri, 
					projection, 
					selection, 
					selectionArgs, 
					sortOrder);
		
		}
			
		}
	
	private synchronized Uri getSuggestUriBase(SearchableInfo searchable) {
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
	private PackageManager m_packageManager;
	private SearchableInfo m_searchInfo;
	private String m_entry = new String();
	private ContentResolver m_contentResolver;
	private Cursor m_cursor = null;
	private Context m_context = null;
	
}
