package com.desireaheza.newsTracker.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desireaheza.newsTracker.R;

public class FragmentHomeNoFavorites extends Fragment {

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.fragment_logedin_home_nofavorite,container,false);
		return rootView;
	}
}
