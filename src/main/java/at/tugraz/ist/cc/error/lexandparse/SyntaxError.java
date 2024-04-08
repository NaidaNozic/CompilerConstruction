package at.tugraz.ist.cc.error.lexandparse;

import at.tugraz.ist.cc.error.JovaError;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This class is used for automated testing and represents a syntax error.
 */
public class SyntaxError extends JovaError {

    /**
     * @param err_msg
     *        The intercepted error message of the JovaParser.
     * @param line
     * 		  The line number in the input where the syntax error occurred.
     * @param char_pos
     * 		  The character position within that line where the syntax error
     * 		  occurred.
     */
    public SyntaxError(String err_msg, int line, int char_pos) {
        super(err_msg, line, char_pos);
    }

    private static final long serialVersionUID = 1L;

}
