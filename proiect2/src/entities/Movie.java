package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.MovieInput;

import java.util.ArrayList;
import java.util.HashMap;

public final class Movie {
    private String name;
    private String year;
    private int duration;
//    private ArrayList<String> genres;
    private ArrayList<Genre> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;
    private int numLikes;
    private double rating;
    private int numRatings;
//    private ArrayList<Integer> ratings;  // Array that contains all ratings recieved by the movie
    private HashMap<String, Integer> hashMapRating; // hashmap where I keep the rating each user
                                                   // gave to the current movie (the key is the
                                                   // user and the value is the rating)

    public static class Builder {
        private String name;
        private String year;
        private int duration = 0;
        private ArrayList<Genre> genres;
        private ArrayList<String> actors;
        private ArrayList<String> countriesBanned;
        private int numLikes;
        private double rating;
        private int numRatings;
        private HashMap<String, Integer> hashMapRating;

        /**
         *
         */
        public Builder() {
            this.genres = new ArrayList<>();
            this.actors = new ArrayList<>();
            this.countriesBanned = new ArrayList<>();
            this.hashMapRating = new HashMap<>();
        }

        /**
         *
         * @param movieInput
         * @return
         */
        public Builder fromMovieInput(final MovieInput movieInput) {
            this.name = movieInput.getName();
            this.year = movieInput.getYear();
            this.duration = movieInput.getDuration();

            this.genres = new ArrayList<>();
            for (String genre : movieInput.getGenres()) {
                this.genres.add(new Genre(genre));
            }

            this.actors = new ArrayList<>();
            for (String actor : movieInput.getActors()) {
                this.actors.add(actor);
            }

            this.countriesBanned = new ArrayList<>();
            for (String countryBanned : movieInput.getCountriesBanned()) {
                this.countriesBanned.add(countryBanned);
            }

            this.numLikes = 0;
            this.rating = 0.00;
            this.numRatings = 0;
            this.hashMapRating = new HashMap<>();

            return this;
        }

        /**
         *
         * @param movie
         * @return
         */
        public Builder fromMovie(final Movie movie) {
            this.name = movie.getName();
            this.year = movie.getYear();
            this.duration = movie.getDuration();

            this.genres = new ArrayList<>();
            for (Genre genre : movie.getGenres()) {
                this.genres.add(new Genre(genre));
            }

            this.actors = new ArrayList<>();
            for (String actor : movie.getActors()) {
                this.actors.add(actor);
            }

            this.countriesBanned = new ArrayList<>();
            for (String countryBanned : movie.getCountriesBanned()) {
                this.countriesBanned.add(countryBanned);
            }

            this.hashMapRating = new HashMap<>();
            this.hashMapRating.putAll(movie.getHashMapRating());

            this.numLikes = movie.getNumLikes();
            this.rating = movie.getRating();
            this.numRatings = movie.getNumRatings();

            return this;
        }

        /**
         *
         * @return
         */
        public Movie build() {
            return new Movie(this);
        }
    }

    private Movie(final Builder builder) {
        this.name = builder.name;
        this.year = builder.year;
        this.duration = builder.duration;
        this.genres = builder.genres;
        this.actors = builder.actors;
        this.countriesBanned = builder.countriesBanned;
        this.numLikes = builder.numLikes;
        this.rating = builder.rating;
        this.numRatings = builder.numRatings;
        this.hashMapRating = builder.hashMapRating;
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
        boolean contains = false;
        for (String genre : genresList) {
            contains = false;
            for (Genre genre1 : this.getGenres()) {
                if (genre.equals(genre1.getName())) {
                    contains = true;
                }
            }
            if (!contains) {
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
    public String  getYear() {
        return year;
    }

    /**
     *
     * @param year
     */
    public void setYear(final String year) {
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
    public ArrayList<Genre> getGenres() {
        return genres;
    }

    /**
     *
     * @param genres
     */
    public void setGenres(final ArrayList<Genre> genres) {
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
    public HashMap<String, Integer> getHashMapRating() {
        return hashMapRating;
    }

    /**
     *
     * @param hashMapRating
     */
    @JsonIgnore
    public void setHashMapRating(final HashMap<String, Integer> hashMapRating) {
        this.hashMapRating = hashMapRating;
    }
}
