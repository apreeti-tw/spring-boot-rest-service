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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$.["+i+"].id").value(books.get(i).getId()))
                    .andExpect(jsonPath("$.["+i+"].bookName").value(books.get(i).getBookName()))
                    .andExpect(jsonPath("$.["+i+"].author").value(books.get(i).getAuthor()))
                    .andExpect(jsonPath("$.["+i+"].isbn").value(books.get(i).getIsbn()))
                    .andExpect(jsonPath("$.["+i+"].aisle").value(books.get(i).getAisle()));
        }
    }

    @Test
    public void getBookByIdControllerTest() throws Exception {
        Library bookDetails = buildLibraryBook();
        Mockito.when(libraryService.getBookById(any())).thenReturn(bookDetails);

        this.mockMvc.perform(get("/getBooks/"+bookDetails.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookDetails.getId()))
                .andExpect(jsonPath("$.bookName").value(bookDetails.getBookName()))
                .andExpect(jsonPath("$.author").value(bookDetails.getAuthor()))
                .andExpect(jsonPath("$.isbn").value(bookDetails.getIsbn()))
                .andExpect(jsonPath("$.aisle").value(bookDetails.getAisle()));
    }

    @Test
    public void getAllBooksControllerTest() throws Exception {
        List<Library> books = new ArrayList<>();
        books.add(buildLibraryBook());
        books.add(buildLibraryBook());
        books.add(buildLibraryBook());
        books.add(buildLibraryBook());
        Mockito.when(libraryRepo.findAll()).thenReturn(books);

        for (int i=0; i< books.size(); i++) {
            this.mockMvc.perform(get("/getBooks")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(4))
                    .andExpect(jsonPath("$.["+i+"].id").value(books.get(i).getId()))
                    .andExpect(jsonPath("$.["+i+"].bookName").value(books.get(i).getBookName()))
                    .andExpect(jsonPath("$.["+i+"].author").value(books.get(i).getAuthor()))
                    .andExpect(jsonPath("$.["+i+"].isbn").value(books.get(i).getIsbn()))
                    .andExpect(jsonPath("$.["+i+"].aisle").value(books.get(i).getAisle()));
        }
    }

    @Test
    public void bookByIdDoesNotExistControllerTest() throws Exception {
        Mockito.when(libraryService.getBookById(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(libraryRepo).deleteById(any());

        this.mockMvc.perform(get("/getBooks/someRandomBookId"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBookByIdControllerTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Library bookDetails = buildLibraryBook();
        String bookId = bookDetails.getId();
        Mockito.when(libraryService.getBookById(any())).thenReturn(bookDetails);

        bookDetails.setAuthor("Dummy Unit Test");
        bookDetails.setBookName("Dummy Unit Testing Book");
        bookDetails.setIsbn("DUMMYISBN7");
        bookDetails.setAisle(54321);
        bookDetails.setId(bookDetails.getIsbn()+bookDetails.getAisle());

        this.mockMvc.perform(put("/updateBook/"+bookId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookDetails)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"bookName\":\"Dummy Unit Testing Book\",\"id\":\"DUMMYISBN754321\",\"isbn\":\"DUMMYISBN7\",\"aisle\":54321,\"author\":\"Dummy Unit Test\"}"));
    }

    @Test
    public void deleteBookByIdControllerTest() throws Exception {
        Mockito.doNothing().when(libraryRepo).deleteById(any());

        this.mockMvc.perform(delete("/deleteBook/ISBN62245"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteBookByIdDoesNotExistControllerTest() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(libraryRepo).deleteById(any());

        this.mockMvc.perform(delete("/deleteBook/someRandomId"))
                .andExpect(status().isNotFound());
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
