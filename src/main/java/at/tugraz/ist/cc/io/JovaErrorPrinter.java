package at.tugraz.ist.cc.io;

import at.tugraz.ist.cc.error.lexandparse.LexicalError;
import at.tugraz.ist.cc.error.lexandparse.SyntaxError;
import at.tugraz.ist.cc.error.semantic.SemanticError;
import at.tugraz.ist.cc.error.warning.JovaWarning;

import java.util.Collection;
/**
 * A static class to print the expected error summaries.
 */
public class JovaErrorPrinter {

    /**
     * Prints a formatted error summary of the provided lexical errors
     * for Task 1
     *
     * @param errors
     *        An ordered collection of {@link at.tugraz.ist.cc.error.lexandparse.LexicalError}s.
     */
    public static void printLexerErrors(Collection<LexicalError> errors) {
        System.out.println("Number of Lexical Errors: " + errors.size());
        errors.forEach(e -> System.out.println("  " + e.getLine() + ":" + e.getCharPos() + " - " + e.getMessage()));
        System.out.println();
    }

    /**
     * Prints a formatted error summary of the provided syntax errors for
     * Task 1.
     *
     * @param errors
     *        An ordered collection of {@link at.tugraz.ist.cc.error.lexandparse.SyntaxError}s.
     */
    public static void printSyntaxErrors(Collection<SyntaxError> errors) {
        System.out.println("Number of Syntax Errors: " + errors.size());
        errors.forEach(e -> System.out.println("  " + e.getLine() + ":" + e.getCharPos() + " - " + e.getMessage()));
        System.out.println();
    }

    /**
     * Prints a formatted error summary of the provided semantic errors for
     * Task 1.2/2.
     *
     * @param errors
     *        An ordered collection of {@link at.tugraz.ist.cc.error.semantic.SemanticError}s.
     */
    public static void printSemanticErrors(Collection<SemanticError> errors) {
        System.out.println("Number of Semantic Errors: " + errors.size());
        errors.forEach(e -> System.out.println("  line "+ e.getLine() + " - " + e.getMessage()));
        System.out.println();
    }

    /**
     * Prints a formatted error summary of the provided compiler warnings for
     * Task 1.2/2.
     *
     * @param warnings
     *        An ordered collection of {@link at.tugraz.ist.cc.error.warning.JovaWarning}s.
     */
    public static void printWarnings(Collection<JovaWarning> warnings) {
        System.out.println("Number of Warnings: " + warnings.size());
        warnings.forEach(w -> System.out.println("  " + w.getLine() + ":" + w.getCharPos() + " - " + w.getMessage()));
        System.out.println();
    }

    /**
     * Prints a formatted error summary of the provided lexical and syntax
     * errors for Task 1.
     *
     * @param lexical_errors
     *        An ordered collection of {@link at.tugraz.ist.cc.error.lexandparse.LexicalError}s.
     * @param syntax_errors
     *        An ordered collection of {@link at.tugraz.ist.cc.error.lexandparse.SyntaxError}s.
     * @param semantic_errors
     *        An ordered collection of {@link at.tugraz.ist.cc.error.semantic.SemanticError}s.
     * @param warnings
     *        An ordered collection of {@link at.tugraz.ist.cc.error.warning.JovaWarning}s.
     */
    public static void printErrorsAndWarnings(Collection<LexicalError> lexical_errors, Collection<SyntaxError> syntax_errors, Collection<SemanticError> semantic_errors, Collection<JovaWarning> warnings) {
        printLexerErrors(lexical_errors);
        printSyntaxErrors(syntax_errors);
        printSemanticErrors(semantic_errors);
        printWarnings(warnings);
    }
}
