package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Status;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAll_ByRequester(User requester);

    @Query("select pr from ParticipationRequest as pr " +
           "where pr.event.id = ?1 " +
           "and pr.status = ?2")
    List<ParticipationRequest> findAll_ByEvent_IdAndStatus(Long id, Status status);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);

    @Query("select pr from ParticipationRequest as pr " +
            "where pr.event.id in :ids " +
            "and pr.status = 'CONFIRMED'")
    List<ParticipationRequest> findAll_ByEvent_IdsList(List<Long> ids);

    ParticipationRequest findByRequester_idAndEvent_id(Long userId, Long eventId);

    List<ParticipationRequest> findAllByIdIn(List<Long> id);

    List<ParticipationRequest> findAll_ByEvent(Event event);
}

