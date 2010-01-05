package com.palm.appcake.model;

import java.util.Arrays;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/*
 * <ac:categories>
 *   <ac:category><![CDATA[Games!Arcade]]></ac:category>
 *   <ac:category><![CDATA[Games!Classics]]></ac:category>
 *   <ac:category><![CDATA[Games!Strategy]]></ac:category>
 * </ac:categories>
 */
@PersistenceCapable(embeddedOnly="true")
@XStreamAlias("category")
public class Categories {
	@XStreamImplicit(itemFieldName="category")
	private List<String> content;

	public Categories() {}

	public Categories(String... content) {
		this.content = Arrays.asList(content);
	}
}
