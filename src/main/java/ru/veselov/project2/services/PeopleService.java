package ru.veselov.project2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.veselov.project2.models.Book;
import ru.veselov.project2.models.Person;
import ru.veselov.project2.repositories.BooksRepository;
import ru.veselov.project2.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;
    private final BooksService booksService;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository, BooksService booksService) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
        this.booksService = booksService;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName).stream().findAny();
    }

    public List<Book> findBorrowedBooks(Person owner) {
        List<Book> borrowedBooks = booksRepository.findByOwner(owner);
        booksService.checkIfExpired(borrowedBooks);
        return borrowedBooks;
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

}
