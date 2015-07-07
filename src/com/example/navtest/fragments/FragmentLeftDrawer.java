package com.example.navtest.fragments;

import java.io.InputStream;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.navtest.MainActivity;
import com.example.navtest.R;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.google.android.gms.common.SignInButton;

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
	RelativeLayout progress_bar_layout;
	ProgressBar progress;

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
			loginButton.setUserInfoChangedCallback(new UserInfoChangedCallback() {

						@Override
						public void onUserInfoFetched(GraphUser user) {
							if (user != null) {

//								username.setText("You are currently logged in as "
//										+ user.getName());

							} else {

								//username.setText("You are not logged in.");

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
		Button lbuttonLogout = (Button) containerView
				.findViewById(R.id.buttonLogout);
		lbuttonLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				not_logged_in.setVisibility(View.VISIBLE);
				logged_in.setVisibility(View.INVISIBLE);
				((MainActivity) getActivity()).mSignInClicked = false;
				((MainActivity) getActivity()).logedIn = false;
				((MainActivity) getActivity()).signOutFromGplus();
			}
		});
		not_logged_in.setVisibility(View.INVISIBLE);
		logged_in.setVisibility(View.VISIBLE);
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
