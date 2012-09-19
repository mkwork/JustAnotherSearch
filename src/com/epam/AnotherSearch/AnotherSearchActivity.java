package com.epam.AnotherSearch;

import com.epam.Suggestions.SuggestionLauncher;
import com.epam.Suggestions.Suggestions.SuggestionIndex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
        mSearchAdapter = new SearchAdapter(this);
        ListView listView = ((ListView)findViewById(R.id.lvSearchResults)); 
        listView.setAdapter(mSearchAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SuggestionIndex index = (SuggestionIndex)parent.getAdapter().getItem(position);
				if (index != null)
				{
					if (!SuggestionLauncher.launch(index))
					{
						Toast.makeText(parent.getContext(),
								parent.getContext().getResources().getText(R.string.suggestion_not_launchable) , 200).show();
					}
				}
				else
				{
					Toast.makeText(parent.getContext(),
							parent.getContext().getResources().getText(R.string.some_uncauched_error) , 200).show();
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
}