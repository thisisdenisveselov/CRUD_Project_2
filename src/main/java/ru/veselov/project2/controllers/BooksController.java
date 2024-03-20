package ru.veselov.project2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.veselov.project2.models.Book;
import ru.veselov.project2.models.Person;
import ru.veselov.project2.services.BooksService;
import ru.veselov.project2.services.PeopleService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) Boolean sortByYear,
                        Model model) {
        if (page != null && booksPerPage != null && sortByYear != null && sortByYear)
            model.addAttribute("books", booksService.findAll(page, booksPerPage, sortByYear));
        else if (page != null && booksPerPage != null)
            model.addAttribute("books", booksService.findAll(page, booksPerPage));
        else if (sortByYear != null && sortByYear)
            model.addAttribute("books", booksService.findAll(sortByYear));
        else
            model.addAttribute("books", booksService.findAll());

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));
        Person owner = booksService.findOne(id).getOwner();

        if(owner != null)
            model.addAttribute("assignedPerson", owner);
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "books/new";

        /*Random random = new Random();
        for (int i = 0; i < 200; i++) {
            Book newBook = new Book("Book name " + i, "Author " + i, random.nextInt(200) + 1820);
            booksService.save(newBook);
        }*/
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.assign(id, person);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/free")
    public String free(@PathVariable("id") int id) {  //чекнуть почему нельзя просто взять book из модели и передать его в метод
        booksService.free(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "prompt", required = false) String prompt, Model model) {
        if (prompt != null) {
            List<Book> books = booksService.findOne(prompt);
            model.addAttribute("books", books);
        }
        return "books/search";
    }
}
