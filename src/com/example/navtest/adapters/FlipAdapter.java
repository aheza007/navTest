package com.example.navtest.adapters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.FeedProvider;
import com.example.navtest.MainActivity;
import com.example.navtest.R;
import com.google.android.gms.plus.PlusShare;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEnclosure;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.squareup.picasso.Picasso;

@SuppressLint("NewApi")
public class FlipAdapter extends BaseAdapter implements OnClickListener {

	public interface Callback {
		public void onPageRequested(int page);
	}

	static class Item {
		static long id = 0;
		String String_category = "News";

		String String_time = "1h";
		String String_feed_title = "Title";
		String String_provider = "TechCrunch";
		String String_authors = "Desire Aheza";
		String String_feed_description = "afdddddddddddddddddddddddddddddddddddddddddd";
		long mId;

		public Item() {
			mId = id++;
			String_category = "News";
		}

		long getId() {
			return mId;
		}

		public String getString_category() {
			return String_category;
		}

		public void setString_category(String string_category) {
			String_category = string_category;
		}

		public String getString_time() {
			return String_time + " " + mId;
		}

		public void setString_time(String string_time) {
			String_time = string_time;
		}

		public String getString_feed_title() {
			return String_feed_title + " " + mId;
		}

		public void setString_feed_title(String string_feed_title) {
			String_feed_title = string_feed_title;
		}

		public String getString_provider() {
			return String_provider + " " + mId;
		}

		public void setString_provider(String string_provider) {
			String_provider = string_provider;
		}

		public String getString_authors() {
			return String_authors + " " + mId;
		}

		public void setString_authors(String string_authors) {
			String_authors = string_authors;
		}

		public String getString_feed_description() {
			return String_feed_description;
		}

		public void setString_feed_description(String string_feed_description) {
			String_feed_description = string_feed_description;
		}

	}

	private LayoutInflater inflater;
	private Callback callback;
	private List<Item> items = new ArrayList<Item>();
	private List<SyndEntry> syndFeedsEntry;
	MainActivity mContext;
	FeedProvider mProvider = new FeedProvider();
	static final int ViewBigPhoto = 0;
	static final int ViewSmallPhoto = 0;
	static boolean isBigPicture = false;

