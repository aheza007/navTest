package com.example.navtest.fragments;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.model.FeedProvider;
import com.example.navtest.MainActivity;
import com.example.navtest.R;
import com.example.navtest.adapters.ExpendableListViewAdapter;
import com.google.android.gms.common.SignInButton;

@SuppressLint("NewApi")
public class FragmentLeftDrawer extends Fragment {

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private View containerView;
	private static String[] titles = null;
	private FragmentDrawerListener drawerListener;
	private SignInButton btnSignIn;
	private Button explorerNotLoggedin;
	LinearLayout not_logged_in;
	LinearLayout logged_in;
	LinearLayout go_to_home;
	LinearLayout go_to_explorer;
	RelativeLayout progress_bar_layout;
	ProgressBar progress;
	ExpendableListViewAdapter listAdapter;
	ExpandableListView mListView;
	List<String> listDataHeader;
	private static boolean mFavoriteDispl=false;
	public static HashMap<String, List<FeedProvider>> listDataChild;

	HashMap<String, FeedProvider> urlProvider;

	public FragmentLeftDrawer() {
	}

	public void setDrawerListener(FragmentDrawerListener listener) {
		this.drawerListener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflating view layout
		View layout = inflater.inflate(R.layout.fragment_not_loggin, container,
				false);
		return layout;
	}

