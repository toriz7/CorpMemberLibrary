package com.david.CorpMemberLibrary.dto.user;

import com.david.CorpMemberLibrary.domain.user.User;
import com.david.CorpMemberLibrary.domain.user.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원가입 요청 DTO
 * 회원가입 폼에서 전달되는 데이터를 담는 객체입니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto {


    @NotBlank(message = "사번은 필수입니다")
    @Size(max = 50, message = "사번은 50자를 초과할 수 없습니다")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 4, max = 100, message = "비밀번호는 4자 이상 100자 이하여야 합니다")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다")
    private String passwordConfirm;

    @NotBlank(message = "이름은 필수입니다")
    @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다")
    private String name;

    @Size(max = 50, message = "직책은 50자를 초과할 수 없습니다")
    private String position;

    @Builder
    public SignupRequestDto(String userId, String password, String passwordConfirm,
                            String name, String position) {
        this.userId = userId;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.position = position;
    }

    /**
     * 비밀번호와 비밀번호 확인이 일치하는지 검증
     * @return 일치하면 true
     */
    public boolean isPasswordMatching() {
        return password != null && password.equals(passwordConfirm);
    }

    /**
     * DTO를 Entity로 변환
     * @param encodedPassword 암호화된 비밀번호
     * @return User 엔티티
     */
    public User toEntity(String encodedPassword) {
        return User.builder()
                .userId(userId)
                .password(encodedPassword)
                .name(name)
                .position(position)
                .role("USER")
                .status(UserStatus.PENDING)
                .build();
    }
}
