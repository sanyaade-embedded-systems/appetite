package com.palm;

import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.List;

public class CatalogConverter {
    public static void main(String[] args) throws Exception {
        CatalogConverter.convert();
    }

    public static void convert() throws JDOMException, IOException {
        File file = new File("catalog.xml");

        Document catalog = new SAXBuilder().build(file);

        StringBuilder sb = new StringBuilder();

        List elements = XPath.selectNodes(catalog, "/rss/channel/item");

        sb.append("title,description,pubDate,guid,packageid,version,installed_size,rating,total_downloads,total_comments,icon_url,price,category\n");

        for (int i = 0; i < elements.size(); i++) {
            Element element = (Element) elements.get(i);

            // filter out non-US elements
            if (!getText(element, "ac:country").equals("US") || !getText(element, "ac:language").equals("en")) continue;

            process(element, sb, new String[] {
                    "title",
                    "description",
                    "pubDate",
                    "guid",
                    "ac:packageid",
                    "ac:version",
                    "ac:installed_size",
                    "ac:rating",
                    "ac:total_downloads",
                    "ac:total_comments",
                    "ac:icons/ac:asset_url[@ac:type = 'large']",
                    "ac:localizations/ac:localization[@ac:country = 'US']/ac:price",
                    "ac:localizations/ac:localization[@ac:country = 'US'][@ac:language = 'en']/ac:categories/ac:category",
            });

            sb.append("\n");
        }

        File output = new File("catalog.csv");
        output.delete();

        FileOutputStream out = new FileOutputStream(output);
        out.write(sb.toString().getBytes());
        out.close();
    }

    private static String getText(Object ctx, String path) throws JDOMException {
        Element e = e(ctx, path);
        return (e == null) ? "" : e.getTextTrim();
    }

    private static void process(Object ctx, StringBuilder csv, String[] paths) throws JDOMException {
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            add(csv, ctx, path);
        }
    }

    private static String escape(String raw) {
        String good = "\"" + raw.replaceAll("\"", "\"\"") + "\"";
        return good;
    }

    /**
     * Returns the element specified by the path
     *
     * @param ctx
     * @param path
     */
    private static Element e(Object ctx, String path) throws JDOMException {
        XPath xp = XPath.newInstance(path);

        xp.addNamespace("ac", "http://palm.com/app.catalog.rss.extensions");

        return (Element) xp.selectSingleNode(ctx);
    }

    private static void add(StringBuilder csv, Object ctx, String path) throws JDOMException {
        Element e = e(ctx, path);

        String text = (e == null) ? "ERROR!" : e.getTextTrim();

        if (csv.length() > 0 && (csv.charAt(csv.length() - 1) != '\n')) csv.append(",");

        csv.append(escape(text));
    }
}
