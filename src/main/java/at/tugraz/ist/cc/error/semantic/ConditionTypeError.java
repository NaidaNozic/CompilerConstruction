package at.tugraz.ist.cc.error.semantic;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class ConditionTypeError extends SemanticError {

    /**
     * @param line
     * 		  The line number in the input where the incompatible expression is used as condition.
     */
    public ConditionTypeError(int line) {
        super("Incompatible type for condition.", line);
    }
}
