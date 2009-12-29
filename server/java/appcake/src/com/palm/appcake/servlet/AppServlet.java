package com.palm.appcake.servlet;

import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import com.palm.appcake.model.App;
import com.palm.appcake.model.DB;

/**
 * AppServlet is responsible for 
 * 
 *  /app?id=[id]&locale=*
 *  
 *  Returns an object modeled on the <item> element in the source XML file; the "ac:" namespace prefix is omitted from the property names. The "Accept-Language" header will be used to select the most appropriate localization to send to the client (and only one localization will be sent).
 *  
 *  Params:
 *    - id: guid
 *    - locale: optional; if "*" is specified, all localizations will be sent; all other values are ignored
 */
@SuppressWarnings("serial")
public class AppServlet extends BaseServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String id = req.getParameter("id");
		String locale = req.getParameter("locale");

		resp.setContentType("text/plain");
		
		if (id == null) {
			resp.getWriter().println("Please give me an id. Please.");
			return;
		}
		
		App app;
		try {
			app = DB.getById(id);
		} catch (JDOObjectNotFoundException e) {
			resp.getWriter().println("{error: 'unable to find an object'}");
			return;
		}
		
        printWithCallback(app.asJSON(), req, resp);
	}
	
	public void init() {
		//getServletContext().setAttribute("db", new DB());
	}
}
