package com.palm.appcake.servlet;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.palm.appcake.model.App;
import com.palm.appcake.model.DB;

/**
 * LoaderServlet is responsible for 
 * 
 * Loads up the JDO database
 */
@SuppressWarnings("serial")
public class LoaderServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
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
	
	public void init() {
		//getServletContext().setAttribute("db", new DB());
	}
}
