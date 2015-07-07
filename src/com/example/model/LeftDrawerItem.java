package com.example.model;

public class LeftDrawerItem {
	private boolean showNotify;
	private String title;
	private int icon;

	public LeftDrawerItem() {

	}

	public LeftDrawerItem(boolean showNotify, String title) {
		this.showNotify = showNotify;
		this.title = title;
	}

	public boolean isShowNotify() {
		return showNotify;
	}

	public void setShowNotify(boolean showNotify) {
		this.showNotify = showNotify;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
}
