package exercise.controller.users;

import java.util.HashSet;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {

    private List<Post> posts = Data.getPosts();

    @GetMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<Post> show(@PathVariable String id) {
        var result = posts.stream()
                .filter(p -> p.getUserId().equals(id))
                .toList();
        return result;
    }


    @PostMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable String id, String slug, String title, String body) {
        posts.add(new Post(Integer.parseInt(id), slug, title, body));
    }
}
// END
