package at.tugraz.ist.cc;

import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task1PrivateTest {
    public static final String path_lexer = Path.of("src", "test", "resources", "private", "task1", "lexer") + File.separator;
    public static final String path_parser = Path.of("src", "test", "resources", "private", "task1", "parser") + File.separator;
    public static final String path_canalysis = Path.of("src", "test", "resources", "private", "task1", "classAnalysis") + File.separator;


    public static Stream<Arguments> lexerTests() {
        String path = path_lexer;
        return Stream.of(
                // positive cases


                // negative cases

        );
    }


    public static Stream<Arguments> parserTests() {
        String path = path_parser;
        return Stream.of(
                // positive cases


                // negative cases

        );

    }


    public static Stream<Arguments> classAnalysisTests() {
        String path = path_canalysis;
        return Stream.of(
                //positive cases
                //methods
                Arguments.of(path + "pass01.jova", true, false),
                Arguments.of(path + "pass02.jova", true, false),
                Arguments.of(path + "pass03.jova", true, false),
                Arguments.of(path + "pass04.jova", true, false),
                Arguments.of(path + "pass05.jova", true, false),
                Arguments.of(path + "pass06.jova", true, false),
                Arguments.of(path + "pass07.jova", true, false),
                Arguments.of(path + "pass08.jova", true, false),
                Arguments.of(path + "pass09.jova", true, false),
                Arguments.of(path + "pass10.jova", true, false),

                //builtin functions
                Arguments.of(path + "pass11.jova", true, false),
                Arguments.of(path + "pass12.jova", true, false),
                Arguments.of(path + "pass13.jova", true, false),
                Arguments.of(path + "pass14.jova", true, false),
                Arguments.of(path + "pass15.jova", true, false),
                Arguments.of(path + "pass16.jova", true, false),
                Arguments.of(path + "pass17.jova", true, false),


                // negative cases
                //methods
                Arguments.of(path + "fail01.jova", false, false),
                Arguments.of(path + "fail02.jova", false, false),
                Arguments.of(path + "fail03.jova", false, false),
                Arguments.of(path + "fail04.jova", false, false),
                Arguments.of(path + "fail05.jova", false, false),
                Arguments.of(path + "fail06.jova", false, false),
                Arguments.of(path + "fail07.jova", false, false),
                Arguments.of(path + "fail08.jova", false, false),
                Arguments.of(path + "fail09.jova", false, false),
                Arguments.of(path + "fail10.jova", false, false),

                //builtin functions
                Arguments.of(path + "fail11.jova", false, false),
                Arguments.of(path + "fail12.jova", false, false),
                Arguments.of(path + "fail13.jova", false, false),
                Arguments.of(path + "fail14.jova", false, false),
                Arguments.of(path + "fail15.jova", false, false),
                Arguments.of(path + "fail16.jova", false, false),
                Arguments.of(path + "fail17.jova", false, false),
                Arguments.of(path + "fail18.jova", false, false),
                Arguments.of(path + "fail19.jova", false, false),
                Arguments.of(path + "fail20.jova", false, false)

        );
    }



    @ParameterizedTest
    @MethodSource("classAnalysisTests")
    @Timeout(2)
    public void testClassAnalysis(String file_path, boolean isPositive, boolean hasWarnings) {
        System.out.println("Running semantic analysis test " + Path.of(file_path).getFileName().toString());

        Jovac jovac = new Jovac();
        jovac.task1(file_path);

        assertTrue(jovac.getLexicalErrors().isEmpty());
        assertTrue(jovac.getSyntaxErrors().isEmpty());
        if (isPositive && hasWarnings) {
            assertTrue(jovac.getSemanticErrors().isEmpty());
            assertFalse(jovac.getWarnings().isEmpty());
        } else if(isPositive && !hasWarnings) {
            assertTrue(jovac.getSemanticErrors().isEmpty());
            assertTrue(jovac.getWarnings().isEmpty());
        } else if(!isPositive)
            assertFalse(jovac.getSemanticErrors().isEmpty());
    }


}
