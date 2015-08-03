package com.example.model;

import java.util.Calendar;

/**
 * This class represents a single entry (post) in the XML feed.
 * 
 * <p>
 * It includes the data members "title," "media link," "link," "authors," and "description."
 */
public class Feed {

	String url;
	String description; 
	String imageUrl;
	String title;
	String authors;
	long publishedOn;
	String parseDescription;
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
	
}
