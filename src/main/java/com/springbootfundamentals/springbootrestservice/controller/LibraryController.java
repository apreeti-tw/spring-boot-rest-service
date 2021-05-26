package com.springbootfundamentals.springbootrestservice.controller;

import com.springbootfundamentals.springbootrestservice.repositories.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LibraryController {
    @Autowired
    LibraryRepository libraryRepo;

    @Autowired
    AddBookResponse addBookResponse;

    @GetMapping("/listBook")
    public String listBookImpl(@RequestParam String id) {
        return "You have requested for Book with id: "+id;
    }

    @PostMapping("/addBook")
    public ResponseEntity<AddBookResponse> addBookImpl(@RequestBody Library library){
        library.setId(library.getIsbn() + library.getAisle());
        library.setAuthor(library.getAuthor());
        library.setBookName(library.getBookName());
        library.setAisle(library.getAisle());
        library.setIsbn(library.getIsbn());
        libraryRepo.save(library);

        //Set message text
        addBookResponse.setId(library.getIsbn() + library.getAisle());
        addBookResponse.setMessage("Successfully added book!");

        //Set headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("unique", library.getIsbn() + library.getAisle());

        //Send response text
        return new ResponseEntity<AddBookResponse>(addBookResponse, httpHeaders, HttpStatus.CREATED);
    }
}
