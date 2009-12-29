package com.palm.appcake.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/*
 * <ac:price ac:currency="USD">0.99000000953674</ac:price>
 */
@XStreamAlias("price")
public class Price {
	@XStreamAsAttribute
	private String currency;

	private String content;

	public Price() {}
	
	public Price(String currency, String content) {
		this.currency = currency;
		this.content = content;
	}
}
