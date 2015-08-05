package com.example.navtest.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.annotation.SuppressLint;

import com.example.model.Feed;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEnclosure;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

public class ParseSyndFeeds {

	@SuppressWarnings({ "unchecked", "deprecation" })
	@SuppressLint("NewApi")
	public static Feed makeFeed(SyndEntry feedItem) {
		Feed feed = new Feed();
		String authors = "";
		if (feedItem.getAuthors().size() > 0) {
			for (Object author : feedItem.getAuthors()) {
				authors += author + "|";
			}
		} else if (!feedItem.getAuthor().isEmpty())
			authors = feedItem.getAuthor();

		List<Element> foreignMarkups = (List<Element>) feedItem
				.getForeignMarkup();
		String imgURL = "";
		for (Element foreignMarkup : foreignMarkups) {
			if (foreignMarkup.getAttribute("url") != null)
				imgURL = foreignMarkup.getAttribute("url").getValue();
			if (imgURL.contains("top-tease.jpg"))
				imgURL = imgURL.replace("top-tease.jpg", "exlarge-169.jpg");
		}
		if (imgURL == "") {
			List<SyndEnclosure> encls = feedItem.getEnclosures();
			if (!encls.isEmpty()) {
				for (SyndEnclosure e : encls) {
					imgURL = e.getUrl().toString();
				}
			}
		}
		String html1 = feedItem.getContents().toString();
		String html2 = feedItem.getDescription().toString();
		Document doc = null;
		String description = "";
		if (html1 != null && html1=="[]") {
			doc = Jsoup.parse(html1);
			description = doc.body().text();
		} 
		if (html2 != null) {
			doc = Jsoup.parse(html2);
			description = doc.body().text();
		} else
			doc = null;

		String Description = "";

		if (feedItem.getDescription() == null) {

			for (Iterator<?> it = feedItem.getContents().iterator(); it
					.hasNext();) {
				SyndContent syndContent = (SyndContent) it.next();

				if (syndContent != null) {
					Description = syndContent.getValue();
				}
			}
		} else if (feedItem.getDescription() != null) {
			Description = feedItem.getDescription().getValue();
		}

		int startImageLink = 0, imageEndTage = 0;
		int endImageLink = 0;
		String ImageUrl = "";
		boolean hasImage = false;
		if (Description.contains("<img")) {
			hasImage = true;
			startImageLink = Description.indexOf("src=",
					Description.indexOf("<img"));
			endImageLink = Description.indexOf('"',
					startImageLink + "src=".length() + 1);
			ImageUrl = Description.substring(startImageLink + 5, endImageLink);

			imageEndTage = Description.indexOf(">", endImageLink);
		}
		String descCont = Description
				.substring(hasImage ? imageEndTage + 1 : 0)
				.replaceAll("\\n", " ").replaceAll("&nbsp;", " ")
				.replaceAll("&#160;", " ").trim();

		if (descCont.contains("<p>")) {
			descCont = descCont.substring(0, descCont.indexOf("</p>") + 4);
		} else if (descCont.contains("<"))
			descCont = descCont.substring(0, descCont.indexOf("<"));
		if (ImageUrl != ""
				&& (!(descCont.isEmpty() || descCont.equals(" ") || descCont
						.equals("")) & descCont.length() > 20)) {
			try {
				URL url = new URL(ImageUrl);
				feed.setImageUrl(ImageUrl);
			} catch (MalformedURLException e) {
				feed.setImageUrl("");
			}
		} else if (imgURL != "") {
			try {
				URL url = new URL(imgURL);
				feed.setImageUrl(imgURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		} else {

			feed.setImageUrl("");
		}

		feed.setAuthors(authors);
		feed.setUrl(feedItem.getUri());
		feed.setTitle(feedItem.getTitle());
		feed.setPublishedOn(feedItem.getPublishedDate() != null ? feedItem
				.getPublishedDate().getTime() : 0);
		feed.setParseDescription(descCont);
		feed.setDescription(Description);

		return feed;
	}

}