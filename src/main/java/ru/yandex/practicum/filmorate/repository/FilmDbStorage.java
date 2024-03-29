package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final LikesDbStorage likesDbStorage;

    @Override
    public Film addFilm(Film film) throws FilmNotFound {
        film.setRate(0);
        String sql = "INSERT INTO FILMS (" +
                "NAME, " +
                "MPA_ID, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"ID"});
            ps.setString(1, film.getName());
            ps.setInt(2, film.getMpa().getId());
            ps.setString(3, film.getDescription());
            LocalDate realeaseDate = film.getReleaseDate();
            if (realeaseDate == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(realeaseDate));
            }
            ps.setInt(5, film.getDuration());
            return ps;

        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        setFilmGenres(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql1 = "SELECT * FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID=MPA.ID";
        return jdbcTemplate.query(sql1, new FilmRowMapper(genreDbStorage, mpaDbStorage, likesDbStorage));
    }

    @Override
    public Film getFilmById(long id) throws FilmNotFound {
        try {
            String sql = "SELECT * FROM FILMS " +
                    "JOIN MPA ON FILMS.MPA_ID=MPA.ID " +
                    "WHERE FILMS.ID = ?";
            return jdbcTemplate.queryForObject(sql, new FilmRowMapper(genreDbStorage,
                    mpaDbStorage, likesDbStorage), id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFound("Неверно указан id = " + id + " фильма");
        }
    }

    @Override
    public Film updateFilm(Film film) throws FilmNotFound {
        film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        getFilmById(film.getId());
        String sql = "UPDATE FILMS SET " +
                "NAME = ?, " +
                "MPA_ID = ?, " +
                "DESCRIPTION = ?, " +
                "RELEASE_DATE = ?, " +
                "DURATION = ? " +
                "WHERE ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getMpa().getId(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );
        setFilmGenres(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public void deleteFilm(long filmId) throws FilmNotFound {
        try {
            String sql = "DELETE FROM FILMS WHERE ID = ?";
            String sqlGenre = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
            String sqlLike = "DELETE FROM LIKES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlGenre, filmId);
            jdbcTemplate.update(sqlLike, filmId);
            jdbcTemplate.update(sql, filmId);
        } catch (IllegalArgumentException e) {
            throw new FilmNotFound("Неверно указан id = " + filmId + " фильма");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT TOP ? * FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID=MPA.ID " +
                "LEFT JOIN LIKES L ON FILMS.ID = L.FILM_ID " +
                "GROUP BY FILMS.ID, " +
                "L.USER_ID " +
                "ORDER BY COUNT(USER_ID) DESC";
        if (count != 0) {
            return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage,
                    likesDbStorage), count);
        } else {
            return jdbcTemplate.query(sql, new FilmRowMapper(genreDbStorage, mpaDbStorage, likesDbStorage), 10);
        }
    }

    @Override
    public void setFilmGenres(long filmId, List<Genre> genres) throws FilmNotFound {
        String sqlCheck = "SELECT COUNT(*) FROM FILMS_GENRES WHERE FILM_ID = ?";
        Integer check = jdbcTemplate.queryForObject(sqlCheck, Integer.class, filmId);
        if (check == 0) {
            for (Genre genre : genres) {
                String sqlInsert = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlInsert, filmId, genre.getId());
            }
        } else {
            String sqlDelete = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlDelete, filmId);
            for (Genre genre : genres) {
                String sqlMerge = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlMerge, filmId, genre.getId());
            }
        }
    }

    @Override
    public List<Genre> getFilmGenres(long filmId) throws FilmNotFound {
        String sqlSelect = "SELECT * FROM FILMS_GENRES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlSelect, new GenreRowMapper(), filmId);
    }

}