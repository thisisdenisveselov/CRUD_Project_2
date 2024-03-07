package ru.veselov.crud1try2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.veselov.crud1try2.dao.BookDAO;
import ru.veselov.crud1try2.dao.PersonDAO;
import ru.veselov.crud1try2.models.Person;
import ru.veselov.crud1try2.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        //get all people from DAO and send them to view
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        //get one person by id from DAO and send him to the view
        model.addAttribute("person", personDAO.show(id));
        model.addAttribute("borrowedBooks", personDAO.showBorrowedBooks(id));
        return "people/show";
    }

    @GetMapping("/new")
   // public String newPerson(Model model) {
    public String newPerson(@ModelAttribute("person") Person person) { //creates an empty object of the class Person with an empty constructor and adds it to the model
     //   model.addAttribute("person", new Person());  // if we use thymeleaf, we must pass an object that used in this form(add an object to model)

        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {//creates an empty object of the class Person, get data from field. set data to the object. Adds an object to the model
        //in bindingResult we have errors from @valid and from method "validate" (object errors)
        personValidator.validate(person, bindingResult);
        // creates new person and fill him with data from form

        //BindingResult - must be placed after object with "@Valid" annotation. It stores all errors of form validation of class Person
        if(bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person); // add person to database
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) { //@PathVariable("id") - extract "id" from request address(URL?)
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}
