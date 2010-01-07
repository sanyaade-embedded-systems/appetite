package com.palm.appcake.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.palm.appcake.model.converter.PriceConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/*
 *                 <ac:localization ac:country="US" ac:language="en">
                    <ac:title><![CDATA[Mine Search]]></ac:title>
                    <ac:summary><![CDATA[Mine searcher is our take.]]></ac:summary>
                    <ac:price ac:currency="USD">0.99000000953674</ac:price>
                    <ac:developer>Engine Equals Car</ac:developer>
                    <ac:developer_url>http://www.engineequalscar.com</ac:developer_url>
                    <ac:support_url>http://www.engineequalscar.com</ac:support_url>
                    <ac:categories>
                    	<ac:category><![CDATA[Games!Arcade]]></ac:category>
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
 */
@PersistenceCapable(embeddedOnly="true")
@XStreamAlias("localization")
public class Localization {
	@XStreamAsAttribute
	private String country;
	@XStreamAsAttribute
	private String language;
	private String title;
	private String summary;
	@XStreamConverter(PriceConverter.class)
	private Price price;
	private String developer;
	private String developer_url;
	@XStreamAlias("support_url")
	private String supportUrl;
	private Categories categories;
	//private Images images;
    @Persistent
    private List<Image> images = new ArrayList<Image>();


	public Localization() {}
	
	public Localization(String country, String language, String title,
			String summary, Price price, String developer, String developerUrl,
			String supportUrl, Categories categories) {
		super();
		this.country = country;
		this.language = language;
		this.title = title;
		this.summary = summary;
		this.price = price;
		this.developer = developer;
		this.developer_url = developerUrl;
		this.supportUrl = supportUrl;
		this.categories = categories;
	}

}
