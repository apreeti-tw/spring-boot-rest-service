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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookDetails.getId()))
                .andExpect(jsonPath("$.message").value("Successfully added book!"));
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
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.id").value(bookDetails.getId()))
                .andExpect(jsonPath("$.message").value("Book already exists!"));;
    }

    @Test
    public void getBookByAuthorControllerTest() throws Exception {
        Library bookDetails1 = buildLibraryBook();
        Library bookDetails2 = buildLibraryBook();
        List<Library> books = new ArrayList<>();
        books.add(bookDetails1);
        books.add(bookDetails2);
        Mockito.when(libraryRepo.findByAuthor(any())).thenReturn(books);

        for (int i=0; i< books.size(); i++) {
            this.mockMvc.perform(get("/getBooks/author")
                    .param("name", "Unit Test")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.["+i+"].id").value(books.get(i).getId()))
                    .andExpect(jsonPath("$.["+i+"]bookName").value(books.get(i).getBookName()))
                    .andExpect(jsonPath("$.["+i+"]author").value(books.get(i).getAuthor()))
                    .andExpect(jsonPath("$.["+i+"]isbn").value(books.get(i).getIsbn()))
                    .andExpect(jsonPath("$.["+i+"]aisle").value(books.get(i).getAisle()));
        }
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
