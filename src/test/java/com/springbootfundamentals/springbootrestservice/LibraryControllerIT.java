package com.springbootfundamentals.springbootrestservice;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

public class LibraryControllerIT {

    @Test
    public void getBookByAuthorNameTest() throws JSONException {
        String expectedResponse = "[{\"bookName\":\"Cypress\",\"id\":\"abcd4\",\"isbn\":\"abcd\",\"aisle\":4,\"author\":\"Rahul\"},{\"bookName\":\"Devops\",\"id\":\"fdsefr343\",\"isbn\":\"fdsefr3\",\"aisle\":43,\"author\":\"Rahul\"}]";
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/getBooks/author?name=Rahul", String.class);
        JSONAssert.assertEquals(expectedResponse, response.getBody(), false);
    }
}
