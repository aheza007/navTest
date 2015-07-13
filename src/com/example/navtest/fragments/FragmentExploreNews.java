package com.example.navtest.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.model.Category;
import com.example.model.FeedProvider;
import com.example.model.LayoutIconsName;
import com.example.navtest.MainActivity;
import com.example.navtest.R;
import com.example.navtest.adapters.GridRecyclerAdapter;
import com.example.navtest.adapters.GridRecyclerAdapter.OnGridItemClickListener;
import com.example.navtest.adapters.ListRecyclerAdapter;

public class FragmentExploreNews extends Fragment {

	View fragmentFavoriteView;
	// grid recyclerView to display main category grid
	RecyclerView rightRecyclerView;
	// list recyclerView to display feed providers in selected category
	RecyclerView rightRecyclerViewList;
	// provider layoutManager
	LinearLayoutManager mLinearLayoutManager;
	LinearLayout mLinearLayout;

	// main grid adapter
	GridRecyclerAdapter myGridAdapter;
	// provider adapter
	ListRecyclerAdapter myListAdapter;
	ArrayList<Category> mCategory = new ArrayList<>();
	List<FeedProvider> feedProviders = new ArrayList<FeedProvider>();
	Map<String, List<FeedProvider>> providerPerCategory = new HashMap<String, List<FeedProvider>>();

	public FragmentExploreNews() {

	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@SuppressLint("NewApi")
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		try {
			fragmentFavoriteView = inflater.inflate(
					R.layout.fragment_home_explorer_favorite, container, false);
			rightRecyclerView = (RecyclerView) fragmentFavoriteView
					.findViewById(R.id.list_favorite);
			ProgressBar progressBar = (ProgressBar) fragmentFavoriteView
					.findViewById(R.id.progressBar_fetchingData);
			progressBar.setVisibility(View.GONE);

			GridLayoutManager gridLayoutManager = new GridLayoutManager(
					getActivity(), 2);

			getLayoutIconsName();
			myGridAdapter = new GridRecyclerAdapter(mCategory);
			rightRecyclerView.setLayoutManager(gridLayoutManager);
			rightRecyclerView.setAdapter(myGridAdapter);

			myGridAdapter
					.setOnGridItemClickLister(new OnGridItemClickListener() {

						@Override
						public void onGridItemClicked(View caller, int position) {
							String categoryName = mCategory.get(position)
									.getCategoryName();

							if (categoryName != null
									&& providerPerCategory
											.containsKey(categoryName)) {

								((MainActivity) getActivity()).selectedProvider = layoutIconsName
										.getFavoriteProviders().get(
												categoryName);
								((MainActivity) getActivity()).displayView(1);
							}

						}
					});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fragmentFavoriteView;
	}

	LayoutIconsName layoutIconsName;

	public void getLayoutIconsName() {
		layoutIconsName = new LayoutIconsName(getActivity());
		// layoutIconsName.getFeedProviders();
		providerPerCategory = layoutIconsName.getProviderPerCategory();
		mCategory = layoutIconsName.getmCategory();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

	}
}
