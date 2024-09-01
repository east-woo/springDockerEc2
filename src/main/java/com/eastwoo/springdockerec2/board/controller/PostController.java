package com.eastwoo.springdockerec2.board.controller;

import com.eastwoo.springdockerec2.board.config.JwtTokenProvider;
import com.eastwoo.springdockerec2.board.dto.PostDto;
import com.eastwoo.springdockerec2.board.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Please explain the class!!
 *
 * @author : dongwoo
 * @fileName : PostController
 * @since : 2024-08-24
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    public PostController(PostService postService, JwtTokenProvider jwtTokenProvider) {
        this.postService = postService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        PostDto createdPost = postService.createPost(postDto, userId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto) {
        PostDto updatedPost = postService.updatePost(postId, postDto);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}