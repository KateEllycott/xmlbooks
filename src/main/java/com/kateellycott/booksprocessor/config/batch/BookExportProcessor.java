package com.kateellycott.booksprocessor.config.batch;

import com.kateellycott.booksprocessor.model.Book;
import com.kateellycott.booksprocessor.model.BookSection;
import com.kateellycott.booksprocessor.services.BookService;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
@Data
@Accessors(chain = true)
@Slf4j
public class BookExportProcessor implements ItemProcessor<Book, List<BookSection>> {
  @Autowired
  private BookService bookService;
  private String importFolder;

  public List<BookSection> process(Book book) throws Exception {
    if (!bookService.findByName(book.getName()).isPresent()) {
      Book savedBook = bookService.save(book);
      return downloadBookSections(savedBook, new File(importFolder));
    }
    log.info("Book: {} is already in DB, skipping processing...", book.getName());
    return null;
  }

  private List<BookSection> downloadBookSections(Book book, File file) throws Exception {
    URL parentURL = new URL(book.getLink()).toURI().resolve(".").toURL();
    List<BookSection> bookSections = new ArrayList<>();
    File sectionsFolder = new File(file, book.getName().replaceAll("[\\\\/:*?\"<>|]", ""));
    File bookLink = new File(book.getLink());
    String sectionFileName = bookLink.getName();

    BookSection processed = saveSection(sectionsFolder, sectionFileName, new URL(parentURL, sectionFileName));
    processed.setBook(book);
    bookSections.add(processed);

    while (processed.getContentLink() != null) {
      processed = saveSection(sectionsFolder, processed.getContentLink(),
          new URL(parentURL, processed.getContentLink()));
      processed.setBook(book);
      bookSections.add(processed);
    }
    return bookSections;
  }

  private BookSection saveSection(File directory, String sectionFileName, URL fileURL)
      throws IOException, SAXException, ParserConfigurationException {
    File destination = new File(directory, sectionFileName);
    FileUtils.copyURLToFile(fileURL, destination);
    return parseSectionXml(destination);
  }

  private BookSection parseSectionXml(File file) throws SAXException, ParserConfigurationException, IOException {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();
    XMLHandler handler = new XMLHandler();
    parser.parse(file, handler);
    return handler.getBookSection();
  }
}
