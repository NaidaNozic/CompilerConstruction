package at.tugraz.ist.cc.error.semantic;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class IntegerSizeError extends SemanticError {

    /**
     * @param line
     * 		  The line number in the input where the integer literal is used.
     */
    public IntegerSizeError(int line) {
        super("Integer number too large.", line);
    }
}
