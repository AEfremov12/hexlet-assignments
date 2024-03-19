package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("")
    public List<PostDTO> getAll() {
        var posts = postRepository.findAll();
        var postDtos = posts.stream()
                .map(this::toPostDTO)
                .toList();
        return postDtos;
    }

    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable long id) {
        var post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        return toPostDTO(post);
    }

    private PostDTO toPostDTO (Post post) {
        var dto = new PostDTO();
        var comments = commentRepository.findByPostId(post.getId());
        var commentDtos = new HashSet<CommentDTO>();
        comments.forEach(comment -> {
            if (comment.getPostId() == post.getId()) commentDtos.add(toCommentDto(comment));
        });
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setComments(commentDtos.stream().toList());
        return dto;
    }

    private CommentDTO toCommentDto (Comment comment) {
        var dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setBody(comment.getBody());
        return dto;
    }
}
// END
