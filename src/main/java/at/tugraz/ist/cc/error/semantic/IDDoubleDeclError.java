package at.tugraz.ist.cc.error.semantic;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class IDDoubleDeclError extends SemanticError {

    /**
     * @param id
     *        The identifier which is redeclared.
     * @param line
     * 		  The line number in the input file where the identifier is redeclared.
     */
    public IDDoubleDeclError(String id, int line) {
        super("Double declaration of identifier: '"+ id+"'.", line);
    }
}
