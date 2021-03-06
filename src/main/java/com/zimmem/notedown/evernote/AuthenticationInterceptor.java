package com.zimmem.notedown.evernote;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;
import com.zimmem.notedown.evernote.oauth.OAuthServiceFactory;
import com.zimmem.notedown.utils.ServletUtils;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private ITokenHolder        tokenHolder;

    @Autowired
    private OAuthServiceFactory oauthServiceFactory;

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
                                Exception arg3) throws Exception {
        return;

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
                           ModelAndView arg3) throws Exception {
        return;

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        String token = ServletUtils.getCookies(request, "en_access_token");
        if (Strings.isNullOrEmpty(token)) {
            OAuthService oauthService = oauthServiceFactory.createOAuthService(createCallbackUrl(request));
            Token requestToken = oauthService.getRequestToken();
            request.getSession().setAttribute("request_token", requestToken);
            String url = oauthService.getAuthorizationUrl(requestToken);
            response.sendRedirect(url);
            return false;
        }

        tokenHolder.setToken(token);
        return true;
    }

    private static String callback_format = "%s://%s:%d/%s";

    public String createCallbackUrl(HttpServletRequest request) {
        return String.format(callback_format, request.getScheme(), request.getServerName(),
                             request.getServerPort(), "evernote_oauth_callback");
    }

}
