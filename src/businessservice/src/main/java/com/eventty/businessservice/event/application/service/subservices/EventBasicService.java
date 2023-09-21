package com.eventty.businessservice.event.application.service.subservices;

import com.eventty.businessservice.event.application.dto.request.EventCreateRequestDTO;
import com.eventty.businessservice.event.application.dto.request.EventUpdateRequestDTO;
import com.eventty.businessservice.event.application.dto.request.TicketCreateRequestDTO;
import com.eventty.businessservice.event.application.dto.response.EventBasicResponseDTO;
import com.eventty.businessservice.event.domain.Enum.Category;
import com.eventty.businessservice.event.domain.entity.EventBasicEntity;
import com.eventty.businessservice.event.domain.exception.EventNotFoundException;
import com.eventty.businessservice.event.domain.exception.AccessDeniedException;
import com.eventty.businessservice.event.domain.repository.EventBasicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventBasicService {

    private final EventBasicRepository eventBasicRepository;

    public List<EventBasicResponseDTO> findAllEvents(){
        return Optional.ofNullable(eventBasicRepository.selectAllEvents())
                .map(events -> events.stream()
                        .map(EventBasicResponseDTO::fromEntity)
                        .collect(Collectors.toList()))
                .orElseThrow(()->EventNotFoundException.EXCEPTION);
    }

    public EventBasicResponseDTO findEventById(Long eventId){
        return Optional.ofNullable(eventBasicRepository.selectEventById(eventId))
                .map(EventBasicResponseDTO::fromEntity)
                .orElseThrow(() -> EventNotFoundException.EXCEPTION);
    }

    public List<EventBasicResponseDTO> findEventsByIdList(List<Long> eventIdList){
        return Optional.ofNullable(eventBasicRepository.selectEventsByIdList(eventIdList))
                .filter(events -> !events.isEmpty())
                .orElseThrow(() -> EventNotFoundException.EXCEPTION)
                .stream()
                .map(EventBasicResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<EventBasicResponseDTO> findEventsByHostId(Long hostId){
        return Optional.ofNullable(eventBasicRepository.selectEventsByHostId(hostId))
                .filter(events -> !events.isEmpty())
                .orElseThrow(() -> EventNotFoundException.EXCEPTION)
                .stream()
                .map(EventBasicResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<EventBasicResponseDTO> findEventsByCategory(Category category){
        return Optional.ofNullable(eventBasicRepository.selectEventsByCategory(category.getId()))
                .filter(events -> !events.isEmpty())
                .orElseThrow(() -> EventNotFoundException.EXCEPTION)
                .stream()
                .map(EventBasicResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<EventBasicResponseDTO> findEventsBySearch(String keyword){
        return Optional.ofNullable(eventBasicRepository.selectEventsBySearch(keyword))
                .filter(events -> !events.isEmpty())
                .orElseThrow(() -> EventNotFoundException.EXCEPTION)
                .stream()
                .map(EventBasicResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Long createEvent(EventCreateRequestDTO eventCreateRequestDTO) {
        // 참가 인원수 계산
        Long participateNum = calculateParticipateNum(eventCreateRequestDTO.getTickets());
        eventCreateRequestDTO.setParticipateNum(participateNum);

        EventBasicEntity eventBasic = eventCreateRequestDTO.toEventBasicEntity();
        eventBasicRepository.insertEvent(eventBasic);

        return eventBasic.getId();
    }

    public Long updateEvent(Long eventId, EventUpdateRequestDTO eventUpdateRequestDTO){
        // 업데이트 전, 해당 데이터 존재 여부 확인
        EventBasicEntity eventBasic = getEventIfExists(eventId);

        // 각 필드가 null 이 아닐때에만 업데이트
        if(eventUpdateRequestDTO.getTitle() != null){
            eventBasic.updateTitle(eventUpdateRequestDTO.getTitle());
        }
        if(eventUpdateRequestDTO.getCategory() != null){
            eventBasic.updateCategory(eventUpdateRequestDTO.getCategory());
        }
        if(eventUpdateRequestDTO.getIsActive() != null){
            eventBasic.updateIsActive(eventUpdateRequestDTO.getIsActive());
        }

        eventBasicRepository.updateEvent(eventBasic);

        return eventId;
    }

    public Long deleteEvent(Long eventId){
        // 삭제 전, 해당 데이터 존재 여부 확인
        EventBasicEntity eventBasic = getEventIfExists(eventId);

        eventBasicRepository.deleteEvent(eventId);

        return eventId;
    }

    public void subtractParticipateNum(Long eventId, Long quantity) {
        // 인원수 감소 전, 해당 데이터 존재 여부 확인
        EventBasicEntity eventBasic = getEventIfExists(eventId);

        eventBasic.subtractParticipateNum(quantity);
        eventBasicRepository.updateEvent(eventBasic);
    }

    public void checkHostId(Long hostId, Long eventId) {
        EventBasicEntity eventBasic = getEventIfExists(eventId);

        if(!eventBasic.getUserId().equals(hostId)){
            throw AccessDeniedException.EXCEPTION;
        }
    }

    private EventBasicEntity getEventIfExists(Long eventId) {
        Optional<EventBasicEntity> eventOptional = Optional.ofNullable(eventBasicRepository.selectEventById(eventId));
        return eventOptional.orElseThrow(() -> EventNotFoundException.EXCEPTION);
    }

    private Long calculateParticipateNum(List<TicketCreateRequestDTO> tickets) {
        return tickets.stream()
                .mapToLong(TicketCreateRequestDTO::getQuantity)
                .sum();
    }
}
