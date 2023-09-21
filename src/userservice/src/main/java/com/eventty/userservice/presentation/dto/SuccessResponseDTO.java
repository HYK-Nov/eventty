package com.eventty.userservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SuccessResponseDTO<T>{

    // code 번호 0으로 고정
    private final T data;

    @JsonCreator
    private SuccessResponseDTO(@JsonProperty("data") T data) {
        this.data = data;
    }

    public static <T> SuccessResponseDTO<T> of(T data) {
        return new SuccessResponseDTO<>(data);
    }

}