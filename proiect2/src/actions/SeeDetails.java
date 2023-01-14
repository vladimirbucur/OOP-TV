package actions;

import entities.Genre;

import pootv.Run;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionInput;
public class SeeDetails extends Action {
    static final int MAXRATE = 5;

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
            ArrayNode curretMovieList = new ObjectMapper().createArrayNode();
            objectNode.put("currentMoviesList", curretMovieList);
            objectNode.put("currentUser", (JsonNode) null);

        } else {
            objectNode.put("error", (JsonNode) null);

            ArrayNode currentMoviesListOutput = objectMapper.createArrayNode();
            currentMoviesListOutput.add(movieOutput(run.getCurrentMovie(), objectMapper));
            objectNode.set("currentMoviesList", currentMoviesListOutput);

            objectNode.set("currentUser", showUser(run.getCurrentUser()));
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
        } else if (run.getCurrentUser().getPurchasedMovies().contains(run.getCurrentMovie())) {
            output.add(actionOutput(true, run, objectMapper));
            run.getCurrentPage().setName("purchase");
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
        } else if (run.getCurrentUser().getWatchedMovies().contains(run.getCurrentMovie())) {
            output.add(actionOutput(false, run, objectMapper));
            run.getCurrentPage().setName("watch");
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
        } else if (run.getCurrentUser().getLikedMovies().contains(run.getCurrentMovie())) {
            output.add(actionOutput(false, run, objectMapper));
        } else {
            if (run.getCurrentUser().getWatchedMovies().contains(run.getCurrentMovie())) {
                run.getCurrentUser().getLikedMovies().add(run.getCurrentMovie());
                run.getCurrentMovie().setNumLikes(run.getCurrentMovie().getNumLikes() + 1);

                for (Genre genre : run.getCurrentMovie().getGenres()) {
                    genre.setNumberOfLikes(genre.getNumberOfLikes() + 1);
                }

                output.add(actionOutput(false, run, objectMapper));
            } else {
                output.add(actionOutput(true, run, objectMapper));
            }

        }
    }

    /**
     * The method that updates the rating if the current user puts a different rating
     * than the one initially put
     * @param run
     * @param actionInput
     */
    private static void updateTheRating(final Run run, final ActionInput actionInput) {
        run.getCurrentMovie().getHashMapRating().put(run.getCurrentUser().getCredentials().
                getName(), Integer.valueOf(actionInput.getRate()));
        int sumOfRatings = 0;
        for (Integer rating : run.getCurrentMovie().getHashMapRating().values()) {
            sumOfRatings += rating;
        }
        run.getCurrentMovie().setRating((double) sumOfRatings / run.getCurrentMovie().
                getNumRatings());
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
        } else if (run.getCurrentUser().getRatedMovies().contains(run.getCurrentMovie())) {
            updateTheRating(run, actionInput);

            output.add(actionOutput(false, run, objectMapper));
        } else {
            if (run.getCurrentUser().getWatchedMovies().contains(run.getCurrentMovie())) {
                if (actionInput.getRate() < 0 || actionInput.getRate() > MAXRATE) {
                    output.add(actionOutput(true, run, objectMapper));
                } else {
                    run.getCurrentUser().getRatedMovies().add(run.getCurrentMovie());
                    run.getCurrentMovie().setNumRatings(run.getCurrentMovie().getNumRatings() + 1);
                    updateTheRating(run, actionInput);

                    output.add(actionOutput(false, run, objectMapper));
                }
            } else {
                output.add(actionOutput(true, run, objectMapper));
            }
        }
    }

    /**
     * The method that performs the "Subscribe" action
     * After checking if the current page is See Details and that the genre to which
     * he user wants to subscribe is in the list of genres of the current movie,
     * it is added to the list of Subscribed Movies of the current user
     * @param run
     * @param actionInput
     * @param output
     * @param objectMapper
     */
    public void subscribe(final Run run, final ActionInput actionInput, final ArrayNode output,
                          final ObjectMapper objectMapper) {
        boolean isInGenresOfCurrentMovies = false;
        for (Genre genre : run.getCurrentMovie().getGenres()) {
            if (genre.getName().equals(actionInput.getSubscribedGenre())) {
                isInGenresOfCurrentMovies = true;
            }
        }

        if (!run.getPagesHistory().peek().getName().equals("seeDetails")) {
            output.add(actionOutput(true, run, objectMapper));
        } else if (!isInGenresOfCurrentMovies) {
            output.add(actionOutput(true, run, objectMapper));
        } else if (run.getCurrentUser().getSubscribedGenres().contains(actionInput.
                getSubscribedGenre())) {
            output.add(actionOutput(true, run, objectMapper));
        } else {
            run.getCurrentUser().getSubscribedGenres().add(actionInput.getSubscribedGenre());
        }
    }
}
