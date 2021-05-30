package com.springbootfundamentals.springbootrestservice.repositories;

import com.springbootfundamentals.springbootrestservice.controller.Library;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILibraryRepository extends CrudRepository<Library, String>, ILibraryCustomImpl {
}
