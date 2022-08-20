package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(of = "ReviewId")
public class Review {
    private long ReviewId;
    @NotBlank
    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;
    private int userId;
    private int filmId;
    private int useful;
}
