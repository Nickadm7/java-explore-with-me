package ru.practicum.ewmservice.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.Status;
import ru.practicum.ewmservice.requests.model.ParticipationRequest;
import ru.practicum.ewmservice.user.model.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAll_ByRequester(User requester);

    @Query("select pr from ParticipationRequest as pr " +
           "where pr.event.id = ?1 " +
           "and pr.status = ?2")
    List<ParticipationRequest> findAll_ByEvent_IdAndStatus(Long id, Status status);

    @Query("select pr from ParticipationRequest as pr " +
            "where pr.event.id in :ids " +
            "and pr.status = 'CONFIRMED'")
    List<ParticipationRequest> findAll_ByEvent_IdsList(List<Long> ids);

    ParticipationRequest findByRequester_idAndEvent_id(Long userId, Long eventId);

    List<ParticipationRequest> findAllByIdIn(List<Long> id);

    List<ParticipationRequest> findAll_ByEvent(Event event);
}

