package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ReviewNotFound;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review addReview(@RequestBody @Valid Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review review) throws ReviewNotFound {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable long id) throws ReviewNotFound {
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable @Valid long id) throws ReviewNotFound {
        return reviewService.getReviewById(id);
    }

    @GetMapping
    public List<Review> getAllReviewByFilmId(@RequestParam(defaultValue = "0") long filmId,
                                             @RequestParam(defaultValue = "10") int count) {
        return reviewService.getAllReviewByFilmId(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Review addLikeReview(@PathVariable long id,
                                @PathVariable long  userId) throws ReviewNotFound, ReviewNotFound {
        return reviewService.addLikeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Review addDislikeReview(@PathVariable long id,
                                   @PathVariable long userId) throws ReviewNotFound, ReviewNotFound {
        return reviewService.addDislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeReview(@PathVariable long id,
                                 @PathVariable long userId) throws ReviewNotFound, ReviewNotFound {
        reviewService.deleteLikeReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeReview(@PathVariable long id,
                                    @PathVariable long userId) throws ReviewNotFound, ReviewNotFound {
        reviewService.deleteDislikeReview(id, userId);
    }

}
