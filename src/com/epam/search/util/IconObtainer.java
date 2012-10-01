package com.epam.search.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;



import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class IconObtainer {
	
	private static Worker mWorker = null;
	public IconObtainer(Loadable<Drawable> loadable)
	{
		mLoadable = loadable;
	}
	public IconObtainer(Context context, String source, ComponentName resourceOwner)
	{
		mContext = context;
		mResIdOrUrl = source;
		mResourceOwner = resourceOwner;
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
		if(mWorker == null)
		{
			mWorker = new Worker();
			mWorker.setPriority(Worker.MIN_PRIORITY);
			mWorker.start();
		}
		synchronized (mWorker.mQueue) {
			mWorker.mQueue.add(this);
		}
		
		
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
	

	private void setIcon(Drawable icon)
	{
		mIcon = icon;
	}
	
	private class Worker extends Thread
	{

		@Override
		public void run() {
			
			while(true)
			{
				IconObtainer obtainer = null;
				synchronized (mQueue) {
					obtainer = mQueue.poll();
				}
				if(obtainer != null)
				{
					Drawable icon = null;
					if(obtainer.mLoadable != null)
					{
						synchronized (obtainer.mLoadable) {
							
								mLoadable.load();
								icon = mLoadable.getLoaded();
							
						}
					}
					
					if(icon == null)
					{
						if(obtainer.mResIdOrUrl != null)
						{
							synchronized (obtainer.mResIdOrUrl) {
								icon = obtainIcon(obtainer.mResIdOrUrl, 
										obtainer.mContext, 
										obtainer.mResourceOwner);
							
							}
						}
					}
						obtainer.setIcon(icon);
						obtainer.onIconReady();
				}
				else
				{
					yield();
				}
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
		

		Queue<IconObtainer> mQueue = new LinkedList<IconObtainer>();
		
	}
		
	Runnable mIconReadyListener = null;
	ComponentName mResourceOwner = null;
	Drawable mIcon = null;
	Context mContext = null;
	String mResIdOrUrl = null;
	Loadable<Drawable> mLoadable = null;
	
}
