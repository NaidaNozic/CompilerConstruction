package at.tugraz.ist.cc.error.semantic;

import java.util.Collection;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class MethodDoubleDefError extends SemanticError {

    /**
     * @param method_name
     *        The name of the method.
     * @param param_types
     *        An ordered Collection of Strings with the names of the used
     *        parameter types. In case no parameters where specified, an empty
     *        Collection is expected to be passed.
     * @param line
     * 		  The line number in the input where the method is redefined.
     */
    public MethodDoubleDefError(String method_name, Collection<String> param_types, int line) {
        super("Double definition of method '"+method_name+"("+ String.join(", ", param_types)+")'.", line);
    }
}
