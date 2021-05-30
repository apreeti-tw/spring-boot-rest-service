package com.springbootfundamentals.springbootrestservice.service;

import com.springbootfundamentals.springbootrestservice.controller.Library;
import com.springbootfundamentals.springbootrestservice.repositories.ILibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibraryService {
    @Autowired
    ILibraryRepository libraryRepo;

    public boolean checkDuplicateBook(String bookId){
        Optional<Library> library = libraryRepo.findById(bookId);
        if(library.isPresent())
            return true;
        return false;
    }

    public String getId(String isbn, int aisle){
        return isbn+aisle;
    }
}
