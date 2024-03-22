package ru.veselov.project2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.veselov.project2.models.Book;
import ru.veselov.project2.models.Person;
import ru.veselov.project2.repositories.BooksRepository;
import ru.veselov.project2.repositories.PeopleRepository;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("publishingYear"));
        else
            return booksRepository.findAll();
    }

    public Page<Book> findAll(int page, int booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("publishingYear")));
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage));
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    public List<Book> findOne(String prompt) {
        return booksRepository.findByNameStartingWith(prompt);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = booksRepository.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner()); // что бы не терялвсь связь при обновлении
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void assign(int id, Person person) {
        /*Book book = booksRepository.findById(id).orElse(null);
        book.setOwner(person);
        book.setBorrowedAt(new Date());*/

        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(person);
            book.setBorrowedAt(new Date());
        });

        //book object in Persistent context, so we don't need save() method
    }

    @Transactional
    public void free(int id) {
        /*Book book = booksRepository.findById(id).orElse(null);
        book.setOwner(null);
        book.setBorrowedAt(null);*/
        //better(avoids NullPointerException)
        booksRepository.findById(id).ifPresent( book -> {
            book.setOwner(null);
        });
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public void checkIfExpired(List<Book> books) {
        for (Book book : books){
            Date borrowedAt = book.getBorrowedAt();
            if (borrowedAt == null)
                continue;
            Date currentDate = new Date();
            long diffInMillies = Math.abs(currentDate.getTime() - borrowedAt.getTime()); // модуль числа
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diff > 10)
                book.setExpired(true);
        }
    }
}
