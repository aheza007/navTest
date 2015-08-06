package com.desireaheza.newsTracker.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.desireaheza.newsTracker.MainActivity;
import com.desireaheza.newsTracker.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;

public class HomeFragment extends Fragment {
	private SignInButton mBtnSignIn;
	private ProgressBar mProgressBar;
	public RelativeLayout mProgressBarLayout;
	public RelativeLayout mLoginLayout;

	public HomeFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment_not_loggedin,
				container, false);
		if (!((MainActivity) getActivity()).isAlreadyLoggedIn()) {
			mProgressBarLayout = (RelativeLayout) rootView
					.findViewById(R.id.progress_bar_layout);
			mProgressBarLayout.setVisibility(View.INVISIBLE);
			mLoginLayout = (RelativeLayout) rootView
					.findViewById(R.id.login_layout);
			mLoginLayout.setVisibility(View.VISIBLE);
			mBtnSignIn = (SignInButton) rootView.findViewById(R.id.btn_sign_in);
			mBtnSignIn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int status = GooglePlayServicesUtil
							.isGooglePlayServicesAvailable(getActivity());
					if (status == ConnectionResult.SUCCESS) {
						if (!((MainActivity) getActivity()).mGoogleApiClient
								.isConnecting()) {
							mLoginLayout.setVisibility(View.INVISIBLE);
							mProgressBarLayout.setVisibility(View.VISIBLE);
							((MainActivity) getActivity()).signInWithGplus();
						}

					} else{
						mLoginLayout.setVisibility(View.VISIBLE);
						Toast.makeText(
								getActivity(),
								"Install Google Play services, To use this features",
								Toast.LENGTH_LONG).show();
						// ((MainActivity)
						// getActivity()).showErrorDialog(status);
					}

				}
			});
		}

		// Inflate the layout for this fragment
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
}