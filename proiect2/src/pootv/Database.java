package pootv;

import actions.ActionFactory;
import actions.Back;
import actions.Login;
import actions.Logout;
import actions.Movies;
import actions.Register;
import actions.SeeDetails;
import actions.Upgrades;
import entities.Movie;
import entities.Page;
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
import java.util.Observable;

public final class Database  extends Observable {
    private ArrayList<User> users;
    private ArrayList<Movie> movies;  // the database's movies
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
        for (User user : this.getUsers()) {
            addObserver(user);
        }

        for (MovieInput movieInput : input.getMovies()) {
            this.getMovies().add(new Movie.Builder().fromMovieInput(movieInput).build());
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
        ActionFactory actionFactory = new ActionFactory();
        Login login = (Login) actionFactory.createAction(ActionFactory.ActionType.Login);
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
        ActionFactory actionFactory = new ActionFactory();
        Register register = (Register) actionFactory.createAction(
                ActionFactory.ActionType.Register);
        if (actionInput.getType().equals("change page")) {
            if (run.getCurrentPage().getName().equals("unAuthPage")) {
                run.getCurrentPage().setName("register");
            } else {
                output.add(register.actionOutput(true, run, objectMapper));
            }
        } else {
            register.registerAction(this.getUsers(), run, output, actionInput, objectMapper);
            addObserver(users.get(users.size() - 1));
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
        ActionFactory actionFactory = new ActionFactory();
        Logout logout = (Logout) actionFactory.createAction(ActionFactory.ActionType.Logout);
        logout.logoutAction(run, output, objectMapper);
    }

    /**
     * The method that navigates to the Homepage page
     * @param run
     * @param output
     * @param objectMapper
     */
    public void changePageAuthPage(final Run run, final ArrayNode output,
                                   final ObjectMapper objectMapper) {
        if (run.getCurrentPage().equals("movies")
                || run.getCurrentPage().equals("seeDetails")
                || run.getCurrentPage().equals("upgrades")) {
            run.getCurrentPage().setName("authPage");

            Page page = new Page();
            page.setName("authPage");
            run.getPagesHistory().push(page);
        } else {
            output.add(this.errorOutput(objectMapper));
        }
    }

    /**
     * The method that performs the "Movie" action if it is of the "change page" type,
     * and if it is of the "on page" type, it performs "Search" or "Filter" depending on
     * what is received at the input
     * @param run
     * @param actionInput
     * @param output
     * @param objectMapper
     */
    public void movies(final Run run, final ActionInput actionInput,
                       final ArrayNode output, final ObjectMapper objectMapper) {
        ActionFactory actionFactory = new ActionFactory();
        Movies moviesAction = (Movies) actionFactory.createAction(
                ActionFactory.ActionType.Movies);
        if (actionInput.getType().equals("change page")) {
            if (run.getCurrentPage().getName().equals("unAuthPage")) {
                output.add(moviesAction.actionOutput(true, run, objectMapper));
            } else {
                run.getCurrentPage().setName("movies");

                Page page = new Page();
                page.setName("movies");
                run.getPagesHistory().push(page);

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
                case "search" -> moviesAction.search(this, run, output, actionInput,
                        objectMapper);
                case "filter" -> moviesAction.filtres(this, run, output, actionInput,
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
        ActionFactory actionFactory = new ActionFactory();
        SeeDetails seeDetails = (SeeDetails) actionFactory.createAction(
                ActionFactory.ActionType.SeeDetails);
        if (actionInput.getType().equals("change page")) {
            if (!run.getCurrentPage().getName().equals("movies")
                    && !run.getPagesHistory().peek().getName().equals("seeDetails")) {
                output.add(seeDetails.actionOutput(true, run, objectMapper));
            } else {
                boolean canSeeDetails = false;
                for (Movie movie : run.getCurrentMoviesList()) {
                    if (actionInput.getMovie().equals(movie.getName())) {
                        run.getCurrentPage().setName("seeDetails");

                        Page page = new Page();
                        page.setName("seeDetails");
                        run.getPagesHistory().push(page);

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
                case "subscribe" -> seeDetails.subscribe(run, actionInput, output, objectMapper);
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
        ActionFactory actionFactory = new ActionFactory();
        Upgrades upgrades = (Upgrades) actionFactory.createAction(
                ActionFactory.ActionType.Upgrades);
        if (actionInput.getType().equals("change page")) {
            if (!run.getCurrentPage().getName().equals("seeDetails")
                    && !run.getCurrentPage().getName().equals("authPage")
                    && !run.getPagesHistory().peek().getName().equals("upgrades")) {
                output.add(upgrades.actionOutput(true, run, objectMapper));
            } else {
                run.getCurrentPage().setName("upgrades");

                Page page = new Page();
                page.setName("upgrades");
                run.getPagesHistory().push(page);
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
     * Method that performs the "Back" action
     * @param run
     * @param output
     * @param objectMapper
     */
    public void back(final Run run, final ArrayNode output, final ObjectMapper objectMapper) {
        ActionFactory actionFactory = new ActionFactory();
        Back back = (Back) actionFactory.createAction(
                ActionFactory.ActionType.Back);
        back.backAction(run, this, output, objectMapper);
    }

    /**
     * Method that checks if a movie is in a list of movies
     * @param movieName
     * @param movieArrayList
     * @return
     */
    private boolean movieIsInArrayWithMovies(final String movieName,
                                             final ArrayList<Movie> movieArrayList) {
        boolean isInArray = false;
        for (Movie movie : movieArrayList) {
            if (movie.getName().equals(movieName)) {
                isInArray = true;
            }
        }
        return isInArray;
    }

    /**
     * Method that remove a movie method is in a list of movies
     * @param movieName
     * @param movieArrayList
     */
    private void removeMovieInArrayWithMovies(final String movieName,
                                              final ArrayList<Movie> movieArrayList) {
        for (int i = 0; i < movieArrayList.size(); i++) {
            if (movieArrayList.get(i).getName().equals(movieName)) {
                movieArrayList.remove(movieArrayList.get(i));
                i = i - 1;
            }
        }
    }

    /**
     * The method that performs the "Add" action
     * After checking whether the movie that is to be added is no longer in the database,
     * all users who are subscribed to at least one of the movie's genres, and who are not
     * in one of the banned countries of the movie's
     * @param actionInput
     * @param output
     * @param objectMapper
     */
    public void add(final ActionInput actionInput, final ArrayNode output,
                    final ObjectMapper objectMapper) {
        if (this.movieIsInArrayWithMovies(actionInput.getAddedMovie().getName(),
                this.getMovies())) {
            output.add(this.errorOutput(objectMapper));
            return;
        }

        this.getMovies().add(new Movie.Builder().fromMovieInput(
                actionInput.getAddedMovie()).build());
        setChanged();
        notifyObservers(actionInput);
    }

    /**
     * The method that performs the "Delete" action
     * After checking whether the movie that is to be deleted is in the database,
     * it is deleted and all users who are subscribed to at least one of the genres
     * of the movie, and who are not in one of the movie's banned countries, are notified
     * @param actionInput
     * @param output
     * @param objectMapper
     */
    public void delete(final ActionInput actionInput, final ArrayNode output,
                    final ObjectMapper objectMapper) {

        if (!this.movieIsInArrayWithMovies(actionInput.getDeletedMovie(), this.getMovies())) {
            output.add(this.errorOutput(objectMapper));
            return;
        }

        for (User user : this.getUsers()) {
            if (this.movieIsInArrayWithMovies(actionInput.getDeletedMovie(),
                    user.getPurchasedMovies())) {
                this.removeMovieInArrayWithMovies(actionInput.getDeletedMovie(),
                        user.getWatchedMovies());
                this.removeMovieInArrayWithMovies(actionInput.getDeletedMovie(),
                        user.getLikedMovies());
                this.removeMovieInArrayWithMovies(actionInput.getDeletedMovie(),
                        user.getRatedMovies());
                this.removeMovieInArrayWithMovies(actionInput.getDeletedMovie(),
                        user.getPurchasedMovies());

                if (user.isPremium()) {
                    user.setNumFreePremiumMovies(user.getNumFreePremiumMovies() + 1);
                } else {
                    user.setTokensCount(user.getTokensCount() + 2);
                }
            }
        }

        setChanged();
        notifyObservers(actionInput);
        this.removeMovieInArrayWithMovies(actionInput.getDeletedMovie(), this.getMovies());
    }

    /**
     * The method that searches by name and returns a movie in the database
     * @param name
     * @return
     */
    public Movie findMovie(final String name) {
        for (Movie movie : this.getMovies()) {
            if (movie.getName().equals(name)) {
                return movie;
            }
        }

        return null;
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