	public void setUp(int fragmentId, DrawerLayout drawerLayout,
			final Toolbar toolbar) {
		try {
			setUpDrawer(drawerLayout, toolbar);
			containerView = getActivity().findViewById(fragmentId);
			not_logged_in = (LinearLayout) containerView
					.findViewById(R.id.not_logged_in);
			// progress = (ProgressBar) containerView
			// .findViewById(R.id.progressBar);
			progress_bar_layout = (RelativeLayout) containerView
					.findViewById(R.id.progress_bar_layout);
			not_logged_in.setVisibility(View.VISIBLE);
			if (logged_in != null)
				logged_in.setVisibility(View.INVISIBLE);
			explorerNotLoggedin = (Button) containerView
					.findViewById(R.id.explorer_not_loggedin);
			btnSignIn = (SignInButton) containerView
					.findViewById(R.id.btn_sign_in);

			explorerNotLoggedin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity) getActivity()).closeDrawer();
					((MainActivity) getActivity()).displayView(3);
				}
			});

			// Button click listeners
			btnSignIn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!((MainActivity) getActivity()).mGoogleApiClient
							.isConnecting()) {
						not_logged_in.setVisibility(View.INVISIBLE);
						progress_bar_layout.setVisibility(View.VISIBLE);
						((MainActivity) getActivity()).signInWithGplus();
					}

				}
			});

		} catch (Exception e) {
			Log.e("SETDRAWER", "Error setting navigation Drawer");
		}

	}

	LinearLayout mAddContentLayout;

	public void setUp(int fragmentId, DrawerLayout drawerLayout,
			final Toolbar toolbar, boolean isLoggedIn) {

		try {
			setUpDrawer(drawerLayout, toolbar);
			containerView = getActivity().findViewById(fragmentId);
			not_logged_in = (LinearLayout) containerView
					.findViewById(R.id.not_logged_in);
			if (progress_bar_layout != null
					&& progress_bar_layout.VISIBLE == View.VISIBLE)
				progress_bar_layout.setVisibility(View.INVISIBLE);
			logged_in = (LinearLayout) containerView
					.findViewById(R.id.logged_in);
			ImageView profile_image = (ImageView) containerView
					.findViewById(R.id.profile_image);
			TextView profile_name = (TextView) containerView
					.findViewById(R.id.profile_name);
			TextView profile_email = (TextView) containerView
					.findViewById(R.id.profile_email);
			profile_email.setText(((MainActivity) getActivity()).email);
			profile_name.setText(((MainActivity) getActivity()).personName);
			String photo_url = ((MainActivity) getActivity()).personPhotoUrl;
			if (photo_url != null)
				new LoadProfileImage(profile_image).execute(photo_url);
			go_to_home = (LinearLayout) containerView
					.findViewById(R.id.go_to_home);
			go_to_explorer = (LinearLayout) containerView
					.findViewById(R.id.go_to_explorer);

			mAddContentLayout = (LinearLayout) containerView
					.findViewById(R.id.go_to_add_content);

			AddContentHomeShow();
			go_to_home.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity) getActivity()).closeDrawer();
					((MainActivity) getActivity()).displayView(2);
					// getData();
				}
			});
			mAddContentLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity) getActivity()).openDrawer();

				}
			});
			go_to_explorer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity) getActivity()).closeDrawer();
					((MainActivity) getActivity()).displayView(3);
				}
			});
			mListView = (ExpandableListView) containerView
					.findViewById(R.id.listView_favorite_news_feeds);

			Button lbuttonLogout = (Button) containerView
					.findViewById(R.id.buttonLogout);

			prepareFavoriteListView();

			lbuttonLogout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					not_logged_in.setVisibility(View.VISIBLE);
					logged_in.setVisibility(View.INVISIBLE);
					((MainActivity) getActivity()).mSignInClicked = false;
					// ((MainActivity) getActivity()).logedIn = false;
					((MainActivity) getActivity()).signOutFromGplus();
				}
			});
			not_logged_in.setVisibility(View.INVISIBLE);
			logged_in.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void AddContentHomeShow() {
		if (listDataChild != null && listDataChild.size() > 0) {
			go_to_home.setVisibility(View.VISIBLE);
			mAddContentLayout.setVisibility(View.GONE);
			//((MainActivity) getActivity()).displayView(2);
		} else {
			go_to_home.setVisibility(View.GONE);
			mAddContentLayout.setVisibility(View.VISIBLE);
		}
	}

	private void prepareFavoriteListView() {
		try {
			listDataHeader = new ArrayList<String>();
			listDataChild = new HashMap<String, List<FeedProvider>>();
			urlProvider = new HashMap<String, FeedProvider>();
			SharedPreferences preference = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			Set<String> mFeedPreference = preference.getStringSet(
					MainActivity.FAVORITE_NEWS, new HashSet<String>());
			if (mFeedPreference.size() > 0) {
				for (String item : mFeedPreference) {
					String[] items = item.split(MainActivity.SPLITER);
					String providerCategory = (String) items[0];
					String providerName = items[1];
					String providerLink = items[2];
					String providerIcon = items[3];

					if (!listDataHeader.contains(providerCategory))
						listDataHeader.add(providerCategory);

					FeedProvider itemProvider = new FeedProvider(
							providerCategory, providerName, providerLink,
							Integer.parseInt(providerIcon));

					if (listDataChild.containsKey(providerCategory)) {
						listDataChild.get(providerCategory).add(itemProvider);

					} else {
						List<FeedProvider> provider = new ArrayList<>();
						provider.add(itemProvider);
						listDataChild.put(providerCategory, provider);
					}

				}
				// if(listAdapter!=null)
				// listAdapter.notifyDataSetChanged();

				listAdapter = new ExpendableListViewAdapter(this.getActivity(),
						listDataHeader, listDataChild);

				// setting list adapter
				mListView.setAdapter(listAdapter);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void setUpDrawer(DrawerLayout drawerLayout, final Toolbar toolbar) {
		mDrawerLayout = drawerLayout;
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
				toolbar, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (mDrawerLayout.isDrawerOpen(Gravity.END)
						&& (drawerView.findViewById(R.id.RecyclerView) != null))
					drawerView.findViewById(R.id.RecyclerView).setVisibility(
							View.VISIBLE);
				if (mDrawerLayout.isDrawerOpen(Gravity.START)
						&& logged_in != null
						&& (logged_in.getVisibility() == View.VISIBLE)
						&& (drawerView
								.findViewById(R.id.listView_favorite_news_feeds) != null)) {
					prepareFavoriteListView();
					AddContentHomeShow();
				}
				getActivity().supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);

				LinearLayout layout = (LinearLayout) drawerView
						.findViewById(R.id.my_right_drawer_linearlayout);
				if (layout != null && layout.VISIBLE == View.VISIBLE) {
					drawerView.findViewById(R.id.my_right_drawer_linearlayout)
							.setVisibility(View.INVISIBLE);
					drawerView.invalidate();
				}
				getActivity().supportInvalidateOptionsMenu();

			}

			@SuppressLint("NewApi")
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				toolbar.setAlpha(1 - slideOffset / 2);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});
	}

	public static interface ClickListener {

		public void onClick(View view, int position);

		public void onLongClick(View view, int position);
	}

	public interface FragmentDrawerListener {

		public void onDrawerItemSelected(View view, int position);
	}

	/**
	 * Background Async task to load user profile picture from url
	 * */
	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

}
