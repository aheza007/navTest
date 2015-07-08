package com.example.navtest.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Toast;

import com.example.model.FeedProvider;
import com.example.navtest.MainActivity;
import com.example.navtest.R;
import com.example.navtest.adapters.ExpendableListViewAdapter;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.google.android.gms.common.SignInButton;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReaderException;

@SuppressLint("NewApi")
public class FragmentLeftDrawer extends Fragment {

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private View containerView;
	private static String[] titles = null;
	private FragmentDrawerListener drawerListener;
	private SignInButton btnSignIn;
	private LoginButton loginButton;
	LinearLayout not_logged_in;
	LinearLayout logged_in;
	LinearLayout go_to_home;
	RelativeLayout progress_bar_layout;
	ProgressBar progress;
	ExpendableListViewAdapter listAdapter;
	ExpandableListView mListView;
	List<String> listDataHeader;
	HashMap<String, List<FeedProvider>> listDataChild;

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
			btnSignIn = (SignInButton) containerView
					.findViewById(R.id.btn_sign_in);
			loginButton = (LoginButton) containerView
					.findViewById(R.id.facebook_login_button);
			loginButton.setReadPermissions(Arrays.asList("email"));
			loginButton
					.setUserInfoChangedCallback(new UserInfoChangedCallback() {

						@Override
						public void onUserInfoFetched(GraphUser user) {
							if (user != null) {

								// username.setText("You are currently logged in as "
								// + user.getName());

							} else {

								// username.setText("You are not logged in.");

							}

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
			logged_in = (LinearLayout) containerView.findViewById(R.id.logged_in);
			ImageView profile_image = (ImageView) containerView
					.findViewById(R.id.profile_image);
			TextView profile_name = (TextView) containerView
					.findViewById(R.id.profile_name);
			TextView profile_email = (TextView) containerView
					.findViewById(R.id.profile_email);
			profile_email.setText(((MainActivity) getActivity()).email);
			profile_name.setText(((MainActivity) getActivity()).personName);
			new LoadProfileImage(profile_image)
					.execute(((MainActivity) getActivity()).personPhotoUrl);
			go_to_home = (LinearLayout) containerView.findViewById(R.id.go_to_home);
			go_to_home.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getData();
				}
			});
			mListView = (ExpandableListView) containerView
					.findViewById(R.id.listView_favorite_news_feeds);
			// int widthMeasureSpec =
			// View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT,
			// View.MeasureSpec.EXACTLY);
			// int heightMeasureSpec =
			// View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT,
			// View.MeasureSpec.EXACTLY);
			// mListView.measure(widthMeasureSpec, heightMeasureSpec);

			// mListView.setOnTouchListener(new OnTouchListener() {
			// // Setting on Touch Listener for handling the touch inside ScrollView
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// // Disallow the touch request for parent scroll on touch of child
			// view
			// v.getParent().requestDisallowInterceptTouchEvent(true);
			// return false;
			// }
			// });

			LayoutInflater inflater = LayoutInflater.from(getActivity());
			final View logoutView = inflater.inflate(R.layout.logout_view,
					mListView, false);
			mListView.addFooterView(logoutView);
			Button lbuttonLogout = (Button) logoutView
					.findViewById(R.id.buttonLogout);

			prepareFavoriteListView();

			lbuttonLogout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mListView.removeFooterView(logoutView);
					not_logged_in.setVisibility(View.VISIBLE);
					logged_in.setVisibility(View.INVISIBLE);
					((MainActivity) getActivity()).mSignInClicked = false;
					((MainActivity) getActivity()).logedIn = false;
					((MainActivity) getActivity()).signOutFromGplus();
				}
			});
			not_logged_in.setVisibility(View.INVISIBLE);
			logged_in.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
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

					FeedProvider itemProvider = new FeedProvider(providerCategory,
							providerName, providerLink,
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
				if (mDrawerLayout.isDrawerOpen(Gravity.START)&&logged_in!=null
						&& (logged_in.getVisibility() == View.VISIBLE)
						&& (drawerView
								.findViewById(R.id.listView_favorite_news_feeds) != null))
					prepareFavoriteListView();
				getActivity().supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
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

	private void getData() {
		try {
			LoadHomeNews loadHomeNews = new LoadHomeNews(this.getActivity());
			Iterator<Entry<String, List<FeedProvider>>> it = listDataChild
					.entrySet().iterator();
			List<String> provs = new ArrayList<>();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				List<FeedProvider> p = (List<FeedProvider>) pair.getValue();
				provs.add(p.get(0).getProviderUrl());
				//it.remove(); // avoids a ConcurrentModificationException
			}
			String[] urls = new String[provs.size()];
			int i=0;
			for(String url: provs)
			{	
				urls[i]=url;
				i++;
			}
			loadHomeNews.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urls);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private class LoadHomeNews extends
			AsyncTask<String, String, List<SyndFeed>> {
		Context mContext;

		public LoadHomeNews(Context context) {
			mContext = context;
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
			return results;
		}

		@Override
		protected void onPostExecute(List<SyndFeed> result) {
			try {
				Toast.makeText(mContext, result.size() + " fetched News   Feeds",
						Toast.LENGTH_LONG).show();
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
