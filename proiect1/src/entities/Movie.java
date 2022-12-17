package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.MovieInput;

import java.util.ArrayList;

public class Movie {
    private String name;
    private int year;
    private int duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;
    private int numLikes;
    private double rating;
    private int numRatings;
    private ArrayList<Integer> ratings;  // Array that contains all ratings recieved by the movie

    public Movie() {
        this.setGenres(new ArrayList<>());
        this.setActors(new ArrayList<>());
        this.setCountriesBanned(new ArrayList<>());
        this.setRatings(new ArrayList<>());
    }

    public Movie(final MovieInput movieInput) {
        this.setName(movieInput.getName());
        this.setYear(movieInput.getYear());
        this.setDuration(movieInput.getDuration());

        this.setGenres(new ArrayList<>());
        for (String genre : movieInput.getGenres()) {
            this.getGenres().add(genre);
        }

        this.setActors(new ArrayList<>());
        for (String actor : movieInput.getActors()) {
            this.getActors().add(actor);
        }

        this.setCountriesBanned(new ArrayList<>());
        for (String countryBanned : movieInput.getCountriesBanned()) {
            this.getCountriesBanned().add(countryBanned);
        }

        this.setNumLikes(0);
        this.setRating(0.00);
        this.setNumRatings(0);
        this.setRatings(new ArrayList<>());
    }

    public Movie(final Movie movie) {
        this.setName(movie.getName());
        this.setYear(movie.getYear());
        this.setDuration(movie.getDuration());

        this.setGenres(new ArrayList<>());
        for (String genre : movie.getGenres()) {
            this.getGenres().add(genre);
        }

        this.setActors(new ArrayList<>());
        for (String actor : movie.getActors()) {
            this.getActors().add(actor);
        }

        this.setCountriesBanned(new ArrayList<>());
        for (String countryBanned : movie.getCountriesBanned()) {
            this.getCountriesBanned().add(countryBanned);
        }

        this.setRatings(new ArrayList<>());
        for (Integer currentRating : movie.getRatings()) {
            this.getRatings().add(currentRating);
        }

        this.setNumLikes(movie.getNumLikes());
        this.setRating(movie.getRating());
        this.setNumRatings(movie.getNumRatings());
    }

    /**
     * The method that checks if the current movie is banned in the country of which the user given
     * as a parameter belongs
     * @param currentUser
     * @return
     */
    public boolean isBanned(final User currentUser) {
        for (String countryBanned : this.getCountriesBanned()) {
            if (countryBanned.equals(currentUser.getCredentials().getCountry())) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method that checks if the current movie contains the list of actors given as a parameter
     * @param actorsList
     * @return
     */
    public boolean containsActors(final ArrayList<String> actorsList) {
        for (String actor : actorsList) {
            if (!this.getActors().contains(actor)) {
                return false;
            }
        }
        return true;
    }

    /**
     * The method that checks if the current movie contains the list of genres given as a parameter
     * @param genresList
     * @return
     */
    public boolean containsGenres(final ArrayList<String> genresList) {
        for (String genre : genresList) {
            if (!this.getGenres().contains(genre)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     *
     * @param year
     */
    public void setYear(final int year) {
        this.year = year;
    }

    /**
     *
     * @return
     */
    public int getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     */
    public void setDuration(final int duration) {
        this.duration = duration;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     *
     * @param genres
     */
    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getActors() {
        return actors;
    }

    /**
     *
     * @param actors
     */
    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getCountriesBanned() {
        return countriesBanned;
    }

    /**
     *
     * @param countriesBanned
     */
    public void setCountriesBanned(final ArrayList<String> countriesBanned) {
        this.countriesBanned = countriesBanned;
    }

    /**
     *
     * @return
     */
    public int getNumLikes() {
        return numLikes;
    }

    /**
     *
     * @param numLikes
     */
    public void setNumLikes(final int numLikes) {
        this.numLikes = numLikes;
    }

    /**
     *
     * @return
     */
    public double getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     */
    public void setRating(final double rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     */
    public int getNumRatings() {
        return numRatings;
    }

    /**
     *
     * @param numRatings
     */
    public void setNumRatings(final int numRatings) {
        this.numRatings = numRatings;
    }

    /**
     *
     * @return
     */
    @JsonIgnore
    public ArrayList<Integer> getRatings() {
        return ratings;
    }

    /**
     *
     * @param ratings
     */
    @JsonIgnore
    public void setRatings(final ArrayList<Integer> ratings) {
        this.ratings = ratings;
    }
}
