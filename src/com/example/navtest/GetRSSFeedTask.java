package com.example.navtest;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.model.Feed;
import com.example.navtest.fragments.FragmentFeedList;
import com.example.navtest.utils.FeedParser;
import com.example.navtest.utils.VolleySingleton;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

public class GetRSSFeedTask extends AsyncTask<String, Integer, SyndFeed> {

	MainActivity mActivty;
	FragmentFeedList mFragmentFeedList;
	ProgressDialog dialog;
	String feedProviderName;
	String categoryName;

	public GetRSSFeedTask(FragmentFeedList pFragmentFeedList) {
		mFragmentFeedList = pFragmentFeedList;
		mActivty = (MainActivity) mFragmentFeedList.getActivity();
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		// dialog = new ProgressDialog(mActivty);
		// if (dialog != null) {
		// dialog.setMessage("Loading...");
		// dialog.show();
		// }
		mFragmentFeedList.mProgressBar.setVisibility(View.VISIBLE);
	}

	protected void onPostExecute(SyndFeed result) {
		super.onPostExecute(result);

		// dialog.dismiss();
		mFragmentFeedList.mProgressBar.setVisibility(View.INVISIBLE);
		mFragmentFeedList.setAdapter(result);
	}

	@Override
	protected SyndFeed doInBackground(String... params) {
		categoryName = params[2];
		feedProviderName = params[1];
		getRSS(params[0]);
		return feed;
	}

	// private SyndFeed getRSS(String rss) {
	//
	// URL feed_description = null;
	// SyndFeed feed = null;
	// try {
	// Log.d("DEBUG", "Entered:" + rss);
	// feed_description = new URL(rss);
	// Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
	// SyndFeedInput input = new SyndFeedInput();
//	 XmlReader reader=new XmlReader(feed_description);
	// feed = input.build(reader);
	//
	// // return feed;
	//
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (FeedException e) {
	// e.printStackTrace();
	// } catch (XmlReaderException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return feed;
	// }
	SyndFeed feed = null;

	@SuppressLint("NewApi")
	private void getRSS(String url) {

		StringRequest request = new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				

				try {

					FeedParser parser = new FeedParser();
					List<Feed> feeds=parser.parse(arg0);
					Toast.makeText(mActivty, feeds.size(), Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					
					e.printStackTrace();
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		});
		VolleySingleton.getInstance(mActivty).getRequestQueue().add(request);
	}
}
