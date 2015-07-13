package com.example.navtest.fragments;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.model.FeedProvider;
import com.example.navtest.R;
import com.example.navtest.adapters.SectionedGridRecyclerViewAdapter;
import com.example.navtest.adapters.SimpleAdapter;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReaderException;

public class FragmentHomeFavorite extends Fragment {

	SimpleAdapter mAdapter;
	RecyclerView mRecyclerView;
	ProgressBar mProgressBar;
	List<LoadHomeNewsTask> myTasks;
	List<SyndFeed> myItems;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_home_explorer_favorite, container, false);
		// Your RecyclerView
		mRecyclerView = (RecyclerView) rootView
				.findViewById(R.id.list_favorite);

		// check number of tasks;
		myTasks = new ArrayList<>();

		mProgressBar = (ProgressBar) rootView
				.findViewById(R.id.progressBar_fetchingData);
		mProgressBar.setVisibility(View.INVISIBLE);
		getData();
		mRecyclerView.setHasFixedSize(true);
		// set the gridLayout with the spanCount=2
		mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
		myItems = new ArrayList<>();

		return rootView;
	}

	private void setGridAdapter(List<SyndFeed> Iitems) {

		List<SyndEntry> entries = null;
		entries = parseFeed(Iitems);
		if (entries != null) {
			// Your RecyclerView.Adapter
			mAdapter = new SimpleAdapter(getActivity(), entries);

			// This is the code to provide a sectioned grid
			List<SectionedGridRecyclerViewAdapter.Section> sections = new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
			int j=0;
			for (int i = 0; i < mSectionNameOffest.size();i++) {
				String sectionHeader=mSectionNameOffest.get(i).getName();
				// Sections
				sections.add(new SectionedGridRecyclerViewAdapter.Section(j,
						sectionHeader));
				int ent=mSectionNameOffest.get(i).getEntries();
				j+=ent-1;
			}

			// Add your adapter to the sectionAdapter
			SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections
					.size()];
			SectionedGridRecyclerViewAdapter mSectionedAdapter = new SectionedGridRecyclerViewAdapter(
					getActivity(), R.layout.list_item_section_header,
					R.id.lblListHeader, mRecyclerView, mAdapter);
			mSectionedAdapter.setSections(sections.toArray(dummy));

			// Apply this adapter to the RecyclerView
			mRecyclerView.setAdapter(mSectionedAdapter);
		}
	}

	private class ProviderNameListsOfNews {
		String name;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the entries
		 */
		public int getEntries() {
			return entries;
		}

		int entries;

		public ProviderNameListsOfNews(String name, int entries) {
			this.name = name;
			this.entries = entries;
		}
	}

	List<ProviderNameListsOfNews> mSectionNameOffest;

	public List<SyndEntry> parseFeed(List<SyndFeed> mFeeds) {
		try {
			mSectionNameOffest = new ArrayList<>();
			;
			List<SyndEntry> feedResults = new ArrayList<>();
			for (SyndFeed feed : mFeeds) {
				int items = feed.getEntries() == null ? 0 : feed.getEntries()
						.size();
				mSectionNameOffest.add(new ProviderNameListsOfNews(feed
						.getTitle(), items));
				feedResults.addAll(feed.getEntries());
			}

			return feedResults;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@SuppressLint("NewApi")
	private void getData() {
		try {
			LoadHomeNewsTask loadHomeNews = new LoadHomeNewsTask(
					this.getActivity());
			Iterator<Entry<String, List<FeedProvider>>> it = FragmentLeftDrawer.listDataChild
					.entrySet().iterator();
			List<String> provs = new ArrayList<>();

			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				List<FeedProvider> p = (List<FeedProvider>) pair.getValue();
				provs.add(p.get(0).getProviderUrl());
				// it.remove(); // avoids a ConcurrentModificationException
			}
			String[] urls = new String[provs.size()];
			int i = 0;
			for (String url : provs) {
				urls[i] = url;
				i++;
			}
			loadHomeNews
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urls);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class LoadHomeNewsTask extends
			AsyncTask<String, String, List<SyndFeed>> {
		Context mContext;

		public LoadHomeNewsTask(Context context) {
			mContext = context;
		}

		@Override
		protected void onPreExecute() {
			if (myTasks.size() == 0)
				mProgressBar.setVisibility(View.VISIBLE);
			myTasks.add(this);
		}
		
		@Override
		protected List<SyndFeed> doInBackground(String... params) {
			List<SyndFeed> results = new ArrayList<>();
			try {
				for (String url : params) {
					results.add(getRSS(url));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String link = results.get(0).getLink();
			return results;
		}

		@Override
		protected void onPostExecute(List<SyndFeed> result) {
			try {
				myTasks.remove(this);
				if (myTasks.size() == 0) {

					mProgressBar.setVisibility(View.INVISIBLE);
					Toast.makeText(mContext,
							result.size() + " fetched News   Feeds",
							Toast.LENGTH_LONG).show();
					setGridAdapter(result);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
