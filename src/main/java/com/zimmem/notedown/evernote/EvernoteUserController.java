package com.zimmem.notedown.evernote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.User;
import com.evernote.thrift.TException;

@RestController
@RequestMapping("/evernote")
public class EvernoteUserController {

    @Autowired
    private ITokenHolder tokenHolder;
    
    @Autowired
    private EvernoteClientFactory clientFactory;

    @RequestMapping("/user")
    public User detail() throws EDAMUserException, EDAMSystemException, TException {
        UserStoreClient userStore = clientFactory.createUserStoreCLient();
        return userStore.getUser();
    }

}
