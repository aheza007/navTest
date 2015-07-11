package com.example.navtest.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navtest.R;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

public class SimpleAdapter extends
		RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

	private final Context mContext;
	private final List<SyndEntry> mItems;
	private int mCurrentItemId = 0;

	public static class SimpleViewHolder extends RecyclerView.ViewHolder {
		public final TextView title;
		public final ImageView feedImageItem;
		public SimpleViewHolder(View view) {
			super(view);
			title = (TextView) view.findViewById(R.id.title);
			feedImageItem=(ImageView)view.findViewById(R.id.item_image);
		}
	}

	public SimpleAdapter(Context context, List<SyndEntry> mLists) {
		mContext = context;
		if (mLists!=null) {
			mItems = new ArrayList<SyndEntry>(mLists.size());
			for (int i = 0; i < mLists.size(); i++) {
				addItem(i,mLists.get(i));
			}
		}
		else
			mItems=new ArrayList<>();
	}

	public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_item_view,
				parent, false);
		return new SimpleViewHolder(view);
	}

	@Override
	public void onBindViewHolder(SimpleViewHolder holder, final int position) {
		holder.title.setText(mItems.get(position).getTitle().toString());
		//holder.feedImageItem.
	}

	public void addItem(int position,SyndEntry entry) {
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
