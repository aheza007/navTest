package com.desireaheza.newsTracker.fragments;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.desireaheza.newsTracker.ActivityPageNewsFeed;
import com.desireaheza.newsTracker.MainActivity;
import com.desireaheza.newsTracker.R;
import com.desireaheza.newsTracker.adapters.SimpleAdapter;
import com.desireaheza.newsTracker.adapters.SimpleAdapter.onGridCardItemClick;
import com.desireaheza.newsTracker.model.Feed;
import com.desireaheza.newsTracker.utils.ParseSyndFeeds;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReaderException;

public class FragmentMyFavoriteNews extends Fragment {
	RecyclerView mRecyclerView;
	SimpleAdapter mAdapter;
	List<LoadNewsFeedTask> myTasks = new ArrayList<>();
	ProgressBar mProgressBar;
	List<Feed> mFeedResults;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_display_more_feeds,
				container, false);
		mRecyclerView = (RecyclerView) rootView
				.findViewById(R.id.list_favorite);
		mProgressBar = (ProgressBar) rootView
				.findViewById(R.id.progressBar_fetchingData);

		LinearLayoutManager layoutManager = new LinearLayoutManager(
				this.getActivity());
		mRecyclerView.setLayoutManager(layoutManager);
		getDataNews();
		return rootView;
	}
	String providerCategory="";
	String providerName="";
	private void getDataNews() {
		try {
			String favoriteDetail = PreferenceManager
					.getDefaultSharedPreferences(getActivity()).getString(
							MainActivity.MY_FAVORITE_FEED_URL, null);
			String[] items = favoriteDetail.split(MainActivity.SPLITER);
			providerCategory = (String) items[0];
			providerName = items[1];
			String providerLink = items[2];
			String providerIcon = items[3];
			if (providerLink != null) {
				LoadNewsFeedTask getNews = new LoadNewsFeedTask(getActivity());
				getNews.execute(new String[] { providerLink });
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void setAdapter(SyndFeed pEntry) {
		try {
			mAdapter = new SimpleAdapter(this.getActivity()
					.getApplicationContext(),
					R.layout.long_list_favorite_item_view, parseFeed(pEntry));
			mRecyclerView.setAdapter(mAdapter);

			mAdapter.setOnGridItemClickListener(new onGridCardItemClick() {

				@Override
				public void gridItemClickListener(View v, int position) {
					Intent intent = new Intent(((MainActivity) getActivity()),
							ActivityPageNewsFeed.class);
					Bundle bundle=new Bundle();
					Feed feed=(Feed)v.getTag();
					feed.setFeedProviderCategory(providerCategory);
					feed.setFeedProviderName(providerName);
					bundle.putParcelable("FEED_ITEM",feed );
					intent.putExtras(bundle);
					getActivity().startActivity(intent);
				}
			});
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private List<Feed> parseFeed(SyndFeed pFeed) {
		try {
			mFeedResults = new ArrayList<>();
			for (final Iterator iter = pFeed.getEntries().iterator(); iter
					.hasNext();) {
				final SyndEntry entry = (SyndEntry) iter.next();
				Feed lFeed = ParseSyndFeeds.makeFeed(entry);
				mFeedResults.add(lFeed);
			}
			return mFeedResults;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private class LoadNewsFeedTask extends AsyncTask<String, String, SyndFeed> {
		Context mContext;

		public LoadNewsFeedTask(Context context) {
			mContext = context;
		}

		@Override
		protected void onPreExecute() {
			if (myTasks.size() == 0)
				mProgressBar.setVisibility(View.VISIBLE);
			myTasks.add(this);
		}

		@Override
		protected SyndFeed doInBackground(String... params) {
			SyndFeed results = null;
			try {
				results = getRSS(params[0]);

			} catch (Exception e) {

				e.printStackTrace();
			}

			return results;
		}

		@Override
		protected void onPostExecute(SyndFeed result) {
			try {
				myTasks.remove(this);
				if (myTasks.size() == 0) {

					mProgressBar.setVisibility(View.INVISIBLE);
					// Toast.makeText(mContext,
					// result.size() + " fetched News   Feeds",
					// Toast.LENGTH_LONG).show();
					if (result != null)
						setAdapter(result);
					else
						Toast.makeText(mContext, " can't fetch now",
								Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				mProgressBar.setVisibility(View.INVISIBLE);
				e.printStackTrace();
			}
		}

		private SyndFeed getRSS(String rss) {

			URL feedUrl = null;
			SyndFeed feed = null;
			try {
				Log.d("DEBUG", "Entered:" + rss);
				feedUrl = new URL(rss);
				Thread.currentThread().setContextClassLoader(
						getClass().getClassLoader());
				SyndFeedInput input = new SyndFeedInput();
				XmlReader reader = new XmlReader(feedUrl);
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
}
