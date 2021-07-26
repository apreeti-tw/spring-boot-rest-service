package com.springbootfundamentals.springbootrestservice;

import com.springbootfundamentals.springbootrestservice.controller.AddBookResponse;
import com.springbootfundamentals.springbootrestservice.controller.Library;
import com.springbootfundamentals.springbootrestservice.controller.LibraryController;
import com.springbootfundamentals.springbootrestservice.repositories.ILibraryRepository;
import com.springbootfundamentals.springbootrestservice.service.LibraryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@SpringBootTest
class SpringBootRestServiceApplicationTests {
	LibraryService libraryService = new LibraryService();

	@Autowired
	LibraryController libraryController;

	@MockBean
	LibraryService libService;

	@MockBean
	ILibraryRepository libraryRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void checkBookIdGeneratedWhenBookIsOld(){
		String bookId = libraryService.getId("ISBNZ", 5566);
		Assertions.assertEquals(bookId, "OLDISBNZ5566");
	}

	@Test
	public void checkBookIdGeneratedWhenBookIsNew(){
		String bookId = libraryService.getId("ISBN", 5566);
		Assertions.assertEquals(bookId, "ISBN5566");
	}

	@Test
	public void testAddBookWhenBookIdDoesNotExist(){
		Library bookDetails = buildLibraryBook();
		when(libService.getId(bookDetails.getIsbn(), bookDetails.getAisle())).thenReturn(bookDetails.getId());
		when(libService.checkDuplicateBook(bookDetails.getId())).thenReturn(false);

		ResponseEntity responseEntity = libraryController.addBookImpl(bookDetails);
		AddBookResponse addBookResponse = (AddBookResponse) responseEntity.getBody();
		Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
		Assertions.assertEquals(addBookResponse.getId(), bookDetails.getId());
		Assertions.assertEquals(addBookResponse.getMessage(), "Successfully added book!");
	}

	@Test
	public void testAddBookWhenBookIdAlreadyExists(){
		Library bookDetails = buildLibraryBook();
		when(libService.getId(bookDetails.getIsbn(), bookDetails.getAisle())).thenReturn(bookDetails.getId());
		when(libService.checkDuplicateBook(bookDetails.getId())).thenReturn(true);

		ResponseEntity responseEntity = libraryController.addBookImpl(bookDetails);
		AddBookResponse addBookResponse = (AddBookResponse) responseEntity.getBody();
		Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CONFLICT);
		Assertions.assertEquals(addBookResponse.getId(), bookDetails.getId());
		Assertions.assertEquals(addBookResponse.getMessage(), "Book already exists!");
	}

	public Library buildLibraryBook(){
		Library bookDetails = new Library();
		bookDetails.setBookName("Unit Testing Book");
		bookDetails.setAuthor("Unit Test");
		bookDetails.setAisle(2245);
		bookDetails.setIsbn("ISBN6");
		bookDetails.setId("ISBN62245");
		return bookDetails;
	}
}
