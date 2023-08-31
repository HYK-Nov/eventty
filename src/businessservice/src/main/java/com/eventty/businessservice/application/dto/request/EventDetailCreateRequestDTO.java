package com.eventty.businessservice.application.dto.request;

import com.eventty.businessservice.domain.entity.EventDetailEntity;
import lombok.Builder;
import lombok.Setter;

import java.sql.Timestamp;

@Builder
@Setter
public class EventDetailCreateRequestDTO {
    private Long id;
    private String content;
    private Timestamp applyStartAt;
    private Timestamp applyEndAt;
}
