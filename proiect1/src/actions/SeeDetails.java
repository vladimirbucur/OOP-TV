package actions;

import entities.Movie;
import entities.User;
import pootv.Run;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionInput;

import java.util.ArrayList;

public class SeeDetails extends Action {
    static final int MAXRATE = 5;

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
            currentMoviesListOutput.add(movieOutput(run.getCurrentMovie(), objectMapper));
            objectNode.set("currentMoviesList", currentMoviesListOutput);

            objectNode.putPOJO("currentUser", new User(run.getCurrentUser()));
        }

        return objectNode;
    }

    /**
     * The method that performs the "Purchase" action
     * After checking if the current page is "See details", if the current user is a premium one
     * and has free movies that he can buy, he uses this advantage, if he doesn't pay 2 tokens
     * and the movie is added to the list of movies bought by him.
     * At the end, the corresponding message is sent to the output
     * @param run
     * @param output
     * @param objectMapper
     */
    public void purchase(final Run run, final ArrayNode output, final ObjectMapper objectMapper) {
        if (!run.getCurrentPage().getName().equals("seeDetails")) {
            output.add(actionOutput(true, run, objectMapper));
        } else {
            if (run.getCurrentUser().isPremium() && run.getCurrentUser().
                    getNumFreePremiumMovies() > 0) {
                run.getCurrentPage().setName("purchase");
                run.getCurrentUser().setNumFreePremiumMovies(run.getCurrentUser().
                        getNumFreePremiumMovies() - 1);
                run.getCurrentUser().getPurchasedMovies().add(run.getCurrentMovie());
            } else {
                if (run.getCurrentUser().getTokensCount() > 1) {
                    run.getCurrentUser().setTokensCount(run.getCurrentUser().getTokensCount() - 2);
                    run.getCurrentUser().getPurchasedMovies().add(run.getCurrentMovie());
                    run.getCurrentPage().setName("purchase");
                } else {
                    output.add(actionOutput(true, run, objectMapper));
                    return;
                }
            }
            output.add(actionOutput(false, run, objectMapper));
        }
    }

    /**
     * The method that performs the "Watch" action
     * After checking if the movie he wants to see is in the list of purchased movies,
     * the movie is added to the list of watched movies
     * At the end, the corresponding message is sent to the output
     * @param run
     * @param output
     * @param objectMapper
     */
    public void watch(final Run run, final ArrayNode output, final ObjectMapper objectMapper) {
        if (!run.getCurrentPage().getName().equals("purchase")) {
            output.add(actionOutput(true, run, objectMapper));
        } else {
            if (run.getCurrentUser().getPurchasedMovies().contains(run.getCurrentMovie())) {
                run.getCurrentPage().setName("watch");
                run.getCurrentUser().getWatchedMovies().add(run.getCurrentMovie());
                output.add(actionOutput(false, run, objectMapper));
            } else {
                output.add(actionOutput(true, run, objectMapper));
            }
        }
    }

    /**
     * The method that performs the "Like" action
     * After checking if the movie he wants to see is in the list of watched movies,
     * the movie is added to the list of appreciated movies and the number of likes
     * of the movie increases
     * At the end, the corresponding message is sent to the output
     * @param run
     * @param output
     * @param objectMapper
     */
    public void like(final Run run, final ArrayNode output, final ObjectMapper objectMapper) {
        if (!run.getCurrentPage().getName().equals("watch")) {
            output.add(actionOutput(true, run, objectMapper));
        } else {
            if (run.getCurrentUser().getWatchedMovies().contains(run.getCurrentMovie())) {
                run.getCurrentPage().setName("like");
                run.getCurrentUser().getLikedMovies().add(run.getCurrentMovie());
                run.getCurrentMovie().setNumLikes(run.getCurrentMovie().getNumLikes() + 1);

                output.add(actionOutput(false, run, objectMapper));
            } else {
                output.add(actionOutput(true, run, objectMapper));
            }

        }
    }

    /**
     * The method that performs the "Rate the film" action
     * After checking if the movie he wants to see is in the list of watched movies,
     * the movie is added to the list of movies to which he has given a rating and the rating
     * of the movie is recalculated and then updated
     * At the end, the corresponding message is sent to the output
     * @param run
     * @param output
     * @param actionInput
     * @param objectMapper
     */
    public void rateTheMovie(final Run run, final ArrayNode output, final ActionInput actionInput,
                             final ObjectMapper objectMapper) {
        if (!run.getCurrentPage().getName().equals("watch")) {
            output.add(actionOutput(true, run, objectMapper));
        } else {
            if (run.getCurrentUser().getWatchedMovies().contains(run.getCurrentMovie())) {
                if (actionInput.getRate() < 0 || actionInput.getRate() > MAXRATE) {
                    output.add(actionOutput(true, run, objectMapper));
                } else {
                    run.getCurrentPage().setName("rateTheMovie");
                    run.getCurrentUser().getRatedMovies().add(run.getCurrentMovie());
                    run.getCurrentMovie().getRatings().add(actionInput.getRate());
                    run.getCurrentMovie().setNumRatings(run.getCurrentMovie().getNumRatings() + 1);
                    int sumOfRatings = 0;
                    for (Integer rating : run.getCurrentMovie().getRatings()) {
                        sumOfRatings += rating;
                    }
                    run.getCurrentMovie().setRating((double) sumOfRatings / run.getCurrentMovie().
                            getNumRatings());

                    output.add(actionOutput(false, run, objectMapper));
                }
            } else {
                output.add(actionOutput(true, run, objectMapper));
            }
        }
    }
}
