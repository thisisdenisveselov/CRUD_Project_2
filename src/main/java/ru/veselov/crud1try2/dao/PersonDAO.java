package ru.veselov.crud1try2.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
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
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
        //new PersonMapper()); we don't need custom RowMapper, because our columns names are identical to fields names in Person class
    }

    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM person WHERE email = ?", new Object[]{email}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, age, email) VALUES (?, ?, ?)",
                person.getName(), person.getAge(), person.getEmail());
    }

    public void update(int id, Person updatedPerson) {
       jdbcTemplate.update("UPDATE person SET name=?, age=?, email=? WHERE id = ?",
               updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void delete(int id) {

        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

    ///Test performance batch update

    public void testMultipleUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        for(Person person : people) {
            jdbcTemplate.update("INSERT INTO person VALUES (?, ?, ?, ?)",
                    person.getId(), person.getName(), person.getAge(), person.getEmail());
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }

    public void testBatchUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();


        jdbcTemplate.batchUpdate("INSERT INTO person VALUES (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override    //values that will go to our package
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setInt(1, people.get(i).getId()); //i == index in people
                        preparedStatement.setString(2, people.get(i).getName());
                        preparedStatement.setInt(3, people.get(i).getAge());
                        preparedStatement.setString(4, people.get(i).getEmail());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));

    }

    public List<Person> create1000People() {
        List<Person> people = new ArrayList<>();

        for(int i = 0; i < 1000; i++)
            people.add(new Person(i, "Name" + i, 30, "test" + i + "@gmail.com"));

        return people;
    }
}
