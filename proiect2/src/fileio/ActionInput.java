package fileio;

public final class ActionInput {
    private String type;
    private String page;
    private String feature;
    private CredentialsInput credentials;
    private String startsWith;
    private FiltersInput filters;
    private String movie;
    private String objectType;
    private int rate;
    private int count;
    private String subscribedGenre;
    private MovieInput addedMovie;
    private String deletedMovie;

    public ActionInput() {
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getPage() {
        return page;
    }

    /**
     *
     * @param page
     */
    public void setPage(final String page) {
        this.page = page;
    }

    /**
     *
     * @return
     */
    public String getFeature() {
        return feature;
    }

    /**
     *
     * @param feature
     */
    public void setFeature(final String feature) {
        this.feature = feature;
    }

    /**
     *
     * @return
     */
    public CredentialsInput getCredentials() {
        return credentials;
    }

    /**
     *
     * @param credentials
     */
    public void setCredentials(final CredentialsInput credentials) {
        this.credentials = credentials;
    }

    /**
     *
     * @return
     */
    public String getStartsWith() {
        return startsWith;
    }

    /**
     *
     * @param startsWith
     */
    public void setStartsWith(final String startsWith) {
        this.startsWith = startsWith;
    }

    /**
     *
     * @return
     */
    public FiltersInput getFilters() {
        return filters;
    }

    /**
     *
     * @param filters
     */
    public void setFilters(final FiltersInput filters) {
        this.filters = filters;
    }

    /**
     *
     * @return
     */
    public String getMovie() {
        return movie;
    }

    /**
     *
     * @param movie
     */
    public void setMovie(final String movie) {
        this.movie = movie;
    }

    /**
     *
     * @return
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     *
     * @param objectType
     */
    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }

    /**
     *
     * @return
     */
    public int getRate() {
        return rate;
    }

    /**
     *
     * @param rate
     */
    public void setRate(final int rate) {
        this.rate = rate;
    }

    /**
     *
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @param count
     */
    public void setCount(final int count) {
        this.count = count;
    }

    /**
     *
     * @return
     */
    public String getSubscribedGenre() {
        return subscribedGenre;
    }

    /**
     *
     * @param subscribedGenre
     */
    public void setSubscribedGenre(final String subscribedGenre) {
        this.subscribedGenre = subscribedGenre;
    }

    /**
     *
     * @return
     */
    public MovieInput getAddedMovie() {
        return addedMovie;
    }

    /**
     *
     * @param addedMovie
     */
    public void setAddedMovie(final MovieInput addedMovie) {
        this.addedMovie = addedMovie;
    }

    /**
     *
     * @return
     */
    public String getDeletedMovie() {
        return deletedMovie;
    }

    /**
     *
     * @param deletedMovie
     */
    public void setDeletedMovie(final String deletedMovie) {
        this.deletedMovie = deletedMovie;
    }
}
