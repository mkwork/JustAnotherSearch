/**
 * 
 */
package com.epam.AnotherSearch;

import java.util.List;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Maxim_Kot
 *
 */
public class ContentSearchAdapter extends BaseAdapter implements ISearchEntry {

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {

		return m_searchableInfo.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return m_searchableInfo.get(position);
	}

	public ContentSearchAdapter(Context context) {
		super();
		m_context = context;
		m_searchManager =(SearchManager)m_context.getSystemService(Context.SEARCH_SERVICE);
		m_packageManager = m_context.getPackageManager();
		reload();
	}
	public void reload()
	{
		m_searchableInfo = m_searchManager.getSearchablesInGlobalSearch();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout topLevel;
		LinearLayout layout;
		TextView textView;
		ImageView imageView;
		ListView searchResults;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	layout = new LinearLayout(parent.getContext());
        	topLevel = new LinearLayout(parent.getContext());
        	searchResults = new ListView(parent.getContext());
        	
        	topLevel.setOrientation(LinearLayout.VERTICAL);
        	//topLevel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        	LinearLayout.LayoutParams textViewParams = 
        			new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        	LinearLayout.LayoutParams imageViewParams = 
        			new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        	        	
        	textViewParams.weight = 0.80f;
        	imageViewParams.weight = 0.20f;
        	
            imageView = new ImageView(layout.getContext());
            textView = new TextView(layout.getContext());
            SearchableInfo sInfo =  m_searchableInfo.get(position);
            ActivityInfo info = null;
            ComponentName searchActivity = sInfo.getSearchActivity();
            Resources res = null;
            
            try {
				info = m_packageManager.getActivityInfo(searchActivity, 0);
				res = m_packageManager.getResourcesForApplication(sInfo.getSearchActivity().getPackageName());
			
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
            
            InProviderSearchAdapter adapter = new InProviderSearchAdapter(m_context, sInfo);
            searchResults.setAdapter(adapter);
            searchResults.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            
            textView.setText(info.loadLabel(m_packageManager));
            textView.setText(res.getText(sInfo.getSettingsDescriptionId()));  
               
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageDrawable(info.loadIcon(m_packageManager));
            
            	
            layout.addView(textView, textViewParams);
            layout.addView(imageView, imageViewParams);
            topLevel.addView(layout);
            topLevel.addView(searchResults, 
            		new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                        
        } else {
        	topLevel = (LinearLayout) convertView; 
            layout =  (LinearLayout) topLevel.getChildAt(0);
            textView = (TextView) layout.getChildAt(0);
            imageView = (ImageView) layout.getChildAt(1);
        }

        
        return topLevel;
	}

	private SearchManager m_searchManager;
	private PackageManager m_packageManager;
	private Context m_context;
	private List<SearchableInfo> m_searchableInfo;
	private String m_entry = new String();
	
	public String getEntry() {
		return m_entry;
	}

	public void setEntry(String entry) {
		m_entry = entry;
	}
	
}
