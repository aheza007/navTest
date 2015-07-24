package com.example.navtest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;

public class FeedItemDetailsActivity extends ActionBarActivity {
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_feed_item_details);
		WebView webview = (WebView) findViewById(R.id.webview_feed_details);
		String feedUrl = getIntent().getStringExtra("FEEDURL");
		// Let's display the progress in the activity title bar, like the
		// browser app does.

		webview.getSettings().setJavaScriptEnabled(true);

//		final Activity activity = this;
//		webview.setWebChromeClient(new WebChromeClient() {
//			public void onProgressChanged(WebView view, int progress) {
//				// Activities and WebViews measure progress with different
//				// scales.
//				// The progress meter will automatically disappear when we reach
//				// 100%
//				activity.setProgress(progress * 1000);
//			}
//		});
//		webview.setWebViewClient(new WebViewClient() {
//			public void onReceivedError(WebView view, int errorCode,
//					String description, String failingUrl) {
//				Toast.makeText(activity, "Oh no! " + description,
//						Toast.LENGTH_SHORT).show();
//			}
//		});

		// webview.loadUrl("http://developer.android.com/");
		webview.loadDataWithBaseURL(null, feedUrl, "html/text", "utf-8", null);
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
