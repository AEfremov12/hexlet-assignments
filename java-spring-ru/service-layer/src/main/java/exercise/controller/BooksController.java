package exercise.controller;

import java.util.List;

import exercise.dto.*;
import exercise.mapper.BookMapper;
import exercise.repository.BookRepository;
import exercise.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookMapper mapper;
    @Autowired
    private BookRepository repository;

    // BEGIN

    @GetMapping("")
    public List<BookDTO> showAll(){
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookDTO showExact(@PathVariable long id){
        return bookService.findById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create (@Valid @RequestBody BookCreateDTO dto) {
        return bookService.create(dto);
    }

    @PutMapping("/{id}")
    public BookDTO update (@PathVariable long id, @RequestBody BookUpdateDTO dto){
        var foundAuthor = bookService.findBookById(id);
        return bookService.update(foundAuthor, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id ){
        bookService.deleteById(id);
    }
    
    // END
}
