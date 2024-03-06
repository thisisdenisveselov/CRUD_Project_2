package ru.veselov.crud1try2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.veselov.crud1try2.dao.BookDAO;
import ru.veselov.crud1try2.dao.PersonDAO;
import ru.veselov.crud1try2.models.Book;
import ru.veselov.crud1try2.models.Person;

@Controller
@RequestMapping("/books")
public class BooksController {
    private BookDAO bookDAO;

    private PersonDAO personDAO;

    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookDAO.show(id));
        int personId = bookDAO.show(id).getPersonId();
        model.addAttribute("assignedPerson", personDAO.show(personId));
        model.addAttribute("people", personDAO.index());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute Book book) {


        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable int id) {
        model.addAttribute("book", bookDAO.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable int id, @ModelAttribute Book book) {
        bookDAO.update(id, book);

        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        bookDAO.delete(id);

        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable int id, @ModelAttribute Person person) {
        bookDAO.assign(id, person.getId());
        return "redirect:/books";
    }

    @PatchMapping("/{id}/free")
    public String free(@PathVariable int id) {
        bookDAO.free(id);
        return "redirect:/books";
    }
}
