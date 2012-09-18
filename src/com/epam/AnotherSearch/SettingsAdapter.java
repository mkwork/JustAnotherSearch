package com.epam.AnotherSearch;
import com.epam.Suggestions.ISuggestion;
import com.epam.Suggestions.Suggestions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


public class SettingsAdapter extends BaseAdapter {

	public SettingsAdapter(Context context) {
		mSuggestions = new Suggestions(context);
	}
	
	public int getCount() {
		
		return mSuggestions.getSuggestions().size();
	}

	public Object getItem(int position) {
		
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ISuggestion suggestion = mSuggestions.getSuggestions().get(position);
		LinearLayout layout = (LinearLayout)convertView;
		TextView textView = null;
		ImageView imageView = null;
		CheckBox checkBox = null;
		if(layout == null)
		{
			layout = new LinearLayout(parent.getContext());
			textView = new TextView(parent.getContext());
			imageView = new ImageView(parent.getContext());
			checkBox = new CheckBox(parent.getContext());
			checkBox.setChecked(position % 2 > 0 ? true : false);
			LinearLayout.LayoutParams params = 
					new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
			params.weight = 10;
			layout.addView(imageView, params);
			params.weight = 80;
			layout.addView(textView, params);
			params.weight = 10;
			layout.addView(checkBox, params);
			
		}
		else
		{
			imageView = (ImageView)layout.getChildAt(0);
			textView = (TextView)layout.getChildAt(1);
			checkBox = (CheckBox)layout.getChildAt(2);
		}
		
		textView.setText(suggestion.getText());
		imageView.setImageDrawable(suggestion.getIcon());
		
		return layout;
	}

	Suggestions mSuggestions = null;
}
