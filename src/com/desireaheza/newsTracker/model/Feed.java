package com.desireaheza.newsTracker.model;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents a single entry (post) in the XML feed.
 * 
 * <p>
 * It includes the data members "title," "media link," "link," "authors," and "description."
 */
public class Feed  implements Parcelable{

	String url;
	String description; 
	String imageUrl;
	String title;
	String authors;
	long publishedOn;
	String parseDescription;
	String feedProviderName;
	String feedProviderCategory;
	public Feed(){
		
	}
	public Feed(String url,String desc,String imageUrl, String title, String authors, long publishedOn, String parseDescr){
		this.url=url;
		this.description=desc;
		this.imageUrl=imageUrl;
		this.title=title;
		this.authors=authors;
		this.publishedOn=publishedOn;
		this.parseDescription=parseDescr;
		this.feedProviderCategory="";
		this.feedProviderName="";
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the authors
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	
	/**
	 * @return the publishedOn
	 */
	public long getPublishedOn() {
		if(this.publishedOn!=0){
			Calendar cal = Calendar.getInstance();
			long currentTime=cal.getTimeInMillis();
			return (currentTime-publishedOn)/1000;
		}
		return publishedOn;
	}
	
	/**
	 * @param publishedOn the publishedOn to set
	 */
	public void setPublishedOn(long publishedOn) {
		this.publishedOn = publishedOn;
	}

	/**
	 * @return the parseDescription
	 */
	public String getParseDescription() {
		return parseDescription;
	}
	
	/**
	 * @param parseDescription the parseDescription to set
	 */
	public void setParseDescription(String parseDescription) {
		this.parseDescription = parseDescription;
	}
	
	/**
	 * @return the feedProviderName
	 */
	public String getFeedProviderName() {
		return feedProviderName;
	}
	
	/**
	 * @param feedProviderName the feedProviderName to set
	 */
	public void setFeedProviderName(String feedProviderName) {
		this.feedProviderName = feedProviderName;
	}
	
	/**
	 * @return the feedProviderCategory
	 */
	public String getFeedProviderCategory() {
		return feedProviderCategory;
	}
	
	/**
	 * @param feedProviderCategory the feedProviderCategory to set
	 */
	public void setFeedProviderCategory(String feedProviderCategory) {
		this.feedProviderCategory = feedProviderCategory;
	}
	
	@Override
	public int describeContents() {
		
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(url);
		dest.writeString(description);
		dest.writeString(imageUrl);
		dest.writeString(title);
		dest.writeString(authors);
		dest.writeLong(publishedOn);
		dest.writeString(parseDescription);
		dest.writeString(feedProviderCategory);
		dest.writeString(feedProviderName);
	}
	
	private Feed(Parcel in){
		this.url=in.readString();
		this.description=in.readString();
		this.imageUrl=in.readString();
		this.title=in.readString();
		this.authors=in.readString();
		this.publishedOn=in.readLong();
		this.parseDescription=in.readString();
		this.feedProviderCategory=in.readString();
		this.feedProviderName=in.readString();
	}
	
	public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
		public Feed createFromParcel(Parcel in) {
			return new Feed(in);
		}

		@Override
		public Feed[] newArray(int size) {
			return new Feed[size];
		}
	};
	
}
