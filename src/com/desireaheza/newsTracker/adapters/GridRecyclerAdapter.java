package com.desireaheza.newsTracker.adapters;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.desireaheza.newsTracker.R;
import com.desireaheza.newsTracker.model.Category;

public class GridRecyclerAdapter extends
		RecyclerView.Adapter<GridRecyclerAdapter.GridViewHolder> {

	List<Category> items;
	OnGridItemClickListener mOnGridItemClickLister;

	public GridRecyclerAdapter(List<Category> modelData) {
		if (modelData == null) {
			throw new IllegalArgumentException("model data can't be null");
		}
		this.items = modelData;
	}

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public  class GridViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
		// each data item is just a string in this case
		public ImageView mainCategoryIcon;
		public TextView txtCategoryName;

		public GridViewHolder(View gridItem) {
			super(gridItem);
			mainCategoryIcon = (ImageView) gridItem
					.findViewById(R.id.categoryIcon);
			txtCategoryName = (TextView) gridItem
					.findViewById(R.id.categoryName);
			gridItem.setOnClickListener(this);
		}

		@Override
		public void onClick(View caller) {
			if (mOnGridItemClickLister != null)
				mOnGridItemClickLister.onGridItemClicked(caller,
						this.getLayoutPosition());
			
		}

	}

	@Override
	public int getItemCount() {

		return items == null ? 0 : items.size();
	}

	@Override
	public void onBindViewHolder(GridViewHolder viewHolder, int position) {
		Category item = items.get(position);
		viewHolder.mainCategoryIcon.setImageResource(item.getCategoryIconId());
		viewHolder.txtCategoryName.setText(item.getCategoryName());

	}

	@Override
	public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View gridItem = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.main_grid_item, viewGroup, false);
		return new GridViewHolder(gridItem);
	}

	public static interface OnGridItemClickListener {
		public void onGridItemClicked(View caller, int position);
	}

	public void setOnGridItemClickLister(
			final OnGridItemClickListener pOnGridItemClickListener) {
		this.mOnGridItemClickLister = pOnGridItemClickListener;
	}
}
