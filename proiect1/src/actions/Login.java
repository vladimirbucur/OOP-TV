package actions;

import entities.User;
import pootv.Run;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionInput;
import fileio.CredentialsInput;


import java.util.ArrayList;

public class Login extends Action {

    /**
     * The method that checks if the user given as a parameter exists in the database
     * @param users
     * @param userLogin
     * @return
     */
    boolean existUserInDatabase(final ArrayList<User> users, final CredentialsInput userLogin) {
        for (User user : users) {
            if (user.getCredentials().getName().equals(userLogin.getName())
                    && user.getCredentials().getPassword().equals(userLogin.getPassword())) {
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
            objectNode.putPOJO("currentMoviesList", new ArrayList<>());
            objectNode.put("currentUser", (JsonNode) null);
        } else {
            objectNode.put("error", (JsonNode) null);
            objectNode.putPOJO("currentMoviesList",
                    new ArrayList<>(run.getCurrentMoviesList()));
            objectNode.putPOJO("currentUser", new User(run.getCurrentUser()));
        }

        return objectNode;
    }

    /**
     * The method that performs the "Login" action
     * Check if the current page is "Login" and after checking if the user who wants to log in is
     * already in the database, log in and arrive at the "Authenticated homepage" page
     * At the end, the corresponding message is sent to the output
     * @param users
     * @param run
     * @param output
     * @param actionInput
     * @param objectMapper
     */
    public void loginAction(final ArrayList<User> users, final Run run, final ArrayNode output,
                            final ActionInput actionInput, final ObjectMapper objectMapper) {
        if (!run.getCurrentPage().getName().equals("login")) {
            output.add(actionOutput(true, run, objectMapper));
            return;
        }

        if (!existUserInDatabase(users, actionInput.getCredentials())) {
            output.add(actionOutput(true, run, objectMapper));
            run.getCurrentPage().setName("unAuthPage");
        } else {
            for (User user : users) {
                if (user.getCredentials().getName().equals(actionInput.getCredentials().getName())
                        && user.getCredentials().getPassword().equals(
                                actionInput.getCredentials().getPassword())) {
                        run.setCurrentUser(user);
                }
            }

            run.getCurrentPage().setName("authPage");
            output.add(actionOutput(false, run, objectMapper));
        }
    }
}
