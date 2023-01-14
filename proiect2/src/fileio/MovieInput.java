package fileio;

import java.util.ArrayList;

public final class MovieInput {
    private String name;
    private String year;
    private int duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;

    public MovieInput() {
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
    public String getYear() {
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
}
