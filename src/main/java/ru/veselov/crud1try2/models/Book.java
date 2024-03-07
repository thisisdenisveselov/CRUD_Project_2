package ru.veselov.crud1try2.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Book {
    private int id;
    private int personId;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    private String name;
    @NotEmpty(message = "Author should not be empty")
    @Size(min = 2, max = 50, message = "Author should be between 2 and 50 characters")
    private String author;
    @Min(value = 1, message = "Publishing year should be greater than 0")
    private int publishingYear;

    public Book() {
    }

/*    public Book(int id, String name, String author, int publishingYear) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publishingYear = publishingYear;
    }*/

     public Book(int id, int personId, String name, String author, int publishingYear) {
        this.id = id;
        this.personId = personId;
        this.name = name;
        this.author = author;
        this.publishingYear = publishingYear;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
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
}
