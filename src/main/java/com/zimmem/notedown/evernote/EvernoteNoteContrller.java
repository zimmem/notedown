package com.zimmem.notedown.evernote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.zimmem.notedown.evernote.model.MNote;

@RestController
@RequestMapping("/evernote/notes")
public class EvernoteNoteContrller {

	@Autowired
	private ITokenHolder tokenHolder;

	@Autowired
	private EvernoteClientFactory clientFactory;

	@RequestMapping(method = RequestMethod.GET)
	public List<MNote> listAll() throws EDAMUserException, EDAMSystemException,
			TException, EDAMNotFoundException {
		NoteStoreClient noteStore = clientFactory.createNoteStoreClient();
		NoteFilter filter = new NoteFilter();
		filter.setWords("source:notedown");
		NoteList notes = noteStore.findNotes(filter, 0, 99);
		List<MNote> mnotes = Lists.transform(notes.getNotes(),
				new Function<Note, MNote>() {

					@Override
					public MNote apply(Note note) {
						return MNote.fromNote(note);
					}
				});
		return mnotes;

	}

	@RequestMapping(method = RequestMethod.POST)
	public MNote createNote(@RequestBody MNote mNote) throws EDAMUserException,
			EDAMSystemException, TException, EDAMNotFoundException {
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

		Note note = mNote.toNote();

		note.setNotebookGuid(theNotedownBook.getGuid());
		NoteAttributes attribute = new NoteAttributes();
		attribute.setSource("notedown");
		note.setAttributes(attribute);

		Note created = noteStore.createNote(note);
		return MNote.fromNote(noteStore.getNote(created.getGuid(), true, false,
				false, false));
	}

	@RequestMapping(value = "/{guid}", method = RequestMethod.GET)
	public MNote getNote(@PathVariable String guid) throws EDAMUserException,
			EDAMSystemException, EDAMNotFoundException, TException {
		NoteStoreClient noteStore = clientFactory.createNoteStoreClient();
		Note note = noteStore.getNote(guid, true, false, false, false);
		return MNote.fromNote(note);
	}

	@RequestMapping(value = "/{guid}/share", method = RequestMethod.GET)
	public String share(@PathVariable String guid) throws EDAMUserException,
			EDAMSystemException, EDAMNotFoundException, TException {
		UserStoreClient userStore = clientFactory.createUserStoreClient();
		String shardId = userStore.getUser().getShardId();
		NoteStoreClient noteStore = clientFactory.createNoteStoreClient();
		String shareKey = noteStore.shareNote(guid);
		return clientFactory.getService().getHost() + "/shard/" + shardId
				+ "/sh/" + guid + "/" + shareKey;
	}

	@RequestMapping(value = "/{guid}/share/stop", method = RequestMethod.GET)
	public String stopShare(@PathVariable String guid)
			throws EDAMUserException, EDAMSystemException,
			EDAMNotFoundException, TException {
		NoteStoreClient noteStore = clientFactory.createNoteStoreClient();
		noteStore.stopSharingNote(guid);
		return "success";
	}

	@RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
	public MNote updateNote(@RequestBody MNote mnote) throws EDAMUserException,
			EDAMSystemException, TException, EDAMNotFoundException {
		NoteStoreClient noteStore = clientFactory.createNoteStoreClient();
		Note updatedNote = noteStore.updateNote(mnote.toNote());
		Note note = noteStore.getNote(updatedNote.getGuid(), true, false,
				false, false);
		return MNote.fromNote(note);
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
