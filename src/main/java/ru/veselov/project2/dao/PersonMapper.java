package ru.veselov.project2.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.veselov.project2.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        Person person = new Person();

        person.setId(resultSet.getInt("person_id"));
        person.setFullName(resultSet.getString("full_name"));
        person.setBirthYear(resultSet.getInt("birth_year"));

        return person;
    }
}
