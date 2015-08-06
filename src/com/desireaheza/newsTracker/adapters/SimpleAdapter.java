package com.desireaheza.newsTracker.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.desireaheza.newsTracker.R;
import com.desireaheza.newsTracker.model.Feed;
import com.desireaheza.newsTracker.utils.VolleySingleton;

public class SimpleAdapter extends
		RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

	private final Context mContext;
	private List<Feed> mItems;
	private int mCurrentItemId = 0;
	LruCache<String, Bitmap> imageCache;
	static onGridCardItemClick mGridItemClick;
	static int ITEM_VIEW_WITH_IMAGE = 1;
	static int ITEM_VIEW_NO_IMAGE = 2;
	int mItemView;
	String mProviderCategory ;
	String mProviderName;

	public static class SimpleViewHolder extends RecyclerView.ViewHolder
			implements OnClickListener {
		public TextView title;
		public NetworkImageView feedImageItem;

		public SimpleViewHolder(View view) {
			super(view);
			title = (TextView) view.findViewById(R.id.title);
			feedImageItem = (NetworkImageView) view
					.findViewById(R.id.item_image);
			view.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (mGridItemClick != null)
				mGridItemClick.gridItemClickListener(v,
						this.getLayoutPosition());
		}
	}

	// public static class SimpleViewHolderNoImage extends
	// RecyclerView.ViewHolder
	// implements OnClickListener {
	// public TextView title;
	// public NetworkImageView feedImageItem;
	//
	// public SimpleViewHolderNoImage(View view) {
	// super(view);
	// title = (TextView) view.findViewById(R.id.title);
	// feedImageItem = (NetworkImageView) view
	// .findViewById(R.id.item_image);
	// view.setOnClickListener(this);
	// }
	//
	// @Override
	// public void onClick(View v) {
	// if (mGridItemClick != null)
	// mGridItemClick.gridItemClickListener(v,
	// this.getLayoutPosition());
	// }
	// }

	@Override
	public int getItemViewType(int position) {
		Feed feedItem = (Feed) mItems.get(position);
		String feedImageUrl=feedItem.getImageUrl();
		int whichView=feedItem.getImageUrl().length()>0? ITEM_VIEW_WITH_IMAGE
				: ITEM_VIEW_NO_IMAGE;
		return whichView;
	}

	public SimpleAdapter(Context context, int itemVw, List<Feed> mLists) {
		mContext = context;
		mItems = mLists;
		mItemView = itemVw;
	}

	public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view;
		SimpleViewHolder holder;
		if (viewType == ITEM_VIEW_WITH_IMAGE) {
			view = LayoutInflater.from(mContext).inflate(mItemView, parent,
					false);
			holder = new SimpleViewHolder(view);
		} else {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.favorite_item_view_no_photo, parent, false);
			holder = new SimpleViewHolder(view);
		}
		return holder;
	}

	@SuppressLint("NewApi")
	@Override
	public void onBindViewHolder(SimpleViewHolder holder, int position) {

		try {
			Feed feedItem = (Feed) mItems.get(position);
			
			if(feedItem.getImageUrl().length()==0 &&feedItem.getParseDescription().length()>0)
				holder.title.setText(feedItem.getParseDescription().toString());
			else
				holder.title.setText(feedItem.getTitle().toString());
			String url = feedItem.getImageUrl();
			String sectionTitle=(String)holder.itemView.getTag(R.id.button_read_more);
			if (url != null) {
				holder.feedImageItem.setImageBitmap(null);

				holder.feedImageItem.setImageUrl(url, VolleySingleton
						.getInstance(mContext).getImageLoader());
				holder.itemView.setTag(feedItem);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

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

	public static interface onGridCardItemClick {
		public void gridItemClickListener(View v, int position);
	}

	public void setOnGridItemClickListener(
			final onGridCardItemClick cardItemClick) {
		mGridItemClick = cardItemClick;
	}
	
	public void setProviderDetails(String providerName,String providerCategory){
		mProviderCategory=providerCategory;
		mProviderName=providerName;
	}
}
