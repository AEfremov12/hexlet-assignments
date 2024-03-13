package exercise.controller.users;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
                .filter(p -> p.getUserId() == Integer.parseInt(id))
                .toList();
        return result;
    }


    @PostMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@PathVariable String id, @RequestBody Post receivedPost) {
        var post = new Post();
        post.setSlug(receivedPost.getSlug());
        post.setTitle(receivedPost.getTitle());
        post.setBody(receivedPost.getBody());
        post.setUserId(Integer.parseInt(id));
        posts.add(post);
        return post;
    }
}
// END
