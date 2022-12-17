package pootv;

import actions.Login;
import actions.Logout;
import actions.Movies;
import actions.Register;
import actions.SeeDetails;
import actions.Upgrades;
import entities.Movie;
import entities.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionInput;
import fileio.Input;
import fileio.MovieInput;
import fileio.UserInput;

import java.util.ArrayList;

public final class Database {
    private ArrayList<User> users;
    private ArrayList<Movie> movies;  // the list of input movies
    private static Database instance = null;

    private Database() {
        this.setUsers(new ArrayList<>());
        this.setMovies(new ArrayList<>());
    }

    /**
     * Method for Singleton Pattern
     * @return
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * The method that populates the database with the movies and users received as input
     * @param input
     */
    public void construct(final Input input) {
        for (UserInput userInput : input.getUsers()) {
            this.getUsers().add(new User(userInput));
        }

        for (MovieInput movieInput : input.getMovies()) {
            this.getMovies().add(new Movie(movieInput));
        }
    }

    /**
     * The method that ensures that the database is empty at the beginning of each use
     */
    public void init() {
        this.getUsers().clear();
        this.getMovies().clear();
    }


    /**
     * The method that creates and returns an ObjectNode that is used for displaying the output
     * @param objectMapper
     * @return
     */
    public ObjectNode errorOutput(final ObjectMapper objectMapper) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("error", "Error");
        objectNode.putPOJO("currentMoviesList", new ArrayList<>());
        objectNode.put("currentUser", (JsonNode) null);

        return objectNode;
    }

    /**
     * The method that performs the "Log in" action regardless of whether it is of
     * the "change page" or "on page" type
     * @param run
     * @param actionInput
     * @param output
     * @param objectMapper
     */
    public void login(final Run run, final ActionInput actionInput, final ArrayNode output,
                      final ObjectMapper objectMapper) {
        Login login = new Login();
        if (actionInput.getType().equals("change page")) {
            if (run.getCurrentPage().getName().equals("unAuthPage")) {
                run.getCurrentPage().setName("login");
            } else {
                output.add(login.actionOutput(true, run, objectMapper));
            }
        } else {
            login.loginAction(this.getUsers(), run, output, actionInput, objectMapper);
        }
    }

    /**
     * The method that performs the "Register" action regardless of whether it is of
     * the "change page" or "on page" type
     * @param run
     * @param actionInput
     * @param output
     * @param objectMapper
     */
    public void register(final Run run, final ActionInput actionInput, final ArrayNode output,
                         final ObjectMapper objectMapper) {
        Register register = new Register();
        if (actionInput.getType().equals("change page")) {
            if (run.getCurrentPage().getName().equals("unAuthPage")) {
                run.getCurrentPage().setName("register");
            } else {
                output.add(register.actionOutput(true, run, objectMapper));
            }
        } else {
            register.registerAction(this.getUsers(), run, output, actionInput, objectMapper);
        }
    }

    /**
     * The method that performs the "Log out" action regardless of whether it is of
     * the "change page" or "on page" type
     * @param run
     * @param output
     * @param objectMapper
     */
    public void logout(final Run run, final ArrayNode output, final ObjectMapper objectMapper) {
        Logout logout = new Logout();
        logout.logoutAction(run, output, objectMapper);
    }

    /**
     * The method that performs the "Movie" action if it is of the "change page" type,
     * and if it is of the "on page" type, it performs "Search" or "Filter" depending on
     * what is received at the input
     * @param run
     * @param database
     * @param actionInput
     * @param output
     * @param objectMapper
     */
    public void movies(final Run run, final Database database, final ActionInput actionInput,
                       final ArrayNode output, final ObjectMapper objectMapper) {
        Movies moviesAction = new Movies();
        if (actionInput.getType().equals("change page")) {
            if (run.getCurrentPage().getName().equals("unAuthPage")) {
                output.add(moviesAction.actionOutput(true, run, objectMapper));
            } else {
                run.getCurrentPage().setName("movies");

                run.setCurrentMoviesList(new ArrayList<>());
                for (Movie movie : this.getMovies()) {
                    if (!movie.isBanned(run.getCurrentUser())) {
                        run.getCurrentMoviesList().add(movie);
                    }
                }

                output.add(moviesAction.actionOutput(false, run, objectMapper));
            }
        } else {
            switch (actionInput.getFeature()) {
                case "search" -> moviesAction.search(database, run, output, actionInput,
                        objectMapper);
                case "filter" -> moviesAction.filtres(database, run, output, actionInput,
                        objectMapper);
                default -> moviesAction.actionOutput(true, run, objectMapper);
            }
        }
    }

    /**
     * The method that performs the "See details" action if it is of the "change page" type,
     * and if it is of the "on page" type, it performs "Purchase", "Watch", "Like" or "Rate"
     * depending on what is received at the input
     * @param run
     * @param actionInput
     * @param output
     * @param objectMapper
     */
    public void seeDetails(final Run run, final ActionInput actionInput, final ArrayNode output,
                           final ObjectMapper objectMapper) {
        SeeDetails seeDetails = new SeeDetails();
        if (actionInput.getType().equals("change page")) {
            if (!run.getCurrentPage().getName().equals("movies")) {
                output.add(seeDetails.actionOutput(true, run, objectMapper));
            } else {
                boolean canSeeDetails = false;
                for (Movie movie : run.getCurrentMoviesList()) {
                    if (actionInput.getMovie().equals(movie.getName())) {
                        run.getCurrentPage().setName("seeDetails");
                        canSeeDetails = true;
                        run.setCurrentMovie(movie);
                        output.add(seeDetails.actionOutput(false, run, objectMapper));
                    }
                }
                if (!canSeeDetails) {
                    output.add(seeDetails.actionOutput(true, run, objectMapper));
                }
            }
        } else {
            switch (actionInput.getFeature()) {
                case "purchase" -> seeDetails.purchase(run, output, objectMapper);
                case "watch" -> seeDetails.watch(run, output, objectMapper);
                case "like" -> seeDetails.like(run, output, objectMapper);
                case "rate" -> seeDetails.rateTheMovie(run, output, actionInput, objectMapper);
                default -> seeDetails.actionOutput(true, run, objectMapper);
            }
        }
    }

    /**
     * The method that performs the "Upgrades" action if it is of the "change page" type,
     * and if it is of the "on page" type, it performs "Buy tokens" or "Buy premium account"
     * depending on what is received at the input
     * @param run
     * @param actionInput
     * @param output
     * @param objectMapper
     */
    public void upgrades(final Run run, final ActionInput actionInput, final ArrayNode output,
                         final ObjectMapper objectMapper) {
        Upgrades upgrades = new Upgrades();
        if (actionInput.getType().equals("change page")) {
            if (!run.getCurrentPage().getName().equals("seeDetails")
                    && !run.getCurrentPage().getName().equals("authPage")) {
                output.add(upgrades.actionOutput(true, run, objectMapper));
            } else {
                run.getCurrentPage().setName("upgrades");
            }
        } else {
            switch (actionInput.getFeature()) {
                case "buy tokens" -> upgrades.buyTokens(run, output, actionInput, objectMapper);
                case "buy premium account" -> upgrades.buyPremiumAccount(run, output,
                        objectMapper);
                default -> upgrades.actionOutput(true, run, objectMapper);
            }
        }
    }

    /**
     * @return
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * @param users
     */
    public void setUsers(final ArrayList<User> users) {
        this.users = users;
    }

    /**
     * @return
     */
    public ArrayList<Movie> getMovies() {
        return movies;
    }

    /**
     * @param movies
     */
    public void setMovies(final ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
