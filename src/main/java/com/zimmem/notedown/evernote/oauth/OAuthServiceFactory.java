package com.zimmem.notedown.evernote.oauth;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.EvernoteApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

@Service
public class OAuthServiceFactory {

    @Value("${evernote.endpoint}")
    private String endpoint;

    @Value("${evernote.oauth.key}")
    private String key;

    @Value("${evernote.oauth.secret}")
    private String secret;

    public OAuthService createOAuthService() {
        Class<EvernoteApi> apiClazz = (Class<EvernoteApi>) ("SANDBOX".equals(endpoint) ? EvernoteApi.Sandbox.class : "YINXIANG".equals(endpoint) ? Yinxing.class : EvernoteApi.class);
        ServiceBuilder builder = new ServiceBuilder().provider(apiClazz).apiKey(key).apiSecret(secret);
        return builder.build();
    }

    public OAuthService createOAuthService(String callback) {
        Class<EvernoteApi> apiClazz = (Class<EvernoteApi>) ("SANDBOX".equals(endpoint) ? EvernoteApi.Sandbox.class : "YINXIANG".equals(endpoint) ? Yinxing.class : EvernoteApi.class);
        ServiceBuilder builder = new ServiceBuilder().provider(apiClazz).apiKey(key).apiSecret(secret);
        builder.callback(callback);
        return builder.build();
    }

    public static class Yinxing extends EvernoteApi {

        private static final String YINXING_URL = "https://www.yinxiang.com/";

        @Override
        public String getRequestTokenEndpoint() {
            return YINXING_URL + "/oauth";
        }

        @Override
        public String getAccessTokenEndpoint() {
            return YINXING_URL + "/oauth";
        }

        @Override
        public String getAuthorizationUrl(Token requestToken) {
            return String.format(YINXING_URL + "/OAuth.action?oauth_token=%s",
                                 requestToken.getToken());
        }
    }

}
