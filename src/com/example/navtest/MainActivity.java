package com.example.navtest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.example.model.Feed;
import com.example.model.FeedProvider;
import com.example.navtest.fragments.FragmentExploreNews;
import com.example.navtest.fragments.FragmentFeedList;
import com.example.navtest.fragments.FragmentHomeFavorite;
import com.example.navtest.fragments.FragmentHomeNoFavorites;
import com.example.navtest.fragments.FragmentLeftDrawer;
import com.example.navtest.fragments.FragmentLeftDrawer.FragmentDrawerListener;
import com.example.navtest.fragments.FragmentMyFavoriteNews;
import com.example.navtest.fragments.HomeFragment;
import com.example.navtest.utils.VolleySingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class MainActivity extends ActionBarActivity implements
		FragmentDrawerListener, OnClickListener, ConnectionCallbacks,
		OnConnectionFailedListener {
	public static final String USERNAME = "USERNAME";
	public static final String USER_EMAIL = "USER_EMAIL";
	public static final String USER_PHOTO_URL = "USER_PHOTO_URL";
	public static final String FAVORITE_NEWS = "FAVORITE_NEWS";
	public static final String MY_FAVORITE_FEED_URL = "MY_FAVORITE_FEED_URL";
	static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
	public static final String SIGNED_IN_GOOGLE = "SIGNED_IN_GOOGLE";
	public static final String SPLITER = "F2:57:C7;com.example.97:BB:48:6D.navtest.fragments";
	private static final int RC_SIGN_IN = 0;
	private static final int PROFILE_PIC_SIZE = 400;

	private static final String TAG = null;
	private FragmentLeftDrawer mDrawerFragment;
	DrawerLayout mDrawerLayout;
	public FeedProvider mSelectedProvider = new FeedProvider();
	public GoogleApiClient mGoogleApiClient;
	public boolean mSignInClicked;
	private boolean mIntentInProgress;
	private ConnectionResult mConnectionResult;
	Toolbar mToolbar;
	SharedPreferences mSharedPref;
	SharedPreferences.Editor mEditor;
	// volley request queue
	public RequestQueue mRequestQueue;

	public static List<Feed> feeds = new ArrayList<Feed>();

	// ImageLoader
	public ImageLoader mImageLoader;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// get instance of the mRequestQueue
		mRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();
		mImageLoader = VolleySingleton.getInstance(this).getImageLoader();

		// As we're using a Toolbar, we should retrieve it and set it
		// to be our ActionBar
		mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("");

		mDrawerFragment = (FragmentLeftDrawer) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_left_drawer);

		// Now retrieve the DrawerLayout so that we can set the status bar
		// color.
		// This only takes effect on Lollipop, or when using
		// translucentStatusBar
		// on KitKat.
		mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		setupSharedPreference();

		updateUI(mSharedPref.getBoolean(SIGNED_IN_GOOGLE, false));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isOnline()) {
			if (checkPlayServices()) {

			}
		} else {
			Toast.makeText(this,
					" Can't connect, Are you connected to the Internet",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}

	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkPlayServices() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (status != ConnectionResult.SUCCESS) {
//			 if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
//			 showErrorDialog(status);
//			 } else {
			Toast.makeText(
					this,
					"Install Google Play service, To use the full features of the app",
					Toast.LENGTH_LONG).show();
			// finish();
//			 }
			return false;
		}
		return true;
	}

	public void showErrorDialog(int code) {
		GooglePlayServicesUtil.getErrorDialog(code, this,
				REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
	}

	/**
	 * 
	 */
	@SuppressLint("NewApi")
	private void setupSharedPreference() {
		mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		mEditor = mSharedPref.edit();
		if (!mSharedPref.contains(FAVORITE_NEWS)) {
			mEditor.putStringSet(FAVORITE_NEWS, new HashSet<String>()).apply();
		}
		if (!mSharedPref.contains(SIGNED_IN_GOOGLE)) {
			mEditor.putBoolean(SIGNED_IN_GOOGLE, false);
		}

		if (!mSharedPref.contains(MY_FAVORITE_FEED_URL)) {
			mEditor.putString(MY_FAVORITE_FEED_URL, null);
		}
		//
		// if (!mSharedPref.contains(USER_EMAIL)) {
		// mEditor.putString(USER_EMAIL, null);
		// }
		// if (!mSharedPref.contains(USER_PHOTO_URL)) {
		// mEditor.putString(USER_PHOTO_URL, null);
		// }
	}

	public void closeDrawer() {
		if (mDrawerLayout.isDrawerOpen(Gravity.END))
			mDrawerLayout.closeDrawer(Gravity.END);
		else
			mDrawerLayout.closeDrawer(Gravity.START);
	}

	public void openDrawer() {
		if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
			mDrawerLayout.closeDrawer(Gravity.START);
		}
		mDrawerLayout.openDrawer(Gravity.END);

	}

	public boolean sharingIntent = false;

	@Override
	public void onActivityResult(int requestCode, int responseCode,
			Intent intent) {

		if (!sharingIntent && requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}

		if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {
			if (responseCode == RESULT_CANCELED) {
				Toast.makeText(this, "Install Google Play services, To use this features",
						Toast.LENGTH_SHORT).show();
				//finish();
			}
		}
		super.onActivityResult(requestCode, responseCode, intent);
	}

	@SuppressLint("RtlHardcoded")
	@Override
	public void onBackPressed() {

		if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
			mDrawerLayout.closeDrawer(Gravity.START);
		} else if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
			mDrawerLayout.closeDrawer(Gravity.END);
		} else {
			if (hasCalled)
				hasCalled = false;
			super.onBackPressed();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_search) {
			openDrawer();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDrawerItemSelected(View view, int position) {

	}

	public void displayView(int position) {
		Fragment fragment = null;
		Boolean canBackStack = true;
		String tag = "";
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			// title = getString(R.string.title_home);
			tag = "HomeFragment";
			canBackStack = false;
			break;
		case 1:
			fragment = new FragmentFeedList();
			tag = "FragmentFeedList";
			break;
		case 2:
			fragment = new FragmentHomeFavorite();
			// title = getString(R.string.title_fragment_home_favorite);
			tag = "FragmentHomeFavorite";
			break;
		case 3:
			fragment = new FragmentExploreNews();
			// title = getString(R.string.title_fragment_home_favorite);
			tag = "FragmentExploreNews";
			break;
		case 4:
			fragment = new FragmentHomeNoFavorites();
			// title = getString(R.string.title_fragment_home_favorite);
			tag = "FragmentHomeNoFavorites";
			canBackStack = false;
			break;
		case 5:
			fragment = new FragmentMyFavoriteNews();
			// title = getString(R.string.title_fragment_home_favorite);
			tag = "FragmentMyFavoriteNews";
			canBackStack = false;
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment);
			// if (canBackStack)
			// fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();

		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// if(isAlreadyLoggedIn())
		mGoogleApiClient.connect();
	}

	/**
	 * @return
	 */
	public boolean isAlreadyLoggedIn() {
		// boolean val = mSharedPref.getString(USERNAME, null) == null
		// && mSharedPref.getString(USER_EMAIL, null) == null;
		boolean val = mSharedPref.getBoolean(SIGNED_IN_GOOGLE, false);
		return val;
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {

		if (result != null && !result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		// Get user's information
		getProfileInformation();
		// Update the UI after signin
		logedIn = mSharedPref.getBoolean(SIGNED_IN_GOOGLE, false);
		updateUI(logedIn);

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		updateUI(false);

	}

	/**
	 * Sign-in into google
	 * */
	public void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	/**
	 * Method to resolve any signin errors
	 * */
	public void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	public String personName = null, personPhotoUrl = null,
			personGooglePlusProfile = null, email = null;
	public boolean logedIn = false;

	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {

				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				personName = currentPerson.getDisplayName();
				personPhotoUrl = currentPerson.getImage().getUrl();
				personGooglePlusProfile = currentPerson.getUrl();
				email = Plus.AccountApi.getAccountName(mGoogleApiClient);
				// if (isAlreadyLoggedIn())
				// storeSigning(personName, email, personPhotoUrl);
				if (!(isAlreadyLoggedIn() || personGooglePlusProfile == null)) {
					storeLoginStatus(true);
					logedIn = mSharedPref.getBoolean(SIGNED_IN_GOOGLE, false);

				}
				Log.d(TAG, "Name: " + personName + ", plusProfile: "
						+ personGooglePlusProfile + ", email: " + email
						+ ", Image: " + personPhotoUrl);

				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;
			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static boolean hasCalled = false;

	/**
	 * Updating the UI, showing/hiding buttons and profile layout
	 * */
	@SuppressLint("NewApi")
	private void updateUI(boolean isSignedIn) {
		if (isSignedIn && personName != null && personPhotoUrl != null) {

			if (!hasCalled) {
				if (mSharedPref.getString(MY_FAVORITE_FEED_URL, null) != null)
					displayView(5);
				else
					displayView(4);
				mDrawerFragment.setUp(R.id.fragment_left_drawer, mDrawerLayout,
						mToolbar, isSignedIn);
				hasCalled = true;
			}
		} else {
			displayView(0);
			mDrawerFragment.setUp(R.id.fragment_left_drawer, mDrawerLayout,
					mToolbar);
		}
		mDrawerFragment.setDrawerListener(this);
	}

	// private void storeSigning(String usernName, String email,
	// String personPhotoUrl) {
	// // String usernm=mSharedPref.getString(USERNAME, personName);
	// // usernm=usernName;
	// mEditor.remove(USERNAME).commit();
	// mEditor.putString(USERNAME, usernName).commit();
	// // String useremail=mSharedPref.getString(USER_EMAIL, email);
	// // usernm=email;
	// mEditor.remove(USERNAME).commit();
	// mEditor.putString(USER_EMAIL, email).commit();
	//
	// mEditor.remove(USER_PHOTO_URL).commit();
	// mEditor.putString(USER_PHOTO_URL, personPhotoUrl).commit();
	// }

	/**
	 * 
	 */
	private void storeLoginStatus(Boolean loginStatus) {

		Boolean signedIn = mSharedPref.getBoolean(SIGNED_IN_GOOGLE, false);
		signedIn = loginStatus;
		mEditor.remove(SIGNED_IN_GOOGLE).commit();
		mEditor.putBoolean(SIGNED_IN_GOOGLE, signedIn).commit();
		logedIn = mSharedPref.getBoolean(SIGNED_IN_GOOGLE, false);
		Log.d("LOGGEDIN", "is logged in" + logedIn);
		// hasCalled = false;

	}

	/**
	 * Sign-out from google
	 * */
	public void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mSignInClicked = false;
			mGoogleApiClient.connect();
			storeLoginStatus(false);
			// displayView(0);
			hasCalled = false;
			// mEditor.clear();
			// mEditor.commit();
			updateUI(false);
		}
	}
}
