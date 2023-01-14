package entities;

public class Genre {
    private String name;
    private int numberOfLikes;

    public Genre(final String genreInput) {
        this.setName(genreInput);
        this.setNumberOfLikes(0);
    }

    public Genre(final Genre genre) {
        this.setName(genre.getName());
        this.setNumberOfLikes(genre.getNumberOfLikes());
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
    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    /**
     *
     * @param numberOfLikes
     */
    public void setNumberOfLikes(final int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }
}
