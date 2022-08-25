package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.exeption.ReviewNotFound;
import ru.yandex.practicum.filmorate.exeption.UserNotFound;
import ru.yandex.practicum.filmorate.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.util.List;

@Getter
@Setter
@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmService filmService;
    private final UserService userService;

    public Boolean isReviewExist(long reviewId) {
        String sql = "SELECT COUNT(*) FROM REVIEWS WHERE REVIEW_ID = ?";
        int check = jdbcTemplate.queryForObject(sql, Integer.class, reviewId);
        if (check != 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Review addReview(Review review) throws UserNotFound, FilmNotFound {
        userService.getUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        String sql = "INSERT INTO REVIEWS (USEFUL, IS_POSITIVE, CONTENT, USER_ID, FILM_ID) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"REVIEW_ID"});
            ps.setInt(1, review.getUseful());
            ps.setBoolean(2, review.getIsPositive());
            ps.setString(3, review.getContent());
            ps.setLong(4, review.getUserId());
            ps.setLong(5, review.getFilmId());
            return ps;
        }, keyHolder);
        review.setReviewId(keyHolder.getKey().longValue());
        return review;
    }

    @Override
    public Review updateReview(Review review) throws ReviewNotFound {
        if (isReviewExist(review.getReviewId())) {
            try {
                String sql = "UPDATE REVIEWS SET " +
                        "IS_POSITIVE = ?, " +
                        "CONTENT = ? " +
                        "WHERE REVIEW_ID = ?";
                jdbcTemplate.update(sql,
                        review.getIsPositive(),
                        review.getContent(),
                        review.getReviewId());
                return review;
            } catch (EmptyResultDataAccessException e) {
                throw new ReviewNotFound("Неверно указан id = " + review.getReviewId() + " отзыва");
            }
        } else {
            throw new ReviewNotFound("Неверно указан id = " + review.getReviewId() + " отзыва");
        }
    }

    @Override
    public void deleteReview(long reviewId) throws ReviewNotFound {
        Review review = getReviewById(reviewId);
        String sql = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    @Override
    public Review getReviewById(long reviewId) throws ReviewNotFound {
        if (isReviewExist(reviewId)) {
            try {
                String sql = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";
                return jdbcTemplate.queryForObject(sql, new ReviewRowMapper(), reviewId);
            } catch (EmptyResultDataAccessException e) {
                throw new ReviewNotFound("");
            }
        } else {
            throw new ReviewNotFound("Неверно указан id = " + reviewId + " отзыва");
        }
    }

    @Override
    public List<Review> getAllReviewByFilmId(long filmId, int count) {
        if (filmId > 0) {
            filmService.getFilmById(filmId);
            String sql = "SELECT TOP ? * FROM REVIEWS WHERE FILM_ID = ? ORDER BY USEFUL DESC";
            if (count != 0) {
                return jdbcTemplate.query(sql, new ReviewRowMapper(), count, filmId);
            } else {
                return jdbcTemplate.query(sql, new ReviewRowMapper(), 10, filmId);
            }
        } else {
            String sql = "SELECT TOP ? * FROM REVIEWS ORDER BY USEFUL DESC";
            if (count != 0) {
                return jdbcTemplate.query(sql, new ReviewRowMapper(), count);
            } else {
                return jdbcTemplate.query(sql, new ReviewRowMapper(), 10);
            }
        }
    }

    @Override
    public Review addLikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound {
        getReviewById(reviewId);
        userService.getUserById(userId);
        String sqlLike = "INSERT INTO REVIEWS_USERS (REVIEW_ID, USER_ID) VALUES (?, ?)";
        String sqlReviewUseful = "UPDATE REVIEWS SET USEFUL = USEFUL + 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlLike, reviewId, userId);
        jdbcTemplate.update(sqlReviewUseful, reviewId);
        return getReviewById(reviewId);
    }

    @Override
    public Review addDislikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound {
        getReviewById(reviewId);
        userService.getUserById(userId);
        String sqlLike = "INSERT INTO REVIEWS_USERS (REVIEW_ID, USER_ID) VALUES (?, ?)";
        String sqlReviewUseful = "UPDATE REVIEWS SET USEFUL = USEFUL - 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlLike, reviewId, userId);
        jdbcTemplate.update(sqlReviewUseful, reviewId);
        return getReviewById(reviewId);
    }

    @Override
    public void deleteLikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound {
        getReviewById(reviewId);
        userService.getUserById(userId);
        String sqlLike = "DELETE FROM REVIEWS_USERS WHERE REVIEW_ID = ? AND USER_ID = ?";
        String sqlReviewUseful = "UPDATE REVIEWS SET USEFUL = USEFUL - 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlLike, reviewId, userId);
        jdbcTemplate.update(sqlReviewUseful, reviewId);
    }

    @Override
    public void deleteDislikeReview(long reviewId, long userId) throws ReviewNotFound, UserNotFound {
        getReviewById(reviewId);
        userService.getUserById(userId);
        String sqlLike = "DELETE FROM REVIEWS_USERS WHERE REVIEW_ID = ? AND USER_ID = ?";
        String sqlReviewUseful = "UPDATE REVIEWS SET USEFUL = USEFUL + 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlLike, reviewId, userId);
        jdbcTemplate.update(sqlReviewUseful, reviewId);
    }

}
