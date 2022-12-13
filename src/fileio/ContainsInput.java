package fileio;

import java.util.ArrayList;

public class ContainsInput {
    private ArrayList<String> actors;
    private ArrayList<String> genre;

    public ContainsInput() { }

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
    public ArrayList<String> getGenres() {
        return genre;
    }

    /**
     *
     * @param genre
     */
    public void setGenre(final ArrayList<String> genre) {
        this.genre = genre;
    }
}
