package at.tugraz.ist.cc.error.semantic;


/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class AssignmentExpectedError extends SemanticError {

    /**
     * .
     * @param line
     * 		  The line number where an expression is used that is not an assignment or method call.
     */
    public AssignmentExpectedError(int line) {
        super("Assignment or method call expected.", line);
    }
}
