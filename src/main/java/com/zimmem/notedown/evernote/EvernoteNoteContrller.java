package com.zimmem.notedown.evernote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;

@RestController
@RequestMapping("/evernote/notes")
public class EvernoteNoteContrller {

    @Autowired
    private ITokenHolder          tokenHolder;

    @Autowired
    private EvernoteClientFactory clientFactory;

    public void listAll() {
    }

    @RequestMapping(method = RequestMethod.POST)
    public Note createNote(Note note) throws EDAMUserException, EDAMSystemException, TException,
                                     EDAMNotFoundException {
        NoteStoreClient noteStore = clientFactory.createNoteStoreClient();
        List<Notebook> notebooks = noteStore.listNotebooks();
        Notebook theNotedownBook = null;
        for (Notebook notebook : notebooks) {
            if (notebook.getName().equals("Notedown")) {
                theNotedownBook = notebook;
                break;
            }
        }

        if (theNotedownBook == null) {
            Notebook notebook = new Notebook();
            notebook.setName("Notedown");
            theNotedownBook = noteStore.createNotebook(notebook);
        }

        note.setNotebookGuid(theNotedownBook.getGuid());
        
        note.setContent(wrapNoteContent(note.getContent()));
        NoteAttributes attribute = new NoteAttributes();
        attribute.setSource("notedown");
        note.setAttributes(attribute);

        return noteStore.createNote(note);
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    public void updateNote() {

    }

    public String wrapNoteContent(String content) {
        String nBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        nBody += "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">";
        nBody += "<en-note>";
        nBody += content;
        nBody += "</en-note>";
        return nBody;
    }

}
