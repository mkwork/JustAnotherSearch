package com.epam.search.util;


import android.graphics.drawable.Drawable;

public interface IconObtainer extends Loadable<Drawable> {
	boolean isReady();
	Drawable getIcon(Drawable placeholder);
	
}
