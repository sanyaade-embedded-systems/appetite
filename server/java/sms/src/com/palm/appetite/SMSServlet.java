package com.palm.appetite;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.protocol.HTTP;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class SMSServlet extends HttpServlet {
    private String smsEndpoint;
    private String smsPostEndpoint;
    private String clientId;

    public void init(ServletConfig config) throws ServletException {
        clientId = config.getInitParameter("clientId");
        smsEndpoint = config.getInitParameter("smsEndpoint");
        smsPostEndpoint = config.getInitParameter("smsPostEndpoint");

        smsEndpoint = smsEndpoint.replaceAll("\\[cid\\]", clientId);
        smsPostEndpoint = smsPostEndpoint.replaceAll("\\[cid\\]", clientId);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("hello") != null) {
            resp.getWriter().write("world, again!");
            return;
        }

        URL url = new URL(smsEndpoint);

        InputStream in = new BufferedInputStream(url.openStream(), 10000);

        byte[] bytes = new byte[10000];
        int b;
        int length = 0;
        while ((b = in.read()) != -1) {
            length++;

            if (length > bytes.length) {
                byte[] newbytes = new byte[bytes.length * 2];
                System.arraycopy(bytes, 0, newbytes, 0, bytes.length);
                bytes = newbytes;
            }

            bytes[length - 1] = (byte) b;
        }

        byte[] newbytes = new byte[length];
        System.arraycopy(bytes, 0, newbytes, 0, length);

        in.close();

        resp.setContentType("text/html");

        if (req.getParameter("iframe") != null) {
            String contents = new String(newbytes);
            contents = contents.replaceAll("border:1px solid #494949;", "");
            resp.getWriter().write("<html><body style=\"margin: 0; padding: 0\"><form id=\"SMSForm\" method=\"POST\" action=\"/sms/post\">" +
                    "<input type=\"hidden\" name=\"packageid\" value=\"" + req.getParameter("packageid") + "\" />" +
                    contents + "</form></body></html>"
            );
        } else {
            resp.getWriter().write(new String(newbytes));
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(smsPostEndpoint);
        post.setHeader("User-Agent", "Project Appetite");

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("cid", clientId));
        nvps.add(new BasicNameValuePair("phone", req.getParameter("palm_sms_ph")));
        nvps.add(new BasicNameValuePair("country", req.getParameter("palm_sms_cc")));
        nvps.add(new BasicNameValuePair("packageid", req.getParameter("packageid")));

        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse response = client.execute(post);

        StatusLine statusLine = response.getStatusLine();

        //System.out.println(statusLine.getStatusCode() + " - " + statusLine.getReasonPhrase());

        int status = statusLine.getStatusCode();
        resp.setStatus(status);

        resp.setContentType("text/html");

        if (status == 200) {
            resp.getWriter().write("<html><body style='font: 12pt Verdana, sans-serif; margin: 20pt; text-align: center'>SMS sent to the phone</body></html>");
        } else {
            resp.getWriter().write("<html><body style='font: 12pt Verdana, sans-serif; margin: 20pt; text-align: center'>Sorry, SMS not sent to phone</body></html>");            
        }
    }
}