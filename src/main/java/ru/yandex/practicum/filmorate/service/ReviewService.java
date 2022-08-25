package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.exeption.ReviewNotFound;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;

    public Review addReview(Review review) {
        return reviewStorage.addReview(review);
    }

    public Review updateReview(Review review) throws ReviewNotFound {
        return reviewStorage.updateReview(review);
    }

    public void deleteReview(long reviewId) throws ReviewNotFound {
        reviewStorage.deleteReview(reviewId);
    }

    public Review getReviewById(long reviewId) throws ReviewNotFound {
        return reviewStorage.getReviewById(reviewId);
    }

    public List<Review> getAllReviewByFilmId(long filmId, int count) throws FilmNotFound {
        return reviewStorage.getAllReviewByFilmId(filmId, count);
    }

    public Review addLikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound {
        return reviewStorage.addLikeReview(reviewId, userId);
    }

    public Review addDislikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound {
        return reviewStorage.addDislikeReview(reviewId, userId);
    }

    public void deleteLikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound {
        reviewStorage.deleteLikeReview(reviewId, userId);
    }

    public void deleteDislikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound {
        reviewStorage.deleteDislikeReview(reviewId, userId);
    }

}
