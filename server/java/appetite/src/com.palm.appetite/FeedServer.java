package com.palm.appetite;

import org.jdom.JDOMException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import com.palm.JSONConverter;

/**
 * Serves the app feed in JavaScript format
 */
public class FeedServer extends HttpServlet {
    // JSON conversion of the master feed XML document
    private String json;

    // date of the master feed XML document that was used for building the JSON representation
    // if json is non-null, jsonDate should also be non-null
    private Date jsonDate = new Date(1);

    // when true, another thread is presently updating the JSON representation
    private final AtomicBoolean updatingJson = new AtomicBoolean(false);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().toLowerCase();
        if (path.endsWith("/all")) {
            allApps(req, resp, false);
        } else if (path.endsWith("/alljs")) {
            allApps(req, resp, true);
        } else {
            resp.setStatus(404);
        }
    }

    /**
     *
     * Returns a JSON array of all of the apps in all channels
     * 
     * @param req
     * @param resp
     */
    private void allApps(HttpServletRequest req, HttpServletResponse resp, boolean asJs) throws IOException {
        DocDate docDate = (DocDate) getServletContext().getAttribute(FeedEater.KEY_MASTER_DOC);
        if (docDate != null) {
            if ( (json == null) || (docDate.getDate().after(jsonDate)) ) {
                updateAllApps();
            }
        }

        String toReturn = (asJs) ? "var apps = " : "";
        toReturn += (json == null) ? "[]" : json;

        resp.setDateHeader("Last-Modified", jsonDate.getTime());
        resp.setContentType("text/javascript");
        resp.getWriter().write(toReturn);
    }

    private void updateAllApps() {
        synchronized (updatingJson) {
            if (updatingJson.get()) return;
            updatingJson.set(true);
        }

        try {
            DocDate docDate = (DocDate) getServletContext().getAttribute(FeedEater.KEY_MASTER_DOC);
            json = JSONConverter.convert(docDate.getDoc());
            jsonDate = docDate.getDate();
        } catch (JDOMException e) {
            System.err.println("Error converting feed to JSON");
            e.printStackTrace();
        } finally {
            synchronized (updatingJson) {
                updatingJson.set(false);
            }
        }
    }
}
