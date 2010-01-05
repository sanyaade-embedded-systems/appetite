package com.palm.appcake.model.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/*
 * Converts from and to "Mon, 28 Dec 2009 17:28:55 -0800"
 * 
 * matches format:      "EEE, dd MMM yyyy HH:mm:ss Z"
 * 
 * 
 */
public class ChannelDateConverter extends AbstractSingleValueConverter {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
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
