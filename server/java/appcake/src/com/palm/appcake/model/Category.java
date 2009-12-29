package com.palm.appcake.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
 * <ac:categories>
 *   <ac:category><![CDATA[Games!Arcade]]></ac:category>
 *   <ac:category><![CDATA[Games!Classics]]></ac:category>
 *   <ac:category><![CDATA[Games!Strategy]]></ac:category>
 * </ac:categories>
 */
@XStreamAlias("category")
public class Category {
	private String content;
	
	public Category() {}
	
	public Category(String content) {
		this.content = content;
	}
}
