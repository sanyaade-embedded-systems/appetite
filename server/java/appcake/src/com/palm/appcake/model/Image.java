package com.palm.appcake.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/*
 * <ac:image_1>
 *   <ac:asset_url ac:type="app">http://cdn.downloads.palm.com/public/6/en/images/1/A/mines_2009-08-09_061656.jpg</ac:asset_url>
 *   <ac:asset_url ac:type="scaled">http://cdn.downloads.palm.com/public/6/en/images/1/S/mines_2009-08-09_061656.jpg</ac:asset_url>
 *   <ac:asset_url ac:type="large">http://cdn.downloads.palm.com/public/6/en/images/1/L/mines_2009-08-09_061656.jpg</ac:asset_url>
 * </ac:image_1>
 */
@XStreamAlias("image")
public class Image {
	//@XStreamAlias("asset_url")
	//@XStreamImplicit(itemFieldName="asset_url")
	private List<Icon> icons = new ArrayList<Icon>();

	public Image() {}
	
	public void addIcon(Icon icon) {
		this.icons.add(icon);
	}
}
