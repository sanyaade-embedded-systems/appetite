package com.palm.appcake.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.palm.appcake.model.DB;
import com.palm.appcake.model.Icon;
import com.palm.appcake.model.Item;

public class IconServlet extends BaseServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
		List<String> iconJSON = new ArrayList<String>();

		int count = 100;
		String countParam = req.getParameter("count");
		if (countParam != null) {
			Integer.parseInt(countParam);
		}

		boolean uniqueIcons = true;
		String uniqueIconsParam = req.getParameter("uniqueIcons");
		if (uniqueIconsParam != null) {
			uniqueIcons = Boolean.parseBoolean(uniqueIconsParam);
		}

		PersistenceManager pm = DB.getPMF().getPersistenceManager();

        try {        	
//        	a). Explicit Parameters 
//        	SELECT FROM " + PersistentPreferences.class.getName() + " where user 
//        	== userParam PARAMETERS " + 
//        	User.class.getName() + " userParam" 
        	
//        	Query query = pm.newQuery("SELECT FROM " + Item.class.getName() + " WHERE pubDate < pubDateParam PARAMETERS java.util.Date pubDateParam");
            Query query = pm.newQuery(Item.class);
            query.setRange(0, count);
//            query.setFilter("pubDate < '2009-12-21 22:00:00'");
//            query.declareParameters("Date pubDate");
//            query.declareImports("import java.util.Date");
//            query.setFilter("icons.type");
//            query.declareParameters("String typeParam");

            try {
                List<Item> results = (List<Item>) query.execute();
                if (results.iterator().hasNext()) {
                    for (Item i : results) {
                    	iconJSON.add(this.makeIconJSON(i));
                    	//resp.getWriter().println(i.getTitle());
                    }
                    String result = "[ ";
                    for (String s : iconJSON) {
                    	result += s;
                    }
                    result += "] ";
                    printWithCallback(result, req, resp);
                    //resp.getWriter().println(iconJSON.
                } else {
                	resp.getWriter().println("no results");
                }
            } finally {
                query.closeAll();
            }
        } finally {
        	pm.close();
        }
	}
	
	private String makeIconJSON(Item i) {
		List <Icon> icons = i.getIcons();
		String iconJSON = "";
		if (!icons.isEmpty()) {
			iconJSON = ", icon: '" + icons.get(0).getUrl() + "'";
		}
		return "{ id: '" + i.getGuid() + "'" + iconJSON + ", name: '" + i.getTitle() + "' },";
	}

}
