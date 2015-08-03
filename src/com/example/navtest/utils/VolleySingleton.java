package com.example.navtest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

	private static VolleySingleton instance;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	
	public VolleySingleton( Context mContext){
		requestQueue=Volley.newRequestQueue(mContext);
		imageLoader=new ImageLoader(requestQueue,new ImageLoader.ImageCache() {
			
			 private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(30);
			@Override
			public void putBitmap(String key_albm, Bitmap arg1) {
				
				cache.put(key_albm,arg1);
			}
			
			@Override
			public Bitmap getBitmap(String key_data) {
				return cache.get(key_data);
			}
		});
	}
	
	public static VolleySingleton getInstance(Context context){
		if(instance==null){
			instance=new VolleySingleton(context);
		}
		return instance;
	}
	
	public RequestQueue getRequestQueue() {
        return requestQueue;
    }
 
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
