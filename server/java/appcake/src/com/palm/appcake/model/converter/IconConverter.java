package com.palm.appcake.model.converter;

import com.palm.appcake.model.Icon;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class IconConverter implements Converter {

        public boolean canConvert(Class clazz) {
                return clazz.equals(Icon.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        	Icon icon = (Icon) value;
        	writer.addAttribute("type", icon.getType());
        	writer.setValue(icon.getUrl());
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        	return new Icon(reader.getAttribute("type"), reader.getValue());
        }
}
