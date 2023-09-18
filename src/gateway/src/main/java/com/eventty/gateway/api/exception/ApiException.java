package com.eventty.gateway.api.exception;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

import java.net.URI;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private String message;
    private HttpStatusCode HttpStatusCode;
    public ApiException(URI uri, HttpMethod httpMethod, HttpStatusCode HttpStatusCode) {
        this.message = "API 호출 실패 URI: " + uri + ", Method: " + httpMethod;
        this.HttpStatusCode = HttpStatusCode;
    }

}
