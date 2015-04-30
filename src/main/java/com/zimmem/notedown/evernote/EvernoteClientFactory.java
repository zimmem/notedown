package com.zimmem.notedown.evernote;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;
import com.evernote.thrift.transport.TTransportException;

public class EvernoteClientFactory implements InitializingBean {

	@Autowired
	private ITokenHolder tokenHolder;

	@Value("${evernote.endpoint}")
	private String endpoint;

	private EvernoteService service;

	public NoteStoreClient createNoteStoreClient() throws EDAMUserException,
			EDAMSystemException, TException {
		ClientFactory clientFactory = new ClientFactory(new EvernoteAuth(
				this.service, tokenHolder.getToken()));
		return clientFactory.createNoteStoreClient();
	}

	public UserStoreClient createUserStoreClient() throws TTransportException {
		ClientFactory clientFactory = new ClientFactory(new EvernoteAuth(
				this.service, tokenHolder.getToken()));
		return clientFactory.createUserStoreClient();
	}

	public EvernoteService getService() {
		return service;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		service = "SANDBOX".equals(endpoint) ? EvernoteService.SANDBOX
				: "YINXIANG".equals(endpoint) ? EvernoteService.YINXIANG
						: EvernoteService.PRODUCTION;

	}
}
