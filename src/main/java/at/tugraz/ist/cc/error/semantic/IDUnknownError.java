package at.tugraz.ist.cc.error.semantic;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class IDUnknownError extends SemanticError {

    /**
     * @param id
     *        The undeclared identifier which is used.
     * @param line
     * 		  The line number in the input where the undeclared identifier is used.
     */
    public IDUnknownError(String id, int line) {
        super("Usage of unknown identifier: '"+id+"'.",line);
    }
}
