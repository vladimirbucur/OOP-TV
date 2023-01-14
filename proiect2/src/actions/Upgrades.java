package actions;

import pootv.Run;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionInput;

public class Upgrades extends Action {
    static final int PREMIUMACCOUNTPRICE = 10;

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
            currentMoviesListOutput.add(movieOutput(run.getCurrentMovie(), objectMapper));
            objectNode.set("currentMoviesList", currentMoviesListOutput);

            objectNode.set("currentUser", showUser(run.getCurrentUser()));
        }

        return objectNode;
    }

    /**
     * The method that performs the "Buy premium account" action
     * After checking whether the current page is "Upgrades" and that the current
     * user is not already premium, 10 tokens are deducted from him to become a premium user
     * @param run
     * @param output
     * @param objectMapper
     */
    public void buyPremiumAccount(final Run run, final ArrayNode output,
                                  final ObjectMapper objectMapper) {
        if (!run.getCurrentPage().getName().equals("upgrades")) {
            output.add(actionOutput(true, run, objectMapper));
        } else if (run.getCurrentUser().isPremium()) {
            output.add(actionOutput(true, run, objectMapper));
        } else {
            if (run.getCurrentUser().getTokensCount() < PREMIUMACCOUNTPRICE) {
                output.add(actionOutput(true, run, objectMapper));
            } else {
                run.getCurrentUser().getCredentials().setAccountType("premium");
                run.getCurrentUser().setTokensCount(run.getCurrentUser().getTokensCount()
                        - PREMIUMACCOUNTPRICE);
            }
        }
    }

    /**
     * The method that performs the "Buy tokens" action
     * After checking if the current page is "Upgrades" and that the current user
     * still has enough money in the balance to be able to buy the number of tokenshe wants,
     * his balance and the number of tokens are updated
     * @param run
     * @param output
     * @param actionInput
     * @param objectMapper
     */
    public void buyTokens(final Run run, final ArrayNode output, final ActionInput actionInput,
                          final ObjectMapper objectMapper) {
        if (!run.getCurrentPage().getName().equals("upgrades")) {
            output.add(actionOutput(true, run, objectMapper));
        } else {
            if (Integer.parseInt(run.getCurrentUser().getCredentials().getBalance())
                    < actionInput.getCount()) {
                output.add(actionOutput(true, run, objectMapper));
            } else {
                run.getCurrentUser().setTokensCount(run.getCurrentUser().getTokensCount()
                        + actionInput.getCount());
                int newBalance = Integer.parseInt(run.getCurrentUser().getCredentials().
                        getBalance())
                        - actionInput.getCount();
                run.getCurrentUser().getCredentials().setBalance(Integer.toString(newBalance));
            }
        }
    }
}
