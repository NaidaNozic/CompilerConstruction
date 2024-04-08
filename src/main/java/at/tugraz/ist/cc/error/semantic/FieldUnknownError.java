package at.tugraz.ist.cc.error.semantic;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class FieldUnknownError extends SemanticError{

    /**
     * @param type_name
     *        The name of the type of the left-hand side expression for the '.' operator. E.g.: "MyClass", "int".
     * @param field_name
     * 		  The string of the right-hand side expression for the '.' operator. E.g.: a field name 'foo', or a
     * 		  literal '123', 'true', 'nix', etc.
     * @param line
     * 		  The line number in the input file where the right-hand side variable occurs.
     */
    public FieldUnknownError(String type_name, String field_name, int line) {
        super("'"+type_name+"' does not have field '"+field_name+"'.", line);
    }
}
