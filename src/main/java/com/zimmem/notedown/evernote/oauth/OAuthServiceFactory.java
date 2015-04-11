package com.zimmem.notedown.evernote.oauth;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.EvernoteApi;
import org.scribe.oauth.OAuthService;

public class OAuthServiceFactory {

    public static OAuthService createOAuthService(String key, String secret, String callbackServer) {
        OAuthService service = new ServiceBuilder().provider(EvernoteApi.Sandbox.class).apiKey(key).apiSecret(secret).callback(callbackServer
                                                                                                                                       + "/evernote_oauth_callback").build();
        return service;
    }

}
