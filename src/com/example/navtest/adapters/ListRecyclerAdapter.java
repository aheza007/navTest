package com.example.navtest.adapters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.FeedProvider;
import com.example.navtest.MainActivity;
import com.example.navtest.R;

public class ListRecyclerAdapter extends
		RecyclerView.Adapter<ListRecyclerAdapter.ListItemViewHolder> {

	List<FeedProvider> mProviders;
	onListItemClick mListItemClicked;
	private Context mContext;
	SharedPreferences pref;

	public ListRecyclerAdapter(Context context, List<FeedProvider> providers) {
		if (providers == null) {
			throw new IllegalArgumentException("Data model Can't be null");
		}
		this.mContext = context;
		mProviders = providers;
		pref = PreferenceManager.getDefaultSharedPreferences(mContext);
	}

	public class ListItemViewHolder extends RecyclerView.ViewHolder implements
			OnClickListener {
		ImageView iconProvider;
		TextView providerName;
		ImageView addToFavorite;

		public ListItemViewHolder(View listItem) {
			super(listItem);
			providerName = (TextView) listItem.findViewById(R.id.providerName);
			iconProvider = (ImageView) listItem
					.findViewById(R.id.imageView_provider_icon);
			addToFavorite = (ImageView) listItem
					.findViewById(R.id.imageView_add_to_favorite);
			if (!(pref.getBoolean(MainActivity.SIGNED_IN_GOOGLE, false) && ((MainActivity) mContext).personName != null))
				addToFavorite.setVisibility(View.GONE);
			else
				addToFavorite.setVisibility(View.VISIBLE);
			listItem.setOnClickListener(this);
		}

		@Override
		public void onClick(View caller) {
			if (mListItemClicked != null) {
				mListItemClicked.itemClick(caller, this.getLayoutPosition());
			}
		}

	}

	@Override
	public int getItemCount() {
		return mProviders.size();
	}

	@Override
	public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
		final FeedProvider lProvider = mProviders.get(position);
		viewHolder.iconProvider.setImageResource(lProvider.getProviderIcon());
		viewHolder.providerName.setText(lProvider.getProviderName());
		if (viewHolder.addToFavorite.VISIBLE == View.VISIBLE) {
			viewHolder.addToFavorite.setOnClickListener(new OnClickListener() {

				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {

					Set<String> set = pref.getStringSet(
							MainActivity.FAVORITE_NEWS, new HashSet<String>());
					set.add(lProvider.getCategoryName() + MainActivity.SPLITER
							+ lProvider.getProviderName()
							+ MainActivity.SPLITER + lProvider.getProviderUrl()
							+ MainActivity.SPLITER
							+ lProvider.getProviderIcon());
					SharedPreferences.Editor editor = pref.edit();
					editor.remove(MainActivity.FAVORITE_NEWS).commit();
					editor.putStringSet(MainActivity.FAVORITE_NEWS, set);
					Log.d(MainActivity.FAVORITE_NEWS,
							editor.commit()
									+ " favorite items "
									+ pref.getStringSet(
											MainActivity.FAVORITE_NEWS,
											new HashSet<String>()).size());
				}
			});
		}
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
