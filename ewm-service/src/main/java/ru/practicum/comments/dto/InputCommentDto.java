package ru.practicum.comments.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InputCommentDto {
    private String text;

    private Long authorId;
}