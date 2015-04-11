package notedown;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.EvernoteApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public class OAuthTest {

	public static void main(String[] args) {
		OAuthService service = new ServiceBuilder()
				.provider(EvernoteApi.Sandbox.class).apiKey("zimmem")
				.apiSecret("c1a837dfaa11d177")
				.callback("http://localhost:8080/evernote_oauth_callback")
				.build();
		Token token = service.getRequestToken();
		System.out.println(token);
		String authorizationUrl = service.getAuthorizationUrl(token);
		System.out.println(authorizationUrl);
	}
}
