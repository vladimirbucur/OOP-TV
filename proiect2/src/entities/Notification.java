package entities;

public class Notification {
    private String movieName;
    private String message;

    public Notification() {

    }
    public Notification(final Notification notification) {
        this.setMovieName(notification.getMovieName());
        this.setMessage(notification.getMessage());
    }

    /**
     *
     * @return
     */
    public String getMovieName() {
        return movieName;
    }

    /**
     *
     * @param movieName
     */
    public void setMovieName(final String movieName) {
        this.movieName = movieName;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
