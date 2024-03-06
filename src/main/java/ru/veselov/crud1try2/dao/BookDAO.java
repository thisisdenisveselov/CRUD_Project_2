package ru.veselov.crud1try2.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.veselov.crud1try2.models.Book;

import java.util.List;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM book", new BookMapper());
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE book_id = ?", new  Object[]{id}, new BookMapper())
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, publishing_year) VALUES(?, ?, ?)",
                book.getName(), book.getAuthor(), book.getPublishingYear());
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE book SET name = ?, author = ?, publishing_year = ? WHERE book_id = ?",
                book.getName(), book.getAuthor(), book.getPublishingYear(), id);
    }

    public void assign(int id, int personId) {
        jdbcTemplate.update("UPDATE book SET person_id = ? WHERE book_id = ?", personId, id);
    }

    public void free(int id) {
        jdbcTemplate.update("UPDATE book SET person_id = null WHERE book_id = ?", id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE book_id = ?", id);
    }

}
