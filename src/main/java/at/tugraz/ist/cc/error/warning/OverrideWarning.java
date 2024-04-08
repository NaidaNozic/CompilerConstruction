package at.tugraz.ist.cc.error.warning;

import java.util.Collection;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class OverrideWarning extends JovaWarning {

    /**
     * @param method_name
     *        The name of the overriding method.
     * @param param_types
     *        An ordered Collection of Strings with the names of the used parameter types. In case no parameters where
     *        specified, an empty Collection is expected to be passed.
     * @param line
     * 		  The line number in the input where overriding method is defined.
     * @param char_pos
     * 		  The character position of the first character of the overriding method in the line.
     */
    public OverrideWarning(String method_name, Collection<String> param_types, int line, int char_pos) {
        super("Method '"+ method_name+ "(" + String.join(",", param_types) + ")' overrides method of superclass.", line, char_pos);
    }
}
