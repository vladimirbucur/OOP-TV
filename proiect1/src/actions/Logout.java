package actions;

import entities.Movie;
import entities.User;
import pootv.Run;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Logout extends Action {
    /**
     * The method that creates and returns an ObjectNode that is used for displaying the output
     * @param objectMapper
     * @return
     */
    public ObjectNode actionOutput(final boolean error, final Run run,
                                            final ObjectMapper objectMapper) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("error", "Error");
        objectNode.putPOJO("currentMoviesList", new ArrayList<>());
        objectNode.put("currentUser", (JsonNode) null);

        return objectNode;
    }
    /**
     * The method that performs the "Logout" action
     * It checks if the current page is not "Unauthenticated homepage" and logs out
     * the current user
     * At the end, the corresponding message is sent to the output
     * @param run
     * @param output
     * @param objectMapper
     */
    public void logoutAction(final Run run, final ArrayNode output,
                             final ObjectMapper objectMapper) {
        if (run.getCurrentPage().getName().equals("unAuthPage")) {
            output.add(actionOutput(true, run, objectMapper));
        }

        run.setCurrentUser(new User());
        run.setCurrentMovie(new Movie());
        run.setCurrentMoviesList(new ArrayList<>());
        run.getCurrentPage().setName("unAuthPage");
    }
}
