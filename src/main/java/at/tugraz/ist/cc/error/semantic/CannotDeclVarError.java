package at.tugraz.ist.cc.error.semantic;
/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class CannotDeclVarError extends SemanticError {
    /**
     * @param line
     *        The line number in the input where the type is set for the
     *        invalid variable declaration.
     */
    public CannotDeclVarError(int line) {
        super("Cannot declare variables at this position.", line);
    }
}