package com.palm.appcake.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/*
 * <rss version="2.0" xmlns:ac="http://palm.com/app.catalog.rss.extensions">
 */
@XStreamAlias("rss")
public class Rss {
	@XStreamAsAttribute
	private float version;
	private Channel channel;

	public Rss() {}
	
	public Rss(float version, Channel channel) {
		this.version = version;
		this.channel = channel;
	}

	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	
}
