package ru.practicum.model;

import lombok.*;
import ru.practicum.dto.ViewStats;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hits")
@NamedNativeQueries({
    @NamedNativeQuery(name = "findAllHitsNoUnique",
        query = "SELECT app AS app, uri AS uri, COUNT(ip) AS hits FROM hits " +
                "WHERE uri IN ?3 AND (timestamp >= ?1 AND timestamp <= ?2) " +
                "GROUP BY app, uri " +
                "ORDER BY hits DESC", resultSetMapping = "ViewStatsMapping"),
    @NamedNativeQuery(name = "findAllHitsUnique",
        query = "SELECT app AS app, uri AS uri, COUNT(DISTINCT ip) AS hits FROM hits " +
                "WHERE uri IN ?3 AND (timestamp >= ?1 AND timestamp <= ?2) " +
                "GROUP BY app, uri " +
                "ORDER BY hits DESC", resultSetMapping = "ViewStatsMapping")})
@SqlResultSetMapping(name = "ViewStatsMapping",
        classes = {
                @ConstructorResult(
                        columns = {
                                @ColumnResult(name = "app", type = String.class),
                                @ColumnResult(name = "uri", type = String.class),
                                @ColumnResult(name = "hits", type = Long.class)
                        },
                        targetClass = ViewStats.class
                )}
)
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; //Идентификатор записи

    @Column(name = "app")
    private String app; //Идентификатор сервиса для которого записывается информация

    @Column(name = "uri")
    private String uri; //URI для которого был осуществлен запрос

    @Column(name = "ip")
    private String ip; //IP-адрес пользователя, осуществившего запрос

    @Column(name = "timestamp")
    private LocalDateTime timestamp; //Дата и время, когда был совершен запрос к эндпоинту
}
