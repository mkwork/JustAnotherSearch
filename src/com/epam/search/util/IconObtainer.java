package com.epam.search.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
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

public class IconObtainer {
	

	private static ThreadPoolExecutor mExecutor = null;
	public IconObtainer(Loadable<Drawable> loadable)
	{
		mLoadable = loadable;
		if(mExecutor == null)
		{
			BlockingQueue<Runnable> q = new LinkedBlockingQueue<Runnable>();
			mExecutor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.MILLISECONDS, q);
		}
		mTask = new FutureTask<Drawable>(new Callable<Drawable>() {

			public Drawable call() throws Exception {
				mLoadable.load();
				
				mIcon = mLoadable.getLoaded();	
				onIconReady();
				return mIcon;
			}
			
		});
		
	}
	public IconObtainer(Context context, String source, ComponentName resourceOwner)
	{
		mContext = context;
		mResIdOrUrl = source;
		mResourceOwner = resourceOwner;
		
		mTask = new FutureTask<Drawable>(new Callable<Drawable>() {

			public Drawable call() throws Exception {
				Drawable icon = obtainIcon(mResIdOrUrl, mContext, mResourceOwner);
				mIcon = icon;
				onIconReady();
				return mIcon;
			}
			
		});

	}
	
	public boolean isReady()
	{
		synchronized (mIcon) {
			return mIcon == null;
		}
		
	}
	
	
	public Drawable getIcon(Drawable placeholder)
	{
		if(mIcon != null)
			return mIcon;
		mExecutor.execute(mTask);
		
		return placeholder;
	}
	
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
	
	Runnable mIconReadyListener = null;
	ComponentName mResourceOwner = null;
	Drawable mIcon = null;
	Context mContext = null;
	String mResIdOrUrl = null;
	FutureTask<Drawable> mTask = null;
	Loadable<Drawable> mLoadable = null;
	
}
