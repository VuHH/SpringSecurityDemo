package com.demo.springsecurity.service;

import com.demo.springsecurity.entity.Post;
import com.demo.springsecurity.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
  private final PostRepository postRepository;

  @Autowired
  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public List<Post> getAllPostsByUser(Long userId) {
    return postRepository.findByUserId(userId);
  }

  public Post createPost(Post post) {
    return postRepository.save(post);
  }

  public void deletePost(Long postId) {
    postRepository.deleteById(postId);
  }
}
