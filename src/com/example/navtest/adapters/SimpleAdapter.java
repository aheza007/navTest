package com.example.navtest.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.protocol.ResponseConnControl;
import org.jdom.Element;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.example.navtest.MainActivity;
import com.example.navtest.R;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEnclosure;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

public class SimpleAdapter extends
		RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

	private final Context mContext;
	private final List<SyndEntry> mItems;
	private int mCurrentItemId = 0;
	LruCache<String, Bitmap> imageCache;

	public static class SimpleViewHolder extends RecyclerView.ViewHolder {
		public final TextView title;
		public final ImageView feedImageItem;

		public SimpleViewHolder(View view) {
			super(view);
			title = (TextView) view.findViewById(R.id.title);
			feedImageItem = (ImageView) view.findViewById(R.id.item_image);

		}
	}

	public SimpleAdapter(Context context, List<SyndEntry> mLists) {
		mContext = context;
		imageCache = new LruCache<>(30);
		if (mLists != null) {
			mItems = new ArrayList<SyndEntry>(mLists.size());
			for (int i = 0; i < mLists.size(); i++) {
				addItem(i, mLists.get(i));
			}
		} else
			mItems = new ArrayList<>();
	}

	public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(mContext).inflate(
				R.layout.favorite_item_view, parent, false);
		return new SimpleViewHolder(view);
	}

	@SuppressLint("NewApi")
	@Override
	public void onBindViewHolder(final SimpleViewHolder holder,
			final int position) {

		SyndEntry feedItem = (SyndEntry) mItems.get(position);

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
			if (imgURL.contains("top-tease.jpg"))
				imgURL = imgURL.replace("top-tease.jpg", "exlarge-169.jpg");
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

		holder.title.setText(mItems.get(position).getTitle().toString());

		if (ImageUrl != ""&& (!(descCont.isEmpty() || descCont.equals(" ") || descCont
				.equals("")) & descCont.length() > 20))
			getImage(holder, ImageUrl);
		else if (imgURL != "") 
			getImage(holder, imgURL);
			//loadImage(holder, imgURL);
//		else {
//			holder.feedImageItem.setImageResource(R.drawable.rounded_corner);
//		}
		
		// holder.feedImageItem.setim
	}

	private void getImage(final SimpleViewHolder holder, final String ImageUrl) {
		ImageRequest imageRequest = new ImageRequest(ImageUrl,
				new Listener<Bitmap>() {

					@Override
					public void onResponse(Bitmap arg0) {
						if (arg0 != null) {
							holder.feedImageItem.setImageBitmap(arg0);
							imageCache.put(ImageUrl, arg0);
						}
					}

				}, 200, 200, Bitmap.Config.ARGB_8888,
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Log.d("HOMEFAVORITE_ADAPTER", arg0.getMessage());
					}
				});
		((MainActivity)mContext).requestQueue.add(imageRequest);
	}

	private void loadImage(final SimpleViewHolder holder, String imgURL) {
		((MainActivity) mContext).imageLoader.get(imgURL, new ImageListener() {

			public void onErrorResponse(VolleyError error) {
				holder.feedImageItem
						.setImageResource(R.drawable.rounded_corner); // set an
																		// error
																		// image
																		// if
																		// the
																		// download
																		// fails
			}

			public void onResponse(ImageContainer response, boolean arg1) {
				if (response.getBitmap() != null) {
					holder.feedImageItem.setImageBitmap(response.getBitmap());
				}
			}
		});
	}

	public void addItem(int position, SyndEntry entry) {
		final int id = mCurrentItemId++;
		mItems.add(position, entry);
		notifyItemInserted(position);
	}

	public void removeItem(int position) {
		mItems.remove(position);
		notifyItemRemoved(position);
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}
}
