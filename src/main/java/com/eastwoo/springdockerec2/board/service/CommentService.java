package com.eastwoo.springdockerec2.board.service;

import com.eastwoo.springdockerec2.board.dto.CommentDto;
import com.eastwoo.springdockerec2.board.entity.Comment;
import com.eastwoo.springdockerec2.board.entity.Post;
import com.eastwoo.springdockerec2.board.entity.User;
import com.eastwoo.springdockerec2.board.exception.ResourceNotFoundException;
import com.eastwoo.springdockerec2.board.repository.CommentRepository;
import com.eastwoo.springdockerec2.board.repository.PostRepository;
import com.eastwoo.springdockerec2.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.eastwoo.springdockerec2.board.service
 * fileName       : CommentController
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

    public CommentDto createComment(CommentDto commentDto) {
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .createdAt(LocalDateTime.now())
                .post(postRepository.findById(commentDto.getPostId())
                        .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + commentDto.getPostId())))
                .user(userRepository.findById(commentDto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + commentDto.getUsername())))
                .build();

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    public CommentDto updateComment(Long commentId, CommentDto commentDto) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));


        Comment updatedComment = Comment.builder()
                .id(existingComment.getId()) // retain existing id
                .content(commentDto.getContent())
                .createdAt(existingComment.getCreatedAt()) // retain original creation time
                .post(existingComment.getPost()) // retain original post association
                .user(existingComment.getUser()) // retain original user association
                .build();

        Comment savedComment = commentRepository.save(updatedComment);
        return convertToDto(savedComment);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment not found with id " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .postId(comment.getPost().getId())
                .username(comment.getUser().getUsername())
                .build();
    }
}