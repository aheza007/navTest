package com.desireaheza.newsTracker.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desireaheza.newsTracker.MainActivity;
import com.desireaheza.newsTracker.R;

public class HomeFragment extends Fragment {

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
			rootView.findViewById(R.id.progressBar).setVisibility(
					View.INVISIBLE);
			rootView.findViewById(R.id.login_layout)
					.setVisibility(View.VISIBLE);
			
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