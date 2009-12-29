package com.palm.appcake.model;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class DB {
	private static final PersistenceManagerFactory pmf =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");
		
    public static PersistenceManagerFactory getPMF() {
        return pmf;
    }
	
	public static App getById(String id) {
		PersistenceManager pm = null;
		try {
        	pm = pmf.getPersistenceManager();
            return (App) pm.getObjectById(App.class, id);
        } finally {
            if (pm != null) pm.close();
        }
	}

}
