package com.kateellycott.booksprocessor.controller;

import com.kateellycott.booksprocessor.model.BookSection;
import com.kateellycott.booksprocessor.services.BookSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookController {
  private final BookSectionService bookSectionService;

  @GetMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<BookSection>> getFeedbackRequestById(@RequestParam("name") String bookName,
                                                                  @PageableDefault(value = 50) Pageable pageable) {
    return ResponseEntity.ok(bookSectionService.getBookSections(bookName, pageable));
  }
}
