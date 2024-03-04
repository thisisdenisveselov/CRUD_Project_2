package ru.veselov.crud1try2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.veselov.crud1try2.dao.PersonDAO;
import ru.veselov.crud1try2.models.Person;

@Component
public class PersonValidator implements Validator {

    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if(personDAO.show(person.getEmail()).isPresent())  // possible to check on null (!= null), but in PersonDAO we need to return Person, not Optional
            errors.rejectValue("email", "", "This email is already taken");
        //check if there is a person with the same email in DB

    }
}
