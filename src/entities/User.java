package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CredentialsInput;
import fileio.UserInput;

import java.util.ArrayList;

public class User {
    private Credentials credentials;
    private int tokensCount;
    private int numFreePremiumMovies;
    private ArrayList<Movie> purchasedMovies;
    private ArrayList<Movie> watchedMovies;
    private ArrayList<Movie> likedMovies;
    private ArrayList<Movie> ratedMovies;
    static final int MAXFREEPREMIUMMOVIES = 15;

    public User() {
        this.setPurchasedMovies(new ArrayList<>());
        this.setWatchedMovies(new ArrayList<>());
        this.setLikedMovies(new ArrayList<>());
        this.setRatedMovies(new ArrayList<>());
    }

    public User(final CredentialsInput credentialsInput) {
        this.setCredentials(new Credentials(credentialsInput));
        this.setNumFreePremiumMovies(MAXFREEPREMIUMMOVIES);
        this.setPurchasedMovies(new ArrayList<>());
        this.setWatchedMovies(new ArrayList<>());
        this.setLikedMovies(new ArrayList<>());
        this.setRatedMovies(new ArrayList<>());

    }

    public User(final UserInput userInput) {
        this.setCredentials(new Credentials(userInput.getCredentials()));
        this.setNumFreePremiumMovies(MAXFREEPREMIUMMOVIES);
        this.setPurchasedMovies(new ArrayList<>());
        this.setWatchedMovies(new ArrayList<>());
        this.setLikedMovies(new ArrayList<>());
        this.setRatedMovies(new ArrayList<>());
    }

    public User(final User user) {
        this.setCredentials(new Credentials(user.getCredentials()));
        this.setTokensCount(user.getTokensCount());
        this.setNumFreePremiumMovies(user.getNumFreePremiumMovies());

        this.setPurchasedMovies(new ArrayList<>());
        for (Movie purchasedMovie : user.getPurchasedMovies()) {
            this.getPurchasedMovies().add(new Movie(purchasedMovie));
        }

        this.setWatchedMovies(new ArrayList<>());
        for (Movie watchedMovie : user.getWatchedMovies()) {
            this.getWatchedMovies().add(new Movie(watchedMovie));
        }

        this.setLikedMovies(new ArrayList<>());
        for (Movie likedMovie : user.getLikedMovies()) {
            this.getLikedMovies().add(new Movie(likedMovie));
        }

        this.setRatedMovies(new ArrayList<>());
        for (Movie ratedMovie : user.getRatedMovies()) {
            this.getRatedMovies().add(new Movie(ratedMovie));
        }
    }

    /**
     * sdsd
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
     *
     * @return
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     *
     * @param credentials
     */
    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     *
     * @return
     */
    public int getTokensCount() {
        return tokensCount;
    }

    /**
     *
     * @param tokensCount
     */
    public void setTokensCount(final int tokensCount) {
        this.tokensCount = tokensCount;
    }

    /**
     *
     * @return
     */
    public int getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    /**
     *
     * @param numFreePremiumMovies
     */
    public void setNumFreePremiumMovies(final int numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    /**
     *
     * @return
     */
    public ArrayList<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    /**
     *
     * @param purchasedMovies
     */
    public void setPurchasedMovies(final ArrayList<Movie> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    /**
     *
     * @return
     */
    public ArrayList<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    /**
     *
     * @param watchedMovies
     */
    public void setWatchedMovies(final ArrayList<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    /**
     *
     * @return
     */
    public ArrayList<Movie> getLikedMovies() {
        return likedMovies;
    }

    /**
     *
     * @param likedMovies
     */
    public void setLikedMovies(final ArrayList<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    /**
     *
     * @return
     */
    public ArrayList<Movie> getRatedMovies() {
        return ratedMovies;
    }

    /**
     *
     * @param ratedMovies
     */
    public void setRatedMovies(final ArrayList<Movie> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }
}
