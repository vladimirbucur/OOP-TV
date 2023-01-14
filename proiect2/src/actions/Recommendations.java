package actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Genre;
import entities.Movie;
import entities.Notification;
import pootv.Database;
import pootv.Run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Recommendations extends Action {
    /**
     * The method that creates and returns an ObjectNode that is used for displaying the output
     *
     * @param error
     * @param run
     * @param objectMapper
     * @return
     */
    public ObjectNode actionOutput(final boolean error, final Run run,
                                   final ObjectMapper objectMapper) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (error) {
            objectNode.put("error", "Error");
            objectNode.putPOJO("currentMoviesList", new ArrayList<>());
            objectNode.put("currentUser", (JsonNode) null);

        } else {
            objectNode.put("error", (JsonNode) null);

            objectNode.put("currentMoviesList", (JsonNode) null);

            objectNode.set("currentUser", showUser(run.getCurrentUser()));
        }

        return objectNode;
    }

    /**
     * The method that performs the "Logout" action
     * After checking whether the last connected user exists and is premium, a top of
     * the genres appreciated by the user is created
     * After sorting the films from the database in descending order according to the total
     * number of likes, it is checked if there is a good film to be recommended, and otherwise
     * it is displayed suggestively
     * @param run
     * @param database
     * @param output
     * @param objectMapper
     */
    public void recomandation(final Run run, final Database database, final ArrayNode output,
                              final ObjectMapper objectMapper) {
        if (run.getCurrentUser() != null && run.getCurrentUser().getCredentials() != null) {
            if (run.getCurrentUser().isPremium()) {
                ArrayList<Genre> likedGenres = new ArrayList<>();
                decreasingLikedGenresTop(run, likedGenres);

                sortDecreasingDataBaseMovies(run, database);

                boolean isGoodToBeRecommended = checkIfExistsAFilmToBeRecommended(run, likedGenres);

                if (!isGoodToBeRecommended) {
                    Notification notification = new Notification();
                    notification.setMovieName("No recommendation");
                    notification.setMessage("Recommendation");
                    run.getCurrentUser().getNotifications().add(notification);
                }
                output.add(actionOutput(false, run, objectMapper));
            }
        }
    }

    /**
     * The method that returns true if a movie is found that is good to be recommended
     * and false otherwise
     * @param run
     * @param likedGenres
     * @return
     */
    private boolean checkIfExistsAFilmToBeRecommended(final Run run,
                                                      final ArrayList<Genre> likedGenres) {
        boolean isGoodToBeRecommended = false;
        exit:
        for (Genre likedgenre : likedGenres) {
            for (Movie movie : run.getCurrentMoviesList()) {
                if (isLikedGenre(likedgenre, movie.getGenres()) && !isWatched(movie,
                        run.getCurrentUser().getWatchedMovies())) {
                    isGoodToBeRecommended = true;
                    Notification notification = new Notification();
                    notification.setMovieName(movie.getName());
                    notification.setMessage("Recommendation");
                    run.getCurrentUser().getNotifications().add(notification);
                    break exit;
                }
            }
        }
        return isGoodToBeRecommended;
    }

    /**
     * The method that sorts the movies from the database in descending order according
     * to the total number of likes
     * @param run
     * @param database
     */
    private static void sortDecreasingDataBaseMovies(final Run run, final Database database) {
        run.setCurrentMoviesList(new ArrayList<>());
        for (Movie movie : database.getMovies()) {
            if (!movie.isBanned(run.getCurrentUser())) {
                run.getCurrentMoviesList().add(movie);
            }
        }

        Collections.sort(run.getCurrentMoviesList(), new Comparator<Movie>() {
            @Override
            public int compare(final Movie movie1, final Movie movie2) {
                return movie2.getNumLikes() - movie1.getNumLikes();
            }
        });
    }

    /**
     * The method that creates a top of the genres appreciated by the user, by sorting them
     * in descending order according to the number of likes
     * @param run
     * @param likedGenres
     */
    private static void decreasingLikedGenresTop(final Run run,
                                                 final ArrayList<Genre> likedGenres) {
        for (Movie movie : run.getCurrentUser().getLikedMovies()) {
            for (Genre genre : movie.getGenres()) {
                boolean isInLikedGenres = false;
                for (Genre likedGenre : likedGenres) {
                    if (likedGenre.getName().equals(genre.getName())) {
                        likedGenre.setNumberOfLikes(genre.getNumberOfLikes() + 1);
                        isInLikedGenres = true;
                    }
                }
                if (!isInLikedGenres) {
                    likedGenres.add(genre);
                }
            }
        }

        Collections.sort(likedGenres, new Comparator<Genre>() {
            @Override
            public int compare(final Genre genre1, final Genre genre2) {
                if (genre1.getNumberOfLikes() < genre2.getNumberOfLikes()) {
                    return 1;
                } else if (genre1.getNumberOfLikes() > genre2.getNumberOfLikes()) {
                    return -1;
                } else {
                    if (genre1.getName().compareTo(genre2.getName()) > 0) {
                        return 1;
                    } else if (genre1.getName().compareTo(genre2.getName()) < 0) {
                        return -1;
                    }
                }
                return 0;
            }
        });
    }

    /**
     * The method that checks if a genre is in the list of liked genres
     * @param genre
     * @param likedGenres
     * @return
     */
    private boolean isLikedGenre(final Genre genre, final ArrayList<Genre> likedGenres) {
        for (Genre likedGenre : likedGenres) {
            if (likedGenre.getName().equals(genre.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method that checks if a movie is in the list of watch movies
     * @param movie
     * @param watchedMovies
     * @return
     */
    private boolean isWatched(final Movie movie, final ArrayList<Movie> watchedMovies) {
        for (Movie watchedMovie : watchedMovies) {
            if (watchedMovie.getName().equals(movie.getName())) {
                return true;
            }
        }
        return false;
    }
}
