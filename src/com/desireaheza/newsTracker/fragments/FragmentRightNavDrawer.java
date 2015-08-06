package com.desireaheza.newsTracker.fragments;

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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.desireaheza.newsTracker.MainActivity;
import com.desireaheza.newsTracker.R;
import com.desireaheza.newsTracker.adapters.GridRecyclerAdapter;
import com.desireaheza.newsTracker.adapters.GridRecyclerAdapter.OnGridItemClickListener;
import com.desireaheza.newsTracker.adapters.ListRecyclerAdapter;
import com.desireaheza.newsTracker.adapters.ListRecyclerAdapter.onListItemClick;
import com.desireaheza.newsTracker.model.Category;
import com.desireaheza.newsTracker.model.FeedProvider;
import com.desireaheza.newsTracker.model.LayoutIconsName;

public class FragmentRightNavDrawer extends Fragment {

	View rightFragmentDrawer;
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

	public FragmentRightNavDrawer() {

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
			rightFragmentDrawer = inflater
					.inflate(R.layout.fragment_right_navigation_drawer,
							container, false);
			rightRecyclerView = (RecyclerView) rightFragmentDrawer
					.findViewById(R.id.RecyclerView);
			rightRecyclerViewList = (RecyclerView) rightFragmentDrawer
					.findViewById(R.id.RecyclerViewList);
			Toolbar toolbar = (Toolbar) rightFragmentDrawer
					.findViewById(R.id.my_awesome_toolbar);
			GridLayoutManager gridLayoutManager = new GridLayoutManager(
					getActivity(), 2);

			mLinearLayout = (LinearLayout) rightFragmentDrawer
					.findViewById(R.id.my_right_drawer_linearlayout);

			// load slide menu items
			if (toolbar != null) {
				// ((ActionBarActivity)getActivity()).setSupportActionBar(mToolbar);
				ImageView icon = new ImageView(getActivity());
				// icon.setImageDrawable(getActivity().getDrawable(
				// R.drawable.ic_action_search));
				toolbar.addView((View) icon);
			}

			toolbar.setTitle(null);
			getLayoutIconsName();
			myGridAdapter = new GridRecyclerAdapter(mCategory);
			rightRecyclerView.setLayoutManager(gridLayoutManager);
			rightRecyclerView.setAdapter(myGridAdapter);
			mLinearLayoutManager = new LinearLayoutManager(getActivity());
			rightRecyclerViewList.setLayoutManager(mLinearLayoutManager);
			myListAdapter = new ListRecyclerAdapter(this.getActivity(),
					feedProviders);
			// rightRecyclerViewList.addItemDecoration(new
			// DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
			myGridAdapter
					.setOnGridItemClickLister(new OnGridItemClickListener() {

						@Override
						public void onGridItemClicked(View caller, int position) {
							// Toast.makeText(
							// getActivity(),
							// mCategory.get(position).getCategoryName()
							// + " is clicked", Toast.LENGTH_LONG).show();
							String categoryName = mCategory.get(position)
									.getCategoryName();

							// getActivity().setTitle(selectedCategory);
							if (categoryName != null
									&& providerPerCategory
											.containsKey(categoryName)) {
								feedProviders.clear();
								feedProviders.addAll(providerPerCategory
										.get(categoryName));
							}

							if (rightRecyclerView != null)
								rightRecyclerView.setVisibility(View.INVISIBLE);
							if (mLinearLayout != null) {
								Toolbar toolbar = (Toolbar) rightFragmentDrawer
										.findViewById(R.id.my_awesome_toolbar_details);
								RelativeLayout toolBarView = (RelativeLayout) LayoutInflater
										.from(getActivity()).inflate(
												R.layout.provider_toolbar,
												toolbar, false);

								toolbar.addView(toolBarView);
								ImageView closeListView = (ImageView) toolbar
										.findViewById(R.id.close_button);
								TextView title = (TextView) toolbar
										.findViewById(R.id.textView_provider_name);
								title.setText(categoryName);
								closeListView
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												closeProviderListView();
											}
										});
								mLinearLayout.setVisibility(View.VISIBLE);
							}

							rightRecyclerViewList.setAdapter(myListAdapter);
							// ((MainActivity)getActivity()).displayView(position);
							myListAdapter
									.setOnListItemClickLister(new onListItemClick() {

										@Override
										public void itemClick(View caller,
												int position) {
											((MainActivity)getActivity()).mSelectedProvider = feedProviders
													.get(position);
											((MainActivity) getActivity()).closeDrawer();
											((MainActivity) getActivity())
													.displayView(1);

										}
									});
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rightFragmentDrawer;
	}

	public void getLayoutIconsName() {
		LayoutIconsName layoutIconsName = new LayoutIconsName(getActivity());
		feedProviders = layoutIconsName.getFeedProviders();
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
		// Inflate the menu; this adds items to the action bar if it is present.
		// inflater.inflate(R.menu.launch, menu);

	}

	private void closeProviderListView() {
		if (mLinearLayout != null
				&& rightRecyclerView != null) {
			mLinearLayout
					.setVisibility(View.INVISIBLE);
			rightRecyclerView
					.setVisibility(View.VISIBLE);
		}
	}
}
