package com.zimmem.notedown.evernote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;

@RestController
@RequestMapping("/evernote/notebooks")
public class EvernoteNotebookController {

    @Autowired
    private EvernoteClientFactory clientFactory;

    @RequestMapping("")
    public List<Notebook> listAll() throws EDAMUserException, EDAMSystemException, TException {
        NoteStoreClient noteStore = clientFactory.createNoteStoreClient();
        return noteStore.listNotebooks();
    }
}
