package com.desireaheza.newsTracker.fragments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.desireaheza.newsTracker.MainActivity;
import com.desireaheza.newsTracker.R;
import com.desireaheza.newsTracker.adapters.FlipAdapter;
import com.desireaheza.newsTracker.model.Feed;
import com.desireaheza.newsTracker.model.FeedProvider;
import com.desireaheza.newsTracker.utils.ParseSyndFeeds;
import com.desireaheza.newsTracker.utils.VolleySingleton;
import com.dzrahza.flipview.FlipView;
import com.dzrahza.flipview.OverFlipMode;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

public class FragmentFeedList extends Fragment implements
		FlipAdapter.Callback,
		com.dzrahza.flipview.FlipView.OnOverFlipListener,
		com.dzrahza.flipview.FlipView.OnFlipListener {

	View rootView;
	private FlipView mFlipView;
	private FlipAdapter mAdapter;
	
	public ProgressBar mProgressBar;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_feeds_lists, container,
				false);
		mFlipView = (FlipView) rootView.findViewById(R.id.flip_view);
		mProgressBar = (ProgressBar) rootView
				.findViewById(R.id.progressBar_fetchingData);
		mAdapter = new FlipAdapter((MainActivity) getActivity(), new ArrayList<Feed>());
		mAdapter.setCallback(this);
		mFlipView.setAdapter(mAdapter);
		getData();
		mFlipView.setOnFlipListener(this);
		mFlipView.peakNext(false);
		mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
		mFlipView.setOnOverFlipListener(this);

		return rootView;
	}

	private void getData() {
		FeedProvider provider = ((MainActivity) getActivity()).mSelectedProvider;
		String url = provider.getProviderUrl();
		StringRequest request = new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String str) {

				try {
					parseDataAsync parse = new parseDataAsync();
					parse.execute(str);
				} catch (Exception e) {
					if (mProgressBar.getVisibility() == View.VISIBLE)
						mProgressBar.setVisibility(View.INVISIBLE);
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				if (mProgressBar.getVisibility() == View.VISIBLE)
					mProgressBar.setVisibility(View.INVISIBLE);
			}
		});
		VolleySingleton.getInstance(getActivity()).getRequestQueue()
				.add(request);
	}

	public void setAdapter(SyndFeed mFeeds) {
		try {
			SyndEntry ent;
			mAdapter.setSelectedProvider(((MainActivity) getActivity()).mSelectedProvider);
			List<SyndEntry> feedResults = mFeeds.getEntries();
			Iterator<SyndEntry> iterator = feedResults.listIterator();
			mAdapter.clear();
			while (iterator.hasNext()) {
				ent = iterator.next();
				mAdapter.add(ParseSyndFeeds.makeFeed(ent));
			}
			mProgressBar.setVisibility(View.INVISIBLE);
		} catch (Exception e) {
			if (mProgressBar.getVisibility() == View.VISIBLE)
				mProgressBar.setVisibility(View.INVISIBLE);
			e.printStackTrace();
		}
	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {
		Log.i("pageflip", "Page: " + position);
		// if (position > mFlipView.getPageCount() - 3
		// && mFlipView.getPageCount() < mFeedResults.size()) {
		// mAdapter.addItems(5);
		// }
	}

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {
		Log.i("overflip", "overFlipDistance = " + overFlipDistance);

	}

	@Override
	public void onPageRequested(int page) {
		mFlipView.smoothFlipTo(page);

	}

	private class parseDataAsync extends AsyncTask<String, String, SyndFeed> {

		@Override
		protected SyndFeed doInBackground(String... params) {

			try {
				Thread.currentThread().setContextClassLoader(
						getClass().getClassLoader());
				SyndFeedInput input = new SyndFeedInput();
				// convert String into InputStream
				InputStream in = new ByteArrayInputStream(params[0].getBytes());
				XmlReader reader = new XmlReader(in);
				return input.build(reader);
			} catch (IllegalArgumentException | FeedException e) {

				e.printStackTrace();
				return null;
			} catch (IOException e) {

				e.printStackTrace();
				return null;
			} 
		}

		@Override
		protected void onPostExecute(SyndFeed result) {
			if (result != null) {

				setAdapter(result);

			} else
				Toast.makeText(getActivity(), " no feeds ", Toast.LENGTH_LONG)
						.show();
			mProgressBar.setVisibility(View.INVISIBLE);
		}
	}
}
