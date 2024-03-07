package at.tugraz.ist.cc.error.semantic;

public class VariableExpectedError extends SemanticError {

    /**
     * @param line
     *        The line number in the input where the invalid expression (i.e.,
     *        the invalid left-hand side operand for '=') occurs.
     */
    public VariableExpectedError(int line) {
        super("Variable or field expected for assignment.", line);
    }
}
