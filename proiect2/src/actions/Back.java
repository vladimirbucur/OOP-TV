package actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionInput;
import pootv.Database;
import pootv.Run;

import java.util.ArrayList;

public class Back extends Action {
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
        objectNode.put("error", "Error");
        objectNode.putPOJO("currentMoviesList", new ArrayList<>());
        objectNode.put("currentUser", (JsonNode) null);

        return objectNode;
    }

    /**
     * The method that performs the "Back" action
     * For this action, a stack is implemented in which every page that we have successfully
     * reached is entered, and when "back" is given, a pop is given from the stack
     * After checking whether the current page is a page that can be "backed",
     * each case is treatedseparately (it is checked which is the current page and,
     * depending on each case, back is given to the previous page)
     * @param run
     * @param database
     * @param output
     * @param objectMapper
     */
    public void backAction(final Run run, final Database database, final ArrayNode output,
                           final ObjectMapper objectMapper) {
        if (run.getCurrentUser() == null
                || run.getCurrentPage().getName().equals("unAuthPage")
                || run.getCurrentPage().getName().equals("login")
                || run.getCurrentPage().getName().equals("register")
                || run.getPagesHistory().peek().getName().equals("authPage")
                || run.getPagesHistory().isEmpty()) {
            output.add(this.actionOutput(true, run, objectMapper));
        } else {
            ActionInput actionInput = new ActionInput();
            actionInput.setType("change page");

            if (run.getPagesHistory().peek().getName().equals("movies")) {
                backFromMoviesPage(run, database, output, objectMapper, actionInput);
            } else if (run.getPagesHistory().peek().getName().equals("seeDetails")) {
                backFromSeeDetailsPage(run, database, output, objectMapper, actionInput);
            } else if (run.getPagesHistory().peek().getName().equals("upgrades")) {
                backFromUpgradesPage(run, database, output, objectMapper, actionInput);
            }
        }
    }

    /**
     * Metoda care executa comanda de "back" in caz ca pagina curenta este Upgrades
     * @param run
     * @param database
     * @param output
     * @param objectMapper
     * @param actionInput
     */
    private static void backFromUpgradesPage(final Run run, final Database database,
                                             final ArrayNode output,
                                             final ObjectMapper objectMapper,
                                             final ActionInput actionInput) {
        run.getPagesHistory().pop();
        if (run.getPagesHistory().peek().getName().equals("authPage")) {
            run.getCurrentPage().setName("authPage");
        } else if (run.getPagesHistory().peek().getName().equals("movies")) {
            database.movies(run, actionInput, output, objectMapper);
            run.getPagesHistory().pop();
        } else if (run.getPagesHistory().peek().getName().equals("seeDetails")) {
            database.seeDetails(run, actionInput, output, objectMapper);
            run.getPagesHistory().pop();
        }
    }

    /**
     * Metoda care executa comanda de "back" in caz ca pagina curenta este SeeDetails
     * @param run
     * @param database
     * @param output
     * @param objectMapper
     * @param actionInput
     */
    private static void backFromSeeDetailsPage(final Run run, final Database database,
                                               final ArrayNode output,
                                               final ObjectMapper objectMapper,
                                               final ActionInput actionInput) {
        database.movies(run, actionInput, output, objectMapper);
        run.getPagesHistory().pop();
        run.getPagesHistory().pop();
    }

    /**
     * Metoda care executa comanda de "back" in caz ca pagina curenta este Movies
     * @param run
     * @param database
     * @param output
     * @param objectMapper
     * @param actionInput
     */
    private static void backFromMoviesPage(final Run run, final Database database,
                                           final ArrayNode output,
                                           final ObjectMapper objectMapper,
                                           final ActionInput actionInput) {
        run.getPagesHistory().pop();
        if (run.getPagesHistory().peek().getName().equals("authPage")) {
            run.getCurrentPage().setName("authPage");
        } else if (run.getPagesHistory().peek().getName().equals("upgrades")) {
            database.upgrades(run, actionInput, output, objectMapper);
            run.getPagesHistory().pop();
        } else if (run.getPagesHistory().peek().getName().equals("seeDetails")) {
            database.seeDetails(run, actionInput, output, objectMapper);
            run.getPagesHistory().pop();
        }
    }
}
