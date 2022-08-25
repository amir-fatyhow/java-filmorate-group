package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "reviewId") // Объект определяется только по полю id
@ToString
public class Review {

    private long reviewId;
    private int useful;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private String content;
    @NotNull
    private Long userId;
    @NotNull
    private Long filmId;

}
