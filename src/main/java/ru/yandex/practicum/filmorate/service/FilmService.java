package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public void addFilm(Film film) throws FilmNotFound {
        filmStorage.addFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long filmId) throws FilmNotFound {
        return filmStorage.getFilmById(filmId);
    }

    public Film updateFilm(Film film) throws FilmNotFound {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(long filmId) throws FilmNotFound {
        filmStorage.deleteFilm(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public void setFilmGenres(long filmId, List<Genre> genres) {
        filmStorage.setFilmGenres(filmId, genres);
    }

    public List<Genre> getFilmGenres(long filmId) {
        return filmStorage.getFilmGenres(filmId);
    }

    public List<Film> getAllFilmsByDirector(int directorId, String sortBy) {
        return filmStorage.getAllFilmsByDirector(directorId, sortBy);
    }

    public Collection<Film> getSearchFilmsByTittleAndDirector(String query) {
        return filmStorage.getSearchFilmsByTittleAndDirector(query);
    }

    public Collection<Film> getSearchFilmsByTittle(String query) {
        return filmStorage.getSearchFilmsByTittle(query);
    }

    public Collection<Film> getSearchFilmsByDirector(String query) {
        return filmStorage.getSearchFilmsByDirector(query);
    }

    public List<Film> getPopularByGenre(int genreId) {
        return filmStorage.getPopularByGenre(genreId);
    }

    public List<Film> getPopularFilmsByYear(String year) {
        return filmStorage.getPopularFilmsByYear(year);
    }

    public List<Film> getPopularFilmsByGenreAndYear(int count, int genreId, String year) {
        return filmStorage.getPopularFilmsByGenreAndYear(count, genreId, year);
    }

    public Collection<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }
}
