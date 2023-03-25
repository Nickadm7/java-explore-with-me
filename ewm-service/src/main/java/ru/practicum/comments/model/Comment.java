package ru.practicum.comments.model;

import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text; //текст комментария, максимальная длина 2000 знаков

    @Column(name = "created")
    private LocalDateTime created; //дата создания комментария

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author; //автор комментария

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event; //событие, к которому написан комментарий

    @Column(name = "published")
    private Boolean published; //опубликован ли комментарий
}