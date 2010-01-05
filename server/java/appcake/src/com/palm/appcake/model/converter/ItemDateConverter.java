package com.palm.appcake.model.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/*
 * Converts from and to "2009-12-21 21:00:58"
 * 
 * matches format:      "yyyy-mm-dd HH:mm:ss"
 * 
 * 
 */
public class ItemDateConverter extends AbstractSingleValueConverter {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	
	public boolean canConvert(Class clazz) {
		return clazz.equals(Date.class);
	}

	@Override
	public Object fromString(String dateString) {
		try {
			return DATE_FORMAT.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
		
	public String toString(Object obj) {
		return DATE_FORMAT.format((Date)obj);
	}
}
