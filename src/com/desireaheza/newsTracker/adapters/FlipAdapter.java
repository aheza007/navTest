package com.desireaheza.newsTracker.adapters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.desireaheza.newsTracker.FeedItemDetailsActivity;
import com.desireaheza.newsTracker.MainActivity;
import com.desireaheza.newsTracker.R;
import com.desireaheza.newsTracker.model.Feed;
import com.desireaheza.newsTracker.model.FeedProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusShare;
import com.squareup.picasso.Picasso;

@SuppressLint("NewApi")
public class FlipAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private Callback callback;
	private List<Feed> syndFeedsEntry;
	MainActivity mContext;
	FeedProvider mProvider = new FeedProvider();
	static final int ViewBigPhoto = 0;
	static final int ViewSmallPhoto = 0;
	static boolean isBigPicture = false;

	// static onFlipItemClick mFlipItemClickListener;

	public FlipAdapter(MainActivity context, List<Feed> pitems) {
		inflater = LayoutInflater.from(context);
		mContext = context;
		syndFeedsEntry = pitems;

	}

	public void setSelectedProvider(FeedProvider pProvider) {
		mProvider = pProvider;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	@Override
	public int getCount() {
		return syndFeedsEntry.size() == 0 ? 0 : syndFeedsEntry.size();
	}

	@Override
	public Object getItem(int position) {

		return syndFeedsEntry.size() == 0 ? null : syndFeedsEntry.get(position);
	}

	@Override
	public long getItemId(int position) {
		// syndFeedsEntry.get(position).getId()
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	Feed feedItem = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		feedItem = (Feed) getItem(position);

		int type = getItemViewType(position);

		if (convertView == null) {
			holder = new ViewHolder();
			if (type == 0)
				convertView = inflater.inflate(R.layout.page_news_feed, parent,
						false);
			else if (type == 1)
				convertView = inflater.inflate(
						R.layout.page_news_feed_second_view, parent, false);
			initializeHolder(convertView, holder, type);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		convertView.setTag(
				R.id.textView_feed_description,
				"<h1>" + feedItem.getTitle() + "</h1>" + feedItem.getAuthors()
						+ "/" + feedItem.getPublishedOn() + "</br>"
						+ feedItem.getDescription());
		String ImageUrl = feedItem.getImageUrl();
		String descCont = feedItem.getParseDescription();
		String nowtime=""+System.currentTimeMillis();
		String feedTime=""+feedItem.getPublishedOn();
		
		CharSequence time=DateUtils.getRelativeTimeSpanString( feedItem.getPublishedOn(),System.currentTimeMillis(), 0);
		holder.textView_time.setText(time);
		holder.textView_category.setText(mProvider.getCategoryName());
		holder.textView_provider.setText(mProvider.getProviderName());
		if(feedItem.getAuthors().length()>0)
			holder.textView_authors.setText("by "+feedItem.getAuthors());
		else
			holder.textView_authors.setVisibility(View.INVISIBLE);
		holder.textView_feed_title.setText(Html.fromHtml(feedItem.getTitle()));
		if (holder.viewType == 0)
			holder.textView_feed_description
					.setText(trimTrailingWhitespace(Html.fromHtml(descCont)));

		if (hasImage(ImageUrl, descCont)) {
			try {
				loadImage(holder, ImageUrl);
			} catch (MalformedURLException e) {
				holder.imageView_feed_image.setVisibility(View.GONE);
			}
			// } else if (imgURL != "") {
			// try {
			// loadImage(holder, imgURL);
			// } catch (MalformedURLException e) {
			// e.printStackTrace();
			// }

		} else {
			holder.imageView_feed_image.setImageResource(mProvider
					.getProviderIcon());
		}
		// convertView.setTag(R.id.textView_feed_description, Description);
		holder.imageViewshare.setTag(R.id.imageView_feed_image, feedItem);
		holder.buttonReadMore.setTag(R.id.imageView_feed_image, feedItem);
		holder.imageViewshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int status = GooglePlayServicesUtil
						.isGooglePlayServicesAvailable(mContext);
				if (status == ConnectionResult.SUCCESS) {
					// Launch the Google+ share dialog with attribution to your
					// app.
					Feed feedItem = (Feed) v.getTag(R.id.imageView_feed_image);
					Uri feedUri = Uri.parse(feedItem.getUrl());
					Intent shareIntent = new PlusShare.Builder(mContext)
							.setType("text/plain").setText(feedItem.getTitle())
							.setContentUrl(feedUri).getIntent();
					mContext.sharingIntent = true;
					mContext.startActivityForResult(shareIntent, 0);

				}
				else
					Toast.makeText(mContext, "Install Google Play services, To use this features",
							Toast.LENGTH_LONG).show();   
			}
		});

		holder.buttonReadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Feed feedItem = (Feed) v.getTag(R.id.imageView_feed_image);
				Uri uri = Uri.parse(feedItem.getUrl());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				if (intent.resolveActivity(mContext.getPackageManager()) != null) {
					mContext.startActivity(intent);
				}
			}
		});
		// convertView.setTag(R.id.textView_feed_description,
		// feedItem.getUri());
		return convertView;
	}

	/**
	 * @param ImageUrl
	 * @param descCont
	 * @return
	 */
	private boolean hasImage(String ImageUrl, String descCont) {
		return ImageUrl != ""
				&& (!(descCont.isEmpty() || descCont.equals(" ") || descCont
						.equals("")) & descCont.length() > 20);
	}

	private void loadImage(ViewHolder holder, String ImageUrl)
			throws MalformedURLException {
		URL myURL = new URL(ImageUrl);
		holder.imageView_feed_image.setVisibility(View.VISIBLE);
		Picasso.with(mContext).load(ImageUrl)
				.placeholder(R.drawable.ic_launcher) // optional
				.error(R.drawable.ic_launcher) // optional
				.into(holder.imageView_feed_image);
	}

	private void initializeHolder(View convertView, ViewHolder holder, int type) {
		holder.textView_category = (TextView) convertView
				.findViewById(R.id.textView_category);
		holder.textView_time = (TextView) convertView
				.findViewById(R.id.textView_time);

		holder.textView_feed_title = (TextView) convertView
				.findViewById(R.id.textView_feed_title);
		holder.textView_provider = (TextView) convertView
				.findViewById(R.id.textView_provider);
		holder.textView_authors = (TextView) convertView
				.findViewById(R.id.textView_authors);
		if (holder.viewType == 0)
			holder.textView_feed_description = (TextView) convertView
					.findViewById(R.id.textView_feed_description);
		holder.imageView_feed_image = (ImageView) convertView
				.findViewById(R.id.imageView_feed_image);
		holder.imageViewshare = (ImageView) convertView
				.findViewById(R.id.imageViewshare);
		holder.buttonReadMore = (Button) convertView
				.findViewById(R.id.buttonReadMore);
		/*convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Toast.makeText(mContext,
				// "item Clicked: "+v.getTag(R.id.textView_feed_description),
				// Toast.LENGTH_LONG).show();
				Intent intent = new Intent(mContext,
						FeedItemDetailsActivity.class);
				intent.putExtra("FEED_DESCRIPTION",
						(String) v.getTag(R.id.textView_feed_description));
				mContext.startActivity(intent);
			}
		});*/
		holder.viewType = type;
		convertView.setTag(holder);
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

	static class ViewHolder {
		public int viewType;
		TextView text;
		TextView textView_category;
		TextView textView_time;
		ImageView imageView_feed_image;
		TextView textView_feed_title;
		TextView textView_provider;
		TextView textView_authors;
		TextView textView_feed_description;
		Button buttonReadMore;
		ImageView imageViewshare;

	}

	@Override
	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.imageViewshare:
		// if (callback != null) {
		// callback.onPageRequested(0);
		// }
		// break;
		// case R.id.buttonReadMore:
		// if (callback != null) {
		// callback.onPageRequested(getCount() - 1);
		// }
		// break;
		// }
	}

	public void clear() {
		if (syndFeedsEntry.size() != 0) {
			syndFeedsEntry.clear();
			notifyDataSetChanged();
		}
	}

	public void add(Feed entry) {
		syndFeedsEntry.add(entry);
		notifyDataSetChanged();
	}

	public void addItems(int amount) {
		// for (int i = 0; i < amount; i++) {
		// items.add(new Item());
		// }
		// notifyDataSetChanged();
	}

	public void addItemsBefore(int amount) {
		// for (int i = 0; i < amount; i++) {
		// items.add(0, new Item());
		// }
		// notifyDataSetChanged();
	}

	@Override
	public int getViewTypeCount() {

		return 2;
	}

	@Override
	public int getItemViewType(int position) {

		return ((Feed) getItem(position)).getDescription() != "" ? 0 : 1;
	}

	public interface Callback {
		public void onPageRequested(int page);
	}

}
