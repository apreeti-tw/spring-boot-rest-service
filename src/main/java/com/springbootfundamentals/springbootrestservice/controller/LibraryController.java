package com.springbootfundamentals.springbootrestservice.controller;

import com.springbootfundamentals.springbootrestservice.repositories.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class LibraryController {
    @Autowired
    LibraryRepository libraryRepo;

    AtomicLong bookId = new AtomicLong();

    @GetMapping("/listBook")
    public String listBookImpl(@RequestParam String id) {
        return "You have requested for Book with id: "+id;
    }

    @PostMapping("/addBook")
    public void addBookImpl(@RequestBody Library library){
        library.setId(String.valueOf(bookId.incrementAndGet()));
        library.setAuthor(library.getAuthor());
        library.setBookName(library.getBookName());
        library.setAisle(library.getAisle());
        library.setIsbn(library.getIsbn());
        libraryRepo.save(library);
    }
}
