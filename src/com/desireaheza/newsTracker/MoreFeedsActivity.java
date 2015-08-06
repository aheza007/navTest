package com.desireaheza.newsTracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.desireaheza.newsTracker.adapters.SimpleAdapter;
import com.desireaheza.newsTracker.adapters.SimpleAdapter.onGridCardItemClick;
import com.desireaheza.newsTracker.model.Feed;

public class MoreFeedsActivity extends ActionBarActivity {

	RecyclerView mRecyclerView;
	SimpleAdapter mAdapter;
	ProgressBar mProgressBar;
	static String pageTitle="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_display_more_feeds);
		
		pageTitle=getIntent().getStringExtra("TITLE_ACTIVITY");
		
		//setup actionbar
		Toolbar toolBar=(Toolbar)findViewById(R.id.my_awesome_toolbar);
		toolBar.setVisibility(View.VISIBLE);
		setSupportActionBar(toolBar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if(pageTitle!="[]"&&pageTitle!=""&&pageTitle!=null)
			getSupportActionBar().setTitle(pageTitle);
		
		mRecyclerView = (RecyclerView) findViewById(R.id.list_favorite);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar_fetchingData);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(layoutManager);
		mAdapter = new SimpleAdapter(this.getApplicationContext(),
				R.layout.long_list_favorite_item_view, MainActivity.feeds);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setOnGridItemClickListener(new onGridCardItemClick() {
			
			@Override
			public void gridItemClickListener(View v, int position) {
				Intent intent = new Intent(MoreFeedsActivity.this,
						ActivityPageNewsFeed.class);
				Bundle bundle=new Bundle();
				Feed feed=(Feed)v.getTag();
				if(pageTitle!="[]"&&pageTitle!=""&&pageTitle!=null)
					feed.setFeedProviderName(pageTitle);
				bundle.putParcelable("FEED_ITEM",feed );
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
		});
		mProgressBar.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.more_feeds, menu);
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
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
