package com.kateellycott.booksprocessor.repository;

import com.kateellycott.booksprocessor.model.BookSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSectionRepository extends JpaRepository<BookSection, Long> {
  Page<BookSection> findAllByBookName(String bookName, Pageable pageable);
}
