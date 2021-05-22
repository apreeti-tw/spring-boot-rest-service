package com.springbootfundamentals.springbootrestservice;

import com.springbootfundamentals.springbootrestservice.controller.Library;
import com.springbootfundamentals.springbootrestservice.repositories.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class SpringBootRestServiceApplication implements CommandLineRunner {
	@Autowired
	LibraryRepository libraryRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestServiceApplication.class, args);
	}

	@Override
	public void run(String[] args){

		Library library = new Library();
		library.setId("test004");
		library.setBookName("TestBook4");
		library.setIsbn("82738273976");
		library.setAisle(8724);
		library.setAuthor("Test2");
		libraryRepository.save(library);

		for (Library book: libraryRepository.findAll()) {
			System.out.println(book.getBookName());
		}

		Optional<Library> lib = libraryRepository.findById("test003");
		System.out.println("----------> " + lib.get().getBookName());
	}
}

