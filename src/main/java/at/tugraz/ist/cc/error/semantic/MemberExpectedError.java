package at.tugraz.ist.cc.error.semantic;
/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class MemberExpectedError extends SemanticError {
    /**
     * @param line
     *        The line number in the input where the invalid expression (i.e.,
     *        the invalid right-hand side operand for '.') occurs.
     */
    public MemberExpectedError(int line) {
        super("Field or method call expected.", line);
    }
}
