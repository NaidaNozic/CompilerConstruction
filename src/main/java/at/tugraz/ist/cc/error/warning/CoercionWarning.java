package at.tugraz.ist.cc.error.warning;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class CoercionWarning extends JovaWarning {

    /**
     * @param actual_type
     *        The name of the type of the expression being used in the program, which gets coerced.E.g.: "MyClass",
     *        "int".
     * @param expected_type
     *        The name of the expected type for the respective operation.
     * @param line
     * 		  The line number in the input where the expression occurs.
     * @param char_pos
     * 		  The character position of the first character of the expression in the line.
     */
    public CoercionWarning(String actual_type, String expected_type, int line, int char_pos) {
        super("'"+actual_type+"' coerced to '"+expected_type+"'.", line, char_pos);
    }
}
