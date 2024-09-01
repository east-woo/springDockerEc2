package com.eastwoo.springdockerec2.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : com.eastwoo.springdockerec2.board.dto
 * fileName       : PostDto
 * author         : dongwoo
 * date           : 2024-08-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-22        dongwoo       최초 생성
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String userId;
    private LocalDateTime createdAt;
}