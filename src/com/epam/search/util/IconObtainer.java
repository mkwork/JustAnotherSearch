package com.epam.search.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;

/**
 * @author Maxim_Kot
 *
 *Icon loader which provide obtaining of icon by resource id, uri or by custom loader. 
 *If icon was provided once it will be cached and next time will be provided without loading.  
 */
public class IconObtainer {
	

	private static ThreadPoolExecutor mExecutor = null;
	private Map<String, Drawable> mCache = new HashMap<String, Drawable>();
	
	/**Creates obtainer with custom loader. As a cache key will be used Loadable.getSign()
	 * @param loadable - Loadable which will load an icon.
	 */
	public IconObtainer(Loadable<Drawable> loadable)
	{
		mLoadable = loadable;
		if(mExecutor == null)
		{
			
			mExecutor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		}
		mTask = new FutureTask<Drawable>(new Callable<Drawable>() {

			public Drawable call() throws Exception {
				mLoadable.load();
				
				mIcon = mLoadable.getLoaded();
				synchronized (mCache) {
					mCache.put(getSign(), mIcon);
				}
				onIconReady();
				return mIcon;
			}
			
		});
		
	}
	
	/**Constructs obtainer which will obtain icon by it's resource id or uri
	 * @param context will be used for obtain resource or load uri
	 * @param source resource id or uri
	 * @param resourceOwner if id is resource then it will be used for search resources
	 */
	public IconObtainer(Context context, String source, ComponentName resourceOwner)
	{
		mContext = context;
		mResIdOrUrl = source;
		mResourceOwner = resourceOwner;
		
		mTask = new FutureTask<Drawable>(new Callable<Drawable>() {

			public Drawable call() throws Exception {
				Drawable icon = obtainIcon(mResIdOrUrl, mContext, mResourceOwner);
				mIcon = icon;
				synchronized (mCache) {
					mCache.put(getSign(), mIcon);
				}
				onIconReady();
				return mIcon;
			}
			
		});

	}
	
	/**
	 * @return true if icon was loaded 
	 */
	public boolean isReady()
	{
		synchronized (mIcon) {
			return mIcon == null;
		}
		
	}
	
	
	/**Returns loaded icon or placeholder if icon still not loaded and starts loading
	 * @param placeholder will be returned if icon not loaded
	 * @return
	 */
	public Drawable getIcon(Drawable placeholder)
	{
		synchronized (mCache) {
			mIcon = mCache.get(getSign());
		}
		if(mIcon != null)
			return mIcon;
		mExecutor.execute(mTask);
		
		return placeholder;
	}
	
	/**
	 * Setups callback for icon readness
	 * @param listener will be called when icon will be loaded
	 * ATTENTION: this call back can be called not in caller thread.
	 */
	public void setIconReadyListener(Runnable listener)
	{
		mIconReadyListener = listener;
	}
	
	private void onIconReady()
	{
		if(mIconReadyListener != null)
		{
			mIconReadyListener.run();
		}
	}
	

	/**Obtains icon from supported sources (resourceID or Uri)
	 * @param source string representation of one of supported sources
	 * @return icon
	 */
	private Drawable obtainIcon(String source, Context context, ComponentName component)
	{
		// if there is resource ID
		try{
		Integer resourceID  = Integer.parseInt(source);
		return obtainIconFromResource(resourceID, context, component);
		}catch (Exception e) {}
		Uri uri = Uri.parse(source);
		try {
			AssetFileDescriptor file = 
					context.getContentResolver().openAssetFileDescriptor(uri,  "r");
			return Drawable.createFromStream(file.createInputStream(), null);
						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**Obtains icon from resources
	 * @param id resource id
	 * @return icon
	 */
	private Drawable obtainIconFromResource(Integer id, Context context, ComponentName component)
	{
		
        Resources res = null;
        
        try {
        	           
			res = context.getPackageManager().
					getResourcesForApplication(component.getPackageName());
		
		} catch (NameNotFoundException e) {
			res = null;
			e.printStackTrace();
		}
        if(res != null)
        {
          return res.getDrawable(id);
        }
		return null;
	}
	
	String getSign()
	{
		if(mLoadable != null)
		{
			return mLoadable.getSign();
		}
		else
		{
			String sign =  mResIdOrUrl;
			if(mResourceOwner != null && sign != null)
				sign += "." + mResourceOwner.getPackageName();
			return sign;
		}
	}
	Runnable mIconReadyListener = null;
	ComponentName mResourceOwner = null;
	Drawable mIcon = null;
	Context mContext = null;
	String mResIdOrUrl = null;
	FutureTask<Drawable> mTask = null;
	Loadable<Drawable> mLoadable = null;
	
	
}
