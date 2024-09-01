package com.eastwoo.springdockerec2.board.service;

import com.eastwoo.springdockerec2.board.dto.PostDto;
import com.eastwoo.springdockerec2.board.entity.Post;
import com.eastwoo.springdockerec2.board.entity.User;
import com.eastwoo.springdockerec2.board.exception.ResourceNotFoundException;
import com.eastwoo.springdockerec2.board.repository.PostRepository;
import com.eastwoo.springdockerec2.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.eastwoo.springdockerec2.board.service
 * fileName       : PostService
 * author         : dongwoo
 * date           : 2024-08-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-22        dongwoo       최초 생성
 */

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostDto createPost(PostDto postDto, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);

        return convertToDto(savedPost);
    }

    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        return convertToDto(post);
    }

    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PostDto updatePost(Long postId, PostDto postDto) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));

        Post updatedPost = Post.builder()
                .id(existingPost.getId())
                .title(postDto.getTitle() != null ? postDto.getTitle() : existingPost.getTitle())
                .content(postDto.getContent() != null ? postDto.getContent() : existingPost.getContent())
                .createdAt(existingPost.getCreatedAt())
                .user(existingPost.getUser())
                .build();

        updatedPost = postRepository.save(updatedPost);
        return convertToDto(updatedPost);
    }


    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));

        postRepository.delete(post);
    }

    private PostDto convertToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .userId(post.getUser().getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private Post convertToEntity(PostDto postDto) {
        return Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();
    }
}