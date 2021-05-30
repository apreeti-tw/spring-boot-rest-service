package com.springbootfundamentals.springbootrestservice.repositories;

import com.springbootfundamentals.springbootrestservice.controller.Library;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class LibraryCustomImplementation implements ILibraryCustomImpl{
    @Autowired
    ILibraryRepository libraryRepo;

    @Override
    public List<Library> findByAuthor(String authorName) {
        List<Library> libraries = (List<Library>) libraryRepo.findAll();
        return libraries.stream().filter(book -> book.getAuthor().equalsIgnoreCase(authorName)).collect(Collectors.toList());
    }
}
