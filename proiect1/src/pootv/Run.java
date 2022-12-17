package pootv;

import entities.Movie;
import entities.Page;
import entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionInput;
import fileio.Input;

import java.util.ArrayList;

public final class Run {
    private Page currentPage;
    private User currentUser;
    private ArrayList<Movie> currentMoviesList;  // the list that is print in output
    private Movie currentMovie;  // movie on which the purchase, watch,
                                 // like and rate the movie actions can be done

    private static Run instance = null;

    private Run() {
    }

    /**
     * Method for Singleton Pattern
     * @return
     */
    public static Run getInstance() {
        if (instance == null) {
            instance = new Run();
        }
        return instance;
    }

    /**
     * The entry point for solving the program
     * The method that populates the database with users and movies received from the input,
     * and checks which commands are received in the command line and then calls
     * the related methods
     * @param inputData
     * @param output
     * @param objectMapper
     */
    public void database(final Input inputData, final ArrayNode output, final ObjectMapper
            objectMapper) {
        Database database = Database.getInstance();
        database.init();
        database.construct(inputData);

        this.setCurrentPage(new Page());
        this.getCurrentPage().setName("unAuthPage");
        this.setCurrentUser(new User());
        this.setCurrentMoviesList(new ArrayList<>());
        this.setCurrentMovie(new Movie());

        ArrayList<ActionInput> actions = inputData.getActions();
        for (ActionInput actionInput : actions) {
            switch (actionInput.getType()) {
                case "change page" -> {
                    switch (actionInput.getPage()) {
                        case "login" -> database.login(this, actionInput, output, objectMapper);
                        case "register" -> database.register(this, actionInput, output,
                                objectMapper);
                        case "logout" -> database.logout(this, output, objectMapper);
                        case "movies" -> database.movies(this, database, actionInput, output,
                                objectMapper);
                        case "see details" -> database.seeDetails(this, actionInput, output,
                                objectMapper);
                        case "upgrades" -> database.upgrades(this, actionInput, output,
                                objectMapper);
                        default -> output.add(database.errorOutput(objectMapper));
                    }
                }
                default -> {
                    switch (actionInput.getFeature()) {
                        case "login" -> database.login(this, actionInput, output, objectMapper);
                        case "register" -> database.register(this, actionInput, output,
                                objectMapper);
                        case "search" -> database.movies(this, database, actionInput, output,
                                objectMapper);
                        case "filter" -> database.movies(this, database, actionInput, output,
                                objectMapper);
                        case "purchase" -> database.seeDetails(this, actionInput, output,
                                objectMapper);
                        case "watch" -> database.seeDetails(this, actionInput, output,
                                objectMapper);
                        case "like" -> database.seeDetails(this, actionInput, output,
                                objectMapper);
                        case "rate" -> database.seeDetails(this, actionInput, output,
                                objectMapper);
                        case "buy tokens" -> database.upgrades(this, actionInput, output,
                                objectMapper);
                        case "buy premium account" -> database.upgrades(this, actionInput, output,
                                objectMapper);
                        default -> output.add(database.errorOutput(objectMapper));
                    }
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public Page getCurrentPage() {
        return currentPage;
    }

    /**
     *
     * @param currentPage
     */
    public void setCurrentPage(final Page currentPage) {
        this.currentPage = currentPage;
    }

    /**
     *
     * @return
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     *
     * @param currentUser
     */
    public void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     *
     * @return
     */
    public ArrayList<Movie> getCurrentMoviesList() {
        return currentMoviesList;
    }

    /**
     *
     * @param currentMoviesList
     */
    public void setCurrentMoviesList(final ArrayList<Movie> currentMoviesList) {
        this.currentMoviesList = currentMoviesList;
    }

    /**
     *
     * @return
     */
    public Movie getCurrentMovie() {
        return currentMovie;
    }

    /**
     *
     * @param currentMovie
     */
    public void setCurrentMovie(final Movie currentMovie) {
        this.currentMovie = currentMovie;
    }
}
