set search_path = public;

CREATE TABLE books
(
    id BIGINT NOT NULL,
    name TEXT NOT NULL,
    link TEXT NOT NULL
);
ALTER TABLE books ADD CONSTRAINT books_pkey PRIMARY KEY(id);

CREATE TABLE book_sections
(
    id BIGINT NOT NULL,
    title TEXT NOT NULL,
    book_id BIGINT NOT NULL
);
ALTER TABLE book_sections ADD CONSTRAINT book_sections_pkey PRIMARY KEY(id);
ALTER TABLE book_sections ADD CONSTRAINT fk_board_id FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE;