package com.palm.appcake.model;

/**
 * 
 */

public class Icon {
	private String id;
	private String url;
	private String name;
	
	public Icon(String id, String url, String name) {
		this.id = id;
		this.url = url;
		this.name = name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
