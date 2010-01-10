package com.palm.appetite;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Element;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.jdom.input.SAXBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Retrieves the feeds from Palm servers, combines them into one XML document, and puts that document into the
 * servlet context.
 */
public class FeedEater extends HttpServlet {
    public static final String KEY_MASTER_DOC = "masterFeed";

    private final AtomicBoolean updatingFeeds = new AtomicBoolean(false);

    private static final Namespace NS = Namespace.getNamespace("ac", "http://palm.com/app.catalog.rss.extensions");
    private static final DateFormat CHANNEL_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    private static final DateFormat ITEM_DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

    private static final String[] FEED_TYPES = { "c", "w", "b" };
    private static final String[] feedUrls = new String[FEED_TYPES.length];
    private static final String[] updateFeedUrls = new String[FEED_TYPES.length];
    private static final File[] backupFeeds = new File[FEED_TYPES.length];
    private static final int[] counts = new int[FEED_TYPES.length];

    private boolean useBackupFeedFirst = false;

    public void init() throws ServletException {
        String value = getServletConfig().getInitParameter("useBackupFeedFirst");
        useBackupFeedFirst = (value != null && value.equals("true"));

        for (int i = 0; i < FEED_TYPES.length; i++) {
            feedUrls[i] = getServletConfig().getInitParameter(FEED_TYPES[i] + "_url");
            updateFeedUrls[i] = getServletConfig().getInitParameter(FEED_TYPES[i] + "_update_url");

            String backupFeedFile = getServletConfig().getInitParameter(FEED_TYPES[i] + "_backup_file");
            if (backupFeedFile != null) {
                File file = null;
                try {
                    file = new File(backupFeedFile);
                    if (!file.exists()) {
                        System.err.println("Warning: backup feed file '" + backupFeedFile + "' does not exist; this may not be an error--perhaps it will exist when it is needed.");
                    }
                    backupFeeds[i] = file;
                } catch (Exception e) {
                    System.err.println("Error: backup feed file '" + backupFeedFile + "' does not appear to be a valid file name");
                    e.printStackTrace();
                }
            }
        }

        try {
            boolean updated = updateFeeds();
            if (updated) System.out.println("Feeds updated");
        } catch (Exception e) {
            System.out.println("Feeds not updated; see error log");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            boolean result = updateFeeds();
            if (result) {
                resp.getWriter().write("Successfully updated feeds: ");
                for (int i = 0; i < FEED_TYPES.length; i++) {
                    if (i > 0) resp.getWriter().write(", ");
                    resp.getWriter().write(FEED_TYPES[i] + ": " + counts[i]);
                }
            } else {
                resp.getWriter().write("Feed update already in process");
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("Couldn't update feeds!");
            e.printStackTrace();
        }
    }

    public boolean updateFeeds() throws Exception {
        synchronized (updatingFeeds) {
            // if we are presently updating the feeds; bail
            if (updatingFeeds.get()) return false;

            // flip the updating feeds switch
            updatingFeeds.set(true);
        }

        try {
            boolean backupFeedUsed = false;

            Document masterDoc = null;
            Date updateDate = null;
            for (int i = 0; i < FEED_TYPES.length; i++) {
                Document mainFeedDoc = null;

                if (useBackupFeedFirst) {
                    try {
                        mainFeedDoc = new SAXBuilder().build(backupFeeds[i]);
                        backupFeedUsed = true;
                    } catch (Exception e) {}
                }

                try {
                    if (mainFeedDoc == null) mainFeedDoc = d(feedUrls[i]);
                } catch (Exception e) {
                    System.err.println("Couldn't retrieve '" + FEED_TYPES[i] + "' from URL '" + feedUrls[i] + "'; will try to load backup feed file");
                    if (backupFeeds[i] != null) {
                        try {
                            mainFeedDoc = new SAXBuilder().build(backupFeeds[i]);
                            backupFeedUsed = true;
                            System.err.println("  Backup feed loaded from '" + backupFeeds[i] + "'");
                        } catch (Exception e1) {
                            System.err.println("  Couldn't load backup feed due to error; throwing original exception up the chain");
                            e1.printStackTrace();
                        }
                    } else {
                        System.err.println("  No backup feed specified; throwing exception up the chain.");
                    }

                    // if we don't have a feed yet, throw the error up the chain
                    if (mainFeedDoc == null) throw e;
                }

                Date mainFeedDate = CHANNEL_DATE_FORMAT.parse(s(mainFeedDoc, "/rss/channel/lastBuildDate"));

                // only process the update feed if we didn't revert to the backup feed
                if (!backupFeedUsed) {
                    Document updateFeedDoc = d(updateFeedUrls[i]);
                    Date updateFeedDate = CHANNEL_DATE_FORMAT.parse(s(updateFeedDoc, "/rss/channel/lastBuildDate"));

                    if (updateFeedDate.after(mainFeedDate)) {
                        mainFeedDate = updateFeedDate;

                        // the update feed has new information; merge it in
                        List allItems = es(updateFeedDoc, "/rss/channel/item");

                        Element mainChannel = e(mainFeedDoc, "/rss/channel");

                        for (int j = 0; j < allItems.size(); j++) {
                            Element item = (Element) allItems.get(j);
                            String guid = s(item, "ac:packageid");
                            Element oldItem = e(mainFeedDoc, "/rss/channel/item[ac:packageid = '" + guid + "']");
                            if (oldItem != null) mainChannel.removeContent(oldItem);

                            item.getParentElement().removeContent(item);
                            mainChannel.addContent(item);
                        }
                    }
                }

                // add the channel
                List allItems = es(mainFeedDoc, "/rss/channel/item");
                counts[i] = allItems.size();
                for (int j = 0; j < allItems.size(); j++) {
                    Element item = (Element) allItems.get(j);
                    Element channel = new Element("channel", NS);
                    item.addContent(channel);
                    channel.setText(FEED_TYPES[i]);
                }

                // merge the items into the master feed doc
                if (masterDoc == null) {
                    masterDoc = mainFeedDoc;
                    updateDate = mainFeedDate;
                } else {
                    Element masterChannel = e(masterDoc, "/rss/channel");
                    for (int j = 0; j < allItems.size(); j++) {
                        Element item = (Element) allItems.get(j);
                        item.getParentElement().removeContent(item);
                        masterChannel.addContent(item);
                    }
                }
            }

            getServletContext().setAttribute("masterFeed", new DocDate(masterDoc, updateDate));
        } finally {
            synchronized (updatingFeeds) {
                // we are no longer updating the feeds
                updatingFeeds.set(false);
            }
            return true;
        }
    }

    private Document d(String surl) throws IOException, JDOMException {
        URL url = new URL(surl);
        InputStream in = url.openStream();
        Document doc = new SAXBuilder().build(in);
        in.close();
        return doc;
    }

    private String s(Object ctx, String xpath) throws JDOMException {
        Element el = e(ctx, xpath);
        return (el != null) ? el.getTextTrim() : "";
    }

    private Element e(Object ctx, String xpath) throws JDOMException {
        XPath xp = XPath.newInstance(xpath);
        xp.addNamespace(NS);
        return (Element) xp.selectSingleNode(ctx);
    }

    private List es(Object ctx, String xpath) throws JDOMException {
        XPath xp = XPath.newInstance(xpath);
        xp.addNamespace(NS);
        return xp.selectNodes(ctx);
    }
}
