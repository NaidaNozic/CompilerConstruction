package at.tugraz.ist.cc.error.semantic;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class OperatorTypeError extends SemanticError {

    /**
     * @param op
     *        The operator symbol. E.g.: "+", "-", "=".
     * @param line
     * 		  The line number in the input where the operator is used.
     */
    public OperatorTypeError(String op, int line) {
        super("Invalid operand type(s) for operator '"+op+"'.", line);
    }
}
