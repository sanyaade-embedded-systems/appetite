package com.palm.appetite;

import org.jdom.Document;

import java.util.Date;

public class DocDate {
    private Date date;
    private Document doc;

    public DocDate(Document doc, Date date) {
        this.doc = doc;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public Document getDoc() {
        return doc;
    }
}
