package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    private String app; //Идентификатор сервиса для которого записывается информация

    private String uri; //URI для которого был осуществлен запрос

    private String ip; //IP-адрес пользователя, осуществившего запрос

    private String timestamp; //Дата и время, когда был совершен запрос к эндпоинту
}