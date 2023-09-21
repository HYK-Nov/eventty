package com.eventty.authservice.applicaiton.service.utils;

import com.eventty.authservice.api.dto.request.UserIdFindApiRequestDTO;
import com.eventty.authservice.api.dto.request.UserCreateApiRequestDTO;
import com.eventty.authservice.api.dto.response.ImageQueryApiResponseDTO;
import com.eventty.authservice.applicaiton.dto.*;
import com.eventty.authservice.domain.entity.AuthUserEntity;
import com.eventty.authservice.domain.entity.AuthorityEntity;
import com.eventty.authservice.domain.model.Authority;
import com.eventty.authservice.presentation.dto.request.UserAuthenticateRequestDTO;
import com.eventty.authservice.presentation.dto.request.EmailFindRequestDTO;
import com.eventty.authservice.presentation.dto.request.PWFindRequestDTO;
import com.eventty.authservice.presentation.dto.request.FullUserCreateRequestDTO;
import com.eventty.authservice.presentation.dto.response.EmailFindResponseDTO;
import com.eventty.authservice.presentation.dto.response.LoginResponseDTO;
import com.eventty.authservice.presentation.dto.response.PWFindResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomConverter {

    private final ObjectMapper objectMapper;

    /*
     * Tokens Converter
     */

    // 유저 인증 요청할 때 필요한 데이터를 전부 모아둔 DTO 생성
    public SessionTokensDTO convertTokensDTO(UserAuthenticateRequestDTO userAuthenticateRequestDTO) {
        return new SessionTokensDTO(userAuthenticateRequestDTO.getAccessToken(), userAuthenticateRequestDTO.getRefreshToken());
    }

    public CsrfTokenDTO convertCsrfTokenDTO(Long userId, String csrfToken) {
        return new CsrfTokenDTO(userId, csrfToken);
    }

    public ValidateRefreshTokenDTO convertToValidationRefreshTokenDTO(Long userId, SessionTokensDTO SessionTokensDTO) {
        return new ValidateRefreshTokenDTO(userId, SessionTokensDTO.refreshToken());
}
    public AuthUserEntity convertAuthEntityConvert(String email, String encryptedPassword) {
        return AuthUserEntity.builder()
                .email(email)
                .password(encryptedPassword)
                .build();
    }

    /*
     * Request, Response Converter
     */

    public UserCreateApiRequestDTO convertUserCreateRequestDTO(FullUserCreateRequestDTO fullUserCreateRequestDTO, Long userId) {
        return new UserCreateApiRequestDTO(
                userId,     // User Id
                fullUserCreateRequestDTO.getName(),    // Name
                fullUserCreateRequestDTO.getAddress(), // Address
                fullUserCreateRequestDTO.getBirth(),   // Birth
                fullUserCreateRequestDTO.getPhone()   // Phone
        );
    }

    public LoginSuccessDTO convertLoginSuccessDTO(SessionTokensDTO sessionTokensDTO, AuthUserEntity authUserEntity, String csrfToken, ImageQueryApiResponseDTO imageQueryApiResponseDTO) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
                authUserEntity.getId(),
                authUserEntity.getEmail(), // email
                getRole(authUserEntity.getAuthorities()),  // Role
                imageQueryApiResponseDTO.getOriginFileName(),
                imageQueryApiResponseDTO.getImagePath()
        );

        return new LoginSuccessDTO(sessionTokensDTO, loginResponseDTO, csrfToken);
    }

    public PWFindResponseDTO convertPWFindResponseDTO(AuthUserEntity authUserEntity) {
        return new PWFindResponseDTO(authUserEntity.getId(), authUserEntity.getEmail());
    }

    public EmailFindResponseDTO convertEmailFindResponseDTO(AuthUserEntity authUserEntity) {
        return new EmailFindResponseDTO(authUserEntity.getEmail());
    }

    // 현재 DTO로 건네주니깐 이러한 상황이 발생
    public UserIdFindApiRequestDTO convertUserIdFindApiRequestDTO(PWFindRequestDTO pwFindRequestDTO) {
        return new UserIdFindApiRequestDTO(pwFindRequestDTO.getName(), pwFindRequestDTO.getPhone());
    }

    public UserIdFindApiRequestDTO convertUserIdFindApiRequestDTO(EmailFindRequestDTO emailFindRequestDTO) {
        return new UserIdFindApiRequestDTO(emailFindRequestDTO.getName(), emailFindRequestDTO.getPhone());
    }

    /*
     * Authorites Converter => User
     */

    public List<Authority> convertAuthority(AuthUserEntity authUserEntity) {
        return authUserEntity.getAuthorities()
                .stream()
                .map(authorityEntity -> Authority.builder().role(authorityEntity.getName()).build())
                .collect(Collectors.toList());
    }

    public String convertAuthoritiesJson(AuthUserEntity authUserEntity) {
        List<Authority> authorities = convertAuthorities(authUserEntity.getAuthorities());
        try {
            return objectMapper.writeValueAsString(authorities);
        } catch (JsonProcessingException e) {
            throw  new RuntimeException("Error Converting authorities to Json", e);
        }
    }

    /*
     * Authorities Method
     */

    private List<Authority> convertAuthorities(List<AuthorityEntity> list) {
        return list.stream()
                .map(authorityEntity -> Authority.builder()
                        .role(authorityEntity.getName())
                        .build())
                .toList();
    }

    // 나중에 수정해야 함 => 유저에 대한 검증 로직이 전부 통과 했지만, Role이 없다? 단순히 예외로 처리할 문제가 아님
    private String getRole(List<AuthorityEntity> list) {
        return list.stream()
                .map(AuthorityEntity::getName)
                .filter(name -> name.startsWith("ROLE_"))
                .findFirst()
                .orElseGet(() -> logging(list.get(0).getId()));
    }

    /*
     * Logging
     */
    private String logging(Long userId) {
        log.error("Having id {} User Validation is OK. But, User Role is Not Found!! Critical Issue!", userId);
        // 임시 방편으로 USER로 찍어서 보내주기
        return "ROLE_USER";
    }
}
