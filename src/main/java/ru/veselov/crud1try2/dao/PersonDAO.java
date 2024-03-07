package ru.veselov.crud1try2.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.veselov.crud1try2.models.Book;
import ru.veselov.crud1try2.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM person", new PersonMapper());
                //new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE person_id = ?", new Object[]{id}, new PersonMapper())
                .stream().findAny().orElse(null);
    }

    public List<Book> showBorrowedBooks(int id) {
        return jdbcTemplate.query("SELECT * FROM book JOIN person " +
                "ON book.person_id = person.person_id WHERE person.person_id = ?", new Object[]{id}, new BookMapper());
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(full_name, birth_year) VALUES (?, ?)",
                person.getName(), person.getBirthYear());
    }

    public void update(int id, Person updatedPerson) {
       jdbcTemplate.update("UPDATE person SET full_name = ?, birth_year = ? WHERE person_id = ?",
               updatedPerson.getName(), updatedPerson.getBirthYear(), id);
    }

    public void delete(int id) {

        jdbcTemplate.update("DELETE FROM person WHERE person_id=?", id);
    }

    public Optional<Person> getFullName(String name) {
        return jdbcTemplate.query("SELECT * FROM person WHERE full_name = ?", new Object[]{name}, new PersonMapper())
                .stream().findAny();
    }


}
