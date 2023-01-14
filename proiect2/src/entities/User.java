package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.ActionInput;
import fileio.CredentialsInput;
import fileio.UserInput;
import pootv.Database;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class User implements Observer {
    private Credentials credentials;
    private int tokensCount;
    private int numFreePremiumMovies;
    private ArrayList<Movie> purchasedMovies;
    private ArrayList<Movie> watchedMovies;
    private ArrayList<Movie> likedMovies;
    private ArrayList<Movie> ratedMovies;
    private ArrayList<Notification> notifications;
    private ArrayList<String> subscribedGenres;
    static final int MAXFREEPREMIUMMOVIES = 15;

    public User() {
        this.setPurchasedMovies(new ArrayList<>());
        this.setWatchedMovies(new ArrayList<>());
        this.setLikedMovies(new ArrayList<>());
        this.setRatedMovies(new ArrayList<>());
        this.setNotifications(new ArrayList<>());
        this.setSubscribedGenres(new ArrayList<>());
    }

    public User(final CredentialsInput credentialsInput) {
        this.setCredentials(new Credentials(credentialsInput));
        this.setNumFreePremiumMovies(MAXFREEPREMIUMMOVIES);
        this.setPurchasedMovies(new ArrayList<>());
        this.setWatchedMovies(new ArrayList<>());
        this.setLikedMovies(new ArrayList<>());
        this.setRatedMovies(new ArrayList<>());
        this.setNotifications(new ArrayList<>());
        this.setSubscribedGenres(new ArrayList<>());

    }

    public User(final UserInput userInput) {
        this.setCredentials(new Credentials(userInput.getCredentials()));
        this.setNumFreePremiumMovies(MAXFREEPREMIUMMOVIES);
        this.setPurchasedMovies(new ArrayList<>());
        this.setWatchedMovies(new ArrayList<>());
        this.setLikedMovies(new ArrayList<>());
        this.setRatedMovies(new ArrayList<>());
        this.setNotifications(new ArrayList<>());
        this.setSubscribedGenres(new ArrayList<>());
    }

    public User(final User user) {
        this.setCredentials(new Credentials(user.getCredentials()));
        this.setTokensCount(user.getTokensCount());
        this.setNumFreePremiumMovies(user.getNumFreePremiumMovies());

        this.setPurchasedMovies(new ArrayList<>());
        for (Movie purchasedMovie : user.getPurchasedMovies()) {
//            this.getPurchasedMovies().add(new Movie(purchasedMovie));
            this.getPurchasedMovies().add(new Movie.Builder().fromMovie(purchasedMovie).build());
        }

        this.setWatchedMovies(new ArrayList<>());
        for (Movie watchedMovie : user.getWatchedMovies()) {
//            this.getWatchedMovies().add(new Movie(watchedMovie));
            this.getWatchedMovies().add(new Movie.Builder().fromMovie(watchedMovie).build());
        }

        this.setLikedMovies(new ArrayList<>());
        for (Movie likedMovie : user.getLikedMovies()) {
//            this.getLikedMovies().add(new Movie(likedMovie));
            this.getLikedMovies().add(new Movie.Builder().fromMovie(likedMovie).build());
        }

        this.setRatedMovies(new ArrayList<>());
        for (Movie ratedMovie : user.getRatedMovies()) {
//            this.getRatedMovies().add(new Movie(ratedMovie));
            this.getRatedMovies().add(new Movie.Builder().fromMovie(ratedMovie).build());
        }

        this.setNotifications(new ArrayList<>());
        for (Notification notification : user.getNotifications()) {
            this.getNotifications().add(new Notification(notification));
        }

        this.setSubscribedGenres(new ArrayList<>());
        for (String genre : user.getSubscribedGenres()) {
            this.getSubscribedGenres().add(new String(genre));
        }
    }

    /**
     * The method that checks if the current user is a premium one or not
     *
     * @return
     */
    @JsonIgnore
    public boolean isPremium() {
        if (this.getCredentials().getAccountType().equals("premium")) {
            return true;
        }
        return false;
    }

    /**
     * The method that sends notifications in case a thread has been added or deleted
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     */
    @Override
    public void update(final Observable o, final Object arg) {
        Database database = (Database) o;
        ActionInput actionInput = (ActionInput) arg;

        if (actionInput.getAddedMovie() != null) {
            addNotification(actionInput);
        } else if (actionInput.getDeletedMovie() != null) {
            deleteNotification(database, actionInput);
        }
    }

    /**
     * The method that sends notifications in case a thread has been deleted
     * @param database
     * @param actionInput
     */
    private void deleteNotification(final Database database, final ActionInput actionInput) {
        Movie deletedMovie = database.findMovie(actionInput.getDeletedMovie());
        boolean ok = false;
        for (Genre genre : deletedMovie.getGenres()) {
            if (subscribedGenres.contains(genre.getName())) {
                ok = true;
            }
        }
        if (ok) {
            Notification notification = new Notification();
            notification.setMovieName(actionInput.getDeletedMovie());
            notification.setMessage("DELETE");
            this.getNotifications().add(notification);
        }
    }

    /**
     * The method that sends notifications in case a thread has been added
     * @param actionInput
     */
    private void addNotification(final ActionInput actionInput) {
        exit:
        if (!actionInput.getAddedMovie().getCountriesBanned().contains(
                this.getCredentials().getCountry())) {
            for (String genre : actionInput.getAddedMovie().getGenres()) {
                if (this.getSubscribedGenres().contains(genre)) {
                    Notification notification = new Notification();
                    notification.setMovieName(actionInput.getAddedMovie().getName());
                    notification.setMessage("ADD");
                    this.getNotifications().add(notification);
                    break exit;
                }
            }
        }
    }

    /**
     * @return
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * @param credentials
     */
    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * @return
     */
    public int getTokensCount() {
        return tokensCount;
    }

    /**
     * @param tokensCount
     */
    public void setTokensCount(final int tokensCount) {
        this.tokensCount = tokensCount;
    }

    /**
     * @return
     */
    public int getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    /**
     * @param numFreePremiumMovies
     */
    public void setNumFreePremiumMovies(final int numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    /**
     * @return
     */
    public ArrayList<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    /**
     * @param purchasedMovies
     */
    public void setPurchasedMovies(final ArrayList<Movie> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    /**
     * @return
     */
    public ArrayList<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    /**
     * @param watchedMovies
     */
    public void setWatchedMovies(final ArrayList<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    /**
     * @return
     */
    public ArrayList<Movie> getLikedMovies() {
        return likedMovies;
    }

    /**
     * @param likedMovies
     */
    public void setLikedMovies(final ArrayList<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    /**
     * @return
     */
    public ArrayList<Movie> getRatedMovies() {
        return ratedMovies;
    }

    /**
     * @param ratedMovies
     */
    public void setRatedMovies(final ArrayList<Movie> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    /**
     * @return
     */
    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications
     */
    public void setNotifications(final ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return
     */
    @JsonIgnore
    public ArrayList<String> getSubscribedGenres() {
        return subscribedGenres;
    }

    /**
     * @param subscribedGenres
     */
    @JsonIgnore
    public void setSubscribedGenres(final ArrayList<String> subscribedGenres) {
        this.subscribedGenres = subscribedGenres;
    }
}
