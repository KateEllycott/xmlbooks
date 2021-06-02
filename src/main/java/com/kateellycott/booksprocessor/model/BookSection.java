package com.kateellycott.booksprocessor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Table(name = "book_sections")
@Data
@ToString(exclude = "book")
@Accessors(chain = true)
public class BookSection {
  @JsonIgnore
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String title;
  @JsonIgnore
  @Transient
  private String contentLink;
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;
}
