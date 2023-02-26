package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {
    private String app; //Название сервиса

    private String uri; //URI сервиса

    private Long hits; //Количество просмотров
}