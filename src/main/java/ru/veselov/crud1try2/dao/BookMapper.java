package ru.veselov.crud1try2.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.veselov.crud1try2.models.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Book book = new Book();

        book.setId(resultSet.getInt("book_id"));
        book.setPersonId(resultSet.getInt("person_id"));
        book.setName(resultSet.getString("name"));
        book.setAuthor(resultSet.getString("author"));
        book.setPublishingYear(resultSet.getInt("publishing_year"));

        return book;
    }
}
