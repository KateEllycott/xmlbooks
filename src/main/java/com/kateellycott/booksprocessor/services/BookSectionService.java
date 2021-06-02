package com.kateellycott.booksprocessor.services;


import com.kateellycott.booksprocessor.model.BookSection;
import com.kateellycott.booksprocessor.repository.BookSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookSectionService {
  private final BookSectionRepository bookSectionRepository;

  public Page<BookSection> getBookSections(String bookName, Pageable pageable) {
    return bookSectionRepository.findAllByBookName(bookName, pageable);
  }
}
