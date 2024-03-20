package ru.veselov.project2.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    private String name;

    @Column(name = "author")
    @NotEmpty(message = "Author should not be empty")
    @Size(min = 2, max = 50, message = "Author should be between 2 and 50 characters")
    private String author;

    @Column(name = "publishing_year")
    @Min(value = 1, message = "Publishing year should be greater than 0")
    private int publishingYear;

    @Column(name = "borrowed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date BorrowedAt;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Transient
    private boolean expired;

    public Book() {
    }

    public Book(String name, String author, int publishingYear) {
        this.name = name;
        this.author = author;
        this.publishingYear = publishingYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(int publishingYear) {
        this.publishingYear = publishingYear;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getBorrowedAt() {
        return BorrowedAt;
    }

    public void setBorrowedAt(Date borrowedAt) {
        BorrowedAt = borrowedAt;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", publishingYear=" + publishingYear +
                ", owner=" + owner +
                '}';
    }
}
