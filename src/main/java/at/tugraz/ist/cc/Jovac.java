package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.lexandparse.LexicalError;
import at.tugraz.ist.cc.error.lexandparse.SyntaxError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.error.warning.JovaWarning;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * DO NOT CHANGE THE NAME OF THIS CLASS OR MOVE IT TO ANOTHER PACKAGE! You can
 * add new fields or methods though.
 * This class will be instantiated to automatically test your submissions.
 *
 * <p>TODO: Start implementing Task 1-3 here...</p>
 */
public class Jovac {
    /**
    * You can use these members to collect the respective compilation errors.
    */
    private final List<LexicalError> lexical_errors = new LinkedList<>();
    private final List<SyntaxError> syntax_errors = new LinkedList<>();
    private final List<SemanticError> semantic_errors = new LinkedList<>();;
    private final List<JovaWarning> warnings = new LinkedList<>();;


    /**
     * DO NOT CHANGE THE SIGNATURE, RETURN TYPE, OR ACCESS MODIFIER OF THIS
     * METHOD! This method is called by the test system to test your Task 1
     * submission.
     *
     * <p>TODO: Start implementing Task 1 here...</p>
     *
     * @param file_path
     *        A string representing the path to the input .jova file.
     */
    public void task1(String file_path) {
      // TODO: Implement Task 1.
    }


    /**
     * DO NOT CHANGE THE SIGNATURE, RETURN TYPE, OR ACCESS MODIFIER OF THIS
     * METHOD! This method is called by the test system to test your Task 2
     * submission.
     *
     * <p>TODO: Start implementing Task 2 here...</p>
     *
     * @param file_path
     *        A string representing the path to the input .jova file.
     */
    public void task2(String file_path) {
      // TODO: Implement Task 2.
    }


    /**
     * DO NOT CHANGE THE SIGNATURE, RETURN TYPE, OR ACCESS MODIFIER OF THIS
     * METHOD! This method is called by the test system to test your Task 3
     * submission.
     *
     * <p>TODO: Start implementing Task 3 here...</p>
     *
     * @param file_path
     *        A string representing the path to the input .jova file.
     *
     * @param out_path
     *        A string representing the path to the output directory where the
     *        generated .j files should be placed.
     */
    public void task3(String file_path, String out_path) {
      // TODO: Implement Task 3.
    }


    /**
     * DO NOT CHANGE THE SIGNATURE, RETURN TYPE, OR ACCESS MODIFIER OF THIS
     * METHOD! This method is called by the test system to test your Task 1
     * submission.
     *
     * <p>TODO: Implement for Task 1.1.</p>
     */
    public Collection<LexicalError> getLexicalErrors() {
        return null;
    }


    /**
     * DO NOT CHANGE THE SIGNATURE, RETURN TYPE, OR ACCESS MODIFIER OF THIS
     * METHOD! This method is called by the test system to test your Task 1
     * submission.
     *
     * <p>TODO: Implement for Task 1.1.</p>
     */
    public Collection<SyntaxError> getSyntaxErrors() {
        return null;
    }


    /**
     * DO NOT CHANGE THE SIGNATURE, RETURN TYPE, OR ACCESS MODIFIER OF THIS
     * METHOD! This method is called by the test system to test your Task 2
     * and 3 submission.
     *
     * <p>TODO: Implement for Task 1.2.</p>
     */
    public Collection<SemanticError> getSemanticErrors() {
        return null;
    }


    /**
     * DO NOT CHANGE THE SIGNATURE, RETURN TYPE, OR ACCESS MODIFIER OF THIS
     * METHOD! This method is called by the test system to test your Task 2
     * and 3 submission.
     *
     * <p>TODO: Implement for Task 1.2.</p>
     */
    public Collection<JovaWarning> getWarnings() {
        return null;
    }
}
