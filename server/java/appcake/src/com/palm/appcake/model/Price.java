package com.palm.appcake.model;

import javax.jdo.annotations.PersistenceCapable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/*
 * <ac:price ac:currency="USD">0.99000000953674</ac:price>
 */
@PersistenceCapable(embeddedOnly="true")
@XStreamAlias("price")
public class Price {
	@XStreamAsAttribute
	private String currency;
	private double amount;

	public Price() {}
	
	public Price(double amount, String currency) {
		this.amount = amount;
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
