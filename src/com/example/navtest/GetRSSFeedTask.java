package com.example.navtest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.navtest.fragments.FragmentFeedList;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReaderException;

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
		if (dialog != null) {
			dialog.setMessage("Loading...");
			dialog.show();
		}
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

		URL feedUrl = null;
		SyndFeed feed = null;
		try {
			Log.d("DEBUG", "Entered:" + rss);
			feedUrl = new URL(rss);
			Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
			SyndFeedInput input = new SyndFeedInput();
			XmlReader reader=new XmlReader(feedUrl);
			feed = input.build(reader);

			// return feed;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (XmlReaderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return feed;
	}
}
