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

import com.zimmem.notedown.evernote.oauth.OAuthServiceFactory;

@Controller
public class OAuthController {

    @Autowired
    private OAuthServiceFactory oAuthServiceFactory;

    @RequestMapping("/evernote_oauth_callback")
    public void callback(@RequestParam("oauth_token") String oauthToken,
                         @RequestParam("oauth_verifier") String oauthVerifier,
                         HttpServletRequest request, HttpServletResponse response)
                                                                                  throws IOException {
        OAuthService oAuthService = oAuthServiceFactory.createOAuthService();
        Token accessToken = oAuthService.getAccessToken((Token) request.getSession().getAttribute("request_token"),
                                                        new Verifier(oauthVerifier));
        Cookie cookie = new Cookie("en_access_token", accessToken.getToken());
        cookie.setMaxAge(9999999);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect("/authenticate_success.html");
    }
    
    @RequestMapping("/evernote/authenticate")
    public String authenticate()  throws IOException {
    	return "redirect:/authenticate_success.html";
    }
}
