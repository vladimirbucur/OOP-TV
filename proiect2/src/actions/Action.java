package actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Genre;
import entities.Movie;
import entities.Notification;
import entities.User;
import pootv.Run;

/**
 * The method that sends to the output what must be displayed.
 * This is to be implemented in each class of action.
 */
public abstract class Action {
    /**
     * The method to be inherited and which one creates and returns an ObjectNode
     * that is used for displaying the output
     * @param error
     * @param run
     * @param objectMapper
     * @return
     */
    public abstract ObjectNode actionOutput(boolean error, Run run, ObjectMapper objectMapper);

    /**
     * The method that creates and returns an ObjectNode for a movie
     * @param movie
     * @param objectMapper
     * @return
     */
    public ObjectNode movieOutput(final Movie movie, final ObjectMapper objectMapper) {
        ObjectNode movieNode = objectMapper.createObjectNode();
        movieNode.put("name", movie.getName());
        movieNode.put("year", movie.getYear());
        movieNode.put("duration", movie.getDuration());

        ArrayNode genreOutput = objectMapper.createArrayNode();
        for (Genre genre : movie.getGenres()) {
            genreOutput.add(genre.getName());
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
     * The method that creates and returns an ObjectNode for a notification
     * @param notification
     * @return
     */
    public ObjectNode showNotification(final Notification notification) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode notificationOutput = objectMapper.createObjectNode();

        notificationOutput.put("movieName", notification.getMovieName());
        notificationOutput.put("message", notification.getMessage());

        return notificationOutput;
    }

    /**
     * The method that creates and returns an ObjectNode for a user
     * @param user
     * @return
     */
    public ObjectNode showUser(final User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode userOutput = null;

        if (user.getCredentials().getName() != null) {
            userOutput = objectMapper.createObjectNode();
            ObjectNode credentialsOutput = objectMapper.createObjectNode();
            credentialsOutput.put("name", user.getCredentials().getName());
            credentialsOutput.put("password", user.getCredentials().getPassword());
            credentialsOutput.put("accountType", user.getCredentials().getAccountType());
            credentialsOutput.put("country", user.getCredentials().getCountry());
            credentialsOutput.put("balance", String.valueOf(user.getCredentials().getBalance()));
            userOutput.set("credentials", credentialsOutput);
            userOutput.put("tokensCount", user.getTokensCount());
            userOutput.put("numFreePremiumMovies", user.getNumFreePremiumMovies());

            ArrayNode purchasedOutput = objectMapper.createArrayNode();
            for (Movie movie : user.getPurchasedMovies()) {
                purchasedOutput.add(movieOutput(movie, objectMapper));
            }
            userOutput.set("purchasedMovies", purchasedOutput);

            ArrayNode watchedOutput = objectMapper.createArrayNode();
            for (Movie movie : user.getWatchedMovies()) {
                watchedOutput.add(movieOutput(movie, objectMapper));
            }
            userOutput.set("watchedMovies", watchedOutput);

            ArrayNode likedOutput = objectMapper.createArrayNode();
            for (Movie movie : user.getLikedMovies()) {
                likedOutput.add(movieOutput(movie, objectMapper));
            }
            userOutput.set("likedMovies", likedOutput);

            ArrayNode ratedOutput = objectMapper.createArrayNode();
            for (Movie movie : user.getRatedMovies()) {
                ratedOutput.add(movieOutput(movie, objectMapper));
            }
            userOutput.set("ratedMovies", ratedOutput);

            ArrayNode notificationsOutput = objectMapper.createArrayNode();
            for (Notification notification : user.getNotifications()) {
                notificationsOutput.add(showNotification(notification));
            }
            userOutput.set("notifications", notificationsOutput);
        }

        return userOutput;
    }
}
