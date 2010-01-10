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
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Converts an app XML file into a JSON format; sort-of cheats
 *
 */
public class JSONConverter {
    public static void main(String[] args) throws Exception {
        String filename = "catalog.device.xml";
        if (args.length > 0 && args[0] != null) {
            filename = args[0];
        }

        JSONConverter.convert(filename);
    }

    public static String convert(Document catalog) throws JDOMException {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        List elements = XPath.selectNodes(catalog, "/rss/channel/item");

        int count = 0;
        for (int i = 0; i < elements.size(); i++) {
            Element element = (Element) elements.get(i);

            // filter out non-US elements
            //if (!getText(element, "ac:country").equals("US") || !getText(element, "ac:language").equals("en")) continue;

            // filter out the element if it doesn't have a US/en or AC localization
            List results = es(element, "ac:localizations/ac:localization[@ac:country='US'][@ac:language='en']");
            if (results.isEmpty()) {
                results = es(element, "ac:localizations/ac:localization[@ac:country='AC']");
                if (results.isEmpty()) continue;
            }

            count++;

            if (count > 1) sb.append(", ");
            sb.append("{\n");

            process(element, sb, new String[] {
                    "title",
                    "description",
                    "pubDate",
                    "guid",
                    "ac:channel",
                    "ac:packageid",
                    "ac:version",
                    "ac:installed_size",
                    "ac:rating",
                    "ac:total_downloads",
                    "ac:total_comments",
                    "ac:min_os",
                    "ac:devices",
                    "ac:language",
                    "ac:country"
            }, "    ");

            processIcons(element, sb);

            processLocalizations(element, sb);

            sb.deleteCharAt(sb.length() - 2); // remove trailing comma

            sb.append("}");
        }

        sb.append("\n]");

        return sb.toString();
    }

    public static void convert(String filename) throws JDOMException, IOException {
        File file = new File(filename);

        Document catalog = new SAXBuilder().build(file);
        String result = convert(catalog);
        result = "var apps = " + result;

        result += "\n// check to see if you are running inside of node.js and export if you are\n";
        result += "if (typeof GLOBAL == \"object\" && typeof GLOBAL['node'] == \"object\") {\n";
        result += "    exports.apps = apps;\n";
        result += "}";

        File output = new File("catalog_us_en.json");
        output.delete();

        FileOutputStream out = new FileOutputStream(output);
        out.write(result.getBytes());
        out.close();
    }

    private static void processIcons(Element element, StringBuilder json) throws JDOMException {
        Namespace ns = Namespace.getNamespace("http://palm.com/app.catalog.rss.extensions");

        json.append("    \"icons\": [");

        // get the icon elements
        List icons = es(element, "ac:icons/ac:asset_url");
        for (int i = 0; i < icons.size(); i++) {
            if (i > 0) json.append(", ");
            json.append("{\n");

            Element icon = (Element) icons.get(i);

            json.append("        \"type\": ");
            json.append("\"" + icon.getAttributeValue("type", ns) + "\"");
            json.append(", \n");
            json.append("        \"url\": ");
            json.append("\"" + escapeURL(icon.getTextTrim()) + "\"");
            json.append("\n    }");
        }

        json.append("],\n");
    }

    private static void processLocalizations(Element element, StringBuilder json) throws JDOMException {
        Namespace ns = Namespace.getNamespace("http://palm.com/app.catalog.rss.extensions");

        json.append("    \"localizations\": [");

        // get the icon elements
        List localizations = es(element, "ac:localizations/ac:localization[@ac:country='US'][@ac:language='en']");
        if (localizations.isEmpty()) {
            localizations = es(element, "ac:localizations/ac:localization[@ac:country='AC'][@ac:language='en']");
        }
        if (localizations.isEmpty()) {
            localizations = es(element, "ac:localizations/ac:localization[@ac:country='AC']");
        }

        for (int i = 0; i < localizations.size(); i++) {
            if (i > 0) json.append(", ");
            json.append("{\n");

            Element l = (Element) localizations.get(i);

            json.append("        \"country\": ");
            json.append("\"" + l.getAttributeValue("country", ns) + "\",\n");

            json.append("        \"language\": ");
            json.append("\"" + l.getAttributeValue("language", ns) + "\",\n");

            process(l, json, new String[] {
//                    "ac:title",
//                    "ac:summary",
                    "ac:price",
                    "ac:developer",
                    "ac:developer_url",
                    "ac:support_url"
            }, "        ");

            json.append("        \"categories\": [");
            json.append("\"" + e(l, "ac:categories").getAttributeValue("primary", ns) + "\"");
            List cats = es(l, "ac:categories/ac:category");
            for (int j = 0; j < cats.size(); j++) {
                Element cat = (Element) cats.get(j);
                json.append(", \"" + cat.getTextTrim() + "\"");
            }
            json.append("],\n");

            json.append("        \"images\": [\n");
            List imgs = es(l, "ac:images/ac:image/ac:asset_url[@ac:type='large']");
            for (int j = 0; j < imgs.size(); j++) {
                if (j > 0) json.append(",\n");
                Element img = (Element) imgs.get(j);
                json.append("            \"" + escapeURL(img.getTextTrim()) + "\"");
            }
            json.append("\n        ]\n");

            json.append("    }");
        }

        json.append("],\n");
    }

    private static String getText(Object ctx, String path) throws JDOMException {
        Element e = e(ctx, path);
        return (e == null) ? "" : e.getTextTrim();
    }

    private static void process(Object ctx, StringBuilder csv, String[] paths, String indent) throws JDOMException {
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            add(csv, ctx, path, indent);
        }
    }

    private static String escapeURL(String url) {
        url = url.replaceAll("\\+", "%20");
        return url;
    }

    private static String escape(String raw) {
//        if (raw.startsWith("http://") && raw.indexOf("+") != -1) {
//            raw = raw.replaceAll("\\+", "%2B");
//        }

        raw = raw.replaceAll("\"", "\\\\\"");
        raw = raw.replaceAll("\n", "<br/>");
        raw = raw.replaceAll("\u2026", "...");
        raw = raw.replaceAll("\u2013", "-"); // EN DASH
        raw = raw.replaceAll("\u2019", "'"); // RIGHT SINGLE QUOTATION
        raw = raw.replaceAll("\u201C", "\\\\\""); // LEFT DOUBLE QUOTATION
        raw = raw.replaceAll("\u201D", "\\\\\""); // RIGHT DOUBLE QUOTATION
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

    private static void add(StringBuilder csv, Object ctx, String path, String indent) throws JDOMException {
        Element e = e(ctx, path);

        String text = (e == null) ? "" : e.getTextTrim();

        String field = path;
        if (field.indexOf(":") != -1) field = field.substring(field.indexOf(":") + 1);

        csv.append(indent + "\"" + field + "\": " + escape(text) + ",\n");
    }
}
