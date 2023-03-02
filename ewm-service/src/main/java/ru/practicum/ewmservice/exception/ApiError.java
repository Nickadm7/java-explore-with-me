package ru.practicum.ewmservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError extends RuntimeException{
    public List<String> errors; //Список стектрейсов или описания ошибок

    public String message; //Сообщение об ошибке

    public String reason; //Общее описание причины ошибки

    public HttpStatus status; //Код статуса HTTP-ответа

    public LocalDateTime timestamp; //Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
}