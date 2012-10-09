package com.epam.AnotherSearch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epam.search.Search;
import com.epam.search.Search.SearchIndex;
import com.epam.search.util.IconObtainer;

public class SearchAdapter extends BaseAdapter {

	public SearchAdapter(Activity activity)
	{
		super();
		mActivity = activity;
		final Activity a = activity;
		final SearchAdapter adapter = this;
		mPlaceholder = activity.getApplication().getResources().getDrawable(android.R.drawable.ic_menu_view);
		mSearch = new FullSearchComposer(mActivity).getSearch();
		mSearch.setSplitByCategories(true);
		mSearch.registerDataSetObserver(new DataSetObserver() {

			@Override
			public void onChanged() {
				// TODO Auto-generated method stub
				super.onChanged();
				a.runOnUiThread(new Runnable() {
					
					public void run() {
							syncSearch();
							notifyDataSetChanged();
											
					}
				});
			}
			
		});
		
		mSearch.setSearchEndListener(new Runnable() {
			
			public void run() {
				a.runOnUiThread(new Runnable() {
					
					public void run() {
						for (ISearchProcessListener listener : adapter.mSearchProcessListeners) {
							listener.onSearchFinished();
						}
							syncSearch();
							notifyDataSetChanged();
						
						
					}
				});
				
			}
		});
		
		mSearch.setSearchStartListener(new Runnable() {
			
			public void run() {
				a.runOnUiThread(new Runnable() {
					
					public void run() {
						for (ISearchProcessListener listener : adapter.mSearchProcessListeners) {
							listener.onSearchStarted();
						}
						
							syncSearch();
							notifyDataSetChanged();
													
					}
				});
				
			}
		});
		
	}
	public void setQuery(CharSequence s)
	{
		clear();
		if(s.length() > 0)
		{
			mSearch.search(s.toString(), 30);
		}
		
		
	}
	public void clear() {
		
		mSearch.cancel();
		for (ISearchProcessListener listener : mSearchProcessListeners) {
			listener.onSearchFinished();
		}
		mSuggestions.clear();
		mCount = 0;
		notifyDataSetChanged();
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	@Override
	public boolean isEnabled(int position) {
		
		return !mSuggestions.get(position).isCategory();
	}
	public int getCount() {
				
		return mCount;
	}

	public Object getItem(int position) {
		
		return mSuggestions.get(position);
	}

	public long getItemId(int pos) {
		
		return pos;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layout = null;
		TextView textView = null;
		ImageView iconView = null;
		try
		{
				SearchIndex index = (SearchIndex)this.getItem(position);
							
				layout = (LinearLayout)convertView;
				if(layout == null)
				{
					layout = new LinearLayout(parent.getContext());
					textView = new TextView(parent.getContext());
					iconView = new ImageView(parent.getContext());
									iconView.setScaleType(ImageView.ScaleType.CENTER_CROP);
					initViewLayout(layout, textView, iconView);
				}
				else
				{
					iconView = (ImageView)layout.getChildAt(0);
					textView = (TextView)layout.getChildAt(1);
					
				}
				
				
				updateLayout(layout, index);
				updateImageView(iconView, index);
				updateTextView(textView, index);
				
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return layout;
	}
	
	/**
	 * @param layout
	 * @param index
	 */
	private void updateLayout(LinearLayout layout, SearchIndex index) {
		if(index.isCategory())
		{
			layout.setBackgroundColor(Color.GRAY);
						
		}
		else
		{
			layout.setBackgroundColor(layout.getDrawingCacheBackgroundColor());
				
		}
	}
	/**
	 * @param iconView
	 * @param index
	 */
	private void updateImageView(ImageView iconView, SearchIndex index) {
		IconObtainer obtainer = null;
		if(index.isCategory())
		{
						
			obtainer = index.getSuggestionProvider().getIcon();
					
		}
		else
		{				
			obtainer = index.getSuggestion().getIcon1();
		}
						
		if(obtainer != null)
		{
			setIcon(iconView, obtainer);
			
		}
	}
	/**
	 * @param textView
	 * @param index
	 */
	private void updateTextView(TextView textView, SearchIndex index) {
		String text = null;
		if(index.isCategory())
		{
			
			text = index.getSuggestionProvider().getName();
			if(index.getSuggestionProvider().getHint() != null)
			{
				text += "\n" + index.getSuggestionProvider().getHint();
			}
			
								
		}
		else
		{
			text = index.getSuggestion().getText1();
		}
		textView.setText(text);
		
		setTextSize(textView, index);
	}
	/**
	 * @param layout
	 * @param textView
	 * @param iconView
	 */
	private void initViewLayout(LinearLayout layout, TextView textView,
			ImageView iconView) {
		LinearLayout.LayoutParams textViewParams = 
		 		   new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
			        			        	        	
		textViewParams.weight = 80f;
		iconView.setAdjustViewBounds(true);
						
		int textSize = (int)textView.getTextSize();
		
		LinearLayout.LayoutParams imageViewParams = 
				new LinearLayout.LayoutParams(textSize * 2, textSize * 2);
							
		layout.addView(iconView, imageViewParams);
		layout.addView(textView, textViewParams);
	}
	/**
	 * @param textView
	 * @param index
	 */
	private void setTextSize(TextView textView, SearchIndex index) {
		if(index.isCategory())
		{
			textView.setTextSize(TypedValue.COMPLEX_UNIT_FRACTION_PARENT, 30);
			textView.setSingleLine(false);
		}
		else
		{
			textView.setTextSize(TypedValue.COMPLEX_UNIT_FRACTION_PARENT, 25);
			textView.setSingleLine(true);
			textView.setEllipsize(TruncateAt.END);
		}
	}
	
	/**
	 * @param iconView
	 * @param obtainer
	 */
	private void setIcon(ImageView iconView, IconObtainer obtainer) {
		Drawable icon;
		final ImageView updatingImageView = iconView;
		final IconObtainer updatingObtainer = obtainer;
		final Activity updatingActivity = mActivity;
		obtainer.setIconReadyListener(new Runnable() {
			
			public void run() {
				updatingActivity.runOnUiThread(new Runnable() {
					
					public void run() {
						updatingImageView.setImageDrawable(updatingObtainer.getIcon(mPlaceholder));
						notifyDataSetChanged();
					}
				});
				
				
			}
		});
		icon = obtainer.getIcon(mPlaceholder);
		iconView.setImageDrawable(icon);
	}
	
	public void addSearchProcessListener(ISearchProcessListener listener)
	{
		if (!mSearchProcessListeners.contains(listener))
			mSearchProcessListeners.add(listener);
	}
	
	public void removeSearchProcessListener(ISearchProcessListener listener)
	{
		mSearchProcessListeners.remove(listener);
	}
	
	
	private void syncSearch()
	{
		List<SearchIndex> suggestions = new ArrayList<Search.SearchIndex>();
		for(int i = 0; i < mSearch.getCount(); i++)
		{
			suggestions.add(mSearch.getAt(i));
		}
		mSuggestions = suggestions;
		mCount = mSuggestions.size();
	}
	
	//Data
	private List<ISearchProcessListener> mSearchProcessListeners = 
			new ArrayList<ISearchProcessListener>();
	private Activity mActivity = null;		
	private Search mSearch = null;
	private List<SearchIndex> mSuggestions = new ArrayList<Search.SearchIndex>();
	private int mCount = 0;
	private Drawable mPlaceholder = null;
}
