package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.lexandparse.LexicalError;
import at.tugraz.ist.cc.error.lexandparse.SyntaxError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.error.warning.JovaWarning;
import at.tugraz.ist.cc.io.JovaErrorPrinter;
import at.tugraz.ist.cc.visitors.ProgramVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private final List<SemanticError> semantic_errors = new LinkedList<>();
    private final List<JovaWarning> warnings = new LinkedList<>();


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
        CharStream input;
        try {
            input = CharStreams.fromFileName(file_path);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        JovaLexer lexer = new JovaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JovaParser parser = new JovaParser(tokens);

        lexer.removeErrorListeners();
        parser.removeErrorListeners();

        lexer.addErrorListener(new JovaErrorListener(lexical_errors, syntax_errors));
        parser.addErrorListener(new JovaErrorListener(lexical_errors, syntax_errors));

        ParseTree parseTree = parser.program();

        if (lexical_errors.size() +syntax_errors.size() == 0){
            new ProgramVisitor(semantic_errors, warnings).visit(parseTree);
        }

        JovaErrorPrinter.printErrorsAndWarnings(lexical_errors, syntax_errors, getSemanticErrors(), warnings);

    }

    // Error listener to intercept lexical and syntax errors
    private static class JovaErrorListener extends BaseErrorListener {

        private final List<LexicalError> lexicalErrors;
        private final List<SyntaxError> syntaxErrors;

        // Constructor
        public JovaErrorListener(List<LexicalError> lexicalErrors, List<SyntaxError> syntaxErrors) {
            this.lexicalErrors = lexicalErrors;
            this.syntaxErrors = syntaxErrors;
        }
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine, String msg, RecognitionException e) {
            if (recognizer instanceof JovaLexer) {
                lexicalErrors.add(new LexicalError(msg, line, charPositionInLine));
            } else if (recognizer instanceof JovaParser) {
                syntaxErrors.add(new SyntaxError(msg, line, charPositionInLine));
            }
        }
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
        return lexical_errors;
    }


    /**
     * DO NOT CHANGE THE SIGNATURE, RETURN TYPE, OR ACCESS MODIFIER OF THIS
     * METHOD! This method is called by the test system to test your Task 1
     * submission.
     *
     * <p>TODO: Implement for Task 1.1.</p>
     */
    public Collection<SyntaxError> getSyntaxErrors() {
        return syntax_errors;
    }


    /**
     * DO NOT CHANGE THE SIGNATURE, RETURN TYPE, OR ACCESS MODIFIER OF THIS
     * METHOD! This method is called by the test system to test your Task 2
     * and 3 submission.
     *
     * <p>TODO: Implement for Task 1.2.</p>
     */
    public Collection<SemanticError> getSemanticErrors() {

        Comparator<SemanticError> compareByLine = Comparator.comparing( SemanticError::getLine );
        Comparator<SemanticError> compareByCharPos = Comparator.comparing( SemanticError::getCharPos );
        Comparator<SemanticError> compareByLineAndCharPos = compareByLine.thenComparing(compareByCharPos);
        Collections.sort(semantic_errors, compareByLineAndCharPos);

        return semantic_errors;
    }


    /**
     * DO NOT CHANGE THE SIGNATURE, RETURN TYPE, OR ACCESS MODIFIER OF THIS
     * METHOD! This method is called by the test system to test your Task 2
     * and 3 submission.
     *
     * <p>TODO: Implement for Task 1.2.</p>
     */
    public Collection<JovaWarning> getWarnings() {
        return warnings;
    }
}