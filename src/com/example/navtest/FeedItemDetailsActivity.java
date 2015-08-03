package com.example.navtest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class FeedItemDetailsActivity extends ActionBarActivity {

	ProgressBar progressBar_fetchingData;
	WebView webview;
	String feed_description;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_item_details);
		webview = (WebView) findViewById(R.id.webview_feed_details);
//		progressBar_fetchingData = (ProgressBar) findViewById(R.id.progressBar_fetchingData);
		feed_description = getIntent().getStringExtra("FEED_DESCRIPTION");

		// Let's display the progress in the activity title bar, like the
		// browser app does.
		webview.getSettings().setJavaScriptEnabled(true);

//		StringRequest request = new StringRequest(feed_description,
//				new Listener<String>() {
//
//					@Override
//					public void onResponse(String webcontent) {
//						webview.loadDataWithBaseURL(null, webcontent,
//								"html/text", "utf-16", null);
//						progressBar_fetchingData.setVisibility(View.INVISIBLE);
//					}
//
//				}, new ErrorListener() {
//
//					@Override
//					public void onErrorResponse(VolleyError arg0) {
//						Toast.makeText(getApplicationContext(),
//								"can't load data", Toast.LENGTH_LONG).show();
//
//					}
//				});
		webview.loadDataWithBaseURL(null, getHtmlData(feed_description),
				"html/text", "utf-8", null);
	//	progressBar_fetchingData.setVisibility(View.INVISIBLE);
	//VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(request);

	}
	private String getHtmlData(String bodyHTML) {
		String head = "<head><style>img{max-width: 100%; width:auto; height: auto;padding-top:7px; padding-bottom: 7px;}</style></head>";
		return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed_details_menu, menu);
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
