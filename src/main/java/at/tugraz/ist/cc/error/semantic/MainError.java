package at.tugraz.ist.cc.error.semantic;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class MainError extends SemanticError {

    /**
     * @param line
     * 		  The line number in the input where a member is accessed or 'this'
     * 		  is used in a main-method.
     */
    public MainError(int line) {
        super("Cannot access a member or 'this' in main method.", line);
    }
}
