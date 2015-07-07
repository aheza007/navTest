package com.example.navtest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
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

import com.example.model.FeedProvider;
import com.example.navtest.fragments.FragmentFeedList;
import com.example.navtest.fragments.FragmentLeftDrawer;
import com.example.navtest.fragments.FragmentLeftDrawer.FragmentDrawerListener;
import com.example.navtest.fragments.HomeFragment;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
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
	private static final int RC_SIGN_IN = 0;
	private static final int PROFILE_PIC_SIZE = 400;
	private static final String TAG = null;
	private FragmentLeftDrawer drawerFragment;
	DrawerLayout drawerLayout;
	public FeedProvider selectedProvider = new FeedProvider();
	public GoogleApiClient mGoogleApiClient;
	public boolean mSignInClicked;
	private boolean mIntentInProgress;
	private ConnectionResult mConnectionResult;
	Toolbar toolbar;
	// Create, automatically open (if applicable), save, and restore the
	// Active Session in a way that is similar to Android UI lifecycles.
	public UiLifecycleHelper uiHelper;
	private Object FacebookSdk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// As we're using a Toolbar, we should retrieve it and set it
		// to be our ActionBar
		toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");
		drawerFragment = (FragmentLeftDrawer) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_left_drawer);

		// Now retrieve the DrawerLayout so that we can set the status bar
		// color.
		// This only takes effect on Lollipop, or when using
		// translucentStatusBar
		// on KitKat.
		drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);

		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		displayView(0);
		updateUI(logedIn);
	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				Log.d("MainActivity", "Facebook session opened.");
			} else if (state.isClosed()) {
				Log.d("MainActivity", "Facebook session closed.");
			}
		}
	};
	public boolean sharingIntent = false;

	@Override
	public void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);
		if (!sharingIntent && requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	@SuppressLint("RtlHardcoded")
	@Override
	public void onBackPressed() {

		if (drawerLayout.isDrawerOpen(Gravity.START)) {
			drawerLayout.closeDrawer(Gravity.START);
		} else if (drawerLayout.isDrawerOpen(Gravity.END)) {
			drawerLayout.closeDrawer(Gravity.END);
		} else {
			super.onBackPressed();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDrawerItemSelected(View view, int position) {

	}

	public void displayView(int position) {
		Fragment fragment = null;
		String title = getString(R.string.app_name);
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			// title = getString(R.string.title_home);
			break;
		case 1:
			// Toast.makeText(this, "need to display list Item fragment",
			// Toast.LENGTH_LONG).show();
			fragment = new FragmentFeedList();
			break;
		// fragment = new FriendsFragment();
		// title = getString(R.string.title_friends);
		// break;
		// case 2:
		// fragment = new MessagesFragment();
		// title = getString(R.string.title_messages);
		// break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment);
			fragmentTransaction.commit();

		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
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
				if (!(personName == null && email == null || personGooglePlusProfile == null))
					logedIn = true;
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

	/**
	 * Updating the UI, showing/hiding buttons and profile layout
	 * */
	private void updateUI(boolean isSignedIn) {
		if (isSignedIn && personName != null && email != null) {
			drawerFragment.setUp(R.id.fragment_left_drawer, drawerLayout,
					toolbar, isSignedIn);
		}

		else
			drawerFragment.setUp(R.id.fragment_left_drawer, drawerLayout,
					toolbar);
		drawerFragment.setDrawerListener(this);
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
			updateUI(mSignInClicked);
		}
	}
}
