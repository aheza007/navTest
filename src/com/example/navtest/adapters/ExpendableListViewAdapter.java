package com.example.navtest.adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.model.FeedProvider;
import com.example.navtest.R;

public class ExpendableListViewAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<String> mListDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<FeedProvider>> mListDataChild;

	public ExpendableListViewAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<FeedProvider>> listChildData) {
		this.mContext = context;
		this.mListDataHeader = listDataHeader;
		this.mListDataChild = listChildData;
	}
	
//	public void addItems(List<String> pListDataHeader,HashMap<String, List<FeedProvider>> pListDataChild){
//		
//		if(mListDataHeader!=null&&mListDataHeader.size()>0){
//			mListDataHeader.clear();
//			mListDataHeader=pListDataHeader;
//		}
//		if(mListDataChild!=null&&mListDataChild.size()>0){
//			mListDataChild.clear();
//			mListDataChild=pListDataChild;
//		}
//		notifyDataSetChanged();
//	}

	@Override
	public int getGroupCount() {

		return this.mListDataHeader.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		 return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
	                .size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.mListDataHeader.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosititon);
    }

	@Override
	public long getGroupId(int groupPosition) {
		 return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		 String headerTitle = (String) getGroup(groupPosition);
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.mContext
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.list_item_section_header, null);
	        }
	 
	        TextView lblListHeader = (TextView) convertView
	                .findViewById(R.id.lblListHeader);
	        lblListHeader.setTypeface(null, Typeface.BOLD);
	        lblListHeader.setText(headerTitle);
	 
	        return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final FeedProvider provider = (FeedProvider) getChild(groupPosition, childPosition);
		 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.simple_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
 
        txtListChild.setText(provider.getProviderName());
        return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
