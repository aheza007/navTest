package com.example.navtest;

import com.android.volley.toolbox.ImageLoader;
import com.example.navtest.adapters.SimpleAdapter;
import com.example.navtest.utils.VolleySingleton;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

public class MoreFeedsActivity extends ActionBarActivity {

	RecyclerView mRecyclerView;
	SimpleAdapter mAdapter;
	ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_display_more_feeds);
		mRecyclerView = (RecyclerView) findViewById(R.id.list_favorite);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar_fetchingData);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(layoutManager);
		mAdapter = new SimpleAdapter(this.getApplicationContext(),
				R.layout.long_list_favorite_item_view, MainActivity.feeds);
		mRecyclerView.setAdapter(mAdapter);
		mProgressBar.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.more_feeds, menu);
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
}
