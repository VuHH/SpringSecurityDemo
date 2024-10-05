package com.demo.springsecurity.controller;

import com.demo.springsecurity.entity.Post;
import com.demo.springsecurity.entity.User;
import com.demo.springsecurity.repository.UserRepository;
import com.demo.springsecurity.service.PostService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final PostService postService;

  private final UserRepository userRepository;

  @Autowired
  public PostController(
      PostService postService, UserRepository userRepository) {
    this.postService = postService;
    this.userRepository = userRepository;
  }

  @GetMapping
  public List<Post> getUserPosts(@AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByUsername(userDetails.getUsername());
    return postService.getAllPostsByUser(user.getId());
  }

  @PostMapping
  public Post createPost(@RequestBody Post post, @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByUsername(userDetails.getUsername());
    post.setUser(user);
    return postService.createPost(post);
  }

  @DeleteMapping("/{postId}")
  public void deletePost(
      @PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByUsername(userDetails.getUsername());
    Post post =
        postService.getAllPostsByUser(user.getId()).stream()
            .filter(p -> p.getId().equals(postId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Post not found"));

    if (!post.getUser().getUsername().equals(userDetails.getUsername())) {
      throw new RuntimeException("You do not have permission to delete this post");
    }

    postService.deletePost(postId);
  }
}
