package com.zimmem.notedown.evernote.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.evernote.edam.type.Note;
import com.google.common.base.Strings;

public class MNote {

    private String guid;

    private String title;

    private String content;

    public Note toNote() {
        Note note = new Note();
        note.setGuid(this.guid);
        note.setTitle(this.title);
        note.setContent(wrapNoteContent(this.content));
        return note;
    }

    public static MNote fromNote(Note note) {
        MNote mNote = new MNote();
        mNote.guid = note.getGuid();
        mNote.title = note.getTitle();
        mNote.content = parseContent(note.getContent());
        return mNote;
    }

    private String wrapNoteContent(String content) {
        String nBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        nBody += "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">";
        nBody += "<en-note>";
        nBody += content;
        nBody += "</en-note>";
        return nBody;
    }

    private static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

    private static String parseContent(String xml) {
        if (Strings.isNullOrEmpty(xml)) {
            return null;
        }
        int index = xml.indexOf("<en-note>");
        return xml.substring(index + 9, xml.length() - 10);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public static void main(String[] args) {
        MNote mote = new MNote();
        mote.setContent("aaaa");
        Note note = mote.toNote();
        String content = note.getContent();
        System.out.println(content);
        mote = MNote.fromNote(note);
        System.out.println(mote.getContent());
    }
}
