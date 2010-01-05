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

/**
 * Converts an app XML file into a JSON format; sort-of cheats
 *
 */
public class JSONConverter {
    public static void main(String[] args) throws Exception {
        JSONConverter.convert();
    }

    public static void convert() throws JDOMException, IOException {
        File file = new File("new.catalog.xml");

        Document catalog = new SAXBuilder().build(file);

        StringBuilder sb = new StringBuilder();

        sb.append("[\n");

        List elements = XPath.selectNodes(catalog, "/rss/channel/item");

        for (int i = 0; i < elements.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append("{\n");

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
                    "ac:total_accounts",
                    "ac:min_os",
                    "ac:devices",
                    "ac:language",
                    "ac:country"
            });

            processIcons(element, sb);

            processLocalizations(element, sb);

            sb.deleteCharAt(sb.length() - 2); // remove trailing comma

            sb.append("}");
        }

        sb.append("\n]");

        File output = new File("catalog.json");
        output.delete();

        FileOutputStream out = new FileOutputStream(output);
        out.write(sb.toString().getBytes());
        out.close();
    }

    private static void processIcons(Element element, StringBuilder json) throws JDOMException {
        Namespace ns = Namespace.getNamespace("http://palm.com/app.catalog.rss.extensions");

        json.append("\"icons\": [");

        // get the icon elements
        List icons = es(element, "ac:icons/ac:asset_url");
        for (int i = 0; i < icons.size(); i++) {
            if (i > 0) json.append(", ");
            json.append("{\n");

            Element icon = (Element) icons.get(i);

            json.append("\"type\": ");
            json.append("\"" + icon.getAttributeValue("type", ns) + "\"");
            json.append(", \n");
            json.append("\"url\": ");
            json.append("\"" + icon.getTextTrim() + "\"");
            
            json.append("\n}\n");
        }

        json.append("],\n");
    }

    private static void processLocalizations(Element element, StringBuilder json) throws JDOMException {
        Namespace ns = Namespace.getNamespace("http://palm.com/app.catalog.rss.extensions");

        json.append("\"localizations\": [");

        // get the icon elements
        List localizations = es(element, "ac:localizations/ac:localization");
        for (int i = 0; i < localizations.size(); i++) {
            if (i > 0) json.append(", ");
            json.append("{\n");

            Element l = (Element) localizations.get(i);

            json.append("\"country\": ");
            json.append("\"" + l.getAttributeValue("country", ns) + "\",\n");

            json.append("\"language\": ");
            json.append("\"" + l.getAttributeValue("language", ns) + "\",\n");

            process(l, json, new String[] {
                    "ac:title",
                    "ac:summary",
                    "ac:price",
                    "ac:developer",
                    "ac:developer_url",
                    "ac:support_url"
            });

            json.append("\"categories\": [");
            json.append("\"" + e(l, "ac:categories").getAttributeValue("primary", ns) + "\"");
            List cats = es(l, "ac:categories/ac:category");
            for (int j = 0; j < cats.size(); j++) {
                Element cat = (Element) cats.get(j);
                json.append(", \"" + cat.getTextTrim() + "\"");
            }
            json.append("],\n");

            json.append("\"images\": [");
            List imgs = es(l, "ac:images/ac:image/ac:asset_url[@ac:type='large']");
            for (int j = 0; j < imgs.size(); j++) {
                if (j > 0) json.append(", ");
                Element img = (Element) imgs.get(j);
                json.append("\"" + img.getTextTrim() + "\"");
            }
            json.append("],\n");

            json.append("\n}\n");
        }

        json.append("],\n");
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
        raw = raw.replaceAll("\"", "\\\\\"");
        raw = raw.replaceAll("\n", "\\\\000a");
        return "\"" + raw + "\"";
    }

    /**
     * Returns the element specified by the path
     *
     * @param ctx
     * @param path
     */
    private static Element e(Object ctx, String path) throws JDOMException {
        List list = es(ctx, path);
        return (list.size() > 0) ? (Element) list.get(0) : null;
    }

    private static List es(Object ctx, String path) throws JDOMException {
        XPath xp = XPath.newInstance(path);

        xp.addNamespace("ac", "http://palm.com/app.catalog.rss.extensions");

        return xp.selectNodes(ctx);
    }

    private static void add(StringBuilder csv, Object ctx, String path) throws JDOMException {
        Element e = e(ctx, path);

        String text = (e == null) ? "" : e.getTextTrim();

        String field = path;
        if (field.indexOf(":") != -1) field = field.substring(field.indexOf(":") + 1);

        csv.append("\"" + field + "\": " + escape(text) + ",\n");
    }
}