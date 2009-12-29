package com.palm.appcake.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * <ac:asset_url ac:type="app">http://cdn.downloads.palm.com/public/6/icon/icon_252D5E.png</ac:asset_url>
 */
@XStreamAlias("asset_url")
public class Icon {
	@XStreamAsAttribute
	private String type;
	private String content;
	
	public Icon(String type, String content) {
		this.type = type;
		this.content = content;
	}
	
	public Icon() {}
}

