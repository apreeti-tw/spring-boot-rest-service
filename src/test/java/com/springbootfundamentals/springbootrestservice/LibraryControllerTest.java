package com.springbootfundamentals.springbootrestservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootfundamentals.springbootrestservice.controller.Library;
import com.springbootfundamentals.springbootrestservice.repositories.ILibraryRepository;
import com.springbootfundamentals.springbootrestservice.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerTest {
    @MockBean
    LibraryService libraryService;

    @MockBean
    ILibraryRepository libraryRepo;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addNewBookControllerTest() throws Exception {
        Library bookDetails = buildLibraryBook();
        ObjectMapper objectMapper = new ObjectMapper();
        Mockito.when(libraryService.getId(bookDetails.getIsbn(), bookDetails.getAisle())).thenReturn(bookDetails.getId());
        Mockito.when(libraryService.checkDuplicateBook(bookDetails.getId())).thenReturn(false);
        Mockito.when(libraryRepo.save(any())).thenReturn(bookDetails);

        this.mockMvc.perform(post("/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDetails)))
                .andExpect(status().isCreated());
    }

    @Test
    public void addExistingBookControllerTest() throws Exception {
        Library bookDetails = buildLibraryBook();
        ObjectMapper objectMapper = new ObjectMapper();
        Mockito.when(libraryService.getId(bookDetails.getIsbn(), bookDetails.getAisle())).thenReturn(bookDetails.getId());
        Mockito.when(libraryService.checkDuplicateBook(bookDetails.getId())).thenReturn(true);

        this.mockMvc.perform(post("/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDetails)))
                .andExpect(status().isConflict());
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
