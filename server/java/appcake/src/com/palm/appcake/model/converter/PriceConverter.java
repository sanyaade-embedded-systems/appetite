package com.palm.appcake.model.converter;

import com.palm.appcake.model.Price;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class PriceConverter implements Converter {

        public boolean canConvert(Class clazz) {
                return clazz.equals(Price.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
                Price price = (Price) value;
                writer.addAttribute("currency", price.getCurrency());
                writer.setValue(price.getAmount() + "");
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
        	return new Price(Double.parseDouble(reader.getValue()), reader.getAttribute("currency"));
        }
}
