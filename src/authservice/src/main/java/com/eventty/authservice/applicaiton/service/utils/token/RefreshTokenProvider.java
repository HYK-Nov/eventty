package com.eventty.authservice.applicaiton.service.utils.token;

import com.eventty.authservice.applicaiton.service.utils.token.jwt.JwtProperties;
import com.eventty.authservice.domain.entity.AuthUserEntity;
import com.eventty.authservice.domain.entity.RefreshTokenEntity;
import com.eventty.authservice.domain.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class RefreshTokenProvider {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    // 나중에 게이트웨이에서 JWT Parsing하고난 후 id랑 refresh Token 보내온 것을 토대로 꺼내와서 검사
    public Optional<RefreshTokenEntity> findByRefreshToken(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

    public String generateToken(AuthUserEntity authUserEntity, Date now, Date expiry) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setHeaderParam("alg", "HS256")
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(authUserEntity.getEmail())
                .claim("userId", authUserEntity.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public RefreshTokenEntity saveOrUpdateRefreshToken(String name, Long userId) {
        // 기존에 저장되어 있는 RefreshToken이 있는지 확인
        Optional<RefreshTokenEntity> existingTokenOpt = refreshTokenRepository.findByUserId(userId);

        if (existingTokenOpt.isPresent()) {
            // 이미 존재하는 경우, token을 업데이트하고 저장
            RefreshTokenEntity existingToken = existingTokenOpt.get();
            existingToken.setName(name);
            return refreshTokenRepository.save(existingToken);
        } else {
            // 존재하지 않는 경우, 새로운 엔터티를 생성하고 저장
            RefreshTokenEntity newRefreshToken = RefreshTokenEntity.builder()
                    .userId(userId)
                    .name(name)
                    .build();

            return refreshTokenRepository.save(newRefreshToken);
        }
    }


    private Date createExpiry(Date now, Duration expiredAt) {
        return new Date(now.getTime() + expiredAt.toMillis());
    }

}
