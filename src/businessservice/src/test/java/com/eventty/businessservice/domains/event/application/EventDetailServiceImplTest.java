package com.eventty.businessservice.domains.event.application;

import com.eventty.businessservice.application.dto.response.EventDetailFindByIdResponseDTO;
import com.eventty.businessservice.application.serviceImpl.EventDetailServiceImpl;
import com.eventty.businessservice.domain.entity.EventDetailEntity;
import com.eventty.businessservice.domain.repository.EventDetailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventDetailServiceImplTest {

    @Mock
    private EventDetailRepository eventDetailRepository;

    @InjectMocks
    private EventDetailServiceImpl eventDetailService;

    @Test
    @DisplayName("특정 이벤트 상세 정보 조회")
    public void findEventDetailByIdTest() {
        // Given
        EventDetailEntity mockDetailEntity = createEventDetailEntity();
        Long eventId = mockDetailEntity.getId();
        when(eventDetailRepository.selectEventDetailById(eventId)).thenReturn(mockDetailEntity);

        // When
        EventDetailFindByIdResponseDTO responseDTO = eventDetailService.findEventDetailById(eventId);

        // Then
        assertEquals(mockDetailEntity.getId(), responseDTO.getId());
        verify(eventDetailRepository, times(1)).selectEventDetailById(eventId);
    }


    private static EventDetailEntity createEventDetailEntity(){
        return EventDetailEntity.builder()
                .id(10L)
                .content("Sample content")
                .applyStartAt(Timestamp.valueOf("2023-08-21 10:00:00"))
                .applyEndAt(Timestamp.valueOf("2023-08-21 15:00:00"))
                .views(100L)
                .deleteDate(Timestamp.valueOf("2023-08-21 12:00:00"))
                .updateDate(Timestamp.valueOf("2023-08-21 13:00:00"))
                .createDate(Timestamp.valueOf("2023-08-21 10:30:00"))
                .build();
    }
}
