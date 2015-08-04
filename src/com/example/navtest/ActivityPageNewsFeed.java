package com.example.navtest;

import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Feed;
import com.squareup.picasso.Picasso;

public class ActivityPageNewsFeed extends ActionBarActivity {

	Feed mFeed;
	TextView mTextView_category;
	TextView mTextView_time;
	ImageView mImageView_feed_image;
	TextView mTextView_feed_title;
	TextView mTextView_provider;
	TextView mTextView_authors;
	TextView mTextView_feed_description;
	Button mButtonReadMore;
	ImageView mImageViewshare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_item);
		try {
			Toolbar toolBar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
			//toolBar.setVisibility(View.VISIBLE);
			Bundle bundle = getIntent().getExtras();
			if (bundle != null)
				mFeed = bundle.getParcelable("FEED_ITEM");

			if (mFeed != null) {
				setupLayout();
				displayItem();
			}
			else
				Toast.makeText(this, "can't display data", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
		
			e.printStackTrace();
		}
	}

	private void setupLayout() {

		mTextView_category = (TextView) findViewById(R.id.textView_category);
		mTextView_time = (TextView) findViewById(R.id.textView_time);
		mImageView_feed_image = (ImageView) findViewById(R.id.imageView_feed_image);
		mTextView_feed_title = (TextView) findViewById(R.id.textView_feed_title);
		mTextView_provider = (TextView) findViewById(R.id.textView_provider);
		mTextView_authors = (TextView) findViewById(R.id.textView_authors);
		mTextView_feed_description = (TextView) findViewById(R.id.textView_feed_description);
		mButtonReadMore = (Button) findViewById(R.id.button_read_more);
		mImageViewshare = (ImageView) findViewById(R.id.imageViewshare);

	}

	private void displayItem() {

		// mTextView_category.setText(mFeed.get)
		mTextView_time.setText("" + mFeed.getPublishedOn());
		try {
			loadImage(mImageView_feed_image, mFeed.getImageUrl());
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
		mTextView_feed_title.setText(mFeed.getTitle());
		// mTextView_provider.settext(mFeed.get)
		mTextView_authors.setText(mFeed.getAuthors());
		mTextView_feed_description.setText(mFeed.getParseDescription());

	}

	private void loadImage(ImageView pImageView_feed_image, String ImageUrl)
			throws MalformedURLException {
		URL myURL = new URL(ImageUrl);
		pImageView_feed_image.setVisibility(View.VISIBLE);
		Picasso.with(this.getApplicationContext()).load(ImageUrl)
				.placeholder(R.drawable.ic_launcher) // optional
				.error(R.drawable.rounded_corner) // optional
				.into(pImageView_feed_image);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_page_news_feed, menu);
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
