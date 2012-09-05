/**
 * 
 */
package com.epam.AnotherSearch;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Maxim_Kot
 *
 */
public class CategorizedAdapter extends BaseAdapter {

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {

		return 10;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		if(position > 0)
		{
			return String.format("Item No %d", position);
		}
		else
		{
			return String.format("Category No %d", position);
		}
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
		
		LinearLayout layout;
		TextView textView;
		ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	layout = new LinearLayout(parent.getContext());
        	
        	LinearLayout.LayoutParams textViewParams = 
        			new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        	LinearLayout.LayoutParams imageViewParams = 
        			new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        	        	
        	textViewParams.weight = 0.80f;
        	imageViewParams.weight = 0.20f;
        	
            imageView = new ImageView(layout.getContext());
            textView = new TextView(layout.getContext());
            textView.setText((String)getItem(position));
            
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            
            imageView.setImageResource(R.drawable.sample);
            
            
            layout.addView(textView, textViewParams);
            layout.addView(imageView, imageViewParams);

                        
        } else {
            layout =  (LinearLayout) convertView;
            textView = (TextView) layout.getChildAt(0);
            imageView = (ImageView) layout.getChildAt(1);
        }

        
        return layout;
	}

}
