package com.desireaheza.newsTracker;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.DateTimeKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusShare;
import com.squareup.picasso.Picasso;

public class ActivityPageNewsFeed extends ActionBarActivity {

	com.desireaheza.newsTracker.model.Feed mFeed;
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
			setSupportActionBar(toolBar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			Bundle bundle = getIntent().getExtras();
			if (bundle != null)
				mFeed = bundle.getParcelable("FEED_ITEM");

			if (mFeed != null) {
				setupLayout();
				displayItem();
			} else
				Toast.makeText(this, "can't display data", Toast.LENGTH_LONG)
						.show();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		if (mFeed.getFeedProviderCategory() != null
				&& mFeed.getFeedProviderName() != null) {
			getSupportActionBar().setTitle(
					mFeed.getFeedProviderCategory() + "-"
							+ mFeed.getFeedProviderName());
		} else if (mFeed.getFeedProviderName() != null)
			getSupportActionBar().setTitle(mFeed.getFeedProviderName());
		super.onResume();
	}

	private void setupLayout() {

		mTextView_category = (TextView) findViewById(R.id.textView_category);

		mTextView_time = (TextView) findViewById(R.id.textView_time);
		mImageView_feed_image = (ImageView) findViewById(R.id.imageView_feed_image);
		mTextView_feed_title = (TextView) findViewById(R.id.textView_feed_title);
		mTextView_provider = (TextView) findViewById(R.id.textView_provider);
		mTextView_authors = (TextView) findViewById(R.id.textView_authors);
		mTextView_feed_description = (TextView) findViewById(R.id.textView_feed_description);
		mButtonReadMore = (Button) findViewById(R.id.buttonReadMore);
		mImageViewshare = (ImageView) findViewById(R.id.imageViewshare);

		mImageViewshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int status = GooglePlayServicesUtil
						.isGooglePlayServicesAvailable(ActivityPageNewsFeed.this);
				if (status == ConnectionResult.SUCCESS) {
					// Launch the Google+ share dialog with attribution to your
					// app.
					if (mFeed != null) {
						Uri feedUri = Uri.parse(mFeed.getUrl());
						Intent shareIntent = new PlusShare.Builder(
								ActivityPageNewsFeed.this)
								.setType("text/plain")
								.setText(mFeed.getTitle())
								.setContentUrl(feedUri).getIntent();
						// sharingIntent = true;
						startActivityForResult(shareIntent, 0);
					}
				} else
					Toast.makeText(
							ActivityPageNewsFeed.this,
							"Install Google Play services, To use this features",
							Toast.LENGTH_LONG).show();
			}
		});

		mButtonReadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mFeed != null) {
					// Feed feedItem = (Feed)
					// v.getTag(R.id.imageView_feed_image);
					Uri uri = Uri.parse(mFeed.getUrl());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					if (intent.resolveActivity(ActivityPageNewsFeed.this
							.getPackageManager()) != null) {
						startActivity(intent);
					}
				}
			}
		});
	}

	private void displayItem() {

		if (mFeed.getFeedProviderCategory() != null && mFeed.getFeedProviderName()!=null) {
			mTextView_category.setVisibility(View.VISIBLE);
			mTextView_provider.setVisibility(View.VISIBLE);
			mTextView_category.setText(mFeed.getFeedProviderCategory());
			mTextView_provider.setText(mFeed.getFeedProviderName());

		} 
		else if (mFeed.getFeedProviderCategory() == null &&mFeed.getFeedProviderName()!=null){
			mTextView_category.setVisibility(View.INVISIBLE);
			mTextView_provider.setVisibility(View.VISIBLE);
			mTextView_provider.setText(mFeed.getFeedProviderName());
		}
		else {
			mTextView_category.setVisibility(View.INVISIBLE);
			mTextView_provider.setVisibility(View.INVISIBLE);
		}
		CharSequence time=DateUtils.getRelativeTimeSpanString( mFeed.getPublishedOn(), System.currentTimeMillis(),0);
			
		mTextView_time.setText(time);
		
		if (mFeed.getAuthors().length() > 0)
			mTextView_authors.setText("by " + mFeed.getAuthors());
		else
			mTextView_authors.setVisibility(View.INVISIBLE);

		try {
			loadImage(mImageView_feed_image, mFeed.getImageUrl());
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
		mTextView_feed_title.setText(mFeed.getTitle());
		// mTextView_provider.settext(mFeed.get)
		mTextView_authors.setText(mFeed.getAuthors());
		//mTextView_feed_description.setText(mFeed.getParseDescription());
		mTextView_feed_description.setText(trimTrailingWhitespace(Html.fromHtml(mFeed.getParseDescription())));
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

	public static CharSequence trimTrailingWhitespace(CharSequence source) {

		if (source == null)
			return "";

		int i = source.length();

		// loop back to the first non-whitespace character
		while (--i >= 0 && Character.isWhitespace(source.charAt(i))) {
		}

		return source.subSequence(0, i + 1);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.activity_page_news_feed, menu);
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
