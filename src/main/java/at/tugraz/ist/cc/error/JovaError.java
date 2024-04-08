package at.tugraz.ist.cc.error;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 * An abstract class representing a compilation error. The base class for all
 * errors.
 */
public abstract class JovaError extends Throwable {
	private static final long serialVersionUID = -8070518169660227619L;
	protected final int line;
	protected final int char_pos;

	/**
	 * @param err_msg
	 *        The reported error message.
	 * @param line
	 * 		  The line number in the input where the error occurred.
	 * @param char_pos
	 *        The character position within that line where the error occurred.
	 */
	protected JovaError(String err_msg, int line, int char_pos) {
		super(err_msg);
		this.line = line;
		this.char_pos = char_pos;
	}

	/**
	 * Returns the line number where the error occurred in the input.
	 *
	 * @return The line number where the error occurred.
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

