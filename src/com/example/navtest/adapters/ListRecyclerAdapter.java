package com.example.navtest.adapters;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.FeedProvider;
import com.example.navtest.R;

public class ListRecyclerAdapter extends
		RecyclerView.Adapter<ListRecyclerAdapter.ListItemViewHolder> {

	List<FeedProvider> mProviders;
	onListItemClick mListItemClicked;

	public ListRecyclerAdapter(List<FeedProvider> providers) {
		if (providers == null) {
			throw new IllegalArgumentException("Data model Can't be null");
		}
		mProviders = providers;
	}

	public class ListItemViewHolder extends RecyclerView.ViewHolder implements
			OnClickListener {
		ImageView iconProvider;
		TextView providerName;

		public ListItemViewHolder(View listItem) {
			super(listItem);
			providerName = (TextView) listItem.findViewById(R.id.providerName);
			iconProvider=(ImageView)listItem.findViewById(R.id.imageView_provider_icon);
			listItem.setOnClickListener(this);
		}

		@Override
		public void onClick(View caller) {
			if (mListItemClicked != null) {
				mListItemClicked.itemClick(caller,this.getLayoutPosition());
			}
		}

	}

	@Override
	public int getItemCount() {
		return mProviders.size();
	}

	@Override
	public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
		FeedProvider lProvider=mProviders.get(position);
		viewHolder.iconProvider.setImageResource(lProvider.getProviderIcon());
		viewHolder.providerName.setText(lProvider.getProviderName());
	}

	@Override
	public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
		View listItem = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.main_list_item, viewGroup, false);
		return new ListItemViewHolder(listItem);
	}

	public static interface onListItemClick {
		public void itemClick(View caller, int position);
	}

	public void setOnListItemClickLister(final onListItemClick pListItemClicked) {
		mListItemClicked = pListItemClicked;
	}
}
