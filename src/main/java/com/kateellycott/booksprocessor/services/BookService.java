package com.kateellycott.booksprocessor.services;

import com.kateellycott.booksprocessor.model.Book;
import com.kateellycott.booksprocessor.repository.BookRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;

  public Book save(Book book) {
    return bookRepository.save(book);
  }

  public Optional<Book> findById(Long id) {
    return bookRepository.findById(id);
  }

  public Optional<Book> findByName(String name) {
    return bookRepository.findByName(name);
  }
}
