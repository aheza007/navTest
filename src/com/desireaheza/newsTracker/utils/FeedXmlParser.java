package com.desireaheza.newsTracker.utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.desireaheza.newsTracker.model.Feed;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

public class FeedXmlParser {

	public static List<Feed> parseFeed(String content) {
		SyndFeed feeds=null;
		try {
			
		    boolean inDataItemTag = false;
		    String currentTagName = "";
		    Feed feed = null;
		    List<Feed> feedList = new ArrayList<>();

		    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		    XmlPullParser parser = factory.newPullParser();
		    parser.setInput(new StringReader(content));
		    
		    int eventType = parser.getEventType();

		    while (eventType != XmlPullParser.END_DOCUMENT) {

		        switch (eventType) {
		            case XmlPullParser.START_TAG:
		                currentTagName = parser.getName();
		                if (currentTagName.equals("product")) {
		                    inDataItemTag = true;
		                    feed = new Feed();
		                    feedList.add(feed);
		                }
		                break;

		            case XmlPullParser.END_TAG:
		                if (parser.getName().equals("product")) {
		                    inDataItemTag = false;
		                }
		                currentTagName = "";
		                break;

		            case XmlPullParser.TEXT:
		                if (inDataItemTag && feed != null) {
		                    switch (currentTagName) {
		                        case "productId":
		                           // feed.setProductID(Integer.parseInt(parser.getText()));
		                            break;
		                        case "name":
		                        	//feed.setName(parser.getText());
		                        	break;
		                        case "instructions":
		                          //  feed.setInstructions(parser.getText());
		                            break;
		                        case "category":
		                          //  feed.setCategory(parser.getText());
		                            break;
		                        case "price" :
		                        	//feed.setPrice(Double.parseDouble(parser.getText()));
		                            break;
		                        case "photo" :
		                        //	feed.setPhotoUrl(parser.getText());
		                        default:
		                            break;
		                    }
		                }
		                break;
		        }

		       eventType = parser.next();

		    }

		    return feedList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 

		
	}
	
}

