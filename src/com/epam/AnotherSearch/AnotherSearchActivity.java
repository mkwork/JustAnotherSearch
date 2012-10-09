package com.epam.AnotherSearch;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.epam.search.Search.SearchIndex;
import com.epam.search.data.Suggestion;

public class AnotherSearchActivity extends Activity implements ISearchProcessListener{
    

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.miSearchSettings:
            	showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
           
    
    }

	private void init() {
		
		
		mSearchAdapter = new SearchAdapter(this);
		ListView listView = ((ListView)findViewById(R.id.lvSearchResults)); 
        listView.setAdapter(mSearchAdapter);
        initCallbacks();
        
      }

	private void initCallbacks() {
		View main = findViewById(R.id.mainLayout);
		
        main.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyBoard();
				return false;
			}
		});
        
        
        ListView listView = (ListView)findViewById(R.id.lvSearchResults);
        listView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyBoard();
				return false;
			}
		});
        
        listView.setOnScrollListener(new OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				hideKeyBoard();
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
								
			}
		});
        listView.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> parent, View view,
    				int position, long id) {
    				SearchIndex index = (SearchIndex)parent.getAdapter().getItem(position);
    				if (index != null)
    				{
    					Suggestion suggestion = index.getSuggestion();
    					if(suggestion == null)
    					{
    						Toast.makeText(parent.getContext(),
    								parent.getContext().getResources().getText(R.string.suggestion_not_launchable) , Toast.LENGTH_SHORT).show();
    					}
    					else
    					{
    						Runnable suggestionLauncher = suggestion.getLauncher();
    						try {
    							suggestionLauncher.run();
    						} catch (Exception e) {
    							Toast.makeText(parent.getContext(),
    									parent.getContext().getResources().getText(R.string.suggestion_not_launchable) , Toast.LENGTH_SHORT).show();
    							e.printStackTrace();
    						}
    					}
    					
    					
    				}
    				else
    				{
    					Toast.makeText(parent.getContext(),
    							parent.getContext().getResources().getText(R.string.some_uncauched_error) , Toast.LENGTH_SHORT).show();
    				}
    			}
    		});
        EditText searchView = ((EditText)findViewById(R.id.etSearch));
        searchView.addTextChangedListener(new TextEditSearchEventslistener());
        mSearchAdapter.addSearchProcessListener(this);

	}
    
    private class TextEditSearchEventslistener implements TextWatcher
    {
    	public TextEditSearchEventslistener()
    	{
    		super();
    	}

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			try
			{
				mSearchAdapter.setQuery(s);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
    	
    }
    
    private SearchAdapter mSearchAdapter = null;

	public void onSearchStarted() {
		EditText searchView = ((EditText)findViewById(R.id.etSearch));
		searchView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_spin, 0);
        Drawable drawable = searchView.getCompoundDrawables()[2];
        android.graphics.drawable.Animatable animation = (android.graphics.drawable.Animatable)drawable; 
        animation.start();
        		
	}

	public void onSearchFinished() {
		EditText searchView = ((EditText)findViewById(R.id.etSearch));
		searchView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
	}
	
	private void showSettings()
	{
		Intent i = new Intent(this, SettingsActivity.class);
		startActivity(i);
	}
	
	private void hideKeyBoard()
	{
		View v = findViewById(R.id.etSearch);
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		if(imm != null)
		{
			imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
		
}