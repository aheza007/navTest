package com.example.navtest.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dzrahza.flipview.FlipView;
import com.dzrahza.flipview.OverFlipMode;
import com.example.model.FeedProvider;
import com.example.navtest.GetRSSFeedTask;
import com.example.navtest.MainActivity;
import com.example.navtest.R;
import com.example.navtest.adapters.FlipAdapter;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

public class FragmentFeedList extends Fragment implements
		com.example.navtest.adapters.FlipAdapter.Callback,
		com.dzrahza.flipview.FlipView.OnOverFlipListener,
		com.dzrahza.flipview.FlipView.OnFlipListener {

	View rootView;
	private FlipView mFlipView;
	private FlipAdapter mAdapter;
	List<SyndEntry> feedResults=new ArrayList<>();
	public ProgressBar mProgressBar;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_feeds_lists, container,
				false);
		mFlipView = (FlipView) rootView.findViewById(R.id.flip_view);
		mProgressBar=(ProgressBar)rootView.findViewById(R.id.progressBar_fetchingData);
		mAdapter = new FlipAdapter((MainActivity)getActivity(),feedResults);
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
		GetRSSFeedTask task = new GetRSSFeedTask(
				this);//((MainActivity) getActivity())
		FeedProvider provider = ((MainActivity)getActivity()).selectedProvider;
		String url = provider.getProviderUrl();
		task.execute(new String[] { url,
				provider.getProviderName(),
				provider.getCategoryName() });
		
	}
	
	public void setAdapter(SyndFeed mFeeds){
		try {
			SyndEntry ent;
			mAdapter.setSelectedProvider(((MainActivity)getActivity()).selectedProvider);
			feedResults = mFeeds.getEntries();
			Iterator<SyndEntry> iterator = feedResults.listIterator();
			mAdapter.clear();
			while (iterator.hasNext()) {
				ent = iterator.next();
				mAdapter.add(ent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {
		Log.i("pageflip", "Page: " + position);
//		if (position > mFlipView.getPageCount() - 3
//				&& mFlipView.getPageCount() < feedResults.size()) {
//			mAdapter.addItems(5);
//		}
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
}
