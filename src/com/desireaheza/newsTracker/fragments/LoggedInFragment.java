package com.desireaheza.newsTracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desireaheza.newsTracker.R;
import com.google.android.gms.common.SignInButton;


public class LoggedInFragment extends Fragment {

	View rootView;
	SignInButton btnSignIn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.logged_in_fragment, container,
				false);
		

		return rootView;
	}
}