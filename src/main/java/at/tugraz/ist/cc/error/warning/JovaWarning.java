package at.tugraz.ist.cc.error.warning;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 * An abstract class representing a compilation warning. The base class for all
 * warnings.
 */
public abstract class JovaWarning extends Throwable {
    private static final long serialVersionUID = -7070518169660227619L;
    protected final int line;
    protected final int char_pos;

    /**
     * @param msg
     *        The reported warning message.
     * @param line
     * 		  The line number in the input where the warning occurred.
     * @param char_pos
     *        The character position within that line where the warning
     *        occurred.
     *
     */
    protected JovaWarning(String msg, int line, int char_pos) {
        super(msg);
        this.line = line;
        this.char_pos = char_pos;
    }

    /**
     * Returns the line number where the warning occurred in the input.
     *
     * @return The line number where the warning occurred.
     */
    public int getLine()
    {
        return line;
    }

    /**
     * Returns the character position within that line where the warning
     * occurred.
     *
     * @return The line number where the warning occurred.
     */
    public int getCharPos()
    {
        return char_pos;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
