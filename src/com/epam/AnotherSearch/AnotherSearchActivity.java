package com.epam.AnotherSearch;

import java.security.KeyStore.LoadStoreParameter;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

public class AnotherSearchActivity extends Activity implements ISearchProcessListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSearchAdapter = new SearchAdapter(this);
        ((ListView)findViewById(R.id.lvSearchResults)).setAdapter(mSearchAdapter);
        EditText searchView = ((EditText)findViewById(R.id.etSearch));
        searchView.addTextChangedListener(new TextEditSearchEventslistener());
        mSearchAdapter.addSearchProcessListener(this);
       /* searchView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_spin, 0);
        Drawable drawable = searchView.getCompoundDrawables()[2];
        android.graphics.drawable.Animatable animation = (android.graphics.drawable.Animatable)drawable; 
        animation.start();*/
    
    
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
}