	public FlipAdapter(MainActivity context, List<SyndEntry> pitems) {
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

	SyndEntry feedItem = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		feedItem = (SyndEntry) getItem(position);

		String authors = "";
		if (feedItem.getAuthors().size() > 0) {
			for (Object author : feedItem.getAuthors()) {
				authors += author + "|";
			}
		} else if (!feedItem.getAuthor().isEmpty())
			authors = feedItem.getAuthor();

		List<Element> foreignMarkups = (List<Element>) feedItem
				.getForeignMarkup();
		String imgURL = "";
		for (Element foreignMarkup : foreignMarkups) {
			if (foreignMarkup.getAttribute("url") != null)
				imgURL = foreignMarkup.getAttribute("url").getValue();
				if(mProvider.getProviderName().equals("CNN"))
					imgURL=imgURL.replace("top-tease.jpg", "exlarge-169.jpg");
			// read width and height
		}
		if (imgURL == "") {
			List<SyndEnclosure> encls = feedItem.getEnclosures();
			if (!encls.isEmpty()) {
				for (SyndEnclosure e : encls) {
					imgURL = e.getUrl().toString();
				}
			}
		}
		String Description = "";

		if (feedItem.getDescription() == null) {

			for (Iterator<?> it = feedItem.getContents().iterator(); it
					.hasNext();) {
				SyndContent syndContent = (SyndContent) it.next();

				if (syndContent != null) {
					Description = syndContent.getValue();
				}
			}
		} else if (feedItem.getDescription() != null) {
			Description = feedItem.getDescription().getValue();
		}

		// convertView.setTag(R.id.imageView_feed_image,
		// "<h1>" + feedItem.getTitle() + "</h1>" + feedItem.getAuthor()
		// + "/" + feedItem.getPublishedDate() + "</br>"
		// + Description);
		int startImageLink = 0, imageEndTage = 0;
		int endImageLink = 0;
		String ImageUrl = "";
		boolean hasImage = false;
		if (Description.contains("<img")) {
			hasImage = true;
			startImageLink = Description.indexOf("src=",
					Description.indexOf("<img"));
			endImageLink = Description.indexOf('"',
					startImageLink + "src=".length() + 1);
			ImageUrl = Description.substring(startImageLink + 5, endImageLink);

			imageEndTage = Description.indexOf(">", endImageLink);
		}
		String descCont = Description
				.substring(hasImage ? imageEndTage + 1 : 0)
				.replaceAll("\\n", " ").replaceAll("&nbsp;", " ")
				.replaceAll("&#160;", " ").trim();

		if (descCont.contains("<p>")) {
			descCont = descCont.substring(0, descCont.indexOf("</p>") + 4);
		} else if (descCont.contains("<"))
			descCont = descCont.substring(0, descCont.indexOf("<"));

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.page_news_feed, parent,
					false);
			initializeHolder(convertView, holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView_category.setText(mProvider.getCategoryName());
		holder.textView_provider.setText(mProvider.getProviderName());
		holder.textView_authors.setText(authors);
		holder.textView_feed_title.setText(Html.fromHtml(feedItem.getTitle()));
		holder.textView_feed_description.setText(trimTrailingWhitespace(Html
				.fromHtml(descCont)));

		if (ImageUrl != ""
				&& (!(descCont.isEmpty() || descCont.equals(" ") || descCont
						.equals("")) & descCont.length() > 20)) {
			try {
				loadImage(holder, ImageUrl);
			} catch (MalformedURLException e) {
				holder.imageView_feed_image.setVisibility(View.GONE);
			}
		} else if (imgURL != "") {
			try {
				loadImage(holder, imgURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		} else {
			holder.imageView_feed_image.setImageResource(mProvider
					.getProviderIcon());
		}
		convertView.setTag(R.id.textView_feed_description, feedItem.getUri());
		holder.imageViewshare.setTag(R.id.imageView_feed_image, feedItem);
		holder.imageViewshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Launch the Google+ share dialog with attribution to your app.
				SyndEntry feedItem=(SyndEntry) v.getTag(R.id.imageView_feed_image);
				Uri feedUri = Uri.parse(feedItem.getUri());
				Intent shareIntent = new PlusShare.Builder(mContext)
						.setType("text/plain").setText(feedItem.getTitle())
						.setContentUrl(feedUri).getIntent();
				mContext.sharingIntent = true;
				mContext.startActivityForResult(shareIntent, 0);

			}
		});
		
		return convertView;
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

	private void initializeHolder(View convertView, ViewHolder holder) {
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
		holder.textView_feed_description = (TextView) convertView
				.findViewById(R.id.textView_feed_description);
		holder.imageView_feed_image = (ImageView) convertView
				.findViewById(R.id.imageView_feed_image);
		holder.imageViewshare = (ImageView) convertView
				.findViewById(R.id.imageViewshare);
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

	public void add(SyndEntry entry) {
		syndFeedsEntry.add(entry);
		notifyDataSetChanged();
	}

	public void addItems(int amount) {
		for (int i = 0; i < amount; i++) {
			items.add(new Item());
		}
		notifyDataSetChanged();
	}

	public void addItemsBefore(int amount) {
		for (int i = 0; i < amount; i++) {
			items.add(0, new Item());
		}
		notifyDataSetChanged();
	}
	//
	// @Override
	// public int getItemViewType(int position) {
	//
	// return isBigPicture?ViewBigPhoto:ViewSmallPhoto;
	// }
	//
	// @Override
	// public int getViewTypeCount() {
	//
	// return 2;
	// }
}
