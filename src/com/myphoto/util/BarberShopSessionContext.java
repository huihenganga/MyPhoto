package com.myphoto.util;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

public class BarberShopSessionContext {
	private static BarberShopSessionContext instance;
	private HashMap<String, HttpSession> mymap;

	private BarberShopSessionContext() {
		mymap = new HashMap<String, HttpSession>();
	}

	public static BarberShopSessionContext getInstance() {
		if (instance == null) {
			instance = new BarberShopSessionContext();
		}
		return instance;
	}

	public synchronized void AddSession(String token, HttpSession session) {
		if (session != null) {
			session.setAttribute("token", token);
			mymap.put(token, session);
		}
	}

	public synchronized void RemoveSession(String token) {
		HttpSession session = mymap.get(token);
		mymap.remove(token);
		if (session != null) {
			session.invalidate();
			session = null;
		}

	}

	public synchronized void DelSession(HttpSession session) {
		if (session != null) {
			String token = (String) session.getAttribute("token");
			mymap.remove(token);
		}
	}

	public synchronized HttpSession getSession(String token) {
		if (token == null)
			return null;
		return mymap.get(token);
	}

	public synchronized Integer getUserIdByToken(String token) {
		if (token == null)
			return null;
		if (mymap.get(token) == null) {
			return null;
		}
		return (Integer) mymap.get(token).getAttribute("userId");
	}

	/**
	 * @Description 从token里获取医生代码
	 * @param token
	 * @return String
	 * @throws
	 * @author 罗渲
	 * @date 2015-7-2 下午3:02:55
	 */
	public synchronized String getCodeByToken(String token) {
		if (token != null && mymap.get(token) != null
				&& mymap.get(token).getAttribute("code") != null)
			return mymap.get(token).getAttribute("code").toString();
		else
			return null;
	}
}