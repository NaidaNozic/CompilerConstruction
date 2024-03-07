package at.tugraz.ist.cc.error.semantic;

import java.util.Collection;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class MethodUnknownError extends SemanticError {

    /**
     * @param method_name
     *        The name of the method.
     * @param arg_types
     *        An ordered Collection of Strings with the names of the used
     *        argument types. In case no arguments where supplied, an empty
     *        Collection is expected to be passed.
     * @param line
     * 		  The line number in the input where the unknown method is invoked.
     */
    public MethodUnknownError(String method_name, Collection<String> arg_types, int line) {
        super("Call to unknown method: '" + method_name + "(" + String.join(", ", arg_types) + ")'.", line);
    }
}
