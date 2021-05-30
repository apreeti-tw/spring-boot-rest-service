package com.springbootfundamentals.springbootrestservice.controller;

import com.springbootfundamentals.springbootrestservice.repositories.LibraryRepository;
import com.springbootfundamentals.springbootrestservice.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LibraryController {
    @Autowired
    LibraryRepository libraryRepo;

    @Autowired
    AddBookResponse addBookResponse;

    @Autowired
    LibraryService libraryService;

    @GetMapping("/listBook")
    public String listBookImpl(@RequestParam String id) {
        return "You have requested for Book with id: "+id;
    }

    @PostMapping("/addBook")
    public ResponseEntity<AddBookResponse> addBookImpl(@RequestBody Library library){
        HttpHeaders httpHeaders = new HttpHeaders();
        String bookId = libraryService.getId(library.getIsbn(), library.getAisle());

        library.setId(bookId);
        library.setAuthor(library.getAuthor());
        library.setBookName(library.getBookName());
        library.setAisle(library.getAisle());
        library.setIsbn(library.getIsbn());

        if(!libraryService.checkDuplicateBook(bookId)){
            libraryRepo.save(library);

            //Set message text
            addBookResponse.setId(bookId);
            addBookResponse.setMessage("Successfully added book!");

            //Set headers
            httpHeaders.add("unique", library.getIsbn() + library.getAisle());

            //Send response text
            return new ResponseEntity<AddBookResponse>(addBookResponse, httpHeaders, HttpStatus.CREATED);
        } else {
            addBookResponse.setId(bookId);
            addBookResponse.setMessage("Book already exists!");
            return new ResponseEntity<AddBookResponse>(addBookResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBook/{bookId}")
    public Library getBookImpl(@PathVariable(value = "bookId") String bookId) {
        try {
            return libraryRepo.findById(bookId).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
