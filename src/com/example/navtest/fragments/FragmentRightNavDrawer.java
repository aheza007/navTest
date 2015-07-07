package com.example.navtest.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.example.model.Category;
import com.example.model.FeedProvider;
import com.example.navtest.MainActivity;
import com.example.navtest.R;
import com.example.navtest.adapters.GridRecyclerAdapter;
import com.example.navtest.adapters.GridRecyclerAdapter.OnGridItemClickListener;
import com.example.navtest.adapters.ListRecyclerAdapter;
import com.example.navtest.adapters.ListRecyclerAdapter.onListItemClick;

public class FragmentRightNavDrawer extends Fragment {

	View rightFragmentDrawer;
	// grid recyclerView to display main category grid
	RecyclerView rightRecyclerView;
	// list recyclerView to display feed providers in selected category
	RecyclerView rightRecyclerViewList;
	// array of category icons
	TypedArray mCategoryIcons;
	// array of category names
	String[] mCategoryTitles;
	ArrayList<Category> mCategory = new ArrayList<>();
	// main grid adapter
	GridRecyclerAdapter myGridAdapter;
	// provider adapter
	ListRecyclerAdapter myListAdapter;
	// provider layoutManager
	LinearLayoutManager mLinearLayoutManager;
	LinearLayout mLinearLayout;

	Map<String, Integer> categoryNamIcon = new HashMap<String, Integer>();
	Map<String, Integer> providerNameIcon = new HashMap<String, Integer>();
	List<FeedProvider> feedProviders = new ArrayList<FeedProvider>();
	Map<String, List<FeedProvider>> providerPerCategory = new HashMap<String, List<FeedProvider>>();

	String[] mProviderIconsName;
	String[] mProviderNames;

	InputStream jsonStream;
	Writer writer;

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
			rightFragmentDrawer = inflater.inflate(
					R.layout.fragment_right_navigation_drawer, container, false);
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
				// ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
				ImageView icon = new ImageView(getActivity());
			//	icon.setImageDrawable(getActivity().getDrawable(
				//		R.drawable.ic_action_search));
				toolbar.addView((View) icon);
			}

			toolbar.setTitle(null);
			getLayoutIconsName();
			myGridAdapter = new GridRecyclerAdapter(mCategory);
			rightRecyclerView.setLayoutManager(gridLayoutManager);
			rightRecyclerView.setAdapter(myGridAdapter);
			mLinearLayoutManager = new LinearLayoutManager(getActivity());
			rightRecyclerViewList.setLayoutManager(mLinearLayoutManager);
			myListAdapter = new ListRecyclerAdapter(this.getActivity(),feedProviders);
			//rightRecyclerViewList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
			myGridAdapter.setOnGridItemClickLister(new OnGridItemClickListener() {

				@Override
				public void onGridItemClicked(View caller, int position) {
//					Toast.makeText(
//							getActivity(),
//							mCategory.get(position).getCategoryName()
//									+ " is clicked", Toast.LENGTH_LONG).show();
					String categoryName = mCategory.get(position).getCategoryName();

					// getActivity().setTitle(selectedCategory);
					if (categoryName != null
							&& providerPerCategory.containsKey(categoryName)) {
						feedProviders.clear();
						feedProviders.addAll(providerPerCategory.get(categoryName));
					}

					if (rightRecyclerView != null)
						rightRecyclerView.setVisibility(View.INVISIBLE);
					if (mLinearLayout != null) {
						Toolbar toolbar = (Toolbar) rightFragmentDrawer
								.findViewById(R.id.my_awesome_toolbar_details);
						RelativeLayout toolBarView = (RelativeLayout) LayoutInflater
								.from(getActivity()).inflate(
										R.layout.provider_toolbar, toolbar, false);

						toolbar.addView(toolBarView);
						ImageView closeListView = (ImageView) toolbar
								.findViewById(R.id.close_button);
						TextView title = (TextView) toolbar
								.findViewById(R.id.textView_provider_name);
						title.setText(categoryName);
						closeListView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (mLinearLayout != null
										&& rightRecyclerView != null) {
									mLinearLayout.setVisibility(View.INVISIBLE);
									rightRecyclerView.setVisibility(View.VISIBLE);
								}
							}
						});
						mLinearLayout.setVisibility(View.VISIBLE);
					}

					rightRecyclerViewList.setAdapter(myListAdapter);
					// ((MainActivity)getActivity()).displayView(position);
					myListAdapter.setOnListItemClickLister(new onListItemClick() {

						@Override
						public void itemClick(View caller, int position) {
							((MainActivity)getActivity()).selectedProvider=feedProviders.get(position);
							((MainActivity) getActivity()).displayView(1);

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

	private void getLayoutIconsName() {

		try {
			// load slide menu items
			mCategoryTitles = this.getActivity().getResources()
					.getStringArray(R.array.category_name);

			mProviderNames = this.getActivity().getResources()
					.getStringArray(R.array.provider_name);
			providerNameIcon.clear();

			// nav drawer icons from resources
			mCategoryIcons = this.getActivity().getResources()
					.obtainTypedArray(R.array.category_icon);

			mProviderIconsName = this.getActivity().getResources()
					.getStringArray(R.array.provider_icons);

			for (int j = 0; j < mProviderIconsName.length; j++) {
				int id;
				try {
					id = this
							.getActivity()
							.getResources()
							.getIdentifier(
									mProviderIconsName[j],
									"drawable",
									getActivity().getApplication()
											.getPackageName());
					providerNameIcon.put(mProviderNames[j].trim(), id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			mCategory.clear();
			for (int i = 0; i < mCategoryIcons.length(); i++) {

				mCategory.add(new Category(mCategoryTitles[i], mCategoryIcons
						.getResourceId(i, -1)));
			}

			JSONObject jsonObject = new JSONObject(getCategories());
			JSONArray jsonCategories = jsonObject.getJSONArray("categories");
			int count = jsonCategories.length();
			for (int i = 0; i < count; i++) {
				JSONObject category = jsonCategories.getJSONObject(i);
				String categoryName = (String) category.keys().next();

				JSONArray providerDetail = category.getJSONArray(categoryName);
				for (int b = 0; b < providerDetail.length(); b++) {
					JSONObject item = providerDetail.getJSONObject(b);
					String providerName = item.getString("providername");
					FeedProvider provder = new FeedProvider(categoryName,
							item.getString("providername"),
							item.getString("link"),
							providerNameIcon.get(providerName.trim()));

					feedProviders.add(provder);
					if (providerPerCategory.containsKey(categoryName.trim()))
						providerPerCategory.get(categoryName.trim()).add(
								provder);
					else {
						List<FeedProvider> newList = new ArrayList<FeedProvider>();
						newList.add(provder);
						providerPerCategory.put(categoryName.trim(), newList);
					}
				}

				// FeedProvider providerItem=new FeedProvider();
				int c = jsonCategories.length();
				// System.out.print(c);
			}

		} catch (JSONException e) {
			Log.e("ERROR_RIGHT_NAVIGATION_DRAWER", e.getMessage());
		}
	}

	private String getCategories() {

		try {
			jsonStream = this.getResources().openRawResource(R.raw.category);
			writer = new StringWriter();
			char[] buffer = new char[1024];

			Reader reader = new BufferedReader(new InputStreamReader(
					jsonStream, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				jsonStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return writer.toString();
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
}
