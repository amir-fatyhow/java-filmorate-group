package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.exeption.ReviewNotFound;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Review addReview(Review review);

    Review updateReview(Review review) throws ReviewNotFound;

    void deleteReview(long reviewId) throws ReviewNotFound;

    Review getReviewById(long reviewId) throws ReviewNotFound;

    List<Review> getAllReviewByFilmId(long filmId, int count) throws FilmNotFound;

    Review addLikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound;

    Review addDislikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound;

    void deleteLikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound;

    void deleteDislikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound;

}
