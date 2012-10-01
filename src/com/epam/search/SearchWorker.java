package com.epam.search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.epam.search.data.Suggestions;
import com.epam.search.util.Loadable;

public class SearchWorker extends Thread {

		
	public void cancel()
	{
		synchronized (this) {
			mTasks.clear();
			mReadySuggestions.clear();
			onWorkEnd();
		}
	}
	
	public void addTask(Loadable<Suggestions> task)
	{
		synchronized (this) {
			mTasks.add(task);			
		}
	}
	
	public List<Suggestions> getReadySuggestions()
	{
		return mReadySuggestions;
	}
	
	
	@Override
	public void run() {
		onWorkStart();
		while(!mTasks.isEmpty())
		{
			Loadable<Suggestions> task = mTasks.poll();
			if(task != null)
			{
				task.load();
				Suggestions suggestions = task.getLoaded();
				if(suggestions != null)
				{
					onSuggestionsReady(suggestions);
					synchronized (mReadySuggestions) {
						mReadySuggestions.add(suggestions);
					}
				}
			}
		}
		onWorkEnd();
	}
	
	public void setWorkProgressListener(SearchWorkerProcessListener listener)
	{
		
			mProcessListener = listener;			
		
	}
	
	public interface SearchWorkerProcessListener
	{
		void onWorkStart();
		void onSuggestionsReady(Suggestions suggestions);
		void onWorkEnd();
	}
	
	private void onWorkStart()
	{
		if(mProcessListener != null)
		{
			synchronized (mProcessListener) {
				mProcessListener.onWorkStart();
			}
			
		}
	}
	private void onSuggestionsReady(Suggestions suggestions)
	{
		if(mProcessListener != null)
		{
			synchronized (mProcessListener) {
				mProcessListener.onSuggestionsReady(suggestions);
			}
			
		}
	}
	private void onWorkEnd()
	{
		if(mProcessListener != null)
		{
			synchronized (mProcessListener) {
				mProcessListener.onWorkEnd();
			}
			
		}
	}
	private Queue<Loadable<Suggestions>> mTasks = new LinkedList<Loadable<Suggestions>>();
	private SearchWorkerProcessListener mProcessListener = null;
	private List<Suggestions> mReadySuggestions = new ArrayList<Suggestions>(); 

}
