package com.zimmem.notedown.evernote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/evernote")
public class EvernoteController {

	@Autowired
	private ITokenHolder token;

	@RequestMapping("/token")
	public Object test() {
		return token.getToken();
	}
}
