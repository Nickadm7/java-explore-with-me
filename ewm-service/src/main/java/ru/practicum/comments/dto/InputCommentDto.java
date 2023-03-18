package ru.practicum.comments.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InputCommentDto {
    @NonNull
    private String text;

    @NonNull
    private Long authorId;
}