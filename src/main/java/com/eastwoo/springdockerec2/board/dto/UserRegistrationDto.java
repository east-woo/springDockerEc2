package com.eastwoo.springdockerec2.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.eastwoo.springdockerec2.board.dto
 * fileName       : UserRegistrationDto
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
public class UserRegistrationDto {

    @NotBlank(message = "사용자 ID는 필수입니다.")
    @Size(min = 3, max = 50, message = "사용자 ID는 3~50자 사이여야 합니다.")
    private String userId;

    @NotBlank(message = "사용자 이름 필수입니다.")
    @Size(min = 3, max = 50, message = "사용자 이름은 3~50자 사이여야 합니다.")
    private String username;

    @NotBlank(message = "Email은 필수입니다.")
    @Email(message = "이메일은 유효해야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, max = 255, message = "비밀번호는 6~255자 사이여야 합니다.")
    private String password;
}
