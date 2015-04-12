package com.zimmem.notedown.evernote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.User;
import com.evernote.edam.userstore.UserStore;
import com.evernote.thrift.TException;
import com.evernote.thrift.transport.TTransportException;

@RestController
@RequestMapping("/evernote")
public class EvernoteUserController {

    @Autowired
    private ITokenHolder tokenHolder;

    @RequestMapping("/user")
    public User detail() throws EDAMUserException, EDAMSystemException, TException {
        ClientFactory factory = new ClientFactory(new EvernoteAuth(EvernoteService.SANDBOX,
                                                                   tokenHolder.getToken()));
        UserStoreClient userStore = factory.createUserStoreClient();
        User user = userStore.getUser();
        return user;
    }

}
