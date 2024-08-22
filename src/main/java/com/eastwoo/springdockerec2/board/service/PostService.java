package com.eastwoo.springdockerec2.board.service;

import com.eastwoo.springdockerec2.board.dto.PostDto;
import com.eastwoo.springdockerec2.board.entity.Post;
import com.eastwoo.springdockerec2.board.entity.User;
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

    public List<PostDto> findAllPosts() {
        return postRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<PostDto> findPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PostDto createPost(PostDto postDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Post post = convertToEntity(postDto, user);
        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }

    private PostDto convertToDto(Post post) {
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getUser().getUsername(), post.getCreatedAt());
    }

    private Post convertToEntity(PostDto postDto, User user) {
        return Post.builder().id(postDto.getId()).title(postDto.getTitle()).content(postDto.getContent()).user(user).createdAt(postDto.getCreatedAt()).build();

    }
}