package com.eastwoo.springdockerec2.board.repository;

import com.eastwoo.springdockerec2.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.eastwoo.springdockerec2.board.repository
 * fileName       : UserRepository
 * author         : dongwoo
 * date           : 2024-08-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-22        dongwoo       최초 생성
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    /*Optional<User> findByEmail(String email);*/

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);
}
