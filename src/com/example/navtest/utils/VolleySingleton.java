package com.example.navtest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

	private static VolleySingleton instance;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	private static Context mContext;
	public VolleySingleton(Context pContext) {
		mContext=pContext;
		requestQueue =getRequestQueue();
		imageLoader = new ImageLoader(requestQueue,
				new ImageLoader.ImageCache() {
					int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
					final int cacheSize = maxMemory / 8;

					private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(
							cacheSize);

					@Override
					public void putBitmap(String key_albm, Bitmap arg1) {

						cache.put(key_albm, arg1);
					}

					@Override
					public Bitmap getBitmap(String key_data) {
						return cache.get(key_data);
					}
					
					
				});
	}

	public static synchronized VolleySingleton getInstance(Context context) {
		if (instance == null) {
			instance = new VolleySingleton(context);
		}
		return instance;
	}
	 public <T> void addToRequestQueue(Request<T> req) {
	        getRequestQueue().add(req);
	    }
	public RequestQueue getRequestQueue() {
		if(requestQueue==null)
			requestQueue=Volley.newRequestQueue(mContext.getApplicationContext());
		return requestQueue;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}
}
