package com.zimmem.notedown.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class ServletUtils {

	public static String getCookies(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null ){
			return null;
		}
		
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
