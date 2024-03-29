package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.FilmNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
public interface FilmStorage {

    Film addFilm(Film Film);

    List<Film> getAllFilms();

    Film getFilmById(long id) throws FilmNotFound;

    Film updateFilm(Film film) throws FilmNotFound;

    void deleteFilm(long filmId) throws FilmNotFound;

    List<Film> getPopularFilms(int count);

    void setFilmGenres(long filmId, List<Genre> genres);

    List<Genre> getFilmGenres(long filmId);

}
