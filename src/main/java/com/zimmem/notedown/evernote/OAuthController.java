package com.zimmem.notedown.evernote;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OAuthController {

	@Autowired
	private OAuthService oauthService;

	@RequestMapping("evernote_oauth_callback")
	public void callback(@RequestParam("oauth_token") String oauthToken,
			@RequestParam("oauth_verifier") String oauthVerifier,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Token accessToken = oauthService.getAccessToken((Token) request
				.getSession().getAttribute("request_token"), new Verifier(
				oauthVerifier));
		Cookie cookie = new Cookie("en_access_token", accessToken.getToken());
		cookie.setMaxAge(9999999);
		response.addCookie(cookie);
		response.sendRedirect("/evernote/xxxxx");
	}
}
