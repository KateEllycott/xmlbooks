package com.kateellycott.booksprocessor.config.batch;

import com.kateellycott.booksprocessor.model.BookSection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

  private BookSection bookSection = new BookSection();
  private String lastElementName;

  public BookSection getBookSection() {
    return this.bookSection;
  }

  @Override
  public void startDocument() throws SAXException {
    super.startDocument();
  }

  @Override
  public void endDocument() throws SAXException {
    super.endDocument();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    lastElementName = qName;
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    String content = new String(ch, start, length);
    content = content.replace("\n", "").trim();
    if (!content.isEmpty() && lastElementName.equals("title")) {
        bookSection.setTitle(content);
    }
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    super.ignorableWhitespace(ch, start, length);
  }

  @Override
  public void processingInstruction(String target, String data) throws SAXException {
    if (target.equals("content-link")) {
      Pattern pattern = Pattern.compile("file=\"(.+.xml)\"");
      Matcher matcher = pattern.matcher(data);
      if (matcher.find()) {
        String contentLink = matcher.group(1);
        bookSection.setContentLink(contentLink);
      }
    }
  }
}
