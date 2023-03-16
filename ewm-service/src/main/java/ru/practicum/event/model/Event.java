package ru.practicum.event.model;

import lombok.*;
import ru.practicum.categories.model.Category;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation")
    private String annotation; //Краткое описание

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmed_requests") //Количество одобренных заявок на участие в данном событии
    private int confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn; //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")

    @Column(name = "description")
    private String description; //Полное описание события

    @Column(name = "event_date")
    private LocalDateTime eventDate; //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator_id")
    private User initiator; //Инициатор события

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location; //Координаты места проведения

    @Column(name = "paid")
    private Boolean paid; //Нужно ли оплачивать участие

    @Column(name = "participant_limit")
    private Integer participantLimit; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @Column(name = "published_on")
    private LocalDateTime publishedOn; //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")

    @Column(name = "request_moderation")
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие

    @JoinColumn(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "title")
    private String title; //Заголовок

    @Column(name = "views")
    private Long views;//Количество просмотрев события
}