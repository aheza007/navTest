package com.example.model;

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

import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.navtest.R;
import com.example.navtest.adapters.GridRecyclerAdapter;
import com.example.navtest.adapters.ListRecyclerAdapter;

public class LayoutIconsName {

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
	Map<String, FeedProvider> favoriteProviders=new HashMap<>();
	Map<String, Integer> categoryNamIcon = new HashMap<String, Integer>();
	Map<String, Integer> providerNameIcon = new HashMap<String, Integer>();
	List<FeedProvider> feedProviders = new ArrayList<FeedProvider>();
	Map<String, List<FeedProvider>> providerPerCategory = new HashMap<String, List<FeedProvider>>();
	String[] mProviderIconsName;
	String[] mProviderNames;
	InputStream jsonStream;
	Writer writer;
	FragmentActivity mActivity;

	public LayoutIconsName(FragmentActivity pActivity) {
		mActivity = pActivity;
		getLayoutIconsName();
	
	}

	private void getLayoutIconsName() {

		try {
			// load slide menu items
			mCategoryTitles = mActivity.getResources().getStringArray(
					R.array.category_name);

			mProviderNames = mActivity.getResources().getStringArray(
					R.array.provider_name);
			providerNameIcon.clear();

			// nav drawer icons from resources
			mCategoryIcons = mActivity.getResources().obtainTypedArray(
					R.array.category_icon);

			mProviderIconsName = mActivity.getResources().getStringArray(
					R.array.provider_icons);

			for (int j = 0; j < mProviderIconsName.length; j++) {
				int id;
				try {
					id = mActivity.getResources().getIdentifier(
							mProviderIconsName[j], "drawable",
							mActivity.getApplication().getPackageName());
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
					
					if(b==0){
						favoriteProviders.put(categoryName, provder);
					}
					
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

	/**
	 * @return the favoriteProviders
	 */
	public Map<String, FeedProvider> getFavoriteProviders() {
		return favoriteProviders;
	}

	private String getCategories() {

		try {
			jsonStream = mActivity.getResources().openRawResource(
					R.raw.category);
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

	/**
	 * @return the feedProviders
	 */
	public List<FeedProvider> getFeedProviders() {
		return feedProviders;
	}

	/**
	 * @return the providerPerCategory
	 */
	public Map<String, List<FeedProvider>> getProviderPerCategory() {
		return providerPerCategory;
	}

	/**
	 * @return the mCategory
	 */
	public ArrayList<Category> getmCategory() {
		return mCategory;
	}
	

}
