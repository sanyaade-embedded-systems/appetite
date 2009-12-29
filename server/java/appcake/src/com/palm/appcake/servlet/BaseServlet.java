package com.palm.appcake.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public abstract class BaseServlet extends HttpServlet {
	private String callback = "callback";
	
	public void printWithCallback(String jsonToWrap, HttpServletRequest req, HttpServletResponse resp) {
		String callback = req.getParameter("callback");
		if (callback == null) {
			callback = this.callback;
		}
        try {
			resp.getWriter().println(callback + "(" + jsonToWrap + ")");
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
}
