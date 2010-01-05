package com.palm.appcake.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.palm.appcake.model.converter.ChannelDateConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/*
		<title>Device App Updates for US</title>
        <link>http://www.palm.com</link>
        <description>Updates on the latest US Device channel applications for Palm webOS devices</description>
        <language>en-US</language>
        <pubDate>Mon, 28 Dec 2009 17:28:55 -0800</pubDate>
        <lastBuildDate>Mon, 28 Dec 2009 17:28:55 -0800</lastBuildDate>
        <ac:distributionChannel>Device</ac:distributionChannel>
        <ac:countryCode>US</ac:countryCode>
        <item>...</item>
 */
@XStreamAlias("channel")
public class Channel {
	private String title;
	private String link;
	private String description;
	private String language;
	@XStreamConverter(ChannelDateConverter.class)
	private Date pubDate;
	@XStreamConverter(ChannelDateConverter.class)
	private Date lastBuildDate;
	// ac:
	private String distributionChannel;
	private String countryCode;
	
	@XStreamImplicit(itemFieldName="item")
	private List<Item> items = new ArrayList<Item>();
	
	public Channel() {}
	
	public Channel(String title, String link, String description, String language, Date pubDate, Date lastBuildDate, String distributionChannel, String countryCode) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.language = language;
		this.pubDate = pubDate;
		this.lastBuildDate = lastBuildDate;
		this.distributionChannel = distributionChannel;
		this.countryCode = countryCode;
	}
	
    public void addItem(Item item) {
        this.items.add(item);
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public Date getLastBuildDate() {
		return lastBuildDate;
	}

	public void setLastBuildDate(Date lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	public String getDistributionChannel() {
		return distributionChannel;
	}

	public void setDistributionChannel(String distributionChannel) {
		this.distributionChannel = distributionChannel;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
    
    
}
