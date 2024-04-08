package at.tugraz.ist.cc.error.semantic;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class ReturnTypeError extends SemanticError {

    /**
     * @param line
     * 		  The line number in the input where the expression to the right of
     * 		  'return' occurs.
     */
    public ReturnTypeError(int line) {

        super("Incompatible type for return.", line);
    }
}
