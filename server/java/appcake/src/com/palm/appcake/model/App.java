package com.palm.appcake.model;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class App {
	@PrimaryKey
	@Persistent
	private String id;
	
	@Persistent
	private String url;

	@Persistent
	private String name;
	
	public App(String id, String url, String name) {
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
	
	public String asJSON() {
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("app", App.class);
        
        return xstream.toXML(this);
	}
}
