package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.model.Author;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    // BEGIN
    @Autowired
    private AuthorRepository repository;
    @Autowired
    private AuthorMapper mapper;

    public List<AuthorDTO> findAll() {
        var all = repository.findAll();
        return all.stream()
                .map(mapper::map)
                .toList();

    }

    public AuthorDTO findById(long id) {
        return mapper.map(findAuthorById(id));
    }

    public Author findAuthorById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    public AuthorDTO create(AuthorCreateDTO dto) {
        var author = mapper.map(dto);
        repository.save(author);
        return mapper.map(author);
    }

    public AuthorDTO update(Author foundAuthor, AuthorUpdateDTO dto) {
        mapper.update(dto, foundAuthor);
        repository.save(foundAuthor);
        return mapper.map(foundAuthor);
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }


    // END
}
