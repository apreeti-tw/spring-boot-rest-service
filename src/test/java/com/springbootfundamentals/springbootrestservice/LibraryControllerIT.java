package com.springbootfundamentals.springbootrestservice;

import com.springbootfundamentals.springbootrestservice.controller.Library;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

public class LibraryControllerIT {

    @Test
    public void getBookByAuthorNameTest() throws JSONException {
        String expectedResponse = "[{\"bookName\":\"Cypress\",\"id\":\"abcd4\",\"isbn\":\"abcd\",\"aisle\":4,\"author\":\"Rahul\"},{\"bookName\":\"Devops\",\"id\":\"fdsefr343\",\"isbn\":\"fdsefr3\",\"aisle\":43,\"author\":\"Rahul\"}]";
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/getBooks/author?name=Rahul", String.class);
        JSONAssert.assertEquals(expectedResponse, response.getBody(), false);
    }

    @Test
    public void addNewBookTest() throws JSONException {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String expectedResponse = "{\"message\": \"Successfully added book!\",\"id\": \"ISBN62245\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Library> request = new HttpEntity(buildLibraryBook(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/addBook", request, String.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expectedResponse,response.getBody(),false);
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
