package com.epam.search;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.epam.search.data.Suggestions;
import com.epam.search.util.NotifiedLoadable;
import com.epam.search.util.NotifiedLoadable.OnLoadFinishedListener;

class SearchTask extends FutureTask<Suggestions> {

	public SearchTask(final Search search, final NotifiedLoadable<Suggestions> loader)
	{
		
		super(new Callable<Suggestions>() {

			public Suggestions call() throws Exception {
				loader.load();
				return loader.getLoaded();
			}
		});
		
		loader.setOnLoadFinishedListener(new OnLoadFinishedListener() {
			
			public void onLoadFinished() {
				if(! isCancelled())
				{
					search.onSuggestionsReady(loader.getLoaded());
				}
				
			}
		});
	}
	

}
