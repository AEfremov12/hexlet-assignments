package exercise.controller;

import exercise.dto.AuthorDTO;
import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorsController {

    @Autowired
    private AuthorService authorService;

    // BEGIN

    @GetMapping("")
    public List<AuthorDTO> showAll(){
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public AuthorDTO showExact(@PathVariable long id){
        return authorService.findById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO create (@RequestBody AuthorCreateDTO dto) {
        return authorService.create(dto);
    }

    @PutMapping("/{id}")
    public AuthorDTO update (@PathVariable long id, @RequestBody AuthorUpdateDTO dto){
        var foundAuthor = authorService.findAuthorById(id);
        return authorService.update(foundAuthor, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id ){
        authorService.deleteById(id);
    }
    // END
}
