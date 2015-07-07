package com.example.navtest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.navtest.fragments.FragmentFeedList;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

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
		dialog = new ProgressDialog(mActivty);
		dialog.setMessage("Loading...");
		dialog.show();
	}

	protected void onPostExecute(SyndFeed result) {
		super.onPostExecute(result);
		
		dialog.dismiss();
		mFragmentFeedList.setAdapter(result);
	}

	@Override
	protected SyndFeed doInBackground(String... params) {
		categoryName = params[2];
		feedProviderName = params[1];
		return getRSS(params[0]);
	}

	private SyndFeed getRSS(String rss) {

		URL feedUrl;
		SyndFeed feed = null;
		try {
			Log.d("DEBUG", "Entered:" + rss);
			feedUrl = new URL(rss);

			SyndFeedInput input = new SyndFeedInput();
			feed = input.build(new XmlReader(feedUrl));

			// return feed;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return feed;
	}
}
