package ru.veselov.project2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.veselov.project2.models.Person;
import ru.veselov.project2.services.PeopleService;

@Component
public class PersonValidator implements Validator {

    //private final PersonDAO personDAO;
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    } // check that is a class Person

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        //check if there is a person with the same full name in DB
        if(peopleService.getPersonByFullName(person.getFullName()).isPresent())  // possible to check on null (!= null), but in PersonDAO we need to return Person, not Optional
            errors.rejectValue("fullName", "", "Person with this full name already exists"); //"fullName" - field where error. "" - code of error(haven't used here)
    }
}
