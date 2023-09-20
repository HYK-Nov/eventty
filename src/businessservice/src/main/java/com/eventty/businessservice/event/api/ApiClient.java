package com.eventty.businessservice.event.api;

import com.eventty.businessservice.event.api.dto.request.QueryAppliesCountRequestDTO;
import com.eventty.businessservice.event.api.dto.response.QueryAppliesCountResponseDTO;
import com.eventty.businessservice.event.api.dto.response.UserFindByIdResponseDTO;
import com.eventty.businessservice.event.api.utils.MakeUrlService;
import com.eventty.businessservice.event.presentation.response.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

@Slf4j
@Component
@AllArgsConstructor
public class ApiClient {
    private static final String CSRF_HEADER = "X-Requires-Auth";

    // @Bean에 이름을 지정하지 않아서 생성자 이름을 따라감
    private final MakeUrlService makeUrlService;

    private final RestTemplate basicRestTemplate;

    private final RestTemplate customRestTemplate;

    public ResponseEntity<ResponseDTO<UserFindByIdResponseDTO>> queryUserInfoApi(Long hostId) {

        HttpEntity<Void> entity = createAuthenticateHttpPostEnttiy(null);

        // !!! zhostId request Parameter로 담아주는 작업 추가
        URI uri = makeUrlService.queryUserInfo();

        // API 호출은 Loggin Level을 Info로 지정해서 로그 관리
        logApiCall("Event server", "User server", "Query user info");
        return customRestTemplate.exchange(
                uri, HttpMethod.GET, entity, new ParameterizedTypeReference<ResponseDTO<UserFindByIdResponseDTO>>() {}
        );
    }

    public ResponseEntity<ResponseDTO<QueryAppliesCountResponseDTO>> queryAppliesCountApi(
            QueryAppliesCountRequestDTO queryAppliesCountRequestDTO
    ) {

        HttpEntity<QueryAppliesCountRequestDTO> entity = createHttpPostEntity(queryAppliesCountRequestDTO);

        URI uri = makeUrlService.queryTicketCount();

        logApiCall("Event server", "User server", "Query applies Count");

        return customRestTemplate.exchange(
                uri, HttpMethod.GET, entity, new ParameterizedTypeReference<ResponseDTO<QueryAppliesCountResponseDTO>>() {}
        );
    }

    private void logApiCall(String from, String to, String purpose) {
        log.info("API 호출 From: {} To: {} Purpose: {}", from, to, purpose);
    }

    private <T> HttpEntity<T> createHttpPostEntity(T dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return new HttpEntity<>(dto, headers);
    }

    private <T> HttpEntity<T> createAuthenticateHttpPostEnttiy(T dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(CSRF_HEADER, "True");

        return new HttpEntity<>(dto, headers);
    }
}
