package com.springbootfundamentals.springbootrestservice;

import com.springbootfundamentals.springbootrestservice.service.LibraryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootRestServiceApplicationTests {
	@Autowired
	LibraryService libraryService;

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
}
