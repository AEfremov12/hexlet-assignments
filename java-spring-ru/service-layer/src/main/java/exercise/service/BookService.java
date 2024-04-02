package exercise.service;

import exercise.dto.*;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.mapper.BookMapper;
import exercise.model.Author;
import exercise.model.Book;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    // BEGIN
    @Autowired
    private BookRepository repository;
    @Autowired
    private BookMapper mapper;

    public List<BookDTO> findAll() {
        var all = repository.findAll();
        return all.stream()
                .map(mapper::map)
                .toList();

    }

    public BookDTO findById(long id) {
        return mapper.map(findBookById(id));
    }

    public Book findBookById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    public BookDTO create(BookCreateDTO dto) {
        var book = mapper.map(dto);
        var result = repository.save(book);
        return mapper.map(result);
    }

    public BookDTO update(Book model, BookUpdateDTO dto) {
        mapper.update(dto, model);
        repository.save(model);
        return mapper.map(model);
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }


    // END
}
