package actions;

import entities.Movie;
import entities.User;
import pootv.Database;
import pootv.Run;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Movies extends Action {

    /**
     * The method that creates and returns an ObjectNode for a movie
     * @param movie
     * @param objectMapper
     * @return
     */
    private ObjectNode movieOutput(final Movie movie, final ObjectMapper objectMapper) {
        ObjectNode movieNode = objectMapper.createObjectNode();
        movieNode.put("name", movie.getName());
        movieNode.put("year", movie.getYear());
        movieNode.put("duration", movie.getDuration());

        ArrayNode genreOutput = objectMapper.createArrayNode();
        for (String genre : movie.getGenres()) {
            genreOutput.add(genre);
        }
        movieNode.set("genres", genreOutput);

        ArrayNode actorsOutput = objectMapper.createArrayNode();
        for (String actor : movie.getActors()) {
            actorsOutput.add(actor);
        }
        movieNode.set("actors", actorsOutput);

        ArrayNode countriesBannedOutput = objectMapper.createArrayNode();
        for (String countryBanned : movie.getCountriesBanned()) {
            countriesBannedOutput.add(countryBanned);
        }
        movieNode.set("countriesBanned", countriesBannedOutput);

        movieNode.put("numLikes", movie.getNumLikes());
        movieNode.put("rating", movie.getRating());
        movieNode.put("numRatings", movie.getNumRatings());

        return movieNode;
    }

    /**
     * The method that creates and returns an ObjectNode that is used for displaying the output
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

            ArrayNode currentMoviesListOutput = objectMapper.createArrayNode();
            for (Movie movie : run.getCurrentMoviesList()) {
                currentMoviesListOutput.add(movieOutput(movie, objectMapper));
            }
            objectNode.set("currentMoviesList", currentMoviesListOutput);

            objectNode.putPOJO("currentUser", new User(run.getCurrentUser()));
        }

        return objectNode;
    }

    /**
     * The method that performs the "Search" action
     * After checking if the current page is "Movies", the database searches for the movies
     * that start with what is received at the input
     * At the end, the corresponding message is sent to the output
     * @param database
     * @param run
     * @param output
     * @param actionInput
     * @param objectMapper
     */
    public void search(final Database database, final Run run, final ArrayNode output,
                       final ActionInput actionInput, final ObjectMapper objectMapper) {
        if (!run.getCurrentPage().getName().equals("movies")) {
            output.add(actionOutput(true, run, objectMapper));
        } else {
            run.setCurrentMoviesList(new ArrayList<>());
            for (Movie movie : database.getMovies()) {
                if (movie.getName().startsWith(actionInput.getStartsWith())
                        && !movie.isBanned(run.getCurrentUser())) {
                    run.getCurrentMoviesList().add(movie);
                }
            }
            output.add(actionOutput(false, run, objectMapper));
        }
    }

    /**
     * The method that performs the "Filtres" action
     * The movies that contain the actors and genres specified in the input are searched and
     * then filtered according to duration and rating
     * At the end, the corresponding message is sent to the output
     * @param database
     * @param run
     * @param output
     * @param actionInput
     * @param objectMapper
     */
    public void filtres(final Database database, final Run run, final ArrayNode output,
                        final ActionInput actionInput, final ObjectMapper objectMapper) {
        if (!run.getCurrentPage().getName().equals("movies")) {
            output.add(actionOutput(true, run, objectMapper));
        } else {
            if (actionInput.getFilters().getContains() != null) {
                contains(database, run, actionInput);
            }
            Collections.sort(run.getCurrentMoviesList(), new Comparator<Movie>() {
                @Override
                public int compare(final Movie movie1, final Movie movie2) {
                    if (actionInput.getFilters().getSort() != null) {
                        if (actionInput.getFilters().getSort().getDuration() != null
                                && actionInput.getFilters().getSort().getRating() != null) {
                            Integer compareCodeDurationAndRating = sortDurationAndRating(movie1,
                                    movie2, actionInput);
                            if (compareCodeDurationAndRating != null) {
                                return compareCodeDurationAndRating;
                            }
                        } else if (actionInput.getFilters().getSort().getDuration() != null) {
                            Integer compareCodeDuration = sortDuration(movie1, movie2,
                                    actionInput);
                            if (compareCodeDuration != null) {
                                return compareCodeDuration;
                            }
                        } else if (actionInput.getFilters().getSort().getRating() != null) {
                            Integer compareCodeRating = sortRating(movie1, movie2,
                                    actionInput);
                            if (compareCodeRating != null) {
                                return compareCodeRating;
                            }
                        }
                    }

                    return 0;
                }
            });

            output.add(actionOutput(false, run, objectMapper));
        }
    }

    /**
     * The method that returns 1, 0 or -1 for the score depending on the duration and rating of
     * the 2 compared movies
     * @param movie1
     * @param movie2
     * @param actionInput
     * @return
     */
    private static Integer sortDurationAndRating(final Movie movie1, final Movie movie2,
                                                 final ActionInput actionInput) {
        if (actionInput.getFilters().getSort().getDuration().equals("increasing")) {
            if (movie1.getDuration() > movie2.getDuration()) {
                return 1;
            } else if (movie1.getDuration() == movie2.getDuration()) {
                Integer compareCode = equalDurations(movie1, movie2, actionInput);
                if (compareCode != null) {
                    return compareCode;
                }
            } else if (movie1.getDuration() < movie2.getDuration()) {
                return -1;
            }
        } else if (actionInput.getFilters().getSort().getDuration().equals("decreasing")) {
            if (movie1.getDuration() < movie2.getDuration()) {
                return 1;
            } else if (movie1.getDuration() == movie2.getDuration()) {
                Integer compareCode = equalDurations(movie1, movie2, actionInput);
                if (compareCode != null) {
                    return compareCode;
                }
            } else if (movie1.getDuration() > movie2.getDuration()) {
                return -1;
            }
        }
        return null;
    }

    /**
     * The method that returns 1, 0 or -1 for the score depending on the rating of
     * the 2 compared movies
     * @param movie1
     * @param movie2
     * @param actionInput
     * @return
     */
    private static Integer sortRating(final Movie movie1, final Movie movie2,
                                      final ActionInput actionInput) {
        if (actionInput.getFilters().getSort().getRating().equals("increasing")) {
            if (movie1.getRating() > movie2.getRating()) {
                return 1;
            } else if (movie1.getRating() == movie2.getRating()) {
                return 0;
            } else {
                return -1;
            }
        }
        if (actionInput.getFilters().getSort().getRating().equals("decreasing")) {
            if (movie1.getRating() < movie2.getRating()) {
                return 1;
            } else if (movie1.getRating() == movie2.getRating()) {
                return 0;
            } else {
                return -1;
            }
        }
        return null;
    }

    /**
     * The method that returns 1, 0 or -1 for the score depending on the duration of
     * the 2 compared movies
     * @param movie1
     * @param movie2
     * @param actionInput
     * @return
     */
    private static Integer sortDuration(final Movie movie1, final Movie movie2,
                                        final ActionInput actionInput) {
        if (actionInput.getFilters().getSort().getDuration().equals("increasing")) {
            return movie1.getDuration() - movie2.getDuration();
        }
        if (actionInput.getFilters().getSort().getDuration().equals("decreasing")) {
            return movie2.getDuration() - movie1.getDuration();
        }
        return null;
    }

    /**
     * The method that returns 1, 0 or -1 for the score in case the duration is equal and
     * the rating of the 2 movies ends up being compared
     * @param movie1
     * @param movie2
     * @param actionInput
     * @return
     */
    private static Integer equalDurations(final Movie movie1, final Movie movie2,
                                          final ActionInput actionInput) {
        if (actionInput.getFilters().getSort().getRating().equals("increasing")) {
            if (movie1.getRating() > movie2.getRating()) {
                return 1;
            } else if (movie1.getRating() == movie2.getRating()) {
                return 0;
            } else {
                return -1;
            }
        } else if (actionInput.getFilters().getSort().getRating().equals("decreasing")) {
            if (movie1.getRating() < movie2.getRating()) {
                return 1;
            } else if (movie1.getRating() == movie2.getRating()) {
                return 0;
            } else {
                return -1;
            }
        }
        return null;
    }

    /**
     * The method that filters movies according to the actors and genres they contain and
     * what is required for the input
     * @param database
     * @param run
     * @param actionInput
     */
    private static void contains(final Database database, final Run run,
                                 final ActionInput actionInput) {
        run.setCurrentMoviesList(new ArrayList<>());
        for (Movie movie : database.getMovies()) {
            if (!movie.isBanned(run.getCurrentUser())) {
                if (actionInput.getFilters().getContains().getActors() != null
                        && actionInput.getFilters().getContains().getGenres() != null) {
                    if (movie.containsActors(actionInput.getFilters().getContains().getActors())
                            && movie.containsGenres(actionInput.getFilters().getContains().
                            getGenres())) {
                        run.getCurrentMoviesList().add(movie);
                    }
                } else if (actionInput.getFilters().getContains().getActors() != null) {
                    if (movie.containsActors(actionInput.getFilters().getContains().getActors())) {
                        run.getCurrentMoviesList().add(movie);
                    }
                } else if (actionInput.getFilters().getContains().getGenres() != null) {
                    if (movie.containsGenres(actionInput.getFilters().getContains().getGenres())) {
                        run.getCurrentMoviesList().add(movie);
                    }
                }
            }
        }
    }
}
