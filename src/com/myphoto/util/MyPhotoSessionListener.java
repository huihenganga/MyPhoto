package com.myphoto.util;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MyPhotoSessionListener implements HttpSessionListener {

	private BarberShopSessionContext myc = BarberShopSessionContext.getInstance();

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		myc.DelSession(session);
	}
}
