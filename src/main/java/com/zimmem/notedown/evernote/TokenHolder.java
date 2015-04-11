package com.zimmem.notedown.evernote;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
public class TokenHolder implements ITokenHolder {

	private String token;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zimmem.notedown.evernote.ITokenHolder#getToken()
	 */
	@Override
	public String getToken() {
		return token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zimmem.notedown.evernote.ITokenHolder#setToken(java.lang.String)
	 */
	@Override
	public void setToken(String token) {
		this.token = token;
	}
}
