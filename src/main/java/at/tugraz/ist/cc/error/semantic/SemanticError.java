package at.tugraz.ist.cc.error.semantic;

import at.tugraz.ist.cc.error.JovaError;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * An abstract class representing a semantic error. This Class is used for
 * automated testing.
 */
public abstract class SemanticError extends JovaError {

    /**
     * @param err_msg
     *        The reported error message.
     * @param line
     * 		  The line number in the input where the semantic error occurred.
     */
    protected SemanticError(String err_msg, int line) {
        super(err_msg, line, -42);
    }
}
