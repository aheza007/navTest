package com.example.navtest.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.model.Feed;
import com.example.navtest.MainActivity;
import com.example.navtest.R;

public class SimpleAdapter extends
		RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

	private final Context mContext;
	private List<Feed> mItems;
	private int mCurrentItemId = 0;
	LruCache<String, Bitmap> imageCache;

	public static class SimpleViewHolder extends RecyclerView.ViewHolder {
		public TextView title;
		public NetworkImageView feedImageItem;

		public SimpleViewHolder(View view) {
			super(view);
			title = (TextView) view.findViewById(R.id.title);
			feedImageItem = (NetworkImageView) view.findViewById(R.id.item_image);

		}
	}

	public SimpleAdapter(Context context, List<Feed> mLists) {
		mContext = context;
		mItems=mLists;
//		if (mLists != null) {
//			mItems = new ArrayList<Feed>(mLists.size());
//			for (int i = 0; i < mLists.size(); i++) {
//				addItem(i, mLists.get(i));
//			}
//		} else
//			mItems = new ArrayList<>();
	}

	public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.favorite_item_view, parent, false);
		return new SimpleViewHolder(view);
	}

	@SuppressLint("NewApi")
	@Override
	public void onBindViewHolder(SimpleViewHolder holder, int position) {

		try {
			Feed feedItem = (Feed) mItems.get(position);
			holder.title.setText(feedItem.getTitle().toString());
			String url = feedItem.getImageUrl();
			if (url != null) {
				 holder.feedImageItem.setImageBitmap(null);
				 holder.feedImageItem.setImageUrl(url,((MainActivity)mContext).imageLoader);
				 holder.itemView.setTag(feedItem);
//				holder.feedImageItem.setImageBitmap(null);
//				Picasso.with(holder.feedImageItem.getContext()).cancelRequest(
//						holder.feedImageItem);
//				Picasso.with(holder.feedImageItem.getContext())
//						.load(feedItem.getImageUrl())
//						.into(holder.feedImageItem);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// private void getImage(SimpleViewHolder holder, String imageUrl,
	// Feed feedItem) {
	//
	// holder.feedImageItem.setImageBitmap(null);
	//
	// holder.feedImageItem.setImageUrl(imageUrl,
	// ((MainActivity) mContext).imageLoader);
	// holder.itemView.setTag(feedItem);
	// // throws MalformedURLException URL url = new URL(ImageUrl);
	// // holder.feedImageItem.setImageUrl(imageUrl, ImageCacheManager
	// // .getInstance().getImageLoader());
	//
	// // ImageRequest imageRequest = new ImageRequest(imageUrl,
	// // new Listener<Bitmap>() {
	// //
	// // @Override
	// // public void onResponse(Bitmap arg0) {
	// // if (arg0 != null) {
	// // holder.feedImageItem.setImageBitmap(arg0);
	// // imageCache.put(imageUrl, arg0);
	// // holder.feedImageItem.setTag(imageUrl);
	// // }
	// // }
	// //
	// // }, 200, 200, Bitmap.Config.ARGB_8888,
	// // new Response.ErrorListener() {
	// //
	// // @Override
	// // public void onErrorResponse(VolleyError arg0) {
	// // Log.d("HOMEFAVORITE_ADAPTER", arg0.getMessage());
	// // }
	// // });
	// // ((MainActivity) mContext).requestQueue.add(imageRequest);
	// // }
	// }

	public void addItem(int position, Feed entry) {
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
