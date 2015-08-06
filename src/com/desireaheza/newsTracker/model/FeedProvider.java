package com.desireaheza.newsTracker.model;

public class FeedProvider {

	private String categoryName;

	//ivate String feedProvider;
	private String providerUrl;
	private String providerName;
	private Integer providerIcon;
	
	public FeedProvider(String categoryName,String providerName, String providerUrl, Integer provederIcon){
		setCategoryName(categoryName);
		setProviderName(providerName);
		setProviderUrl(providerUrl);
		setProviderIcon(provederIcon);
	}

	public FeedProvider() {
		
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

//	public String getFeedProvider() {
//		return feedProvider;
//	}
//
//	public void setFeedProvider(String feedProvider) {
//		this.feedProvider = feedProvider;
//	}

	public String getProviderUrl() {
		return providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public Integer getProviderIcon() {
		return providerIcon;
	}

	public void setProviderIcon(Integer providerIcon) {
		this.providerIcon = providerIcon;
	}
}
