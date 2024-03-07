package at.tugraz.ist.cc.error.semantic;
/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class MissingReturnError extends SemanticError {
    /**
     * @param line
     *        The line number in the input where the closing bracket of the
     *        respective method body occurs.
     */
    public MissingReturnError(int line) {
        super("Missing return statement.", line);
    }
}
