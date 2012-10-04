package com.epam.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.database.DataSetObserver;

import com.epam.search.data.ProvidersPack;
import com.epam.search.data.Suggestion;
import com.epam.search.data.SuggestionProvider;
import com.epam.search.data.Suggestions;


/**
 * @author Maxim_Kot
 *Search in setuped providers
 */
public class Search {

	public Search()
	{
		super();
		new ThreadPoolExecutor(2, 4, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
				
	}
	/**
	 * @return is there need split results by categories
	 */
	public boolean getSplitByCategories() {
		return mSplitByCategories;
	}
	
	/** setup is there need split results by categories
	 * @param splitByCategories 
	 */
	public void setSplitByCategories(boolean splitByCategories) {
		mSplitByCategories = splitByCategories;
	}
	
	/**
	 * @return settings which use this search instance 
	 */
	public SearchSettings getSettings() {
		return mSettings;
	}
	
	/**set settings 
	 * @param settings
	 */
	public void setSettings(SearchSettings settings) {
		mSettings = settings;
	}
	
	/**
	 * stops all current search, and clear all results
	 */
	public void cancel()
	{
		
		if(mExecutor != null)
		{
			mExecutor.shutdown();
			for (Runnable runnable : mExecutor.getQueue()) {
				SearchTask task = (SearchTask)runnable;
				if(task != null)
				{
					task.cancel(false);
				}
			}
		}
		mSuggestionsInternal.clear();
		mCount = 0;
		mExecutor = new ThreadPoolExecutor(getMinThreadsCount(), getMaxThreadsCount(), 1000, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
		mSuggestions.clear();
		onDataSetChanged();
		
	}
	
	/**start new search
	 * @param query search query
	 * @param maxResults maximum search results
	 */
	public void search(String query, int maxResults)
	{
		
			this.cancel();
			if(query != null && query.length() > 0)
			{
				Boolean searchStarted = false;
				for (ProvidersPack pack : mProviderPacks) {
					for (SuggestionProvider provider : pack.providers()) {

						if(useProvider(provider))
						{
							mExecutor.submit(new SearchTask(this, provider.getSuggestionsLoader(query, maxResults)));
							searchStarted = true;
						}
							
					}
				}
				if(searchStarted)
				{
					onWorkStart();					
				}
						
			}
	}
	
	/**
	 * @return ready to use results count
	 */
	public int getCount()
	{
		synchronized (mSuggestionsInternal) {
			return mCount;	
		}
		
	}
	
	/** obtains SearchIndex for given position
	 * @param pos position
	 * @return new instance of index object or null if there no so object.
	 */
	public SearchIndex getAt(int pos)
	{
		
		synchronized (mSuggestionsInternal) {
			int i = 0;
			for (Suggestions suggestions : mSuggestionsInternal) {
				if(suggestions == null || suggestions.getCount() <= 0 
						)
				{
					continue;
				}
				
				int bound = i + suggestions.getCount();
				if(!getSplitByCategories())
				{
					bound --;
				}
				
				if (i == pos || pos <= bound )
				{
					Integer index = null;
					if (i != pos || !getSplitByCategories())
					{
						index = i + suggestions.getCount() - pos;
					}
					
					SearchIndex sIndex =  null;
					if(getSplitByCategories() && i == pos)
					{
						sIndex =  new SearchIndex(suggestions.getProvider(), null);
					}
					else
					{
						if(index != null)
						{
							sIndex =  new SearchIndex(suggestions.getProvider(), suggestions.get(index));
						}
					}
													
					return sIndex;
				}
						
				i += suggestions.getCount();
				if(getSplitByCategories())
				{
					i++;
				}
				
			}
			
			return null;
		}
						
	}
	
	/**appends new provider pack
	 * @param pack 
	 */
	public void addProvidersPack(ProvidersPack pack)
	{
		mProviderPacks.add(pack);
	}
	
	/**removes pack
	 * @param name name of pack
	 */
	public void removeProvidersPack(String name)
	{
		ProvidersPack forRemove = null;
		for (ProvidersPack pack : mProviderPacks) {
			if(pack.getName() == name)
			{
				forRemove = pack;
				break;
			}
		}
		if(forRemove != null)
		{
			mProviderPacks.remove(forRemove);
		}
	}
	
	/**
	 * @return list of registered for this search provider packs
	 */
	public final List<ProvidersPack> getProvidersPacks()
	{
		return mProviderPacks;
	}
	
	/**Register observer which will get notifications about search state changing
	 * @param observer
	 */
	public void registerDataSetObserver(DataSetObserver observer)
	{
		mObservers.add(observer);
	}
	
	/**Unregister observer which will get notifications about search state changing
	 * @param observer
	 */
	public void unregisterDataSetObserver(DataSetObserver observer)
	{
		mObservers.remove(observer);
	}
	
	
	/** Must be run when part of suggestions ready
	 * @param suggestions
	 */
	public void onSuggestionsReady(Suggestions suggestions) {
		
		synchronized (mSuggestionsInternal) {
			mSuggestionsInternal.add(suggestions);
			mCount = getCountFromState();
			onDataSetChanged();
			if(mExecutor.getCompletedTaskCount() == mExecutor.getTaskCount() - 1)
			{
				onWorkEnd();
			}
		}
			
	}
			
	/** Setup callback which will be called when search will start
	 * @param listener
	 */
	public void setSearchStartListener(Runnable listener)
	{
		
			mStartListener = listener;
		
	}
	
	/**Setup callback which will be called when search will be finished
	 * @param listener
	 */
	public void setSearchEndListener(Runnable listener)
	{
		
			mEndListener = listener;	
		
	}
	
	/**
	 * @author Maxim_Kot
	 *Via this index you can access search results
	 */
	public class SearchIndex
	{
		
		protected SearchIndex(SuggestionProvider provider, Suggestion suggestion)
		{
			mSuggestionProvider = provider;
			mSuggestion = suggestion;
		}
		
		/**
		 * @return provider which suggestion represented
		 */
		public SuggestionProvider getSuggestionProvider()
		{
			if(mSuggestion != null)
			{
				return mSuggestion.getSuggestions().getProvider();
			}
			return mSuggestionProvider;
		}
		
		/**
		 * @return search result or null if index refers on category header
		 */
		public Suggestion getSuggestion()
		{
			return mSuggestion;
		}
		
		/**
		 * @return true if index refers on category header
		 */
		public boolean isCategory()
		{
			return mSuggestion == null;
		}
		private Suggestion mSuggestion = null;
		private SuggestionProvider mSuggestionProvider = null;
	}
	
	
	private void onDataSetChanged()
	{
		
			for (DataSetObserver observer : mObservers) {
				observer.onChanged();
			}	
		
		
	}
	
	private int getCountFromState()
	{
			int count = 0;
			for (Suggestions suggestions : mSuggestionsInternal) {
				if(suggestions == null || suggestions.getCount() <= 0 
						)
				{
					continue;
				}
				
				count += suggestions.getCount();
				if(getSplitByCategories())
				{
					count++;
				}
				
			}
			return count;	
		
		
	}
	
	@SuppressWarnings("unused")
	private void onDataSetInvalidated()
	{
		for (DataSetObserver observer : mObservers) {
			observer.onInvalidated();
		}
	}
	
	private void onWorkStart() {
		synchronized (mSuggestionsInternal) {
			mCount = getCountFromState();
			onDataSetChanged();
			if(mStartListener != null)
			{
				mStartListener.run();
			}	
		}
		
	}
	
	private void onWorkEnd() {
		synchronized (mSuggestionsInternal) {
			
			mCount = getCountFromState();
			if(mEndListener != null)
			{
				
				mEndListener.run();
				
			}	
			onDataSetChanged();
		}
		
	}

	private boolean useProvider(SuggestionProvider provider)
	{
		if(mSettings == null)
		{
			return true;
		}
		else
		{
			return mSettings.isProviderOn(provider);
		}
	}
	
	
	
	private int getMinThreadsCount()
	{
		return 2;
	}
	
	private int getMaxThreadsCount()
	{
		return 8;
	}
	
	private List<Future<Suggestions>> mSuggestions = new ArrayList<Future<Suggestions>>();
	private List<ProvidersPack> mProviderPacks = new ArrayList<ProvidersPack>(); 
	private Boolean mSplitByCategories = false;
	private SearchSettings mSettings = null;
	private Set<DataSetObserver> mObservers = new HashSet<DataSetObserver>();
	private List<Suggestions> mSuggestionsInternal = new ArrayList<Suggestions>();
	private int mCount = 0;
	private Runnable mStartListener = null;
	private Runnable mEndListener = null;
	private ThreadPoolExecutor mExecutor = null;
	
	
	
	
}
