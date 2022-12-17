package actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pootv.Run;

/**
 * The method that sends to the output what must be displayed.
 * This is to be implemented in each class of action.
 */
public abstract class Action {
    public abstract ObjectNode actionOutput(final boolean error, final Run run,
                                  final ObjectMapper objectMapper);
}
