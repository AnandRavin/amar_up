package com.cypherx.session;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Service;

@Service
public class SessionCollector implements HttpSessionListener {
	private static final HashMap<String, HttpSession> sessions = new HashMap<String, HttpSession>();

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		sessions.put(session.getId(), session);
		System.out.println("Get SessionId:::::::::::"+session.getId());
	}

	@Override
	 public void sessionDestroyed(HttpSessionEvent event) {
	  sessions.remove(event.getSession().getId());
	 }
	
	public static HttpSession find(String sessionId) {
		return sessions.get(sessionId);
	}

}
