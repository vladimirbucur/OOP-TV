package fileio;

import java.util.ArrayList;

public final class Input {
    private ArrayList<UserInput> users;
    private ArrayList<MovieInput> movies;
    private ArrayList<ActionInput> actions;

    public Input() {
    }

    /**
     *
     * @return
     */
    public ArrayList<UserInput> getUsers() {
        return users;
    }

    /**
     *
     * @param users
     */
    public void setUsers(final ArrayList<UserInput> users) {
        this.users = users;
    }

    /**
     *
     * @return
     */
    public ArrayList<MovieInput> getMovies() {
        return movies;
    }

    /**
     *
     * @param movies
     */
    public void setMovies(final ArrayList<MovieInput> movies) {
        this.movies = movies;
    }

    /**
     *
     * @return
     */
    public ArrayList<ActionInput> getActions() {
        return actions;
    }

    /**
     *
     * @param actions
     */
    public void setActions(final ArrayList<ActionInput> actions) {
        this.actions = actions;
    }
}
