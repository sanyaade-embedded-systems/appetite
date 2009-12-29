package com.palm.appcake.servlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.palm.appcake.model.App;
import com.palm.appcake.model.DB;
import com.palm.appcake.model.Rss;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * LoaderServlet is responsible for 
 * 
 * Loads up the JDO database
 */
@SuppressWarnings("serial")
public class LoaderServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		xstream.alias("rss", Rss.class);
		Rss rss = (Rss) xstream.fromXML(this.loadXml());
		resp.getWriter().println(rss.getChannel().getItems().get(0).getTitle());
		if (true) return;
		
		PersistenceManager pm = DB.getPMF().getPersistenceManager();
		
		try {
			Query query = pm.newQuery(App.class);
			query.deletePersistentAll();

            pm.makePersistent(new App("App1", "http://almaer.com/", "Application One"));
        } finally {
            pm.close();
        }
        
        try {
        	pm = DB.getPMF().getPersistenceManager();
            App app = pm.getObjectById(App.class, "App1");
            resp.getWriter().println(app.getUrl());    
        } finally {
            pm.close();
        }
		
        resp.getWriter().println("Loaded");
        
//		DB db = (DB) getServletContext().getAttribute("db");
//		App app = db.get("app1");

//		List<App> apps = new ArrayList<App>();
//		
//		App app = new App("App1", "http://almaer.com/", "Application One");
//		apps.add(app);
//		apps.add(app);
		
//        XStream xstream = new XStream(new JettisonMappedXmlDriver());
//        xstream.setMode(XStream.NO_REFERENCES);
//        xstream.alias("app", App.class);
//
//        resp.getWriter().println(xstream.toXML(app));
	}
	
	private String loadXml() {
		return "<rss version=\"2.0\">" +
		"    <channel>" +
		"        <title>Device App Updates for US</title>" +
		"        <link>http://www.palm.com</link>" +
		"        <description>Updates on the latest US Device channel applications for Palm webOS devices</description>" +
		"        <language>en-US</language>" +
		"        <pubDate>Mon, 28 Dec 2009 17:28:55 -0800</pubDate>" +
		"        <lastBuildDate>Mon, 28 Dec 2009 17:28:55 -0800</lastBuildDate>" +
		"        <distributionChannel>Device</distributionChannel>" +
		"        <countryCode>US</countryCode>" +
		"        <item>" +
		"            <title><![CDATA[Mine Search]]></title>" +
		"            <link>http://developer.palm.com/appredirect/?packageid=com.engineequalscar.games.mines</link>" +
		"            <description><![CDATA[Mine searcher is our take on this classic game. The object of the game is to select tiles to clear the field and to avoid mines. As the tiles are selected the number of mines that are adjacent to that tile is revealed to help the player determine the mines' locations. The game allows you to change board size and the number of mines in the field for varying difficulty and endless fun. Records are kept so you can keep track of your best times." +
		"" +
		"-- If you had the beta version you must uninstall that prior to downloading.]]></description>" +
		"            <pubDate>2009-12-21 21:00:58</pubDate>" +
		"            <guid>6.6</guid>" +
		"            <packageid>com.engineequalscar.games.mines</packageid>" +
		"            <version>0.9.5</version>" +
		"            <installed_size>1038810</installed_size>" +
		"            <rating>2.9</rating>" +
		"            <total_downloads>1322</total_downloads>" +
		"            <total_comments>17</total_comments>" +
		"            <min_os>webOS 1.0.0</min_os>" +
		"            <devices>Palm Pre, Palm Pixi</devices>" +
		"            <language>en</language><country>US</country>" +
		"            <icons>" +
		"                <asset_url type=\"app\">http://cdn.downloads.palm.com/public/6/icon/icon_252D5E.png</asset_url>" +
		"                <asset_url type=\"scaled\">http://cdn.downloads.palm.com/public/6/icon/S/icon_252D5E.png</asset_url>" +
		"                <asset_url type=\"large\">http://cdn.downloads.palm.com/public/6/icon/L/icon_big.png</asset_url>" +
		"            </icons>" +
		"            <localizations>" +
		"                <localization country=\"US\" language=\"en\">" +
		"                    <title><![CDATA[Mine Search]]></title>" +
		"                    <summary><![CDATA[Mine searcher is our take on this classic game. The object of the game is to select tiles to clear the field and to avoid mines. As the tiles are selected the number of mines that are adjacent to that tile is revealed to help the player determine the mines' locations. The game allows you to change board size and the number of mines in the field for varying difficulty and endless fun. Records are kept so you can keep track of your best times." +
		"" +
		"-- If you had the beta version you must uninstall that prior to downloading.]]></summary>" +
		"                    <price currency=\"USD\">0.99000000953674</price>" +
		"                    <developer>Engine Equals Car</developer>" +
		"                    <developer_url>http://www.engineequalscar.com</developer_url>" +
		"                    <support_url>http://www.engineequalscar.com</support_url>" +
		"                    <categories>" +
		"                        <category><![CDATA[Games!Arcade]]></category>" +
		"                        <category><![CDATA[Games!Classics]]></category>" +
		"                        <category><![CDATA[Games!Strategy]]></category>" +
		"                    </categories>" +
//		"                    <images>" +
//		"                        <image>" +
//		"                            <asset_url type=\"app\">http://cdn.downloads.palm.com/public/6/en/images/1/A/mines_2009-08-09_061656.jpg</asset_url>" +
//		"                            <asset_url type=\"scaled\">http://cdn.downloads.palm.com/public/6/en/images/1/S/mines_2009-08-09_061656.jpg</asset_url>" +
//		"                            <asset_url type=\"large\">http://cdn.downloads.palm.com/public/6/en/images/1/L/mines_2009-08-09_061656.jpg</asset_url>" +
//		"                        </image>" +
//		"                        <image>" +
//		"                            <asset_url type=\"app\">http://cdn.downloads.palm.com/public/6/en/images/2/A/mines_2009-20-08_071859_256A3E.jpg</asset_url>" +
//		"                            <asset_url type=\"scaled\">http://cdn.downloads.palm.com/public/6/en/images/2/S/mines_2009-20-08_071859_256A3E.jpg</asset_url>" +
//		"                            <asset_url type=\"large\">http://cdn.downloads.palm.com/public/6/en/images/2/L/mines_2009-20-08_071859_256A3E.jpg</asset_url>" +
//		"                        </image>" +
//		"                        <image>" +
//		"                            <asset_url type=\"app\">http://cdn.downloads.palm.com/public/6/en/images/3/A/mines_2009-08-09_061855.jpg</asset_url>" +
//		"                            <asset_url type=\"scaled\">http://cdn.downloads.palm.com/public/6/en/images/3/S/mines_2009-08-09_061855.jpg</asset_url>" +
//		"                            <asset_url type=\"large\">http://cdn.downloads.palm.com/public/6/en/images/3/L/mines_2009-08-09_061855.jpg</asset_url>" +
//		"                        </image>" +
//		"                    </images>" +
		"                </localization>" +
		"            </localizations>" +
		"        </item>" +
		"    </channel>" +
		"</rss>";
	}
	private String loadXml2() {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("/SourceControl/Palm/github/appcake/samples/catalog.single.xml")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        char[] buf = new char[1024];
        int numRead = 0;
        try {
			while ((numRead = reader.read(buf)) != -1) {
			    String readData = String.valueOf(buf, 0, numRead);
			    fileData.append(readData);
			    buf = new char[1024];
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return fileData.toString();		
	}
	
	public void init() {
		//getServletContext().setAttribute("db", new DB());
	}
}
