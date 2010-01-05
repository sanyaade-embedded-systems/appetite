package com.palm.appcake.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.palm.appcake.model.converter.ItemDateConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/*
 *         <item>
                <title><![CDATA[Mine Search]]></title>
                <link>http://developer.palm.com/appredirect/?packageid=com.engineequalscar.games.mines</link>
                <description><![CDATA[Mine searcher .]]></description>
                <pubDate>2009-12-21 21:00:58</pubDate>
                <guid>6.6</guid>
                <ac:packageid>com.engineequalscar.games.mines</ac:packageid>
                <ac:version>0.9.5</ac:version>
                <ac:installed_size>1038810</ac:installed_size>
                <ac:rating>2.9</ac:rating>
                <ac:total_downloads>1322</ac:total_downloads>
                <ac:total_comments>17</ac:total_comments>
                <ac:min_os>webOS 1.0.0</ac:min_os>
                <ac:devices>Palm Pre, Palm Pixi</ac:devices>
                <ac:language>en</ac:language>
                <ac:country>US</ac:country>
                <ac:icons>
                    <ac:asset_url ac:type="app">http://cdn.downloads.palm.com/public/6/icon/icon_252D5E.png</ac:asset_url>
                    <ac:asset_url ac:type="scaled">http://cdn.downloads.palm.com/public/6/icon/S/icon_252D5E.png</ac:asset_url>
                    <ac:asset_url ac:type="large">http://cdn.downloads.palm.com/public/6/icon/L/icon_big.png</ac:asset_url>
                </ac:icons>
                <ac:localizations>
                    <ac:localization ac:country="US" ac:language="en">
                        <ac:title><![CDATA[Mine Search]]></ac:title>
                        <ac:summary><![CDATA[Mine searcher is our take .]]></ac:summary>
                        <ac:price ac:currency="USD">0.99000000953674</ac:price>
                        <ac:developer>Engine Equals Car</ac:developer>
                        <ac:developer_url>http://www.engineequalscar.com</ac:developer_url>
                        <ac:support_url>http://www.engineequalscar.com</ac:support_url>
                        <ac:categories><ac:category><![CDATA[Games!Arcade]]></ac:category>
                        <ac:category><![CDATA[Games!Classics]]></ac:category>
                        <ac:category><![CDATA[Games!Strategy]]></ac:category>
                    </ac:categories>
                    <ac:images>
                        <ac:image_1>
                            <ac:asset_url ac:type="app">http://cdn.downloads.palm.com/public/6/en/images/1/A/mines_2009-08-09_061656.jpg</ac:asset_url>
                            <ac:asset_url ac:type="scaled">http://cdn.downloads.palm.com/public/6/en/images/1/S/mines_2009-08-09_061656.jpg</ac:asset_url>
                            <ac:asset_url ac:type="large">http://cdn.downloads.palm.com/public/6/en/images/1/L/mines_2009-08-09_061656.jpg</ac:asset_url>
                        </ac:image_1>
                        <ac:image_2>
                            <ac:asset_url ac:type="app">http://cdn.downloads.palm.com/public/6/en/images/2/A/mines_2009-20-08_071859_256A3E.jpg</ac:asset_url>
                            <ac:asset_url ac:type="scaled">http://cdn.downloads.palm.com/public/6/en/images/2/S/mines_2009-20-08_071859_256A3E.jpg</ac:asset_url>
                            <ac:asset_url ac:type="large">http://cdn.downloads.palm.com/public/6/en/images/2/L/mines_2009-20-08_071859_256A3E.jpg</ac:asset_url>
                        </ac:image_2>
                        <ac:image_3>
                            <ac:asset_url ac:type="app">http://cdn.downloads.palm.com/public/6/en/images/3/A/mines_2009-08-09_061855.jpg</ac:asset_url>
                            <ac:asset_url ac:type="scaled">http://cdn.downloads.palm.com/public/6/en/images/3/S/mines_2009-08-09_061855.jpg</ac:asset_url>
                            <ac:asset_url ac:type="large">http://cdn.downloads.palm.com/public/6/en/images/3/L/mines_2009-08-09_061855.jpg</ac:asset_url>
                        </ac:image_3>
                    </ac:images>
                </ac:localization>
            </ac:localizations>
        </item>
 */
@XStreamAlias("item")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Item {
	@Persistent private String title;
	@Persistent private String link;
	@Persistent private String description;

	@Persistent
	@XStreamConverter(ItemDateConverter.class)
	private Date pubDate;

//	@PrimaryKey
	@Persistent
	private String guid;

	@Persistent private String packageid;
	@Persistent private String version;
	
	@Persistent 
	@XStreamAlias("installed_size")
	private String installed_size;
	
	@Persistent private String rating;
	
	@Persistent 
	@XStreamAlias("total_downloads")
	private String total_downloads;
	
	@Persistent 
	@XStreamAlias("total_comments")
	private String total_comments;
	
	@Persistent 
	@XStreamAlias("min_os")
	private String minOs;
	
	@Persistent private String devices;
	@Persistent private String language;
	@Persistent private String country;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private List<Icon> icons = new ArrayList<Icon>();
    
    @Persistent
    private List<Localization> localizations = new ArrayList<Localization>();
	
	public Item() {}

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

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getInstalled_size() {
		return installed_size;
	}

	public void setInstalled_size(String installedSize) {
		installed_size = installedSize;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getTotal_downloads() {
		return total_downloads;
	}

	public void setTotal_downloads(String totalDownloads) {
		total_downloads = totalDownloads;
	}

	public String getTotal_comments() {
		return total_comments;
	}

	public void setTotal_comments(String totalComments) {
		total_comments = totalComments;
	}

	public String getMinOs() {
		return minOs;
	}

	public void setMinOs(String minOs) {
		this.minOs = minOs;
	}

	public String getDevices() {
		return devices;
	}

	public void setDevices(String devices) {
		this.devices = devices;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<Icon> getIcons() {
		return icons;
	}

	public void setIcons(List<Icon> icons) {
		this.icons = icons;
	}

	public List<Localization> getLocalizations() {
		return localizations;
	}

	public void setLocalizations(List<Localization> localizations) {
		this.localizations = localizations;
	}
}
