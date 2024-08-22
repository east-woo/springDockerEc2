package com.eastwoo.springdockerec2.board.service;

import com.eastwoo.springdockerec2.board.dto.CommentDto;
import com.eastwoo.springdockerec2.board.entity.Comment;
import com.eastwoo.springdockerec2.board.entity.Post;
import com.eastwoo.springdockerec2.board.entity.User;
import com.eastwoo.springdockerec2.board.repository.CommentRepository;
import com.eastwoo.springdockerec2.board.repository.PostRepository;
import com.eastwoo.springdockerec2.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * packageName    : com.eastwoo.springdockerec2.board.service
 * fileName       : CommentService
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<CommentDto> findCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<CommentDto> findCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public CommentDto createComment(CommentDto commentDto, Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Comment comment = convertToEntity(commentDto, user, post);
        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    private CommentDto convertToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getContent(), comment.getUser().getUsername(), comment.getPost().getId(), comment.getCreatedAt());
    }

    private Comment convertToEntity(CommentDto commentDto, User user, Post post) {
        return new Comment(commentDto.getId(), commentDto.getContent(), user, post, commentDto.getCreatedAt());
    }
}