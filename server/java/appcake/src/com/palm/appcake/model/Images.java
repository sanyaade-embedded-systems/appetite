package com.palm.appcake.model;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
 * <ac:image_1>
 *   <ac:asset_url ac:type="app">http://cdn.downloads.palm.com/public/6/en/images/1/A/mines_2009-08-09_061656.jpg</ac:asset_url>
 *   <ac:asset_url ac:type="scaled">http://cdn.downloads.palm.com/public/6/en/images/1/S/mines_2009-08-09_061656.jpg</ac:asset_url>
 *   <ac:asset_url ac:type="large">http://cdn.downloads.palm.com/public/6/en/images/1/L/mines_2009-08-09_061656.jpg</ac:asset_url>
 * </ac:image_1>
 */
@PersistenceCapable(embeddedOnly="true")
@XStreamAlias("images")
public class Images {
	private List<Icon> image_1 = new ArrayList<Icon>();
	private List<Icon> image_2 = new ArrayList<Icon>();
	private List<Icon> image_3 = new ArrayList<Icon>();
	private List<Icon> image_4 = new ArrayList<Icon>();
	private List<Icon> image_5 = new ArrayList<Icon>();
}