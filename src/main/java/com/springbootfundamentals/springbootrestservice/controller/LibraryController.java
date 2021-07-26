package com.springbootfundamentals.springbootrestservice.controller;

import com.springbootfundamentals.springbootrestservice.repositories.ILibraryRepository;
import com.springbootfundamentals.springbootrestservice.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class LibraryController {
    @Autowired
    ILibraryRepository libraryRepo;

    @Autowired
    AddBookResponse addBookResponse;

    @Autowired
    LibraryService libraryService;

    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

    @GetMapping("/getBooks")
    public Iterable<Library> getAllBooksImpl() {
        logger.info("Fetching all books");
        return libraryRepo.findAll();
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
            logger.info("Creating new book "+library.getBookName());

            libraryRepo.save(library);

            //Set message text
            addBookResponse.setId(bookId);
            addBookResponse.setMessage("Successfully added book!");
            logger.info("Successfully added book "+library.getBookName()+"!");

            //Set headers
            httpHeaders.add("unique", library.getIsbn() + library.getAisle());

            //Send response text
            return new ResponseEntity<AddBookResponse>(addBookResponse, httpHeaders, HttpStatus.CREATED);
        } else {
            logger.info("Book creation failed. Duplicate book entry "+library.getBookName());
            addBookResponse.setId(bookId);
            addBookResponse.setMessage("Book already exists!");
            return new ResponseEntity<AddBookResponse>(addBookResponse, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getBooks/{bookId}")
    public Library getBooksByIdImpl(@PathVariable String bookId) {
        try {
            Library library = libraryService.getBookById(bookId);
            logger.info("Found book "+library.getBookName());
            return library;
        } catch (Exception e) {
            logger.info("Requested book not found"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getBooks/author")
    public List<Library> getBooksByAuthorImpl(@RequestParam String name){
        try {
            logger.info("Fetching all books by "+name);
            return libraryRepo.findByAuthor(name);
        } catch (Exception e){
            logger.info("Error fetching books by "+name+" - "+e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateBook/{bookId}")
    public ResponseEntity<Library> updateBookImpl(@PathVariable String bookId, @RequestBody Library library){
        try {
            Library existingBook = libraryService.getBookById(bookId);

            existingBook.setAisle(library.getAisle());
            existingBook.setBookName(library.getBookName());
            existingBook.setAuthor(library.getAuthor());
            libraryRepo.save(existingBook);
            logger.info("Book updated successfully "+existingBook.getBookName());

            return new ResponseEntity<Library>(existingBook, HttpStatus.OK) ;
        } catch (Exception e){
            logger.info("Error updating book"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteBook/{bookId}")
    public ResponseEntity<String> deleteBookImpl(@PathVariable String bookId){
        try {
            libraryRepo.deleteById(bookId);
            logger.info("Book deleted successfully");
            return new ResponseEntity<>("Book is deleted!", HttpStatus.NO_CONTENT);
        } catch (Exception e){
            logger.info("Error deleting book "+e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/actuator/info")
    public ResponseEntity<String> actuatorInfoImpl(){
        logger.info("Hitting actuator info endpoint");
        return new ResponseEntity<>("You have hit the actuator endpoint. Status: UP", HttpStatus.OK);
    }
}
