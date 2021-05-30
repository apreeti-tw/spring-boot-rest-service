package com.springbootfundamentals.springbootrestservice.repositories;

import com.springbootfundamentals.springbootrestservice.controller.Library;

import java.util.List;

public interface ILibraryCustomImpl {
    List<Library> findByAuthor(String authorName);
}
