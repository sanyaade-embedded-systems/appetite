package com.palm.appcake.model;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * <ac:asset_url ac:type="app">http://cdn.downloads.palm.com/public/6/icon/icon_252D5E.png</ac:asset_url>
 */
@PersistenceCapable(embeddedOnly="true")
@XStreamAlias("asset_url")
public class Icon {
	@XStreamAsAttribute
	private String type;
	private String url;
	
	public Icon(String type, String url) {
		this.type = type;
		this.url = url;
	}
	
	public Icon() {}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}

