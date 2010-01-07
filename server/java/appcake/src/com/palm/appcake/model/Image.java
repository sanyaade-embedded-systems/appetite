package com.palm.appcake.model;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * <image number="1">
 * <ac:asset_url ac :type="app">http://cdn.downloads.palm.com/public/6/icon/icon_252D5E.png</ac:asset_url>
 * </image>
 */
@PersistenceCapable(embeddedOnly="true")
@XStreamAlias("asset_url")
public class Image {
	@XStreamAsAttribute
	private int number;
	
	public Image(int number) {
		this.number = number;
	}
	
	public Image() {}


	
}

