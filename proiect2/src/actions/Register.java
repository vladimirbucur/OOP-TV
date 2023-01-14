package actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Movie;
import entities.Page;
import entities.User;
import fileio.ActionInput;
import fileio.CredentialsInput;
import pootv.Run;

import java.util.ArrayList;

public class Register extends Action {
    /**
     * The method that checks if the user given as a parameter exists in the database
     * @param users
     * @param userRegister
     * @return
     */
    boolean existUserInDatabase(final ArrayList<User> users, final CredentialsInput userRegister) {
        for (User user : users) {
            if (user.getCredentials().getName().equals(userRegister.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method that creates and returns an ObjectNode that is used for displaying the output
     * @param error
     * @param run
     * @param objectMapper
     * @return
     */
    public ObjectNode actionOutput(final boolean error, final Run run,
                                   final ObjectMapper objectMapper) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (error) {
            objectNode.put("error", "Error");
            ArrayNode curretMovieList = new ObjectMapper().createArrayNode();
            objectNode.put("currentMoviesList", curretMovieList);
            objectNode.put("currentUser", (JsonNode) null);

        } else {
            objectNode.put("error", (JsonNode) null);

            ArrayNode currentMoviesListOutput = objectMapper.createArrayNode();
            for (Movie movie : run.getCurrentMoviesList()) {
                currentMoviesListOutput.add(movieOutput(movie, objectMapper));
            }
            objectNode.set("currentMoviesList", currentMoviesListOutput);

            objectNode.set("currentUser", showUser(run.getCurrentUser()));
        }

        return objectNode;
    }

    /**
     * The method that performs the "Register" action
     * Check if the current page is "Register" and after checking if the user who wants to
     * register is not already in the database, register and arrive at the "Authenticated
     * homepage" page
     * At the end, the corresponding message is sent to the output
     * @param users
     * @param run
     * @param output
     * @param actionInput
     * @param objectMapper
     */
    public void registerAction(final ArrayList<User> users, final Run run, final ArrayNode output,
                               final ActionInput actionInput, final ObjectMapper objectMapper) {
        if (!run.getCurrentPage().getName().equals("register")) {
            output.add(actionOutput(true, run, objectMapper));
            return;
        }

        if (existUserInDatabase(users, actionInput.getCredentials())) {
            output.add(actionOutput(true, run, objectMapper));
            run.getCurrentPage().setName("unAuthPage");
        } else {
            users.add(new User(actionInput.getCredentials()));
            run.setCurrentUser(users.get(users.size() - 1));

            run.getCurrentPage().setName("authPage");

            Page page = new Page();
            page.setName("authPage");
            run.getPagesHistory().push(page);

            output.add(actionOutput(false, run, objectMapper));
        }
    }
}
