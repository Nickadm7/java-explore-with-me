package ru.practicum.ewmservice.compilations.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmservice.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {
    @Id
    @Column(name = "compilation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pinned")
    private Boolean pinned; //Закреплена ли подборка на главной странице сайта

    @Column(name = "title")
    private String title; //Заголовок подборки

    @ManyToMany
    @JoinTable(
            name = "event_compilations",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events; //Список событий входящих в подборку

    @Override
    public String toString() {
        return "Compilation{" +
                "id=" + id +
                ", pinned=" + pinned +
                ", title='" + title + '\'' +
                ", events=" + events +
                '}';
    }
}